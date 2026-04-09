@echo off

@REM  --paths 옵션을 주어서 생략해도 됨
@REM  call ..\common\_copy_to_Lib.bat

@REM python -m venv myenv
call myenv\Scripts\activate.bat

@REM pip install vosk
@REM pip install sounddevice

@REM pip install transformers soundfile
@REM pip install openai-whisper

@REM  최신 1.x 라인인 1.25.2 로 고정하려면 아래와 같이 실행하세요
@REM pip install "numpy<2.0.0,>=1.25.0"

@REM 보다 정교한 음성 검출을 위해 webrtcvad https://github.com/wiseman/py-webrtcvad 와 같은 라이브러리를 사용
@REM pip install webrtcvad


pyinstaller -w -F --additional-hooks-dir=. main.py --name cb --paths ..
call myenv\Scripts\deactivate.bat

del *.spec

@REM pip freeze > requirements.txt
@REM pip install -r requirements.txt