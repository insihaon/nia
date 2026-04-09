#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Elasticsearch 상태 확인 스크립트
- 연결 상태 확인
- 인덱스 목록 확인
- 데이터 개수 확인
"""

import requests
import json

def check_elasticsearch_status():
    """Elasticsearch 상태 확인"""
    base_url = "http://localhost:9200"
    
    try:
        # 1. 기본 연결 확인
        print("🔍 Elasticsearch 연결 확인 중...")
        response = requests.get(f"{base_url}/")
        if response.status_code == 200:
            info = response.json()
            print(f"✅ 연결 성공!")
            print(f"   클러스터: {info.get('cluster_name', 'N/A')}")
            print(f"   버전: {info.get('version', {}).get('number', 'N/A')}")
        else:
            print(f"❌ 연결 실패: {response.status_code}")
            return
        
        # 2. 인덱스 목록 확인
        print("\n📋 인덱스 목록 확인 중...")
        response = requests.get(f"{base_url}/_cat/indices?format=json&v")
        if response.status_code == 200:
            indices = response.json()
            if indices:
                print(f"✅ 인덱스 {len(indices)}개 발견:")
                for idx in indices:
                    print(f"   - {idx['index']}: {idx['docs.count']}개 문서, {idx['store.size']}")
            else:
                print("ℹ️ 인덱스가 없습니다.")
        else:
            print(f"❌ 인덱스 목록 조회 실패: {response.status_code}")
        
        # 3. my-index 상태 확인
        print("\n🎯 my-index 상태 확인 중...")
        response = requests.get(f"{base_url}/my-index")
        if response.status_code == 200:
            index_info = response.json()
            print(f"✅ my-index 존재함")
            
            # 문서 개수 확인
            count_response = requests.get(f"{base_url}/my-index/_count")
            if count_response.status_code == 200:
                count_info = count_response.json()
                doc_count = count_info.get('count', 0)
                print(f"   📊 문서 개수: {doc_count}개")
                
                if doc_count == 0:
                    print("   ⚠️ 문서가 없습니다. data_initializer.py를 실행해야 합니다.")
                else:
                    print("   ✅ 데이터가 정상적으로 들어있습니다.")
                    
                    # 실제 데이터 샘플 확인
                    print(f"\n📝 데이터 샘플 확인 중...")
                    sample_response = requests.get(f"{base_url}/my-index/_search?size=3")
                    if sample_response.status_code == 200:
                        sample_data = sample_response.json()
                        hits = sample_data.get('hits', {}).get('hits', [])
                        print(f"   📋 처음 3개 문서:")
                        for i, hit in enumerate(hits, 1):
                            source = hit['_source']
                            print(f"     {i}. 질문: {source.get('question', 'N/A')}")
                            print(f"        카테고리: {source.get('category', 'N/A')}")
                            print(f"        API: {source.get('api_call', 'N/A')}")
                            print()
            else:
                print(f"   ❌ 문서 개수 확인 실패: {count_response.status_code}")
                
        elif response.status_code == 404:
            print("❌ my-index가 존재하지 않습니다.")
            print("   💡 data_initializer.py를 실행하여 인덱스를 생성하세요.")
        else:
            print(f"❌ my-index 상태 확인 실패: {response.status_code}")
            
    except requests.exceptions.ConnectionError:
        print("❌ Elasticsearch에 연결할 수 없습니다.")
        print("   🔧 Elasticsearch가 실행 중인지 확인하세요.")
    except Exception as e:
        print(f"❌ 오류 발생: {e}")

if __name__ == "__main__":
    check_elasticsearch_status()
