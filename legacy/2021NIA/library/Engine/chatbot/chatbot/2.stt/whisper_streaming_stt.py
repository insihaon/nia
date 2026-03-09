#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
optimized_whisper_silence_stt.py

📌 개선 포인트
1. pathlib로 로컬 모델 디렉터리(희망 시) 검증
2. try/except로 예외 처리 및 상세 에러 메시지
3. DRY 원칙 적용: 모델 로드, 녹음, 디코딩 함수 분리
4. 타입 힌팅, 로깅 강화
5. threading 모듈로 녹음 스레드 분리
6. queue로 오디오 청크 버퍼링 → 무음(3초) 감지 시점에 STT 수행
"""

import argparse
import logging
import sys
import threading
import queue
from pathlib import Path
from typing import Generator, Optional

import numpy as np
import sounddevice as sd
import whisper

# ─── 로깅 설정 ─────────────────────────────────────────────────────────
logging.basicConfig(
    level=logging.INFO,
    format="[%(asctime)s] %(levelname)s: %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S"
)

# ─── 기본값 ───────────────────────────────────────────────────────────
SAMPLING_RATE       = 16000    # Hz
BLOCK_DURATION      = 0.5      # 스트림 블록 단위(초)
DEFAULT_MODEL       = "small"  # Whisper 모델 이름
DEFAULT_SILENCE_SEC = 3.0      # 무음 감지 시간(초)
SILENCE_THRESHOLD   = 0.01     # 진폭 기준(≤ 이면 무음으로 간주)
# ────────────────────────────────────────────────────────────────────────

def load_whisper_model(model_name: str) -> whisper.Whisper:
    """
    Whisper 모델을 로드합니다.
    :param model_name: "small", "medium" 등 또는 로컬 경로
    :return: whisper.Whisper 인스턴스
    """
    path = Path(model_name)
    if path.exists() and path.is_dir():
        logging.info(f"로컬 Whisper 모델 디렉터리 확인: {path.resolve()}")
    try:
        logging.info(f"Whisper 모델 로드: '{model_name}'")
        return whisper.load_model(model_name)
    except Exception as e:
        logging.error(f"모델 로드 실패: {e}")
        sys.exit(1)

def record_audio(q: queue.Queue, samplerate: int, block_dur: float) -> None:
    """
    InputStream으로부터 블록 단위 오디오를 읽어 큐에 저장합니다.
    Ctrl+C로 중단할 때까지 계속 녹음합니다.
    """
    blocksize = int(samplerate * block_dur)
    def callback(indata: np.ndarray, frames, time, status) -> None:
        if status:
            logging.warning(f"InputStream 상태: {status}")
        q.put(indata.copy())

    try:
        with sd.InputStream(
            samplerate=samplerate,
            blocksize=blocksize,
            dtype="float32",
            channels=1,
            callback=callback
        ):
            logging.info("녹음 시작 (Ctrl+C 로 중단)...")
            threading.Event().wait()
    except KeyboardInterrupt:
        logging.info("녹음 종료 요청 수신, 스트림 닫는 중...")
    except Exception as e:
        logging.error(f"녹음 중 오류 발생: {e}")

def recognize_on_silence(
    model: whisper.Whisper,
    q: queue.Queue,
    samplerate: int,
    silence_sec: float,
    block_dur: float,
    threshold: float
) -> Generator[str, None, None]:
    """
    큐에서 float32 오디오 청크를 모아,
    연속된 무음 구간이 silence_sec 이상이면
    그간 버퍼된 오디오에 STT를 수행하고 yield 합니다.
    """
    buffer = np.empty((0,1), dtype=np.float32)
    silence_frames_req = int(samplerate * silence_sec)
    current_silence = 0

    while True:
        data: np.ndarray = q.get()
        buffer = np.concatenate((buffer, data), axis=0)

        # 현재 블록이 무음이면 카운터 증가, 아니면 리셋
        if np.max(np.abs(data)) <= threshold:
            current_silence += len(data)
        else:
            current_silence = 0

        # 무음 지속 시간이 임계치 이상일 때
        if current_silence >= silence_frames_req and len(buffer) > 0:
            # 버퍼에서 마지막 silence 구간 제외
            total_frames = len(buffer)
            speech_frames = total_frames - current_silence
            audio_chunk = buffer[:speech_frames].flatten()
            buffer = buffer[total_frames:]  # 버퍼 초기화

            try:
                result = model.transcribe(audio_chunk, language="ko", fp16=False)
                yield result["text"]
            except Exception as e:
                logging.error(f"디코딩 실패: {e}")
                yield ""

def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Whisper STT: 무음 감지 시점에 인식 수행"
    )
    parser.add_argument(
        "-m", "--model", type=str, default=DEFAULT_MODEL,
        help=f"모델 이름 혹은 로컬 경로 (기본값 '{DEFAULT_MODEL}')"
    )
    parser.add_argument(
        "-s", "--silence-duration", type=float, default=DEFAULT_SILENCE_SEC,
        help=f"무음 감지 시간(초), 기본값 {DEFAULT_SILENCE_SEC}"
    )
    return parser.parse_args()

def main():
    args = parse_args()
    model = load_whisper_model(args.model)

    q: queue.Queue = queue.Queue()
    recorder = threading.Thread(
        target=record_audio,
        args=(q, SAMPLING_RATE, BLOCK_DURATION),
        daemon=True
    )
    recorder.start()

    logging.info(f"무음 {args.silence_duration}s 이상 감지 시 STT 수행. 종료하려면 Ctrl+C.")
    try:
        for idx, text in enumerate(
            recognize_on_silence(
                model, q,
                samplerate=SAMPLING_RATE,
                silence_sec=args.silence_duration,
                block_dur=BLOCK_DURATION,
                threshold=SILENCE_THRESHOLD
            ),
            start=1
        ):
            if text:
                print(f"[Utterance {idx}] {text}")
    except KeyboardInterrupt:
        logging.info("프로그램 종료 요청 수신.")
    finally:
        sys.exit(0)

if __name__ == "__main__":
    main()
