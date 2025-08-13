#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
optimized_whisper_silence_stt_gpu_v2.py

📌 수정 사항:
- --cuda/--cpu 상호 배타적 옵션 지원
- CPU 옵션 선택 시 항상 CPU 및 FP16 비활성화
- FP16 옵션은 GPU 사용 시에만 적용
- 기타 코드 구조 및 옵션 유효성 검증 강화

python optimized_whisper_silence_stt_gpu_v2.py --cpu -m small -s 1.0
python optimized_whisper_silence_stt_gpu_v2.py --cuda --fp16 -m medium -s 1.0
"""
import argparse
import logging
import os
import threading
import queue
import time
from pathlib import Path
from typing import Generator, List
from collections import deque
from concurrent.futures import ProcessPoolExecutor, Future

import numpy as np
import sounddevice as sd
import whisper
import webrtcvad

# 환경 변수 설정
os.environ['TORIO_DISABLE'] = '1'

logging.basicConfig(
    level=logging.DEBUG,
    format="[%(asctime)s] %(levelname)s: %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S"
)

# 지원 가능한 Whisper 모델 목록
AVAILABLE_WHISPER_MODELS = [
    "tiny", "tiny.en", "base", "base.en",
    "small", "small.en", "medium", "medium.en",
    "large"
]

# 상수 정의
SAMPLING_RATE = 16000
BLOCK_DURATION = 0.5
DEFAULT_MODEL = "small"
DEFAULT_SILENCE_SEC = 1.0
INPUT_LANGUAGE = "ko"
COMMON_WORDS: List[str] = [
    "링크", "장비별 장애", "인터페이스", "CPU", "네트워크",
    "토폴로지", "SOP", "장애", "조회", "이력", "통계",
    "이벤트", "알람", "장비", "트래픽", "성능", "메모리",
    "사용률", "상태", "정상", "비정상"
]
SIMILARITY_THRESHOLD = 0.5
MAX_BUFFER_SEC = 10


def load_whisper_model(name: str, device: str, fp16: bool) -> whisper.Whisper:
    """Whisper 모델을 로드하고 필요 시 FP16 모드 활성화"""
    path = Path(name)
    if path.exists() and path.is_dir():
        logging.info(f"로컬 모델 디렉터리: {path.resolve()}")
    model = whisper.load_model(name, device=device)
    if fp16 and device == 'cuda':
        model = model.to_fp16()
        logging.info("FP16 모드 활성화됨")
    return model


def record_audio(q: queue.Queue, sr: int, dur: float, stop_event: threading.Event):
    """오디오를 스트리밍하여 Queue에 저장"""
    block = int(sr * dur)

    def callback(indata, frames, time_info, status):
        if status:
            logging.warning(f"InputStream 상태: {status}")
        q.put(indata.copy())

    with sd.InputStream(
        samplerate=sr, blocksize=block,
        dtype='float32', channels=1, callback=callback
    ):
        logging.info("녹음 시작... (Ctrl+C로 중단 가능)")
        while not stop_event.is_set():
            time.sleep(0.1)
    logging.info("녹음 스레드 종료")


def boost_common_words(text: str) -> str:
    """자주 쓰이는 단어와 유사어 교정"""
    import difflib
    words = text.split()
    for i, w in enumerate(words):
        m = difflib.get_close_matches(w, COMMON_WORDS, n=1, cutoff=SIMILARITY_THRESHOLD)
        if m:
            words[i] = m[0]
    return ' '.join(words)


def transcribe_chunk(model, chunk: np.ndarray, fp16: bool) -> str:
    """주어진 오디오 청크를 텍스트로 전사"""
    res = model.transcribe(chunk, language=INPUT_LANGUAGE, fp16=fp16)
    return res.get('text', '').strip()


def recognize_on_silence(
    model: whisper.Whisper,
    q: queue.Queue,
    sr: int,
    silence: float,
    dur: float,
    executor: ProcessPoolExecutor,
    fp16: bool,
    stop_event: threading.Event
) -> Generator[str, None, None]:
    """
    무음 구간이 탐지된 후 버퍼에 쌓인 오디오를 전사
    stop_event로 종료 신호를 받을 수 있음
    """
    vad = webrtcvad.Vad(3)
    buffer = deque(maxlen=int(sr * MAX_BUFFER_SEC / dur))
    frame_size = int(sr * 0.03)
    req_silence = int(sr * silence)
    sil_count = 0

    while not stop_event.is_set():
        try:
            data = q.get(timeout=0.1)
        except queue.Empty:
            continue

        buffer.append(data)
        pcm = (data.flatten() * 32768).astype(np.int16)
        frames = [pcm[i:i+frame_size] for i in range(0, len(pcm), frame_size)
                  if len(pcm[i:i+frame_size]) == frame_size]

        if any(vad.is_speech(frame.tobytes(), sr) for frame in frames):
            sil_count = 0
        else:
            sil_count += data.shape[0]

        if sil_count >= req_silence and buffer:
            chunk = np.concatenate(list(buffer), axis=0).flatten()
            buffer.clear()
            sil_count = 0
            future: Future = executor.submit(transcribe_chunk, model, chunk, fp16)
            try:
                text = future.result()
                if text:
                    yield boost_common_words(text)
            except Exception as e:
                logging.error(f"전사 오류: {e}")

    logging.info("인식 루프 종료")


def main():
    parser = argparse.ArgumentParser(description="VAD 기반 실시간 STT (GPU/FP16 지원)")
    group = parser.add_mutually_exclusive_group()
    group.add_argument('--cuda', action='store_true', help="CUDA 사용 (GPU)")
    group.add_argument('--cpu', action='store_true', help="CPU 사용")
    parser.add_argument('-m', '--model', default=DEFAULT_MODEL,
                        help="모델 이름 또는 경로")
    parser.add_argument('-s', '--silence', type=float, default=DEFAULT_SILENCE_SEC,
                        help="무음 감지 시간(초)")
    parser.add_argument('--fp16', action='store_true', help="FP16 연산 사용 (GPU 전용)")
    args = parser.parse_args()

    # 디바이스 설정: GPU 우선 순위, CPU 옵션 적용
    if args.cuda:
        device = 'cuda'
    else:
        device = 'cpu'
        if args.fp16:
            logging.warning("FP16 옵션은 GPU에서만 지원됩니다. FP16 비활성화합니다.")
    fp16 = args.fp16 and device == 'cuda'

    model = load_whisper_model(args.model, device, fp16)

    q: queue.Queue = queue.Queue()
    stop_event = threading.Event()
    recorder = threading.Thread(
        target=record_audio,
        args=(q, SAMPLING_RATE, BLOCK_DURATION, stop_event),
        daemon=False
    )
    recorder.start()

    with ProcessPoolExecutor() as executor:
        try:
            for out in recognize_on_silence(
                    model, q, SAMPLING_RATE, args.silence,
                    BLOCK_DURATION, executor, fp16, stop_event):
                logging.info(f"인식: {out}")
        except KeyboardInterrupt:
            logging.info("종료 요청 수신, 녹음 및 인식 종료 중...")
            stop_event.set()
            recorder.join()
            executor.shutdown(wait=False)
    logging.info("프로그램 종료")

if __name__ == '__main__':
    main()
