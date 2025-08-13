@echo off

@REM  --paths 옵션을 주어서 생략해도 됨
@REM  call ..\common\_copy_to_Lib.bat

@REM python -m venv myenv
call myenv\Scripts\activate.bat

@REM 9.x 버전 elasticsearch 는 동작이 안 될 수 있음
@REM pip install elasticsearch==8.18.1 &
@REM pip install elastic-transport &

@REM pip install vosk
@REM pip install sounddevice

@REM pip install transformers soundfile

@REM pip install openai-whisper

pyinstaller -w -F --additional-hooks-dir=. main.py --name cb --paths ..
call myenv\Scripts\deactivate.bat

del *.spec

@REM pip freeze > requirements.txt
@REM pip install -r requirements.txt