package com.nia.engine.mapper;

import com.nia.engine.vo.*;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrfficVo;
import com.nia.engine.vo.profile.ProfileVo;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface TicketMapper{

	/**
	 * 티켓 조회
	 * @return
	 */
	RCATicket selectRcaTicket(String ticketId);

	/**
	 * 티켓 리스트 조회
	 * @return
	 */
	List<RCATicket> selectRcaTicketList(String day);

	/**
	 * 티켓 알람 리스트 조회
	 * @return
	 */
	List<RCATicketAl> selectRcaTicketAlList(String ticketId);

	/**
	 * 티켓 key 생성
	 * @param
	 * @return
	 */
	String selectTicketKey();

	/**
	 * 티켓 데이터 저장
	 * @param
	 * @return
	 */
	void insertRcaTicket(RCATicket rcaTicket);

	/**
	 * 티켓 데이터 알람 저장
	 * @param
	 * @return
	 */
	void insertRcaTicketAl(Map<String, Object> map);

	/**
	 * Rca Ticket Status 저장
	 * @param
	 * @return
	 */
	void insertRCATicketHandlingStatus(RCAHandlingStatus rcaTicketStatus);

	/**
	 * Rca Ticket Status update
	 * @param
	 * @return
	 */
	void updateRCATicketHandlingStatus(RCAHandlingStatus rcaTicketStatus);

	/**
	 * Rca Ticket Status Hist 저장
	 * @param
	 * @return
	 */
	void insertRCATicketHandlingStatusHist(RCAHandlingStatus rcaTicketStatus);

	/**
	 * Rca Ticket Status 업데이트
	 * @param
	 * @return
	 */
	void updateRcaTicketChild(RCATicket rcaTicket);

	/**
	 * 티켓 updateTime
	 * @param
	 * @return
	 */
	void updateRcaTicketUpdateTime(HashMap<String, String> ticketUpdateTime);

	/**
	 * ticket cnt 저장
	 * @param
	 * @return
	 */
	void insertRcaTicketCnt(RCATicket rcaTicket);

	void updateTicketCnt(RCATicket rcaTicket);

	/**
	 * 티켓 클리어 체크
	 * @return
	 */
	List<String> selectClearTicketCheckList();

	/**
	 * Handling Time 조회
	 * @return
	 */
	String selectTicketHandlingStatusCurrent(String ticket_id);

	/**
	 * Sop 저장
	 * @param
	 * @return
	 */
	void insertSop(RCATicketHandlingStatus rcaTicketHandlingStatus);

	/**
	 * Sop Performance 저장
	 * @param
	 * @return
	 */
	void insertSopPerformance(RCATicketHandlingStatus rcaTicketHandlingStatus);

	/**
	 * Sop Mail 저장
	 * @param
	 * @return
	 */
	void insertSopMail(RCATicketHandlingStatus rcaTicketHandlingStatus);

	/**
	 * Sop 업데이트
	 * @param
	 * @return
	 */
	void updateSop(RCATicketHandlingStatus rcaTicketHandlingStatus);

	/**
	 * Sop Upsert
	 * @param
	 * @return
	 */
	void upsertSop(RCATicketHandlingStatus rcaTicketHandlingStatus);

	/**
	 * Rca Ticket Current State 업데이트
	 * @param
	 * @return
	 */
	void updateRcaTicketCurrentState(RCATicketHandlingStatus rcaTicketHandlingStatus);

	/**
	 * TICKET CLEAR
	 *
	 * @return
	 */
	String fcClearTicket(String parentTicketId);

	/**
	 * ALARM CLEARL
	 *
	 * @param alarmNo
	 * @return
	 */
	List<String> fcClearAlarm(String alarmNo);


	/**
	 * fcClearTicketCheck
	 *
	 * @param ticketId
	 * @return
	 */
	ClearTicketResultVo fcClearTicketCheck(String ticketId);

	/**
	 * Sop key 생성
	 * @param
	 * @return
	 */
	String selectSopKey();

	/**
	 * Profile 조회
	 * @param
	 * @return
	 */
	ProfileVo selectProfile(HashMap<String, String> map);


	/**
	 * ticket cnt 삭제
	 *
	 * @param
	 * @return
	 */
	void deleteRcaTicketCnt(String ticketId);

	/**
	 * Set Ticket Status as Profile
	 *
	 * @return
	 */
	String fcSetTicketStatusAsPofile(RCATicket rcaTicket);

	/**
	 * Ticket Check Profile
	 *
	 * @return
	 */
	List<RCATicket> selectRcaTicketProfileCheckList();

	/**
	 * TB_ANOMALOUS_TRAFFIC Ticket Id 업데이트
	 * @param
	 * @return
	 */
	void updateAnomalousTrafficTicketId(HashMap<String, String> map);

	/**
	 * TB_NOXIOUS_TRAFFIC Ticket Id 업데이트
	 * @param
	 * @return
	 */
	void updateNoxiousTrafficTicketId(HashMap<String, String> map);

	/**
	 * Anomalous Traffic Alarm 조회
	 * @param
	 * @return
	 */
	AnomalousTrafficVo selectAnomalousTrafficAlarm(HashMap<String, String> map);

	/**
	 * Noxious Traffic Alarm 조회
	 * @param
	 * @return
	 */
	NoxiousTrfficVo selectNoxiousTrafficAlarm(HashMap<String, String> map);

	UserOrganVo selectUserOrgan(HashMap<String, String> map);

	RCATicket selectTrafficeMageParentTicket(HashMap<String, String> map);

	int selectChildTicketCnt(String ticketId);
}
