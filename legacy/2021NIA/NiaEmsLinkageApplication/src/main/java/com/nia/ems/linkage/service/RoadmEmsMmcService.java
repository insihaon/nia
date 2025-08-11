package com.nia.ems.linkage.service;

public interface RoadmEmsMmcService {
    void roadmSipcMMC();
    void roadmPmMMC();
    void roadmNetWorkMmc();
    void roadmAlarmMMC();
    void roadmSlotMMC();
    void createRoadmUniTopology();
    void getSystemList();
    void updateSystemInfo(); // 광장비 정보 리스트 정보 수집
}
