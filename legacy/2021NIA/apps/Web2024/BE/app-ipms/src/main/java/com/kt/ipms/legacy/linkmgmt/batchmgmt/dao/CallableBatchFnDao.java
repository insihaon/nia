package com.kt.ipms.legacy.linkmgmt.batchmgmt.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CallableBatchFnDao {

	public int callUfBatchWhois();
	
	public int callUfBatchWhoisNew();
	
	public int callUfFileMakeNTbAlloc();
	
	public int callUfFileMakeNTbOthersAllocData();
	
}

