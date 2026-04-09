#!/bin/bash
echo "=== NodeFactor 수집 관련 로그 확인 ==="
echo ""

echo "1. StartConfig 실행 여부 확인:"
docker logs ipsdn_opt_1 2>&1 | grep -i "StartConfig\|automeasurement" | tail -10
echo ""

echo "2. Auto-Collection 관련 로그:"
docker logs ipsdn_opt_1 2>&1 | grep -i "Auto-Collection" | tail -20
echo ""

echo "3. latencyCheckAllNode 호출 확인:"
docker logs ipsdn_opt_1 2>&1 | grep -i "latencyCheckAllNode\|All Node Latency Measuring" | tail -20
echo ""

echo "4. Probe API 호출 관련 (linklatency):"
docker logs ipsdn_opt_1 2>&1 | grep -i "linklatency\|probe.*Call" | tail -20
echo ""

echo "5. NodeFactor 저장 관련 (probe 측):"
docker logs ipsdn_opt_1 2>&1 | grep -i "nodefactor\|Node.*Measuring.*complete" | tail -20
echo ""

echo "6. 최근 에러 로그 (latencyCheckAllNode 관련):"
docker logs ipsdn_opt_1 2>&1 | grep -i "latency.*fail\|probe.*fail\|node.*fail" | tail -20
echo ""

echo "7. 최근 전체 로그 (마지막 50줄):"
docker logs ipsdn_opt_1 2>&1 | tail -50
