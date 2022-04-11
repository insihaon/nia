package com.nia.engine.service;

import java.io.File;

public interface FaultEventService {

	/**
	 * 장애 이벤트 데이터 저장
	 * @param
	 * @return
	 */
	void insertFaultEvent(String startTime, String endTime, String title);

    void jsonObjToFile(String faultEventKey);

    File createJsonFile(String eventType, String jsonData, String faultEventKey, String titleName);
}
