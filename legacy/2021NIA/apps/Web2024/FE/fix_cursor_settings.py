#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Cursor 전역 설정 파일 수정 스크립트
terminal.integrated.env.windows 설정을 추가/수정합니다.
"""
import json
import os
import sys
from pathlib import Path

def fix_cursor_settings():
    # 전역 설정 파일 경로
    appdata = os.environ.get('APPDATA')
    if not appdata:
        print("APPDATA 환경 변수를 찾을 수 없습니다.")
        return False

    settings_path = Path(appdata) / "Cursor" / "User" / "settings.json"

    if not settings_path.exists():
        print(f"설정 파일을 찾을 수 없습니다: {settings_path}")
        return False

    # 백업 생성
    backup_path = settings_path.with_suffix('.json.backup')
    try:
        import shutil
        shutil.copy2(settings_path, backup_path)
        print(f"백업 생성: {backup_path}")
    except Exception as e:
        print(f"백업 생성 실패: {e}")
        return False

    # 파일 읽기
    try:
        with open(settings_path, 'r', encoding='utf-8') as f:
            content = f.read()
    except Exception as e:
        print(f"파일 읽기 실패: {e}")
        return False

    # JSON 파싱 시도
    try:
        settings = json.loads(content)
    except json.JSONDecodeError as e:
        print(f"JSON 파싱 오류: {e}")
        print("원본 파일에 JSON 오류가 있습니다. 백업에서 복원하거나 수동으로 수정이 필요합니다.")
        return False

    # terminal.integrated.env.windows 설정 추가/수정
    if "terminal.integrated.env.windows" not in settings:
        settings["terminal.integrated.env.windows"] = {}

    # PATH 설정
    current_path = settings["terminal.integrated.env.windows"].get("PATH", "${env:PATH}")

    # Node.js 경로가 없으면 추가
    nodejs_path = r"C:\Program Files\nodejs"
    if nodejs_path not in current_path:
        if current_path == "${env:PATH}":
            settings["terminal.integrated.env.windows"]["PATH"] = f"${{env:PATH}};{nodejs_path}"
        else:
            settings["terminal.integrated.env.windows"]["PATH"] = f"{current_path};{nodejs_path}"
        print(f"PATH 업데이트: {settings['terminal.integrated.env.windows']['PATH']}")
    else:
        print("PATH에 이미 Node.js 경로가 포함되어 있습니다.")

    # 파일 쓰기
    try:
        with open(settings_path, 'w', encoding='utf-8') as f:
            json.dump(settings, f, indent=2, ensure_ascii=False)
        print(f"설정 파일 업데이트 완료: {settings_path}")
        return True
    except Exception as e:
        print(f"파일 쓰기 실패: {e}")
        return False

if __name__ == "__main__":
    success = fix_cursor_settings()
    sys.exit(0 if success else 1)
