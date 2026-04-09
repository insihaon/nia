package com.nia.data.linkage.ai.service.ip.sflow;

import java.io.File;

public interface IpSflowToAiLinkageService {

    void sendSflowLogData();
    File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath);
}
