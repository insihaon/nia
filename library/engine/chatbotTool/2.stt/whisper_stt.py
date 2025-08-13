#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
whisper_stt.py: OpenAI Whisper를 이용한 음성인식 테스트 스크립트

설치:
    pip install openai-whisper sounddevice numpy

참고:
    Whisper GitHub 리포지토리: [Whisper GitHub](https://github.com/openai/whisper) (https://github.com/openai/whisper)
    whisper PyPI: [openai-whisper](https://pypi.org/project/openai-whisper) (https://pypi.org/project/openai-whisper)
"""

import argparse
import logging
from typing import Tuple

import numpy as np
import sounddevice as sd
import whisper
from whisper import DecodingOptions, decode, log_mel_spectrogram, pad_or_trim

# 기본 설정
SAMPLING_RATE = 16000
DEFAULT_MODEL = "small"


def record_audio(duration: float, sr: int = SAMPLING_RATE) -> np.ndarray:
    """
    duration 초만큼 마이크에서 오디오를 녹음하여 1차원 float32 numpy 배열로 반환.
    :param duration: 녹음 시간(초)
    :param sr: 샘플링 레이트(Hz)
    :return: numpy.ndarray, shape=(sr*duration,)
    """
    logging.info(f"Recording {duration} seconds of audio at {sr} Hz...")
    audio = sd.rec(int(duration * sr), samplerate=sr, channels=1, dtype="float32")
    sd.wait()
    return audio.flatten()


def transcribe_audio_v1(audio: np.ndarray, model: whisper.Whisper) -> str:
    """
    방법 1: 낮은 레벨 decode() 사용, fp16=False 반드시 지정
    """
    audio = pad_or_trim(audio)
    mel = log_mel_spectrogram(audio).to(model.device)
    opts = DecodingOptions(language="ko", fp16=False)  # CPU 환경에서 fp16은 지원되지 않으므로 False로 설정
    result = whisper.decode(model, mel, opts)
    return result.text


def transcribe_audio_v2(audio: np.ndarray, model: whisper.Whisper) -> str:
    """
    방법 2: 간단하게 model.transcribe() 사용
    Whisper 내부에서 자동으로 pad_or_trim, log_mel, decode 과정을 처리합니다.
    """
    # numpy 배열을 직접 넘길 수 있으며, language 파라미터로 한국어 설정
    result = model.transcribe(audio, language="ko", fp16=False)
    return result["text"]


def parse_args() -> argparse.Namespace:
    """
    커맨드라인 인자 파싱.
    --duration: 녹음 시간(초)
    --model: Whisper 모델 크기(small, medium, large 등)
    """
    parser = argparse.ArgumentParser(description="Whisper STT 테스트 스크립트")
    parser.add_argument(
        "--duration", "-d", type=float, default=5.0,
        help="녹음 시간(초), 기본값 5.0"
    )
    parser.add_argument(
        "--model", "-m", type=str, default=DEFAULT_MODEL,
        help=f"Whisper 모델 이름, 기본값 '{DEFAULT_MODEL}'"
    )
    return parser.parse_args()


def main():
    args = parse_args()
    logging.basicConfig(level=logging.INFO, format="%(message)s")

    # 모델 로드 (한 번만 수행)
    logging.info(f"Loading Whisper model '{args.model}'...")
    model = whisper.load_model(args.model)

    # 오디오 녹음 및 STT
    audio_data = record_audio(duration=args.duration, sr=SAMPLING_RATE)
    
    # 방법 1 혹은 방법 2 중 하나를 선택
    text = transcribe_audio_v2(audio_data, model)
    # text = transcribe_audio_v1(audio_data, model)

    # 결과 출력
    print("\n=== 인식 결과 ===")
    print(text)


if __name__ == "__main__":
    main()
