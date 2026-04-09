#!/bin/bash

# Elasticsearch 설정
ES_URL="http://localhost:9200"
ES_USER="elastic"
ES_PASS="changeme123"
INDEX_NAME="chatbot-qa"

# 첫 번째 줄 데이터 (chatbot.data의 첫 번째 줄)
QUESTION="현재 발생한 장애를 보여줘"
ANSWER="http://116.89.191.47:8080/fault/current?status=active"
CATEGORY="장애 조회"
API_CALL="/fault/current?status=active"
DESCRIPTION="현재 활성 장애 조회"
CREATED=$(date -u +"%Y-%m-%dT%H:%M:%S.%3NZ")

echo "🚀 Elasticsearch에 단일 데이터를 입력합니다..."

# Elasticsearch 연결 테스트
echo "1️⃣ Elasticsearch 연결 테스트 중..."
if curl -s -u "$ES_USER:$ES_PASS" "$ES_URL/_cluster/health" > /dev/null; then
    echo "✅ 연결 성공!"
else
    echo "❌ Elasticsearch 연결 실패!"
    echo "🔧 Elasticsearch가 실행 중인지 확인하세요."
    exit 1
fi

# 인덱스 존재 여부 확인 및 생성
echo "2️⃣ 인덱스 확인 및 생성 중..."
if ! curl -s -u "$ES_USER:$ES_PASS" "$ES_URL/$INDEX_NAME" > /dev/null; then
    echo "📝 인덱스가 존재하지 않습니다. 생성 중..."
    
    # 인덱스 생성 (매핑 포함)
    curl -X PUT -u "$ES_USER:$ES_PASS" \
        "$ES_URL/$INDEX_NAME" \
        -H "Content-Type: application/json" \
        -d '{
            "settings": {
                "analysis": {
                    "filter": {
                        "custom_stop_filter": {
                            "type": "stop",
                            "stopwords": ["보고", "싶어", "출력해줘", "알려줘", "보여줘"]
                        }
                    },
                    "analyzer": {
                        "default": {
                            "type": "custom",
                            "tokenizer": "standard",
                            "filter": ["lowercase", "custom_stop_filter"]
                        },
                        "custom_analyzer": {
                            "type": "custom",
                            "tokenizer": "standard",
                            "filter": ["lowercase", "custom_stop_filter"]
                        }
                    }
                }
            },
            "mappings": {
                "properties": {
                    "question": {"type": "text", "analyzer": "custom_analyzer"},
                    "answer": {"type": "text", "index": false},
                    "category": {"type": "keyword"},
                    "api_call": {"type": "keyword"},
                    "description": {"type": "text", "index": false},
                    "created": {"type": "date"}
                }
            }
        }'
    
    if [ $? -eq 0 ]; then
        echo "✅ 인덱스 생성 완료!"
    else
        echo "❌ 인덱스 생성 실패!"
        exit 1
    fi
else
    echo "✅ 인덱스가 이미 존재합니다."
fi

# 데이터 입력
echo "3️⃣ 데이터 입력 중..."
curl -X POST -u "$ES_USER:$ES_PASS" \
    "$ES_URL/$INDEX_NAME/_doc/1" \
    -H "Content-Type: application/json" \
    -d "{
        \"question\": \"$QUESTION\",
        \"answer\": \"$ANSWER\",
        \"category\": \"$CATEGORY\",
        \"api_call\": \"$API_CALL\",
        \"description\": \"$DESCRIPTION\",
        \"created\": \"$CREATED\"
    }"

if [ $? -eq 0 ]; then
    echo "✅ 데이터 입력 완료!"
else
    echo "❌ 데이터 입력 실패!"
    exit 1
fi

# 입력된 데이터 확인
echo "4️⃣ 입력된 데이터 확인 중..."
curl -s -u "$ES_USER:$ES_PASS" "$ES_URL/$INDEX_NAME/_doc/1" | jq '._source' 2>/dev/null || \
curl -s -u "$ES_USER:$ES_PASS" "$ES_URL/$INDEX_NAME/_doc/1"

echo ""
echo "🎉 단일 데이터 입력이 완료되었습니다!"
echo "📊 인덱스: $INDEX_NAME"
echo "🔍 Kibana에서 확인: $ES_URL/_plugin/kibana"
