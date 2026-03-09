#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
optimized_whisper_silence_stt_refactored.py

📌 Refactored 주요 변경점
1. recognize_on_silence 에 try/except/else 구조 적용
2. except 블록에서 불필요한 yield 제거하여 Unreachable 코드 제거
3. 에러 발생 시 재시도 로직 유지, 정상 처리 시 yield 수행
4. Python generator 의 yield 동작에 대한 주석 추가
"""

import argparse
import logging
import os
import sys
import threading
import queue
from pathlib import Path
from typing import Generator, List

import numpy as np
import sounddevice as sd
import whisper
import webrtcvad

# torio FFmpeg 확장 무조건 비활성화
os.environ['TORIO_DISABLE'] = '1'

# ─── 로깅 설정 ─────────────────────────────────────────────────────────
logging.basicConfig(
    level=logging.DEBUG,
    format="[%(asctime)s] %(levelname)s: %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S"
)

# ─── 기본값 ───────────────────────────────────────────────────────────
SAMPLING_RATE       = 16000
BLOCK_DURATION      = 0.5
DEFAULT_MODEL       = "medium"
DEFAULT_SILENCE_SEC = 1.0
INPUT_LANGUAGE      = "ko"

COMMON_WORDS = [
    "링크", "장비별 장애", "인터페이스", "CPU", "네트워크",
    "토폴로지", "SOP", "장애", "조회", "이력", "통계",
    "이벤트", "알람", "장비", "트래픽", "성능",
    "메모리", "사용률", "상태", "정상", "비정상"
]
SIMILARITY_THRESHOLD = 0.5


def load_whisper_model(model_name: str) -> whisper.Whisper:
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
    blocksize = int(samplerate * block_dur)
    def callback(indata, frames, time, status):
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


def boost_common_words(text: str, common: List[str], threshold: float) -> str:
    import difflib
    words = text.split()
    for i, w in enumerate(words):
        matches = difflib.get_close_matches(w, common, n=1, cutoff=threshold)
        if matches:
            words[i] = matches[0]
    return " ".join(words)


def recognize_on_silence(
    model: whisper.Whisper,
    q: queue.Queue,
    samplerate: int,
    silence_sec: float,
    block_dur: float
) -> Generator[str, None, None]:
    vad = webrtcvad.Vad(3)
    buffer = np.empty((0,1), dtype=np.float32)
    required_silence = int(samplerate * silence_sec)
    current_silence = 0

    while True:
        data = q.get()
        buffer = np.concatenate((buffer, data), axis=0)
        # PCM 변환
        pcm = (data * 32768).astype(np.int16).tobytes()
        frame_ms = 30
        frame_bytes = int(samplerate * frame_ms / 1000) * 2
        speech_flag = False

        for i in range(0, len(pcm), frame_bytes):
            chunk = pcm[i:i+frame_bytes]
            if len(chunk) < frame_bytes:
                break
            try:
                if vad.is_speech(chunk, samplerate):
                    speech_flag = True
                    break
            except webrtcvad.Error:
                continue

        # 무음 검출 로직
        if speech_flag:
            current_silence = 0
        else:
            current_silence += len(data)

        # 충분한 무음 구간 탐지 시 STT 수행
        if current_silence >= required_silence and buffer.size > 0:
            total = len(buffer)
            speech_end = total - current_silence
            audio_chunk = buffer[:speech_end].flatten()
            buffer = buffer[total:]
            current_silence = 0

            # 음성 전사
            try:
                result = model.transcribe(audio_chunk, language=INPUT_LANGUAGE, fp16=False)
            except Exception as e:
                logging.error(f"전사 오류 발생: {e}")
                # 에러 발생 시 해당 청크 건너뛰고 재시도
                continue
            else:
                # 정상 수행 시 텍스트 반환 (yield)
                # yield 는 generator 에서 값을 호출자에게 반환하고 일시 중단합니다.
                yield result["text"].strip()


def main() -> None:
    parser = argparse.ArgumentParser(description="VAD 기반 실시간 STT")
    parser.add_argument("--model", "-m", default=DEFAULT_MODEL, help="Whisper 모델 이름 또는 경로")
    parser.add_argument("--silence", "-s", type=float, default=DEFAULT_SILENCE_SEC, help="무음 감지 시간(초)")
    args = parser.parse_args()

    model = load_whisper_model(args.model)
    q: queue.Queue = queue.Queue()

    recorder = threading.Thread(
        target=record_audio,
        args=(q, SAMPLING_RATE, BLOCK_DURATION),
        daemon=True
    )
    recorder.start()

    try:
        for text in recognize_on_silence(model, q, SAMPLING_RATE, args.silence, BLOCK_DURATION):
            if text:
                boosted = boost_common_words(text, COMMON_WORDS, SIMILARITY_THRESHOLD)
                logging.debug(f"인식 텍스트: {text}")
                logging.info(f"변환 텍스트: {boosted}")
    except KeyboardInterrupt:
        logging.info("종료 요청 수신, 프로그램 종료 중...")
    finally:
        sys.exit(0)

if __name__ == "__main__":
    main()
