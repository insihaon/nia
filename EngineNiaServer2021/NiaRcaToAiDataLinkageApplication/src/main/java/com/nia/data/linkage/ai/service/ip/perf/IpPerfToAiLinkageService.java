package com.nia.data.linkage.ai.service.ip.perf;

import java.io.File;

public interface IpPerfToAiLinkageService {

    void sendPerfLogData();
    File createJsonFile(String eventType, String jsonData, String perfKey);
}
