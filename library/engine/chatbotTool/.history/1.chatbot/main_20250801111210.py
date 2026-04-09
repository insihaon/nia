#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
chatbot_indexer.py

- ES 연결 테스트
- Elasticsearch 클라이언트 생성
- CSV/TSV/; 구분 파일 자동 스니핑 후 문서 리스트 생성
- 인덱스 생성 (custom analyzer: 불필요 단어 stopwords 제거)
- Bulk 색인 (refresh wait_for)
- 인덱스·문서 조회 및 검색
- 문서/인덱스 삭제 기능
"""

import os
import sys
import csv
import logging
import datetime
from dataclasses import dataclass
from pathlib import Path
from typing import List, Dict, Any

import requests
from elasticsearch import Elasticsearch, helpers
from urllib3.exceptions import InsecureRequestWarning

# ─── 로깅 설정 ───────────────────────────────────────────────────────────────
logging.basicConfig(
    level=logging.INFO,
    format='[%(asctime)s] %(levelname)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)

# ─── 환경 설정 데이터클래스 ───────────────────────────────────────────────────
@dataclass(frozen=True)
class ESConfig:
    # url: str            = os.getenv("ES_URL",     "https://localhost:9200")
     url: str            = os.getenv("ES_URL",     "http://localhost:9200")
    user: str           = os.getenv("ES_USER",    "elastic")
    password: str       = os.getenv("ES_PASS",    "changeme123")
    verify: bool        = os.getenv("ES_VERIFY",  "false").lower() == "true"
    timeout: int        = int(os.getenv("ES_TIMEOUT", "30"))
    index: str          = "my-index"
    csv_path: Path      = Path(os.getenv("CSV_PATH", "chatbot.data"))

config = ESConfig()
warnings = InsecureRequestWarning
# 경고 억제
import warnings as _w; _w.simplefilter("ignore", InsecureRequestWarning)

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

def test_connection() -> None:
    """Basic Auth GET / 로 ES 연결 테스트"""
    try:
        res = requests.get(
            config.url + "/",
            auth=(config.user, config.password),
            verify=config.verify,
            timeout=config.timeout
        )
        res.raise_for_status()
    except requests.RequestException as exc:
        logging.error(f"ES 연결 실패: {exc}")
        sys.exit(1)
    logging.info("ES 연결 테스트 성공 (GET /)")

def get_es_client() -> Elasticsearch:
    """Elasticsearch 클라이언트 생성 및 info() 호출로 연결 확인"""
    es = Elasticsearch(
        hosts=[config.url],
        basic_auth=(config.user, config.password),
        verify_certs=config.verify,
        request_timeout=config.timeout,
        max_retries=3,
        retry_on_timeout=True
    )
    info = es.info()
    logging.info(f"ES 연결 성공: cluster={info['cluster_name']}, node={info['name']}")
    return es

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

def create_index(es: Elasticsearch) -> None:
    """인덱스가 없으면 생성 (설정·매핑 적용)"""
    if es.indices.exists(index=config.index):
        logging.info(f"인덱스 이미 존재: {config.index}")
        return

    body = {
        "settings": {"analysis": ANALYSIS_SETTINGS},
        "mappings": MAPPINGS
    }

    es.indices.create(index=config.index, body=body, request_timeout=config.timeout)
    logging.info(f"인덱스 생성됨: {config.index}")

def bulk_index(es: Elasticsearch, docs: List[Dict[str, Any]]) -> None:
    """helpers.bulk 을 이용한 대량 색인(Refresh wait_for)"""
    actions = [
        {"_index": config.index, "_id": d["id"], "_source": d}
        for d in docs
    ]
    helpers.bulk(es, actions, request_timeout=config.timeout, refresh="wait_for")
    logging.info(f"Bulk 색인 완료: {len(docs)}건")

def list_indices(es: Elasticsearch) -> None:
    inds = es.cat.indices(format="json", request_timeout=config.timeout)
    logging.info(f"전체 인덱스 ({len(inds)}개):")
    for idx in inds:
        logging.info(f"  - {idx['index']} (docs={idx['docs.count']})")

def print_all_docs(es: Elasticsearch) -> None:
    logging.info(f"'{config.index}' 전체 문서 출력 시작")
    for hit in helpers.scan(
        es, index=config.index,
        query={"query": {"match_all": {}}},
        size=1000, request_timeout=config.timeout
    ):
        src = hit["_source"]
        logging.info(f"  - ID={hit['_id']}, Q={src['question']}")
    logging.info(f"'{config.index}' 전체 문서 출력 완료")

def search_docs(es: Elasticsearch, query: str, size: int = 10) -> None:
    body = {
        "query": {"match": {"question": {"query": query}}},
        "size": size
    }
    res = es.search(index=config.index, body=body, sort=["_score"], request_timeout=config.timeout)
    hits = res["hits"]["hits"]
    logging.info(f"'{query}' 검색결과: {len(hits)}건")
    for h in hits:
        src = h["_source"]
        score = h.get("_score", 0.0)
        logging.info(f"  - score={score:.3f}, Q={src['question']}, ID={h['_id']}")

def delete_all_docs(es: Elasticsearch) -> None:
    """인덱스 내 모든 문서 삭제"""
    resp = es.delete_by_query(
        index=config.index,
        body={"query": {"match_all": {}}},
        refresh=True,
        request_timeout=config.timeout
    )
    logging.info(f"문서 삭제됨: {resp.get('deleted', 0)}건")

def delete_all_indices(es: Elasticsearch) -> None:
    """클러스터 내 모든 인덱스 삭제"""
    es.indices.delete(index="*", ignore_unavailable=True, request_timeout=config.timeout)
    logging.info("모든 인덱스 삭제 완료")

def main() -> None:
    test_connection()
    es = get_es_client()

    # ▼ 필요시 아래 주석 해제 ▼
    # delete_all_docs(es)
    # delete_all_indices(es)

    create_index(es)

    # 색인이 비어있다면 CSV → Bulk 색인
    if es.count(index=config.index)["count"] == 0:
        docs = load_csv_docs(config.csv_path)
        bulk_index(es, docs)

    # 기능 시연
    list_indices(es)
    print_all_docs(es)

    # 샘플 검색
    for q in ["현재 발생한 장애", "오늘 장애 상황", "최근 장애 현황 출력해줘"]:
        search_docs(es, q)

if __name__ == "__main__":
    main()
