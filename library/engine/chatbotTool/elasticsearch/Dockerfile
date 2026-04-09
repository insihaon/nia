# 1) 공식 Elasticsearch 이미지 기반
FROM docker.elastic.co/elasticsearch/elasticsearch:8.6.2

############################################################
# root 권한으로 스크립트 복사 및 실행 권한 부여
############################################################
USER root

# init 스크립트 복사
COPY init-api-key.sh /usr/local/bin/init-api-key.sh

# 실행 권한 추가
RUN chmod +x /usr/local/bin/init-api-key.sh

############################################################
# 다시 elasticsearch 비(非)root 사용자로 전환
############################################################
USER elasticsearch

# 4) 환경변수(예시)
#   - ELASTIC_PASSWORD: elastic 계정 비밀번호 (필수)
# ENV ELASTIC_PASSWORD=changeme123

# 5) 커스텀 엔트리포인트 지정
ENTRYPOINT ["/usr/local/bin/init-api-key.sh"]
