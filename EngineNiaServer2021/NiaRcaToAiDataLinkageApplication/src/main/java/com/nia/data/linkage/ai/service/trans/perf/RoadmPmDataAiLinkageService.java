package com.nia.data.linkage.ai.service.trans.perf;

import java.io.File;

public interface RoadmPmDataAiLinkageService {
    void sendRoadmPmData();
    File createJsonFile(String eventType, String jsonData, String ocrtime, String ftpUpdatePath);
}
