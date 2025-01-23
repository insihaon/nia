package com.nia.data.linkage.ai.service.ip.sop;

import org.springframework.stereotype.Service;

import java.io.File;

public interface IpSopToAiService {

    void sendSopData();

    File createJsonFile(String eventType, String jsonData, String dataKey, String ftpUpdatePath);
}
