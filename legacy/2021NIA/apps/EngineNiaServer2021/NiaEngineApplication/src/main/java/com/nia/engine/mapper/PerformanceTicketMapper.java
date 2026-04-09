package com.nia.engine.mapper;

import com.nia.engine.vo.TicketCableVo;
import com.nia.engine.vo.TopologyDataVo;
import com.nia.engine.vo.TopologyObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface PerformanceTicketMapper {

	/**
	 * selectAlarmMstList
	 * @param
	 * @return
	 */
    void insertPerformanceTicket(TicketCableVo ticketCableVo);
}
