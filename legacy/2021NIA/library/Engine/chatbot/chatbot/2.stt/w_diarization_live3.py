#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
필요 패키지 설치:
- sounddevice: 실시간 녹음 라이브러리
  [sounddevice](https://pypi.org/project/sounddevice/) https://pypi.org/project/sounddevice/
- WhisperX: 고정밀 음성 인식 + 타임스탬핑
  [whisperX](https://github.com/m-bain/whisperX) https://github.com/m-bain/whisperX
- webrtcvad: 보이스 액티비티 디텍션
  [webrtcvad](https://github.com/wiseman/py-webrtcvad) https://github.com/wiseman/py-webrtcvad

pip install torch torchvision torchaudio sounddevice whisperx webrtcvad
"""

import argparse
import gc
import sys
import threading
import queue
import logging
from pathlib import Path
from typing import Any, Tuple, Dict, List

import torch
import numpy as np
import sounddevice as sd
import whisperx
import webrtcvad

# ─── 로그 설정 ─────────────────────────────────────────────────────
logging.basicConfig(
    level=logging.DEBUG,
    format='[%(asctime)s] %(levelname)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)

# ─── 상수 정의 ─────────────────────────────────────────────────────
DEVICE = "cuda" if torch.cuda.is_available() else "cpu"
COMPUTE_TYPE = "float16" if DEVICE == "cuda" else "int8"

MODEL_NAME = "medium"  # 지원 모델: tiny, base, small, medium, large, large-v2, large-v3, turbo
SR = 16000             # 샘플링 레이트
CHUNK_SEC = 3.0        # 녹음 청크 길이 (초)
BATCH_SIZE = 8         # WhisperX 배치 사이즈
MIN_SEGMENT_DUR = 0.3  # 최소 유효 세그먼트 길이 (초)
VAD_MODE = 3           # WebRTC A 모드 (0~3, 높을수록 보수적)

# ─── 경로 설정 ──────────────────────────────────────────────────────
BASE_DIR = Path(__file__).resolve().parent
# 라이브 전사 시 결과를 저장할 디렉토리 예시
TRANSCRIPTS_DIR = BASE_DIR / "transcripts"
TRANSCRIPTS_DIR.mkdir(exist_ok=True)
# ────────────────────────────────────────────────────────────────────

# VAD 초기화
VAD = webrtcvad.Vad(VAD_MODE)

def validate_model_name(name: str) -> None:
    """지원 가능한 모델 이름 검증"""
    supported = {"tiny","base","small","medium","large","large-v2","large-v3","turbo"}
    if name not in supported:
        raise ValueError(f"지원하지 않는 모델명입니다: {name}\n허용된 모델: {', '.join(sorted(supported))}")

def load_models() -> Tuple[Any, Any, Dict]:
    """WhisperX 전사 및 정렬 모델 로드"""
    try:
        validate_model_name(MODEL_NAME)
        model = whisperx.load_model(MODEL_NAME, DEVICE, compute_type=COMPUTE_TYPE)
        align_model, metadata = whisperx.load_align_model(language_code="ko", device=DEVICE)
        return model, align_model, metadata
    except Exception as e:
        logging.error(f"모델 로드 실패: {e}", exc_info=True)
        sys.exit(1)

def record_chunk(duration: float, sr: int = SR) -> np.ndarray:
    """duration 초 동안 녹음하여 Mono float32 numpy 배열 반환"""
    try:
        logging.debug(f"🎤 녹음 시작: {duration}s")
        audio = sd.rec(int(duration * sr), samplerate=sr, channels=1, dtype='float32')
        sd.wait()
        return audio.flatten()
    except Exception as e:
        logging.error(f"녹음 실패: {e}", exc_info=True)
        return np.array([])

def has_voice(chunk: np.ndarray, sr: int = SR) -> bool:
    """webrtcvad를 이용해 음성 존재 여부 판단"""
    try:
        pcm = (chunk * 32768).astype(np.int16).tobytes()
        frame_size = int(sr * 30 / 1000) * 2
        for i in range(0, len(pcm), frame_size):
            frame = pcm[i:i+frame_size]
            if len(frame) < frame_size:
                break
            if VAD.is_speech(frame, sr):
                logging.debug("✅ VAD 감지: 음성 있음")
                return True
        logging.debug("❌ VAD 감지: 무음 또는 잡음")
        return False
    except Exception as e:
        logging.error(f"VAD 처리 오류: {e}", exc_info=True)
        return False

def filter_segments(segments: List[Dict], min_dur: float = MIN_SEGMENT_DUR) -> List[Dict]:
    """최소 길이 기준으로 세그먼트 필터링"""
    return [seg for seg in segments if seg["end"] - seg["start"] >= min_dur]

def transcribe_and_align(model: Any, align_model: Any, metadata: Dict, audio: np.ndarray) -> List[Dict]:
    """한국어 전사 및 타임스탬핑(정렬) 수행"""
    try:
        with torch.inference_mode():
            result = model.transcribe(audio, batch_size=BATCH_SIZE, language="ko", task="transcribe")
        segments = filter_segments(result["segments"])
        if not segments:
            logging.debug("=== 유효 세그먼트 없음 ===")
            return []
        aligned = whisperx.align(segments, align_model, metadata, audio, DEVICE, return_char_alignments=False)
        return aligned["segments"]
    except Exception as e:
        logging.error(f"전사/정렬 실패: {e}", exc_info=True)
        return []

def record_loop(q: queue.Queue, stop_evt: threading.Event) -> None:
    """녹음 스레드: CHUNK_SEC 단위로 녹음하여 큐에 추가"""
    while not stop_evt.is_set():
        chunk = record_chunk(CHUNK_SEC)
        if chunk.size:
            q.put(chunk)

def process_loop(q: queue.Queue, stop_evt: threading.Event, model: Any, align_model: Any, metadata: Dict) -> None:
    """처리 스레드: 큐에서 음성 청크를 꺼내 전사 후 로그 출력"""
    while not stop_evt.is_set():
        try:
            audio = q.get(timeout=1)
        except queue.Empty:
            continue

        if not has_voice(audio):
            continue

        segments = transcribe_and_align(model, align_model, metadata, audio)
        for seg in segments:
            text = seg.get("text", "").strip()
            start, end = seg["start"], seg["end"]
            logging.info(f"[{start:.2f}s → {end:.2f}s] {text}")
        if segments:
            logging.info("––––– Next Chunk –––––")

def cleanup(*models: Any) -> None:
    """모델 제거 및 GPU 캐시 정리"""
    for m in models:
        del m
    gc.collect()
    if DEVICE == "cuda":
        torch.cuda.empty_cache()

def main() -> None:
    parser = argparse.ArgumentParser(description="WhisperX 기반 실시간 한국어 전사기")
    parser.add_argument("--file", "-f", type=Path, help="전사할 오디오 파일 경로")
    args = parser.parse_args()

    model, align_model, metadata = load_models()

    # 파일 전사 모드
    if args.file:
        audio_path = args.file
        if not audio_path.exists():
            logging.error(f"파일을 찾을 수 없습니다: {audio_path}")
            sys.exit(1)
        audio = whisperx.load_audio(str(audio_path))
        segments = transcribe_and_align(model, align_model, metadata, audio)
        for seg in segments:
            logging.info(f"[{seg['start']:.2f}s → {seg['end']:.2f}s] {seg['text'].strip()}")
        cleanup(model, align_model)
        sys.exit(0)

    # 실시간 전사 모드
    audio_queue = queue.Queue(maxsize=2)
    stop_event = threading.Event()

    rec_thread = threading.Thread(target=record_loop, args=(audio_queue, stop_event), daemon=True)
    proc_thread = threading.Thread(target=process_loop, args=(audio_queue, stop_event, model, align_model, metadata), daemon=True)

    rec_thread.start()
    proc_thread.start()

    try:
        while rec_thread.is_alive() and proc_thread.is_alive():
            stop_event.wait(0.1)
    except KeyboardInterrupt:
        logging.info("🛑 종료 신호 수신")
        stop_event.set()
    finally:
        rec_thread.join()
        proc_thread.join()
        cleanup(model, align_model)
        logging.info("🛑 전사 종료")

if __name__ == "__main__":
    main()
