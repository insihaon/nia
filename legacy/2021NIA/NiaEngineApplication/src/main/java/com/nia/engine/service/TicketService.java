package com.nia.engine.service;

import com.nia.engine.vo.*;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrfficVo;
import com.nia.engine.vo.profile.ProfileVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TicketService {

	/**
	 * 티켓 조회
	 * @return
	 */
	RCATicket selectRcaTicket(String ticketId) throws Exception;

	/**
	 * 티켓 리스트 조회
	 * @return
	 */
	List<RCATicket> selectRcaTicketList(String day) throws Exception;

	/**
	 * 티켓 알람 리스트 조회
	 * @return
	 */
	List<RCATicketAl> selectRcaTicketAlList(String ticketId) throws Exception;

	/**
	 * 티켓 key 생성
	 * @return
	 */
	String selectTicketKey() throws Exception;

	/**
	 * 티켓 데이터 저장
	 * @param
	 * @return
	 */
	void insertRcaTicket(RCATicket rcaTicket) throws Exception;

	/**
	 * 티켓 데이터 알람 저장
	 * @param
	 * @return
	 */
	void insertRcaTicketAl(List<RCATicketAl> rcaTicketAlList) throws Exception;

	/**
	 * Rca Ticket Status 저장
	 * @param
	 * @return
	 */
	void insertRCATicketHandlingStatus(RCAHandlingStatus rcaTicketStatus) throws Exception;

	/**
	 * Rca Ticket Status 저장
	 * @param
	 * @return
	 */
	void updateRCATicketHandlingStatus(RCAHandlingStatus rcaTicketStatus) throws Exception;

	/**
	 * Rca Ticket Status 저장
	 * @param
	 * @return
	 */
	void insertRCATicketHandlingStatusHist(RCAHandlingStatus rcaTicketStatus) throws Exception;

	/**
	 * Rca Ticket 정보 업데이트
	 * @param
	 * @return
	 */
	void updateRcaTicketChild(RCATicket rcaTicket) throws Exception;

	/**
	 * 티켓 updateTime
	 * @param ticketUpdateTime
	 * @return
	 */
	void updateRcaTicketUpdateTime(HashMap<String, String> ticketUpdateTime) throws Exception;

	/**
	 * ticket cnt 저장
	 * @param
	 * @return
	 */
	void insertRcaTicketCnt(RCATicket rcaTicket) throws Exception;

	void updateTicketCnt(RCATicket rcaTicket) throws Exception;


	/**
	 * Sop 저장
	 * @param
	 * @return
	 */
	void insertSop(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception;

	/**
	 * Sop Performance 저장
	 * @param
	 * @return
	 */
	void insertSopPerformance(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception;

	/**
	 * Sop Mail 저장
	 * @param
	 * @return
	 */
	void insertSopMail(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception;

	/**
	 * Sop 업데이트
	 * @param
	 * @return
	 */
	void updateSop(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception;

	/**
	 * Sop Upsert
	 * @param
	 * @return
	 */
	void upsertSop(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception;

	/**
	 * Rca Ticket Current State 업데이트
	 * @param
	 * @return
	 */
	void updateRcaTicketCurrentState(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception;

	/**
	 * TICKET CLEAR
	 * @return
	 */
	String fcClearTicket(String parentTicketId) throws Exception;

	/**
	 * RLARM CLEARL
	 * @param alarmNo
	 * @return
	 */
	List<String> fcClearAlarm(String alarmNo) throws Exception;

	/**
	 * fcClearTicketCheck
	 * @param ticketId
	 * @return
	 */
	ClearTicketResultVo fcClearTicketCheck(String ticketId);

	/**
	 * 티켓 클리어 체크
	 *
	 * @return
	 */
	List<String> selectClearTicketCheckList() throws Exception;

	/**
	 * Sop key 생성
	 * @param
	 * @return
	 */
	String selectSopKey() throws Exception;

	/**
	 * Profile 조회
	 * @param
	 * @return
	 */
	ProfileVo selectProfile(HashMap<String, String> map) throws Exception;

	/**
	 * ticket cnt 삭제
	 * @param
	 * @return
	 */
	void deleteRcaTicketCnt(String ticketId) throws Exception;

	/**
	 * Set Ticket Status as Profile
	 *
	 * @return
	 */
	String fcSetTicketStatusAsPofile(RCATicket rcaTicket) throws Exception;

	/**
	 * Ticket Check Profile
	 *
	 * @return
	 */
	List<RCATicket> selectRcaTicketProfileCheckList() throws Exception;

	/**
	 * TB_ANOMALOUS_TRAFFIC Ticket Id 업데이트
	 * @param
	 * @return
	 */
	void updateAnomalousTrafficTicketId(HashMap<String, String> map) throws Exception;

	/**
	 * TB_NOXIOUS_TRAFFIC Ticket Id 업데이트
	 * @param
	 * @return
	 */
	void updateNoxiousTrafficTicketId(HashMap<String, String> map) throws Exception;

	/**
	 * Anomalous Traffic Alarm 조회
	 * @param
	 * @return
	 */
	AnomalousTrafficVo selectAnomalousTrafficAlarm(HashMap<String, String> map) throws Exception;

	/**
	 * Noxious Traffic Alarm 조회
	 * @param
	 * @return
	 */
	NoxiousTrfficVo selectNoxiousTrafficAlarm(HashMap<String, String> map) throws Exception;

	UserOrganVo selectUserOrgan(HashMap<String, String> map) throws Exception;

	int selectChildTicketCnt(String ticketId) throws Exception;
}
