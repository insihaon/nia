#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
검색 엔진 모듈
- 텍스트 검색 기능
- 검색 결과 포맷팅
- 검색 점수 분석
"""

import logging
from typing import List, Dict, Any
from elasticsearch import helpers

from config import config
from elasticsearch_client import get_es_client

def search_docs(es, query: str, size: int = 10) -> List[Dict[str, Any]]:
    """텍스트 검색 수행"""
    body = {
        "query": {"match": {"question": {"query": query}}},
        "size": size
    }
    
    res = es.search(index=config.index, body=body, sort=["_score"], request_timeout=config.timeout)
    hits = res["hits"]["hits"]
    
    logging.info(f"'{query}' 검색결과: {len(hits)}건")
    
    results = []
    for h in hits:
        src = h["_source"]
        score = h.get("_score", 0.0)
        
        result = {
            "id": h["_id"],
            "question": src["question"],
            "answer": src["answer"],
            "category": src["category"],
            "api_call": src["api_call"],
            "description": src["description"],
            "score": score
        }
        results.append(result)
        
        logging.info(f"  - score={score:.3f}, Q={src['question']}, ID={h['_id']}")
    
    return results

def print_all_docs(es) -> List[Dict[str, Any]]:
    """전체 문서 조회"""
    logging.info(f"'{config.index}' 전체 문서 출력 시작")
    
    docs = []
    for hit in helpers.scan(
        es, index=config.index,
        query={"query": {"match_all": {}}},
        size=1000, request_timeout=config.timeout
    ):
        src = hit["_source"]
        docs.append({
            "id": hit["_id"],
            "question": src["question"],
            "answer": src["answer"],
            "category": src["category"],
            "api_call": src["api_call"],
            "description": src["description"]
        })
        logging.info(f"  - ID={hit['_id']}, Q={src['question']}")
    
    logging.info(f"'{config.index}' 전체 문서 출력 완료")
    return docs

def search_by_category(es, category: str, size: int = 10) -> List[Dict[str, Any]]:
    """카테고리별 검색"""
    body = {
        "query": {"term": {"category": category}},
        "size": size
    }
    
    res = es.search(index=config.index, body=body, request_timeout=config.timeout)
    hits = res["hits"]["hits"]
    
    results = []
    for h in hits:
        src = h["_source"]
        results.append({
            "id": h["_id"],
            "question": src["question"],
            "answer": src["answer"],
            "category": src["category"],
            "api_call": src["api_call"],
            "description": src["description"]
        })
    
    logging.info(f"카테고리 '{category}' 검색결과: {len(results)}건")
    return results

def get_search_suggestions(es, partial_query: str, size: int = 5) -> List[str]:
    """검색 제안 (자동완성)"""
    body = {
        "query": {
            "prefix": {
                "question": partial_query
            }
        },
        "size": size
    }
    
    res = es.search(index=config.index, body=body, request_timeout=config.timeout)
    hits = res["hits"]["hits"]
    
    suggestions = [hit["_source"]["question"] for hit in hits]
    return suggestions
