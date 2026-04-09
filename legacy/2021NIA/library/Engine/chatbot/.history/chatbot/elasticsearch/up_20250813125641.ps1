# Elasticsearch 실행 스크립트 (PowerShell)
Write-Host "Elasticsearch를 시작합니다..." -ForegroundColor Green

# Docker Compose 실행
docker-compose up --build

Write-Host "Elasticsearch가 시작되었습니다." -ForegroundColor Green
Write-Host "Elasticsearch: http://localhost:9200" -ForegroundColor Yellow
Write-Host "Kibana: http://localhost:5601" -ForegroundColor Yellow
Write-Host "계정: elastic / changeme123" -ForegroundColor Yellow
