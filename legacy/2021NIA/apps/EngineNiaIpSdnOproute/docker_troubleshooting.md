# Docker 컨테이너 문제 해결 가이드

## 1. 컨테이너 상태 확인
```bash
docker ps -a | grep ipsdn_opt_1
```

## 2. 컨테이너 로그 확인 (최근 100줄)
```bash
docker logs --tail 100 ipsdn_opt_1
```

## 3. 실시간 로그 확인
```bash
docker logs -f ipsdn_opt_1
```

## 4. 컨테이너 내부 접속
```bash
docker exec -it ipsdn_opt_1 /bin/bash
# 또는
docker exec -it ipsdn_opt_1 /bin/sh
```

## 5. 애플리케이션 로그 파일 확인 (컨테이너 내부)
```bash
docker exec -it ipsdn_opt_1 ls -la /app/logs/
docker exec -it ipsdn_opt_1 tail -f /app/logs/application.log
```

## 6. 스케줄러 실행 확인
```bash
# 스케줄러 로그에서 "calculateDayDataAndOptimizeRoute" 검색
docker logs ipsdn_opt_1 | grep -i "calculateDayDataAndOptimizeRoute"
docker logs ipsdn_opt_1 | grep -i "latencyCheckAllNode"
```

## 7. 데이터베이스 연결 확인
```bash
# 컨테이너 내부에서 DB 연결 테스트
docker exec -it ipsdn_opt_1 ping -c 3 203.255.249.31
```

## 8. 최근 nodefactor 데이터 확인 (DB 직접 접속)
```bash
# MySQL/MariaDB 클라이언트가 있다면
mysql -h 203.255.249.31 -P 3306 -u optroute -p optroute
# 그 다음 SQL 실행:
SELECT * FROM nodefactor ORDER BY measureddatetime DESC LIMIT 10;
```

## 9. 애플리케이션 설정 확인
```bash
# application-prod.yml 확인
docker exec -it ipsdn_opt_1 cat /app/config/application-prod.yml
# 또는
docker exec -it ipsdn_opt_1 env | grep -i spring
```

## 10. 프로세스 확인
```bash
docker exec -it ipsdn_opt_1 ps aux
```

## 11. 네트워크 연결 확인
```bash
# 컨테이너에서 probe 서버로 연결 테스트
docker exec -it ipsdn_opt_1 curl -v http://[probe_ip]:[probe_port]/ipsdn/opt/probe/linklatency?measured_time=2025-01-01T00:00:00
```

## 12. 메모리/CPU 사용량 확인
```bash
docker stats ipsdn_opt_1
```

## 문제 해결 체크리스트

1. ✅ 컨테이너가 실행 중인가? (`docker ps`)
2. ✅ 애플리케이션이 정상적으로 시작되었는가? (`docker logs`)
3. ✅ 스케줄러가 활성화되어 있는가? (`@Profile("prod")` 확인)
4. ✅ `automeasurement` 설정이 true인가? (DB의 settings 테이블 확인)
5. ✅ probe 서버들이 정상적으로 동작하는가?
6. ✅ DB 연결이 정상적인가? (203.255.249.31:3306)
7. ✅ 최근 nodefactor 데이터가 있는가? (DB 직접 확인)
