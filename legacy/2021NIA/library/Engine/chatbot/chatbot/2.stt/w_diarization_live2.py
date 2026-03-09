#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
필요 패키지 설치
- sounddevice: 실시간 녹음 라이브러리  
  [sounddevice](https://pypi.org/project/sounddevice/) https://pypi.org/project/sounddevice/  
- WhisperX: 고정밀 음성 인식 + 타임스탬핑  
  [whisperX](https://github.com/m-bain/whisperX) https://github.com/m-bain/whisperX  
- webrtcvad: 보이스 액티비티 디텍션  
  [webrtcvad](https://github.com/wiseman/py-webrtcvad) https://github.com/wiseman/py-webrtcvad  

pip install torch torchvision torchaudio sounddevice whisperx webrtcvad
"""

import gc
import torch
import sounddevice as sd
import numpy as np
import whisperx
import logging
import threading
import queue
import webrtcvad
from typing import Any, Tuple, Dict, List

# ─── 로그 설정 (디버깅 모드 활성화) ──────────────────────────────────
logging.basicConfig(
    level=logging.DEBUG,
    format='[%(asctime)s] %(levelname)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)

# ─── 상수 정의 ─────────────────────────────────────────────────────
DEVICE          = "cuda" if torch.cuda.is_available() else "cpu"
COMPUTE_TYPE    = "float16" if DEVICE == "cuda" else "int8"

"""
모델 이름	파라미터 수	필요 VRAM	상대 속도	한국어 전사용 추천 용도
tiny	39 M	약 1 GB	약 10×	제한된 리소스 환경에서 간단한 실시간 전사
base	74 M	약 1 GB	약 7×	빠른 응답이 필요한 한국어 간단 전사
small	244 M	약 2 GB	약 4×	속도와 정확도의 균형, 한국어 번역 포함 워크플로우
medium	769 M	약 5 GB	약 2×	높은 정확도가 필요한 한국어 전사 및 번역
large	1 550 M	약 10 GB	1×	최상의 한국어 전사 품질, 배치 처리용
turbo	809 M	약 6 GB	약 8×	영어 전사 최적화 모델로, 한국어 전사에는 비추천
"""
MODEL_NAME      = "medium"
SR              = 16000
CHUNK_SEC       = 3.0
BATCH_SIZE      = 8
MIN_SEGMENT_DUR = 0.3  # 최소 세그먼트 길이 (초)

# ─── VAD 설정 ─────────────────────────────────────────────────────
VAD = webrtcvad.Vad(0)

def record_chunk(duration: float, sr: int = SR) -> np.ndarray:
    """duration(초)만큼 녹음하여 mono float32 numpy 배열 반환"""
    logging.debug(f"🎤 녹음 시작: {duration}s")
    audio = sd.rec(int(duration * sr), samplerate=sr, channels=1, dtype='float32')
    sd.wait()
    return audio.flatten()


def has_voice(chunk: np.ndarray, sr: int = SR) -> bool:
    """webrtcvad를 이용해 음성 존재 여부 판단"""
    pcm16 = (chunk * 32768).astype(np.int16).tobytes()
    frame_ms   = 30
    frame_size = int(sr * frame_ms / 1000) * 2
    for i in range(0, len(pcm16), frame_size):
        frame = pcm16[i:i+frame_size]
        if len(frame) < frame_size:
            break
        if VAD.is_speech(frame, sr):
            logging.debug("✅ VAD 감지: 음성 있음")
            return True
    logging.debug("❌ VAD 감지: 무음 또는 잡음")
    return False


def filter_segments(
    segments: List[Dict],
    min_dur: float = MIN_SEGMENT_DUR
) -> List[Dict]:
    """최소 길이 기준으로 세그먼트 필터링"""
    filtered = []
    for seg in segments:
        dur = seg['end'] - seg['start']
        logging.debug(f"세그먼트: dur={dur:.2f}s")
        if dur >= min_dur:
            filtered.append(seg)
    return filtered


def load_models() -> Tuple[Any, Any, Dict]:
    """Whisper 전사 모델 및 정렬 모델 로드"""
    model = whisperx.load_model(MODEL_NAME, DEVICE, compute_type=COMPUTE_TYPE)
    align_model, metadata = whisperx.load_align_model(language_code="ko", device=DEVICE)
    return model, align_model, metadata


def transcribe_and_align(
    model: Any,
    align_model: Any,
    metadata: Dict,
    audio: np.ndarray
) -> List[Dict]:
    """한국어 전사 및 정렬 수행"""
    with torch.inference_mode():
        result = model.transcribe(
            audio,
            batch_size=BATCH_SIZE,
            language="ko",
            task="transcribe"
        )
    segments = filter_segments(result["segments"])
    if not segments:
        logging.debug("=== 필터링 후 남은 세그먼트 없음 ===")
        return []
    aligned = whisperx.align(
        segments, align_model, metadata,
        audio, DEVICE, return_char_alignments=False
    )
    return aligned["segments"]


def record_loop(q: queue.Queue, stop_event: threading.Event):
    """녹음 스레드: 청크 녹음 후 큐에 추가"""
    while not stop_event.is_set():
        chunk = record_chunk(CHUNK_SEC)
        q.put(chunk)


def process_loop(
    q: queue.Queue,
    stop_event: threading.Event,
    model: Any,
    align_model: Any,
    metadata: Dict
):
    """전사 스레드: 큐에서 청크 꺼내 전사 및 출력"""
    while not stop_event.is_set():
        try:
            audio = q.get(timeout=1)
        except queue.Empty:
            continue

        if not has_voice(audio):
            continue

        segments = transcribe_and_align(model, align_model, metadata, audio)
        if not segments:
            continue
        for seg in segments:
            text = seg.get("text", "").strip()
            logging.info(f"[{seg['start']:.2f}s → {seg['end']:.2f}s] {text}")
        logging.info("––––– Next Chunk –––––")


def cleanup(*models: Any):
    """모델 삭제 및 GPU 캐시 정리"""
    for m in models:
        if m is not None:
            del m
    gc.collect()
    if DEVICE == "cuda":
        torch.cuda.empty_cache()


def main():
    model, align_model, metadata = load_models()
    audio_queue = queue.Queue(maxsize=2)
    stop_event = threading.Event()

    rec_thread = threading.Thread(
        target=record_loop,
        args=(audio_queue, stop_event),
        daemon=True
    )
    proc_thread = threading.Thread(
        target=process_loop,
        args=(audio_queue, stop_event, model, align_model, metadata),
        daemon=True
    )

    rec_thread.start()
    proc_thread.start()

    try:
        while rec_thread.is_alive() and proc_thread.is_alive():
            stop_event.wait(0.1)
    except KeyboardInterrupt:
        logging.info("🛑 종료 신호 수신")
        stop_event.set()
        rec_thread.join()
        proc_thread.join()
    finally:
        cleanup(model, align_model)
        logging.info("🛑 실시간 전사 종료")

if __name__ == "__main__":
    main()
