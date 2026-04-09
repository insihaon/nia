#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
데이터 초기화 모듈
- 최초 한 번만 실행
- CSV 데이터 로드 및 Elasticsearch 색인
- 초기 설정 완료 후 종료
"""

import logging
import sys

from components.config import config
from components.elasticsearch_client import test_connection, get_es_client, create_index, check_data_exists
from components.data_loader import initialize_data, get_data_count

# ─── 로깅 설정 ───────────────────────────────────────────────────────────────
logging.basicConfig(
    level=logging.INFO,
    format='[%(asctime)s] %(levelname)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)

def main() -> None:
    """데이터 초기화 메인 함수"""
    print("🚀 Elasticsearch 데이터 초기화를 시작합니다...")
    
    try:
        # 1. Elasticsearch 연결 테스트
        print("1️⃣ Elasticsearch 연결 테스트 중...")
        test_connection()
        print("✅ 연결 성공!")
        
        # 2. Elasticsearch 클라이언트 생성
        print("2️⃣ Elasticsearch 클라이언트 생성 중...")
        es = get_es_client()
        print("✅ 클라이언트 생성 완료!")
        
        # 3. 인덱스 생성
        print("3️⃣ 인덱스 생성 중...")
        create_index(es)
        print("✅ 인덱스 생성 완료!")
        
        # 4. 데이터 존재 여부 확인
        print("4️⃣ 데이터 상태 확인 중...")
        if check_data_exists(es):
            current_count = get_data_count()
            print(f"ℹ️ 이미 데이터가 설정되어 있습니다. (현재 {current_count}건)")
            print("🔄 데이터를 다시 설정하려면 기존 데이터를 삭제하세요.")
            return
        
        # 5. CSV 데이터 로드 및 색인
        print("5️⃣ CSV 데이터 로드 및 색인 중...")
        if initialize_data():
            final_count = get_data_count()
            print(f"✅ 데이터 초기화 완료! (총 {final_count}건)")
        else:
            print("❌ 데이터 초기화 실패!")
            sys.exit(1)
        
        # 6. 완료 메시지
        print("\n" + "="*50)
        print("🎉 데이터 초기화가 완료되었습니다!")
        print("="*50)
        print("📋 다음 단계:")
        print("   1. Elasticsearch가 실행 중인지 확인")
        print("   2. 'python main.py'로 챗봇 서비스를 실행하세요")
        print("="*50)
        
    except Exception as e:
        print(f"❌ 오류 발생: {e}")
        print("🔧 해결 방법:")
        print("   1. Elasticsearch가 실행 중인지 확인")
        print("   2. 네트워크 연결 상태 확인")
        print("   3. 로그를 확인하여 상세 오류 파악")
        sys.exit(1)

if __name__ == "__main__":
    main()
