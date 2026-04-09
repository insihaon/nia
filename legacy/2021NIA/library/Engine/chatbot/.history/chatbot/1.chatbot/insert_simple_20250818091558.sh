#!/bin/bash

# 환경 변수로 설정 가능 (기본값 제공)
ES_URL=${ES_URL:-"http://localhost:9200"}
ES_USER=${ES_USER:-"elastic"}
ES_PASS=${ES_PASS:-"changeme123"}
INDEX_NAME=${INDEX_NAME:-"chatbot-qa"}

echo "🚀 Elasticsearch에 단일 데이터 입력 중..."
echo "📍 URL: $ES_URL"
echo "👤 사용자: $ES_USER"
echo "📊 인덱스: $INDEX_NAME"

# 첫 번째 줄 데이터 입력
curl -X POST -u "$ES_USER:$ES_PASS" \
    "$ES_URL/$INDEX_NAME/_doc/1" \
    -H "Content-Type: application/json" \
    -d '{
        "question": "현재 발생한 장애를 보여줘",
        "answer": "http://116.89.191.47:8080/fault/current?status=active",
        "category": "장애 조회",
        "api_call": "/fault/current?status=active",
        "description": "현재 활성 장애 조회",
        "created": "'$(date -u +"%Y-%m-%dT%H:%M:%S.%3NZ")'"
    }'

echo ""
echo "✅ 데이터 입력 완료!"
