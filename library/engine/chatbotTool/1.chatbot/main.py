#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
챗봇 메인 실행 모듈
- 사용자 입력을 받아 검색 수행
- 검색 결과에 따라 URL 자동 열기
- test.py.1의 URL 열기 기능 활용
"""

import logging
import sys
import webbrowser
from typing import List, Dict, Any

from components.chatbot_service import ChatbotService

# ─── 로깅 설정 ───────────────────────────────────────────────────────────────
logging.basicConfig(
    level=logging.INFO,
    format='[%(asctime)s] %(levelname)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)

def open_url(url: str) -> None:
    """URL을 기본 브라우저에서 열기"""
    try:
        # URL이 http:// 또는 https://로 시작하는지 확인
        if not url.startswith(('http://', 'https://')):
            # 상대 경로인 경우 기본 도메인 추가
            if url.startswith('/'):
                url = f"http://116.89.191.47:8080{url}"
            else:
                url = f"http://116.89.191.47:8080/{url}"
        
        print(f"🌐 브라우저에서 URL 열기: {url}")
        webbrowser.open(url)
        
    except Exception as e:
        print(f"❌ URL 열기 실패: {e}")

def display_search_results(results: List[Dict[str, Any]]) -> None:
    """검색 결과를 보기 좋게 출력"""
    if not results:
        print("❌ 검색 결과가 없습니다.")
        return
    
    print(f"\n🔍 검색 결과: {len(results)}건")
    print("=" * 60)
    
    for i, result in enumerate(results, 1):
        print(f"\n📋 결과 {i}:")
        print(f"  질문: {result['question']}")
        print(f"  답변: {result['answer']}")
        print(f"  카테고리: {result['category']}")
        print(f"  API 호출: {result['api_call']}")
        print(f"  설명: {result['description']}")
        print(f"  유사도 점수: {result['score']:.3f}")
        print("-" * 40)

def interactive_search(chatbot: ChatbotService) -> None:
    """대화형 검색 인터페이스"""
    print("\n🤖 챗봇 검색 서비스를 시작합니다!")
    print("💡 질문을 입력하면 관련 정보를 찾아드립니다.")
    print("🚪 종료하려면 'quit', 'exit', '종료'를 입력하세요.")
    print("📚 전체 문서를 보려면 'all'을 입력하세요.")
    print("🔍 카테고리별 검색을 하려면 'category [카테고리명]'을 입력하세요.")
    
    while True:
        try:
            # 사용자 입력 받기
            user_input = input("\n❓ 질문을 입력하세요: ").strip()
            
            if not user_input:
                continue
            
            # 종료 명령어 확인
            if user_input.lower() in ['quit', 'exit', '종료', 'q']:
                print("👋 챗봇 서비스를 종료합니다. 안녕히 가세요!")
                break
            
            # 전체 문서 조회
            if user_input.lower() == 'all':
                print("\n📚 전체 문서를 조회합니다...")
                all_docs = chatbot.get_all_docs()
                print(f"총 {len(all_docs)}건의 문서가 있습니다.")
                continue
            
            # 카테고리별 검색
            if user_input.lower().startswith('category '):
                category = user_input[9:].strip()  # 'category ' 제거
                if category:
                    print(f"\n🏷️ '{category}' 카테고리로 검색합니다...")
                    results = chatbot.search_by_category(category)
                    display_search_results(results)
                    
                    # 첫 번째 결과의 URL 열기 여부 확인
                    if results:
                        open_first_result_url(results[0])
                continue
            
            # 일반 검색 수행
            print(f"\n🔍 '{user_input}'로 검색 중...")
            results = chatbot.search(user_input, size=5)
            
            # 검색 결과 출력
            display_search_results(results)
            
            # 검색 결과가 있으면 URL 열기 옵션 제공
            if results:
                open_first_result_url(results[0])
            
        except KeyboardInterrupt:
            print("\n\n👋 Ctrl+C로 서비스를 종료합니다. 안녕히 가세요!")
            break
        except Exception as e:
            print(f"❌ 오류 발생: {e}")
            print("🔧 다시 시도해주세요.")

def open_first_result_url(result: Dict[str, Any]) -> None:
    """첫 번째 검색 결과의 URL 열기"""
    api_call = result.get('api_call', '')
    if api_call:
        print(f"\n🌐 이 결과의 API URL을 열까요?")
        print(f"URL: {api_call}")
        
        while True:
            choice = input("열기? (y/n): ").strip().lower()
            if choice in ['y', 'yes', '네', '예']:
                open_url(api_call)
                break
            elif choice in ['n', 'no', '아니오']:
                print("URL을 열지 않습니다.")
                break
            else:
                print("y 또는 n을 입력해주세요.")

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
        
        # 대화형 검색 시작
        interactive_search(chatbot)
        
    except Exception as e:
        print(f"❌ 오류 발생: {e}")
        print("\n🔧 해결 방법:")
        print("   1. Elasticsearch가 실행 중인지 확인")
        print("   2. 'python data_initializer.py'를 먼저 실행하여 데이터를 설정하세요")
        sys.exit(1)

if __name__ == "__main__":
    main()
