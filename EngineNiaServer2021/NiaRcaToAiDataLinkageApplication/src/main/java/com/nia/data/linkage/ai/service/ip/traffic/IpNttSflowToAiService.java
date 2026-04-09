package com.nia.data.linkage.ai.service.ip.traffic;

import java.io.File;

public interface IpNttSflowToAiService {

    void sendNttSflowData();

    File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath);
}
