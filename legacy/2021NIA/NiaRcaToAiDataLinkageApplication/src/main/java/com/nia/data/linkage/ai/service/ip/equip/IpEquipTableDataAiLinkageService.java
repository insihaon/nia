package com.nia.data.linkage.ai.service.ip.equip;

import java.io.File;

public interface IpEquipTableDataAiLinkageService {
    void sendBackBoneLinkData();
    void sendNodeData();
    void sendPortData();
    void sendCvnmsResourceData();
    void sendCvnmsResourceIfData();
    File createJsonFile(String eventType, String jsonData, String perfKey);
}
