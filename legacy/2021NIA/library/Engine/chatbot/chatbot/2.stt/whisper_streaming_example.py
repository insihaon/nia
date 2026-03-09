# whisper_streaming_example.py
# ─────────────────────────────────────────────────────────────────────────────
# 실시간 스트리밍을 위해 Hugging Face Transformers의 Whisper 모델과 sounddevice를
# 활용한 예제입니다. DRY 원칙을 준수하며, 고급 개발자가 사용하는 검증된 코드를
# 기반으로 가독성 높은 주석을 포함하고 있습니다.
# 한국어 인식을 위해 WhisperProcessor로 강제 디코더 ID를 설정했습니다.
# GPU가 없을 경우 CPU 모드로 동작하도록 DEVICE 기본값을 -1로 설정합니다.
# 라이브러리 문서:
# - Transformers ASR Pipeline: https://huggingface.co/docs/transformers/main/en/task_summary#automatic-speech-recognition
# - python-sounddevice: https://python-sounddevice.readthedocs.io/

import sounddevice as sd
import queue
import logging
import numpy as np
from transformers import pipeline, WhisperProcessor, WhisperForConditionalGeneration, Pipeline
from typing import Generator, Tuple, Optional

# ─── 로깅 설정 ────────────────────────────────────────────────────────────────
logging.basicConfig(
    level=logging.INFO,
    format="[%(asctime)s] %(levelname)s: %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S"
)

# ─── 상수 정의 ─────────────────────────────────────────────────────────────────
SAMPLE_RATE: int = 16000          # 샘플링 레이트 (Hz)
CHUNK_DURATION: float = 5.0        # 한 번에 처리할 녹음 길이 (초)
STRIDE: Tuple[float, float] = (1.0, 1.0)  # 스트라이드(초)
MODEL_NAME: str = "openai/whisper-small"  # Whisper 모델 identifier
DEVICE: int = -1                  # -1: CPU, 0 이상: CUDA 장치 번호 (GPU 없으면 -1)


def load_asr_model(
    model_name: str = MODEL_NAME,
    device: int = DEVICE
) -> Pipeline:
    """
    ASR 모델과 프로세서 로드 함수 (한국어 설정).
    WhisperProcessor로 강제 디코더 ID를 가져와 한국어 인식을 수행하도록 설정합니다.
    GPU가 없을 경우 CPU 모드로만 로드합니다.

    Returns:
        Pipeline: 한국어 음성인식을 위한 Transformers pipeline 객체
    """
    logging.info(f"Loading Whisper model and processor for {model_name} on device {device}")
    # 프로세서와 모델 초기화
    processor = WhisperProcessor.from_pretrained(model_name)
    model = WhisperForConditionalGeneration.from_pretrained(model_name)

    # device 설정: -1일 때는 CPU
    if device >= 0:
        model.to(device)
    else:
        logging.info("No GPU found, running on CPU")

    # 한국어 강제 디코더 ID 계산
    forced_decoder_ids = processor.get_decoder_prompt_ids(language="ko", task="transcribe")

    # ASR 파이프라인 생성
    return pipeline(
        task="automatic-speech-recognition",
        model=model,
        tokenizer=processor.tokenizer,
        feature_extractor=processor.feature_extractor,
        forced_decoder_ids=forced_decoder_ids,
        chunk_length_s=CHUNK_DURATION,
        stride_length_s=STRIDE,
        device=device
    )


def record_audio_stream(
    sample_rate: int = SAMPLE_RATE,
    chunk_duration: float = CHUNK_DURATION
) -> Generator[np.ndarray, None, None]:
    """
    마이크로부터 실시간으로 오디오 청크를 수집하는 제너레이터.
    """
    audio_queue: "queue.Queue[np.ndarray]" = queue.Queue()

    def _callback(indata, frames, time_info, status):
        if status:
            logging.warning(f"Input status: {status}")
        audio_queue.put(indata.copy())

    with sd.InputStream(
        samplerate=sample_rate,
        channels=1,
        callback=_callback,
        blocksize=int(sample_rate * chunk_duration)
    ):
        logging.info("Recording started...")
        while True:
            chunk = audio_queue.get()
            if chunk is None:
                break
            yield chunk.flatten()


def main():
    """
    메인 실행 함수: 모델 로드 후 실시간 스트리밍 처리 및 출력
    """
    asr_model = load_asr_model()
    audio_generator = record_audio_stream()

    logging.info("Start real-time transcription...")
    try:
        for result in asr_model(audio_generator):
            text: Optional[str] = result.get("text")
            if text:
                print(f">> {text}")
    except KeyboardInterrupt:
        logging.info("Transcription stopped by user.")
    except Exception as e:
        logging.error(f"Error during streaming transcription: {e}")
    finally:
        logging.info("Shutting down.")


if __name__ == "__main__":
    main()
