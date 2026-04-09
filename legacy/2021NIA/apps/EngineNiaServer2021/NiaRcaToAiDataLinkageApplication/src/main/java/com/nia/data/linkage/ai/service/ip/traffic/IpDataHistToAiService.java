package com.nia.data.linkage.ai.service.ip.traffic;

import java.io.File;

public interface IpDataHistToAiService {

    void sendData();

    File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath);
}
