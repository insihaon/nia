package com.nia.data.linkage.ip.sflow.service;

import java.io.File;

public interface SflowToAiLinkageService {

    void sendSflowLogData();
    File createJsonFile(String eventType, String jsonData, String faultEventKey);
}
