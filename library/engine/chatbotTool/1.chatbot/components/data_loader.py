#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
데이터 로더 모듈
- CSV 데이터 로드 및 파싱
- Elasticsearch 대량 색인
- 데이터 검증 및 변환
"""

import sys
import csv
import logging
import datetime
from pathlib import Path
from typing import List, Dict, Any
from elasticsearch import helpers

from .config import config
from .elasticsearch_client import get_es_client

def load_csv_docs(path: Path) -> List[Dict[str, Any]]:
    """
    CSV/TSV/; 파일을 자동 스니핑한 뒤 DictReader 로 읽어
    문서 리스트 생성 (필드명: 질문, 답변, 카테고리, API 호출, 설명)
    """
    docs: List[Dict[str, Any]] = []
    now = datetime.datetime.utcnow().isoformat()
    
    if not path.exists():
        logging.error(f"CSV 파일을 찾을 수 없습니다: {path}")
        sys.exit(1)
    
    try:
        with path.open(encoding="utf-8-sig", newline="") as f:
            sample = f.read(1024)
            f.seek(0)
            dialect = csv.Sniffer().sniff(sample, delimiters=[',','\t',';'])
            reader = csv.DictReader(f, dialect=dialect)
            
            for idx, row in enumerate(reader, start=1):
                docs.append({
                    "id":          idx,
                    "question":    row.get("질문", "").strip(),
                    "answer":      row.get("답변", "").strip(),
                    "category":    row.get("카테고리", "").strip(),
                    "api_call":    row.get("API 호출", "").strip(),
                    "description": row.get("설명",    "").strip(),
                    "created":     now
                })
    except csv.Error as exc:
        logging.error(f"CSV 파싱 실패: {exc}")
        sys.exit(1)
    
    logging.info(f"CSV 로드 완료: {len(docs)}건")
    return docs

def bulk_index(es, docs: List[Dict[str, Any]]) -> None:
    """helpers.bulk 을 이용한 대량 색인(Refresh wait_for)"""
    actions = [
        {"_index": config.index, "_id": d["id"], "_source": d}
        for d in docs
    ]
    helpers.bulk(es, actions, request_timeout=config.timeout, refresh="wait_for")
    logging.info(f"Bulk 색인 완료: {len(docs)}건")

def initialize_data() -> bool:
    """데이터 초기화 (CSV 로드 및 색인)"""
    try:
        # Elasticsearch 연결
        es = get_es_client()
        
        # CSV 데이터 로드
        docs = load_csv_docs(config.csv_path)
        
        # 대량 색인
        bulk_index(es, docs)
        
        logging.info("✅ 데이터 초기화 완료!")
        return True
        
    except Exception as e:
        logging.error(f"❌ 데이터 초기화 실패: {e}")
        return False

def get_data_count() -> int:
    """현재 데이터 개수 조회"""
    try:
        es = get_es_client()
        count = es.count(index=config.index)["count"]
        return count
    except Exception:
        return 0
