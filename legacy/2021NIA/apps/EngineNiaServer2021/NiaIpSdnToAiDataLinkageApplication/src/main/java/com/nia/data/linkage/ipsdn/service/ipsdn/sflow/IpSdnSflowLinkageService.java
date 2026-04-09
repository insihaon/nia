package com.nia.data.linkage.ipsdn.service.ipsdn.sflow;

import java.io.File;

public interface IpSdnSflowLinkageService {

    void sendSflowData();

    File createJsonFile(String eventType, String jsonData,String dataKey, String ftpUpdatePath);
}
