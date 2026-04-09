#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
필요 패키지 설치

- sounddevice: 실시간 녹음 라이브러리  
  [sounddevice](https://pypi.org/project/sounddevice/) https://pypi.org/project/sounddevice/  
- WhisperX: 고정밀 음성 인식 + 타임스탬핑  
  [whisperX](https://github.com/m-bain/whisperX) https://github.com/m-bain/whisperX  

pip install torch torchvision torchaudio sounddevice whisperx
"""

import gc
import torch
import sounddevice as sd
import numpy as np
import whisperx
import logging
from typing import Any, Tuple, Dict, List  # Any로 변경

# ─── 로깅 설정 ───────────────────────────────────────────────────────────────
logging.basicConfig(
    level=logging.INFO,
    format='[%(asctime)s] %(levelname)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)

# ─── 상수 정의 ───────────────────────────────────────────────────────────────
DEVICE      = "cuda" if torch.cuda.is_available() else "cpu"
COMPUTE_TYPE= "float16" if DEVICE == "cuda" else "int8"
SR          = 16000   # 샘플링 레이트
CHUNK_SEC   = 5.0     # 청크 당 초 단위 길이
BATCH_SIZE  = 16      # Whisper 배치 크기

def record_chunk(duration: float, sr: int = SR) -> np.ndarray:
    """ duration(초)만큼 녹음하여 mono float32 numpy 배열 반환 """
    logging.info(f"🎤 녹음 시작: {duration}s")
    audio = sd.rec(int(duration * sr), samplerate=sr, channels=1, dtype='float32')
    sd.wait()
    return audio.flatten()

def load_models() -> Tuple[Any, Any, Dict]:
    """
    WhisperX 전사 모델과 (나중에 로드할) 정렬 모델 placeholder 반환
    """
    model = whisperx.load_model("large-v2", DEVICE, compute_type=COMPUTE_TYPE)
    model_a, metadata = None, {}
    return model, model_a, metadata

def transcribe_and_align(
    model: Any,
    model_a: Any,
    metadata: Dict,
    audio: np.ndarray
) -> List[Dict]:
    """
    1) Whisper 전사
    2) 정렬(align) 모델 로드 및 정렬
    3) 세그먼트 리스트 반환
    """
    result = model.transcribe(audio, batch_size=BATCH_SIZE)
    if model_a is None:
        model_a, metadata = whisperx.load_align_model(
            language_code=result["language"],
            device=DEVICE
        )
    aligned = whisperx.align(
        result["segments"], model_a, metadata, audio, DEVICE,
        return_char_alignments=False
    )
    return aligned["segments"]

def cleanup(*models: Any):
    """ 모델 삭제 및 GPU 캐시 비우기 """
    for m in models:
        if m is not None:
            del m
    gc.collect()
    torch.cuda.empty_cache()

def main():
    model, model_a, metadata = load_models()
    try:
        while True:
            chunk = record_chunk(CHUNK_SEC)
            segments = transcribe_and_align(model, model_a, metadata, chunk)
            for seg in segments:
                logging.info(f"[{seg['start']:.2f}s → {seg['end']:.2f}s] {seg.get('text','').strip()}")
            logging.info("––––– Next Chunk –––––")
    except KeyboardInterrupt:
        logging.info("🛑 실시간 전사 종료")
    finally:
        cleanup(model, model_a)

if __name__ == "__main__":
    main()
