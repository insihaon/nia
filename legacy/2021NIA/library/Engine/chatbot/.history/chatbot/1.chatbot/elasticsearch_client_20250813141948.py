#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Elasticsearch 클라이언트 모듈
- ES 연결 및 테스트
- 인덱스 관리
- 기본 CRUD 작업
"""

import sys
import logging
import requests
from elasticsearch import Elasticsearch
from urllib3.exceptions import InsecureRequestWarning

from config import config, ANALYSIS_SETTINGS, MAPPINGS

# 경고 억제
import warnings as _w
_w.simplefilter("ignore", InsecureRequestWarning)

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

def list_indices(es: Elasticsearch) -> None:
    """전체 인덱스 목록 조회"""
    inds = es.cat.indices(format="json", request_timeout=config.timeout)
    logging.info(f"전체 인덱스 ({len(inds)}개):")
    for idx in inds:
        logging.info(f"  - {idx['index']} (docs={idx['docs.count']})")

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

def check_data_exists(es: Elasticsearch) -> bool:
    """데이터 존재 여부 확인"""
    try:
        count = es.count(index=config.index)["count"]
        return count > 0
    except Exception:
        return False
