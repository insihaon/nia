MBA - Momentary Breakoff Analysis (순단 분석)                                                             
- 매퍼 L107: root_cause_type = 'MomentaryBreakoff'                                                     
- Vue: 순단장애 감시 화면, MbaTicket 컴포넌트
- 광케이블의 순간적 신호 단절을 분석하는 도메인

PM - Predictive Maintenance (예지보전) 

- 테이블명: tb_roadm_predictive_maintenance_ticket
- Vue: PredictiveMaintenanceTicket.vue, 화면 타이틀 "광모듈 장애 예측"
- 광레벨 변화 추이를 분석하여 장애를 사전 예측

ROADM - Reconfigurable Optical Add-Drop Multiplexer (재구성 가능 광 분기/결합 다중화기)

- 통신 업계 표준 약어 (NIA 고유가 아님)
- 광통신 네트워크에서 파장 단위로 신호를 추가/삭제하는 장비
- 코드에서 ROADM, POTN, MSPP, PTS 등 장비 유형이 구분됨 (L25~29)
- tb_roadm_trunk_topology는 ROADM 장비 간 트렁크 경로(토폴로지)를 나타냄