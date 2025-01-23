package com.nia.data.linkage.ai.service.ip.traffic;

import java.io.File;
import java.sql.Timestamp;

public interface IpAttTrafficToAiService {

    void sendAttTrafficData();

    File createJsonFile(String eventType, String jsonData, Timestamp perfKey, String ftpUpdatePath);
}
