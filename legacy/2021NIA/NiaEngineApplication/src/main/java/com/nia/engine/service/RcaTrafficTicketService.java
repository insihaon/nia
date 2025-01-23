package com.nia.engine.service;

import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficListVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrafficListVo;
import com.nia.engine.vo.sdn.factor.NodeFactorListVo;
import com.nia.engine.vo.sdn.traffic.SdnTrafficListVo;
import com.nia.engine.vo.syslog.SyslogListVo;

public interface RcaTrafficTicketService {

    void createAnomalousTrafficTicket(AnomalousTrafficListVo anomalousTrafficListVo);

    void createNoxiousTrfficTicket(NoxiousTrafficListVo noxiousTrafficListVo);

    void createNodeFactorTicket(NodeFactorListVo nodeFactorListVo);

    void createSdnTrafficTicket(SdnTrafficListVo sdnTrafficListVo);

    void createSyslogTicket(SyslogListVo syslogListVo);
}
