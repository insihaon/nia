package com.nia.data.linkage.ipsdn.service.ipsdn.traffic;

import java.io.File;

public interface IpSdnTrafficLinkageService {

    void sendTrafficData();

    File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath);
}
