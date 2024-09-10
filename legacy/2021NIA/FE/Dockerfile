# FE/Dockerfile

# Stage 1: Build Stage
FROM node:14 AS build

# 작업 디렉토리 설정
WORKDIR /app

# npm 버전 업데이트
RUN npm install -g npm@9.6.7

# 패키지 파일 복사 및 종속성 설치
COPY package*.json ./
RUN npm install --force

# 애플리케이션 소스 코드 복사
COPY . .

# 애플리케이션 빌드
RUN npm run build:ipms

# Stage 2: Production Stage
# Nginx + alpine Linux 기반 이미지 사용
FROM nginx:alpine

ENV NODE_ENV=production



### AJP(8009) -> 리버스프록시(8080) 로 변경함 <<< 

# # Alpine Linux 기반에서 필요한 빌드 도구 및 라이브러리 설치
# RUN apk add --no-cache \
#     wget \
#     build-base \
#     pcre \
#     pcre-dev \
#     zlib-dev \
#     git \
#     autoconf \
#     automake \
#     libtool \
#     alpine-sdk \
#     libc-dev \
#     linux-headers

# # OpenSSL 1.1.x 소스 다운로드 및 설치
# WORKDIR /usr/local/src
# RUN wget https://www.openssl.org/source/openssl-1.1.1k.tar.gz
# RUN tar -zxvf openssl-1.1.1k.tar.gz
# WORKDIR /usr/local/src/openssl-1.1.1k
# RUN ./config
# RUN make
# RUN make install

# # Nginx 소스 및 AJP 모듈 다운로드
# WORKDIR /usr/local/src
# RUN wget http://nginx.org/download/nginx-1.18.0.tar.gz
# RUN tar -zxvf nginx-1.18.0.tar.gz
# RUN git clone https://github.com/yaoweibin/nginx_ajp_module.git

# # Nginx 컴파일 및 AJP 모듈 추가
# WORKDIR /usr/local/src/nginx-1.18.0
# RUN ./configure --add-module=../nginx_ajp_module \
#     --with-http_ssl_module \
#     --with-http_v2_module \
#     --with-http_stub_status_module \
#     --with-openssl=/usr/local/src/openssl-1.1.1k
# RUN make
# RUN make install

### AJP 사용 >>>




# 빌드된 앱을 Nginx의 기본 경로로 복사
COPY --from=build ../BE/app-ipms/src/main/resources/static /usr/share/nginx/html
RUN date > /usr/share/nginx/html/version.txt && \
    uname -a >> /usr/share/nginx/html/version.txt

# Nginx 설정 파일 복사
COPY nginx.conf /etc/nginx/nginx.conf

# RUN nginx -t

# 컨테이너가 노출할 포트 지정
EXPOSE 80

# Nginx 실행
# CMD ["nginx", "-g", "daemon off;"]