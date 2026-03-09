package com.nia.data.linkage.ai.service.ip.traffic;

import java.io.File;

public interface IpDataToAiService {

    void sendData();

    File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath);
}
