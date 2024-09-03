# FE/Dockerfile

# Stage 1: Build Stage
FROM node:14 AS build

# 작업 디렉토리 설정
WORKDIR /app

# npm 버전 8.5.4으로 업데이트
RUN npm install -g npm@8.5.4

# 패키지 파일 복사 및 종속성 설치
COPY package*.json ./
RUN npm install

# 애플리케이션 소스 코드 복사
COPY . .

# 애플리케이션 빌드
RUN npm run build:ipms

# Stage 2: Production Stage
FROM nginx:alpine

# 빌드된 앱을 Nginx의 기본 경로로 복사
COPY --from=build /app/build /usr/share/nginx/html

# Nginx 설정 파일 복사
COPY nginx.conf /etc/nginx/nginx.conf

# 컨테이너가 노출할 포트 지정
EXPOSE 80

# Nginx 실행
# CMD ["nginx", "-g", "daemon off;"]