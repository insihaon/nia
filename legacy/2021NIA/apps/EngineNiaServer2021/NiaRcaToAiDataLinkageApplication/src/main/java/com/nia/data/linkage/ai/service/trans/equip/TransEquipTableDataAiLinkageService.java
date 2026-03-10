package com.nia.data.linkage.ai.service.trans.equip;

import java.io.File;

public interface TransEquipTableDataAiLinkageService {
    void sendEquipMstData();
    void sendEquipPortData();
    void sendEquipSlotData();
    void sendNniTopologyData();
    void sendUniTopologyData();
    void sendRoadmTrunkData();
    File createJsonFile(String eventType, String jsonData, String perfKey);
}
