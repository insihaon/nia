package com.nia.engine.service.impl;

import com.nia.engine.mapper.PerformanceTicketMapper;
import com.nia.engine.mapper.TopologyMapper;
import com.nia.engine.service.PerformanceTicketService;
import com.nia.engine.service.TopologyService;
import com.nia.engine.vo.TicketCableVo;
import com.nia.engine.vo.TopologyDataVo;
import com.nia.engine.vo.TopologyObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 * @author
 *
 */
@Service("PerformanceTicketService")
@Scope(value = "prototype")
public class PerformanceTicketServiceImpl implements PerformanceTicketService {
	private final Logger LOGGER = Logger.getLogger(PerformanceTicketServiceImpl.class);

	@Autowired
	private PerformanceTicketMapper performanceTicketMapper;

    /**
     * 티켓 데이터 알람 저장
     * @param
     * @return
     */
    @Override
    public void insertPerformanceTicket(TicketCableVo ticketCableVo) throws Exception {
        performanceTicketMapper.insertPerformanceTicket(ticketCableVo);
    }
}
