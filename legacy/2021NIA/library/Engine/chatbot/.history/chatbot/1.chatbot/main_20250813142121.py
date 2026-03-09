#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
챗봇 메인 실행 모듈
- 데이터가 이미 있는 상태에서 동작하는 챗봇 서비스
- 검색 기능 테스트 및 시연
"""

import logging
import sys

from chatbot_service import ChatbotService

# ─── 로깅 설정 ───────────────────────────────────────────────────────────────
logging.basicConfig(
    level=logging.INFO,
    format='[%(asctime)s] %(levelname)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)

def main() -> None:
    """챗봇 서비스 메인 실행 함수"""
    print("🤖 챗봇 서비스를 시작합니다...")
    
    try:
        # 챗봇 서비스 초기화
        chatbot = ChatbotService()
        print("✅ 챗봇 서비스 초기화 완료!")
        
        # 서비스 정보 출력
        info = chatbot.get_service_info()
        print(f"📊 서비스 정보: {info}")
        
        # 전체 문서 조회
        print("\n📚 전체 문서 조회:")
        all_docs = chatbot.get_all_docs()
        print(f"총 {len(all_docs)}건의 문서가 있습니다.")
        
        # 검색 기능 테스트
        print("\n🔍 검색 기능 테스트:")
        chatbot.test_search_functionality()
        
        print("\n🎉 챗봇 서비스 실행 완료!")
        
    except Exception as e:
        print(f"❌ 오류 발생: {e}")
        print("\n🔧 해결 방법:")
        print("   1. Elasticsearch가 실행 중인지 확인")
        print("   2. 'python data_initializer.py'를 먼저 실행하여 데이터를 설정하세요")
        sys.exit(1)

if __name__ == "__main__":
    main()
