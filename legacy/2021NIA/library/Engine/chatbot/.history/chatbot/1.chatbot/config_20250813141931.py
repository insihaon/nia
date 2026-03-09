#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
설정 관리 모듈
- ES 연결 설정
- 분석기 설정
- 매핑 설정
"""

import os
from dataclasses import dataclass
from pathlib import Path
from typing import Dict, Any

# ─── 환경 설정 데이터클래스 ───────────────────────────────────────────────────
@dataclass(frozen=True)
class ESConfig:
    url: str            = os.getenv("ES_URL",     "http://localhost:9200")
    user: str           = os.getenv("ES_USER",    "elastic")
    password: str       = os.getenv("ES_PASS",    "changeme123")
    verify: bool        = os.getenv("ES_VERIFY",  "false").lower() == "true"
    timeout: int        = int(os.getenv("ES_TIMEOUT", "30"))
    index: str          = "my-index"
    csv_path: Path      = Path(os.getenv("CSV_PATH", "../chatbot.data"))

# ─── 분석기 설정 (DRY 원칙) ────────────────────────────────────────────────────
ANALYSIS_SETTINGS: Dict[str, Any] = {
    "filter": {
        "custom_stop_filter": {
            "type": "stop",
            "stopwords": ["보고", "싶어", "출력해줘", "알려줘", "보여줘"]
        }
    },
    "analyzer": {
        # default analyzer 정의
        "default": {
            "type": "custom",
            "tokenizer": "standard",
            "filter": ["lowercase", "custom_stop_filter"]
        },
        # 명시적 custom_analyzer (mappings 에서 참조)
        "custom_analyzer": {
            "type": "custom",
            "tokenizer": "standard",
            "filter": ["lowercase", "custom_stop_filter"]
        }
    }
}

# ─── 매핑 설정 ─────────────────────────────────────────────────────────────────
MAPPINGS: Dict[str, Any] = {
    "properties": {
        "question":    {"type": "text",    "analyzer": "custom_analyzer"},
        "answer":      {"type": "text",    "index": False},
        "category":    {"type": "keyword"},
        "api_call":    {"type": "keyword"},
        "description": {"type": "text",    "index": False},
        "created":     {"type": "date"}
    }
}

# 전역 설정 인스턴스
config = ESConfig()
