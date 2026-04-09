@echo off
chcp 65001 >nul
echo 한글이 포함된 경로는 에러가 발생할 수 있습니다.
echo ================================================

:: npm 캐시 정리
call npm cache clean --force
if %ERRORLEVEL% neq 0 (
    echo "npm cache clean 중 에러가 발생했지만, 계속 진행합니다."
)

:: package-lock.json 삭제
call del package-lock.json
if %ERRORLEVEL% neq 0 (
    echo "package-lock.json 삭제 중 에러가 발생했지만, 계속 진행합니다."
)

:: npm 패키지 설치
call npm install
if %ERRORLEVEL% neq 0 (
    echo "npm install 중 에러가 발생했지만, 계속 진행합니다."
)

:: 개발 서버 실행
call npm run dev
if %ERRORLEVEL% neq 0 (
    echo "npm run dev 중 에러가 발생했지만, 계속 진행합니다."
)

echo 모든 작업이 완료되었습니다.
pause
