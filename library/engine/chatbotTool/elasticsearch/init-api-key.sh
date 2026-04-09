#!/usr/bin/env bash
set -euo pipefail

# ------------------------------------------------------------
# init-api-key.sh (버전 8 수정)
# - API Key 파일을 데이터 볼륨에 저장해 유지
# - 매번 docker-compose up시 API Key(encoded) 로그 출력
# - 최초 실행시 키 발급 후 저장, 이후 스킵하여 즉시 ES 기동
# - ES 기동 후 테스트(Index/Create/Search) 자동 실행
# ------------------------------------------------------------

# 기본 설정
ES_USER="elastic"

# 비밀번호 로드 (Secrets 또는 ENV)
if [[ -n "${ELASTIC_PASSWORD_FILE:-}" && -f "${ELASTIC_PASSWORD_FILE}" ]]; then
  ES_PASSWORD=$(<"${ELASTIC_PASSWORD_FILE}")
elif [[ -n "${ELASTIC_PASSWORD:-}" ]]; then
  ES_PASSWORD="$ELASTIC_PASSWORD"
else
  echo "[ERROR] ELASTIC_PASSWORD_FILE 또는 ELASTIC_PASSWORD가 설정되지 않았습니다."
  exit 1
fi

# 프로토콜 및 인증 설정
PROTOCOL="https"
CURL_AUTH="-k -u ${ES_USER}:${ES_PASSWORD}"
if [[ "${ES_XPACK_SECURITY_ENABLED:-true}" == "false" ]]; then
  PROTOCOL="http"
  CURL_AUTH=""
fi
ES_HOST="${PROTOCOL}://localhost:9200"

# 파일 경로 및 인덱스 설정
DATA_DIR="/usr/share/elasticsearch/data"
OUTPUT_FILE="${DATA_DIR}/api_key.json"
API_NAME="docker-auto-key"
TEST_INDEX="test-index"

echo "1) API Key 파일 존재시: 키 출력 후 ES 바로 실행"
if [[ -f "${OUTPUT_FILE}" ]]; then
  echo "[INFO] Existing API Key found. Skipping creation."
  if command -v jq >/dev/null 2>&1; then
    encoded_key=$(jq -r .encoded < "${OUTPUT_FILE}")
  else
    encoded_key=$(grep -o '"encoded"[[:space:]]*:[[:space:]]*"[^"]*"' "${OUTPUT_FILE}" \
      | sed -E 's/"encoded" *: *"([^"]*)"/\1/')
  fi
  echo "======================================================================================="
  echo "[INFO] Elasticsearch API Key (encoded): ${encoded_key}"
  echo "======================================================================================="
  echo "[INFO] To test with curl:"
  echo "  curl ${CURL_AUTH} -s -H 'Authorization: ApiKey ${encoded_key}' '${ES_HOST}/_security/api_key?owner=true&pretty'"
  echo "======================================================================================="
  exec /usr/local/bin/docker-entrypoint.sh eswrapper
fi

echo "2) 최초 실행: ES 백그라운드 기동 및 가용성 대기"
echo "[INFO] Starting Elasticsearch..."
/usr/local/bin/docker-entrypoint.sh eswrapper &
ES_PID=$!

echo "[INFO] Waiting for Elasticsearch at ${ES_HOST}..."
until curl ${CURL_AUTH} -s -o /dev/null -w "%{http_code}" "${ES_HOST}" | grep -qE '^[23]..$'; do
  echo "[DEBUG] Elasticsearch not ready, retrying..."
  sleep 2
done

echo "[INFO] Elasticsearch is up!"

echo "3) API Key 발급 및 저장"
echo "[INFO] No existing API Key found. Creating new API Key..."
response=$(curl ${CURL_AUTH} -s \
  -X POST "${ES_HOST}/_security/api_key?pretty" \
  -H 'Content-Type: application/json' \
  -d "{\
    \"name\": \"${API_NAME}\",\
    \"role_descriptors\": {\
      \"full_manager\": {\
        \"cluster\":[\"manage\",\"manage_api_key\",\"read_security\"],\
        \"index\": [{\"names\":[\"*\"],\"privileges\":[\"all\"]}]\
      }\
    }\
  }")
echo "${response}" > "${OUTPUT_FILE}"
echo "[INFO] API Key created and saved to ${OUTPUT_FILE}"

echo "4) 키 출력 및 테스트 안내"
echo "======================================================================================="
if command -v jq >/dev/null 2>&1; then
  encoded_key=$(jq -r .encoded < "${OUTPUT_FILE}")
else
  encoded_key=$(grep -o '"encoded"[[:space:]]*:[[:space:]]*"[^"]*"' "${OUTPUT_FILE}" \
    | sed -E 's/"encoded" *: *"([^"]*)"/\1/')
fi
cat <<EOF
[INFO] Elasticsearch API Key (encoded): ${encoded_key}
[INFO] To test with curl:
  curl ${CURL_AUTH} -s -H 'Authorization: ApiKey ${encoded_key}' \
       '${ES_HOST}/_security/api_key?owner=true&pretty'
=======================================================================================
EOF

# # 5) 테스트: 인덱스 생성
# echo "[INFO] Testing index creation for '${TEST_INDEX}'..."
# echo "  curl ${CURL_AUTH} -s -X PUT '${ES_HOST}/${TEST_INDEX}?pretty' -H 'Content-Type: application/json' -d '{\"settings\":{\"number_of_shards\":1},\"mappings\":{\"properties\":{\"msg\":{\"type\":\"text\"}}}}'"
# if curl ${CURL_AUTH} -s -X PUT "${ES_HOST}/${TEST_INDEX}?pretty" -H 'Content-Type: application/json' -d '{"settings":{"number_of_shards":1},"mappings":{"properties":{"msg":{"type":"text"}}}}'; then
#   echo "[INFO] Index '${TEST_INDEX}' created"
# else
#   echo "[ERROR] Index create failed"
# fi

# # 6) 문서 색인
# echo "[INFO] Testing document indexing for '${TEST_INDEX}'..."
# echo "  curl ${CURL_AUTH} -s -X POST '${ES_HOST}/${TEST_INDEX}/_doc/1?pretty' -H 'Content-Type: application/json' -d '{\"msg\":\"Hello, Elasticsearch with API Key!\"}'"
# if curl ${CURL_AUTH} -s -X POST "${ES_HOST}/${TEST_INDEX}/_doc/1?pretty" -H 'Content-Type: application/json' -d '{"msg":"Hello, Elasticsearch with API Key!"}'; then
#   echo "[INFO] Document indexed"
# else
#   echo "[ERROR] Document indexing failed"
# fi

# # 7) 검색
# echo "[INFO] Testing search for '${TEST_INDEX}'..."
# echo "  curl ${CURL_AUTH} -s -X GET '${ES_HOST}/${TEST_INDEX}/_search?q=Hello&pretty'"
# if curl ${CURL_AUTH} -s -X GET "${ES_HOST}/${TEST_INDEX}/_search?q=Hello&pretty"; then
#   echo "[INFO] Search completed"
# else
#   echo "[ERROR] Search failed"
# fi

echo "======================================================================================="

# 8) ES 프로세스 대기
wait ${ES_PID}
