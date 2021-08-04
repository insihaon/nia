package com.nia.engine.service.impl;

import com.nia.engine.mapper.TicketMapper;
import com.nia.engine.service.TicketService;
import com.nia.engine.vo.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("TicketService")
public class TicketServiceImpl implements TicketService {
	private final Logger LOGGER = Logger.getLogger(TicketServiceImpl.class);

	@Autowired
	private TicketMapper ticketMapper;

	/**
	 * 티켓 조회
	 * @return
	 */
	@Override
	public RCATicket selectRcaTicket(String ticketId) throws Exception {
		return ticketMapper.selectRcaTicket(ticketId);
	}

	/**
	 * 티켓 리스트 조회
	 * @return
	 */
	@Override
	public List<RCATicket> selectRcaTicketList(String day) throws Exception {
		return ticketMapper.selectRcaTicketList(day);
	}

	/**
	 * 티켓 알람 리스트 조회
	 * @return
	 */
	@Override
	public List<RCATicketAl> selectRcaTicketAlList(String ticketId) throws Exception {
		return ticketMapper.selectRcaTicketAlList(ticketId);
	}

	/**
	 * 티켓 key 생성
	 * @return
	 */
	@Override
	public synchronized String selectTicketKey() throws Exception {
		return ticketMapper.selectTicketKey();
	}

	/**
	 * 티켓 데이터 저장
	 * @param
	 * @return
	 */
	@Override
	public void insertRcaTicket(RCATicket rcaTicket) throws Exception {
		ticketMapper.insertRcaTicket(rcaTicket);
	}

	/**
	 * 티켓 데이터 알람 저장
	 * @param
	 * @return
	 */
	@Override
	public void insertRcaTicketAl(List<RCATicketAl> rcaTicketAlList) throws Exception {

		Map<String, Object> paramMap;

		paramMap = new HashMap<String, Object>();
		paramMap.put("rcaTicketAlList", rcaTicketAlList);
		ticketMapper.insertRcaTicketAl(paramMap);
	}

	/**
	 * Rca Ticket Status 저장
	 * @param
	 * @return
	 */
	@Override
	public void insertRCATicketHandlingStatus(RCATicketHandlingStatus rcaTicketStatus) throws Exception {
		ticketMapper.insertRCATicketHandlingStatus(rcaTicketStatus);
	}

	/**
	 * Rca Ticket Status 저장
	 * @param
	 * @return
	 */
	@Override
	public void updateRCATicketHandlingStatus(RCATicketHandlingStatus rcaTicketStatus) throws Exception {
		ticketMapper.updateRCATicketHandlingStatus(rcaTicketStatus);
	}

	/**
	 * Rca Ticket Status 저장
	 * @param
	 * @return
	 */
	@Override
	public void insertRCATicketHandlingStatusHist(RCATicketHandlingStatus rcaTicketStatus) throws Exception {
		ticketMapper.insertRCATicketHandlingStatusHist(rcaTicketStatus);
	}

	/**
	 * Rca Ticket 정보 업데이트
	 * @param
	 * @return
	 */
	@Override
	public void updateRcaTicketChild(RCATicket rcaTicket) throws Exception {
		ticketMapper.updateRcaTicketChild(rcaTicket);
	}

	/**
	 * 티켓 updateTime
	 * @return
	 */
	public void updateRcaTicketUpdateTime(HashMap<String, String> ticketUpdateTime) throws Exception {
		ticketMapper.updateRcaTicketUpdateTime(ticketUpdateTime);
	}

	/**
	 * ticket cnt 저장
	 * @param
	 * @return
	 */
	@Override
	public void insertRcaTicketCnt(RCATicket rcaTicket) throws Exception {
		ticketMapper.insertRcaTicketCnt(rcaTicket);
	}

	/**
	 * Handling Time 조회
	 * @return
	 */
	public String selectTicketHandlingStatusCurrent(String ticket_id) throws Exception {
		return ticketMapper.selectTicketHandlingStatusCurrent(ticket_id);
	}

	/**
	 * Sop 저장
	 * @param
	 * @return
	 */
	@Override
	public void insertSop(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception {
		ticketMapper.insertSop(rcaTicketHandlingStatus);
	}

	/**
	 * Sop Performance 저장
	 * @param
	 * @return
	 */
	@Override
	public void insertSopPerformance(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception {
		ticketMapper.insertSopPerformance(rcaTicketHandlingStatus);
	}

	/**
	 * Sop Mail 저장
	 * @param
	 * @return
	 */
	@Override
	public void insertSopMail(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception {
		ticketMapper.insertSopMail(rcaTicketHandlingStatus);
	}

	/**
	 * Sop 업데이트
	 * @param
	 * @return
	 */
	@Override
	public void updateSop(RCATicketHandlingStatus rcaTicketHandlingStatus)  throws Exception {
		ticketMapper.updateSop(rcaTicketHandlingStatus);
	}

	/**
	 * Rca Ticket Current State 업데이트
	 * @param
	 * @return
	 */
	@Override
	public void updateRcaTicketCurrentState(RCATicketHandlingStatus rcaTicketHandlingStatus) throws Exception {
		ticketMapper.updateRcaTicketCurrentState(rcaTicketHandlingStatus);
	}

	/**
	 * TICKET CLEAR
	 * @return
	 */
	@Override
	public String fcClearTicket(String parentTicketId) throws Exception {
		return ticketMapper.fcClearTicket(parentTicketId);
	}

	/**
	 * RLARM CLEARL
	 * @param alarmNo
	 * @return
	 */
	@Override
	public List<String> fcClearAlarm(String alarmNo) throws Exception {
		return ticketMapper.fcClearAlarm(alarmNo);
	}

	/**
	 * fcClearTicketCheck
	 * @param ticketId
	 * @return
	 */
	@Override
	public ClearTicketResultVo fcClearTicketCheck(String ticketId) {
		return ticketMapper.fcClearTicketCheck(ticketId);
	}

	/**
	 * 티켓 클리어 체크
	 *
	 * @return
	 */
	@Override
	public List<String> selectClearTicketCheckList() throws Exception {
		return ticketMapper.selectClearTicketCheckList();
	}

	/**
	 * Sop key 생성
	 * @param
	 * @return
	 */
	@Override
	public synchronized String selectSopKey() throws Exception {
		return ticketMapper.selectSopKey();
	}
}
