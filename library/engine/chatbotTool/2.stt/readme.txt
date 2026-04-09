# STT 구현
1. openai-whisper 사용 (whisper_streaming_stt)
2. openai-whisper-x 사용 (w_diarization_live)
3. vosk_stt 사용 (vosk_stt)

3개의 모델을 여러가지 모델 및 옵션으로 테스트 한 결과 현재로서는 openai-whisper 에 의한 인식률이 가장 좋은 것으로 판단됨

# 개선 필요성
1. STT 인식률 낮음
2. STT 인식 속도 느림

# 환경설정
pip install vosk
pip install sounddevice
pip install transformers soundfile
pip install openai-whisper

@REM  최신 1.x 라인인 1.25.2 로 고정하려면 아래와 같이 실행하세요
pip install "numpy<2.0.0,>=1.25.0"

@REM 보다 정교한 음성 검출을 위해 webrtcvad https://github.com/wiseman/py-webrtcvad 와 같은 라이브러리를 사용
pip install webrtcvad

# 코드 
1. 실행 및 테스트 : 각각 모델별 파일을 실행하고 마이크를 통해 말한 내용을 인식되는지 확인하면 됨
2. 특징 : 코드에 따라서는 인식률을 높이기 위해 "자주 쓰이는 단어와 유사어 교정" 보정 과정을 추가로 구현함
