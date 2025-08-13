#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
optimized_vosk_stt.py

📌 개선 포인트
1. pathlib로 경로 검증
2. try/except로 예외 처리 및 상세 에러 메시지
3. DRY 원칙 적용: 로드, 녹음, 디코딩 함수 분리
4. 타입 힌팅, 로깅 강화
5. threading 모듈 import 추가
"""

import sounddevice as sd
import queue
import json
import logging
import threading                   # ← threading 모듈 import
from pathlib import Path
from vosk import Model, KaldiRecognizer
from typing import Generator

# ─── 로깅 설정 ───────────────────────────────────────────────
logging.basicConfig(
    level=logging.INFO,
    format='[%(asctime)s] %(levelname)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)

# ─── 모델 로드 함수 ────────────────────────────────────────────
def load_vosk_model(model_path: str) -> Model:
    """
    Vosk 모델을 로드하고, 경로 유효성을 검증합니다.
    
    :param model_path: 압축 해제된 모델 디렉터리 경로
    :return: vosk.Model 인스턴스
    :raises: FileNotFoundError, Exception
    """
    model_dir = Path(model_path)
    if not model_dir.exists() or not model_dir.is_dir():
        raise FileNotFoundError(f"모델 디렉터리가 없습니다: {model_dir.resolve()}")
    
    try:
        logging.info(f"Vosk 모델 로드 시도: {model_dir}")
        return Model(str(model_dir))
    except Exception as e:
        raise Exception(
            f"모델 생성에 실패했습니다. 경로와 호환성을 확인하세요.\n"
            f"  경로: {model_dir.resolve()}\n"
            f"  원본 오류: {e}"
        ) from e

# ─── 오디오 스트림 녹음 함수 ─────────────────────────────────
def record_audio(q: queue.Queue, sample_rate: int = 16000) -> None:
    """
    마이크에서 RawInputStream으로 오디오를 블록 단위로 읽어 큐에 저장합니다.
    Ctrl+C 로 중단할 때까지 계속 녹음합니다.
    """
    def callback(indata, frames, time, status):
        if status:
            logging.warning(f"녹음 상태: {status}")
        q.put(bytes(indata))

    with sd.RawInputStream(
        samplerate=sample_rate,
        blocksize=8000,
        dtype='int16',
        channels=1,
        callback=callback
    ):
        logging.info("녹음 중... (Ctrl+C 로 중단)")
        try:
            while True:
                threading.Event().wait(1)  # 1초마다 대기
        except KeyboardInterrupt:
            logging.info("녹음 종료 요청 수신")

# ─── 인식 루프 함수 ───────────────────────────────────────────
def recognize_stream(
    model: Model,
    q: queue.Queue,
    sample_rate: int = 16000
) -> Generator[str, None, None]:
    """
    큐에서 데이터를 꺼내 KaldiRecognizer로 디코딩 후 완전 결과를 반환합니다.
    """
    recognizer = KaldiRecognizer(model, sample_rate)
    while True:
        data = q.get()
        if recognizer.AcceptWaveform(data):
            result = json.loads(recognizer.Result())
            yield result.get("text", "")

# ─── 메인 함수 ───────────────────────────────────────────────
def main(
    model_path: str = r"./34. stt/models/vosk-model-small-ko-0.22",
    sample_rate: int = 16000
):
    """
    1) 모델 로드
    2) 녹음 스레드 시작
    3) 인식 결과 출력
    """
    # 1) 모델 로드
    model = load_vosk_model(model_path)

    # 2) 녹음 큐 및 스레드 설정
    q: queue.Queue = queue.Queue()
    recorder = threading.Thread(
        target=record_audio,
        args=(q, sample_rate),
        daemon=True
    )
    recorder.start()

    # 3) 결과 출력
    for text in recognize_stream(model, q, sample_rate):
        if text:
            print(f"[인식된 텍스트] {text}")

if __name__ == "__main__":
    main()
