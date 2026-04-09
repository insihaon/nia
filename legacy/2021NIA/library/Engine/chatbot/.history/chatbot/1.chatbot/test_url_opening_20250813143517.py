#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
URL 열기 기능 테스트 스크립트
- test.py.1의 URL 열기 기능을 테스트
- 다양한 URL 형식으로 테스트
"""

import webbrowser

def test_url_opening():
    """URL 열기 기능 테스트"""
    print("🌐 URL 열기 기능을 테스트합니다...")
    
    # 테스트할 URL들
    test_urls = [
        "http://116.89.191.47:8080/fault/current?status=active",
        "/fault/current?status=active",
        "fault/current?status=active",
        "http://localhost:9200",
        "https://www.google.com"
    ]
    
    for i, url in enumerate(test_urls, 1):
        print(f"\n📋 테스트 {i}: {url}")
        
        try:
            # URL이 http:// 또는 https://로 시작하는지 확인
            if not url.startswith(('http://', 'https://')):
                # 상대 경로인 경우 기본 도메인 추가
                if url.startswith('/'):
                    full_url = f"http://116.89.191.47:8080{url}"
                else:
                    full_url = f"http://116.89.191.47:8080/{url}"
            else:
                full_url = url
            
            print(f"  완성된 URL: {full_url}")
            
            # 사용자 확인
            choice = input("  이 URL을 열까요? (y/n): ").strip().lower()
            
            if choice in ['y', 'yes', '네', '예']:
                print(f"  🌐 브라우저에서 열기: {full_url}")
                webbrowser.open(full_url)
                print("  ✅ URL 열기 완료!")
            else:
                print("  ❌ URL을 열지 않습니다.")
                
        except Exception as e:
            print(f"  ❌ 오류 발생: {e}")
    
    print("\n🎉 URL 열기 테스트 완료!")

if __name__ == "__main__":
    test_url_opening()
