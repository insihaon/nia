#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
챗봇 서비스 모듈
- 데이터가 이미 있는 상태에서 동작하는 서비스
- 검색 기능 제공
- 사용자 질문 처리
"""

import logging
from typing import List, Dict, Any, Optional

from .config import config
from .elasticsearch_client import get_es_client, check_data_exists, list_indices
from .search_engine import search_docs, print_all_docs, search_by_category, get_search_suggestions

class ChatbotService:
    """챗봇 서비스 클래스"""
    
    def __init__(self):
        """챗봇 서비스 초기화"""
        try:
            # Elasticsearch 클라이언트 생성
            self.es = get_es_client()
            
            # 데이터 존재 여부 확인
            if not check_data_exists(self.es):
                raise Exception("데이터가 설정되지 않았습니다. 'python data_initializer.py'를 먼저 실행하세요.")
            
            logging.info("챗봇 서비스 초기화 완료")
            
        except Exception as e:
            logging.error(f"챗봇 서비스 초기화 실패: {e}")
            raise
    
    def search(self, query: str, size: int = 10) -> List[Dict[str, Any]]:
        """질문 검색"""
        try:
            results = search_docs(self.es, query, size)
            return results
        except Exception as e:
            logging.error(f"검색 실패: {e}")
            return []
    
    def get_all_docs(self) -> List[Dict[str, Any]]:
        """전체 문서 조회"""
        try:
            docs = print_all_docs(self.es)
            return docs
        except Exception as e:
            logging.error(f"전체 문서 조회 실패: {e}")
            return []
    
    def search_by_category(self, category: str, size: int = 10) -> List[Dict[str, Any]]:
        """카테고리별 검색"""
        try:
            results = search_by_category(self.es, category, size)
            return results
        except Exception as e:
            logging.error(f"카테고리 검색 실패: {e}")
            return []
    
    def get_suggestions(self, partial_query: str, size: int = 5) -> List[str]:
        """검색 제안 (자동완성)"""
        try:
            suggestions = get_search_suggestions(self.es, partial_query, size)
            return suggestions
        except Exception as e:
            logging.error(f"검색 제안 실패: {e}")
            return []
    
    def get_service_info(self) -> Dict[str, Any]:
        """서비스 정보 조회"""
        try:
            # 인덱스 정보
            indices = list_indices(self.es)
            
            # 데이터 개수
            data_count = self.es.count(index=config.index)["count"]
            
            return {
                "status": "running",
                "index": config.index,
                "data_count": data_count,
                "elasticsearch_url": config.url
            }
        except Exception as e:
            logging.error(f"서비스 정보 조회 실패: {e}")
            return {"status": "error", "message": str(e)}
    
    def test_search_functionality(self) -> None:
        """검색 기능 테스트"""
        print("🔍 검색 기능 테스트를 시작합니다...")
        
        # 샘플 검색어들
        test_queries = [
            "현재 발생한 장애",
            "오늘 장애 상황", 
            "최근 장애 현황 출력해줘"
        ]
        
        for query in test_queries:
            print(f"\n📝 테스트 쿼리: '{query}'")
            results = self.search(query, size=3)
            
            if results:
                print(f"✅ 검색 결과: {len(results)}건")
                for i, result in enumerate(results[:2], 1):
                    print(f"  {i}. {result['question']} (점수: {result['score']:.3f})")
            else:
                print("❌ 검색 결과 없음")
        
        print("\n✅ 검색 기능 테스트 완료!")
