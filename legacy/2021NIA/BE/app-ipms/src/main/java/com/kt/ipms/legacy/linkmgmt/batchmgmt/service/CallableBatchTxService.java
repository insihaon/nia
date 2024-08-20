package com.kt.ipms.legacy.linkmgmt.batchmgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.linkmgmt.batchmgmt.dao.CallableBatchFnDao;

@Component
public class CallableBatchTxService {
	
	@Autowired
	private CallableBatchFnDao callableBatchFnDao;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int callUfBatchWhois() {
		// return callableBatchFnDao.callUfBatchWhois();
		return callableBatchFnDao.callUfBatchWhoisNew();		// 2019 신규
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int callUfFileMakeNTbAlloc() {
		return callableBatchFnDao.callUfFileMakeNTbAlloc();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int callUfFileMakeNTbOthersAllocData() {
		return callableBatchFnDao.callUfFileMakeNTbOthersAllocData();
	}

}
