package com.kt.ipms.legacy.linkmgmt.socketmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.linkmgmt.socketmgmt.service.SocketMgmtService;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsRequstListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsResponseListVo;

@Component
@Transactional
public class SocketMgmtAdapterService {
	
@Lazy @Autowired
	private SocketMgmtService socketMgmtService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void startWhoisBatchScheduler() {
		socketMgmtService.startWhoisBatchScheduler();
	}
	
	@Transactional(readOnly = true)
	public String executeTraceRoute(String query) {
		return socketMgmtService.getTraceRoute(query);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public TacsResponseListVo getCheckTacsRouteList(TacsRequstListVo tacsRequstListVo, String userId) {
		return socketMgmtService.getCheckTacsRouteList(tacsRequstListVo, userId);
	}
}
