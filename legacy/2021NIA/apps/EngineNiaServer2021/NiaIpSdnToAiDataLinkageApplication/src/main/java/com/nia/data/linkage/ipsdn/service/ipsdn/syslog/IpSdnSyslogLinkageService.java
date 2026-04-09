package com.nia.data.linkage.ipsdn.service.ipsdn.syslog;

import java.io.File;

public interface IpSdnSyslogLinkageService {

    void sendSyslogData();

    File createJsonFile(String eventType, String jsonData,String dataKey, String ftpUpdatePath);
}
