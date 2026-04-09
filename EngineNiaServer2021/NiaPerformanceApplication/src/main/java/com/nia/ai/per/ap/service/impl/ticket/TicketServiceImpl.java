package com.nia.ai.per.ap.service.impl.ticket;

import com.nia.ai.per.ap.mapper.TopologyMapper;
import com.nia.ai.per.ap.service.ticket.TicketService;
import com.nia.ai.per.ap.vo.PerformanceClusterVo;
import com.nia.ai.per.ap.vo.RoadmPerformanceOrgVo;
import com.nia.ai.per.ap.vo.TicketCableVo;
import com.nia.ai.per.ap.vo.TopologyDataVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("TicketService")
public class TicketServiceImpl implements TicketService {
    private final static Logger LOGGER = Logger.getLogger(TicketServiceImpl.class);

    @Autowired
    private TopologyMapper topologyMapper;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<TicketCableVo> ticketCableVoObjectFactory;

    @Override
    public void createTicket(PerformanceClusterVo performanceClusterVo) {
        String ticketId;
        TopologyDataVo topologyDataVo;
        HashMap<String, String> parameterMap = null;
        List<List<RoadmPerformanceOrgVo>> rowPerformanaceList;
        TicketCableVo ticketCableVo;
        List<TicketCableVo> ticketCableList;

        try {
            if(performanceClusterVo.getRoadmPerformanceList() != null && performanceClusterVo.getRoadmPerformanceList().size() > 0){
                rowPerformanaceList = new ArrayList<List<RoadmPerformanceOrgVo>>();

                if("UP".equals(performanceClusterVo.getDirection())){
                    Collections.sort(performanceClusterVo.getRoadmPerformanceList(), new RoutenumDescendingPerformanaceData());
                    rowPerformanaceList.add(performanceClusterVo.getRoadmPerformanceList());
                }

                if("DOWN".equals(performanceClusterVo.getDirection())){
                    Collections.sort(performanceClusterVo.getRoadmPerformanceList(), new RoutenumAscendingPerformanaceData());
                    rowPerformanaceList.add(performanceClusterVo.getRoadmPerformanceList());
                }

                if(rowPerformanaceList != null && rowPerformanaceList.size() > 0){
                    ticketCableList = new ArrayList<TicketCableVo>();

                    for(List<RoadmPerformanceOrgVo> performanaceVoList : rowPerformanaceList){
                        if(performanaceVoList.size() > 0){

                            for(int i = 0; i <= performanaceVoList.size(); i++){
                                if(i == 0){
                                    if("REPEATER".equals(performanaceVoList.get(i).getRoadmCode())){
                                        if(performanaceVoList.get(i).getIsInRowSignal()){
                                            parameterMap = new HashMap<String, String>();
                                            parameterMap.put("sysname", performanaceVoList.get(i).getSysname().split("-")[0]);

                                            if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                                parameterMap.put("ptpNameBau", performanaceVoList.get(i).getSysname().split("-")[1]+"-"+performanaceVoList.get(i).getPort());
                                            }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                                parameterMap.put("ptpNamePau", performanaceVoList.get(i).getSysname().split("-")[1]+"-"+performanaceVoList.get(i).getPort());
                                            }
                                            topologyDataVo = topologyMapper.selectTopology(parameterMap);

                                            if(topologyDataVo != null){
                                                ticketCableVo = ticketCableVoObjectFactory.getObject();

                                                if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                                    ticketCableVo.setSysnamea(topologyDataVo.getSysnamea());
                                                    ticketCableVo.setPorta(topologyDataVo.getPtpnameaBau());
                                                    ticketCableVo.setSysnamez(performanaceVoList.get(i).getSysname());
                                                    ticketCableVo.setPortz(performanaceVoList.get(i).getPort());
                                                }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                                    ticketCableVo.setSysnamea(performanaceVoList.get(i).getSysname());
                                                    ticketCableVo.setPorta(performanaceVoList.get(i).getPort());
                                                    ticketCableVo.setSysnamez(topologyDataVo.getSysnamez());
                                                    ticketCableVo.setPortz(topologyDataVo.getPtpnamezBau());
                                                }
                                                ticketCableVo.setIsRootRowSignalLine(true);
                                                ticketCableList.add(ticketCableVo);
                                            }
                                        }else if(performanaceVoList.get(i).getIsOutRowSignal()){
                                            ticketCableVo = ticketCableVoObjectFactory.getObject();

                                            if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                                ticketCableVo.setSysnamea(performanaceVoList.get(i).getSysname());
                                                ticketCableVo.setPorta(performanaceVoList.get(i).getPort());
                                                ticketCableVo.setSysnamez(performanaceVoList.get(i+1).getSysname());
                                                ticketCableVo.setPortz(performanaceVoList.get(i+1).getPort());
                                            }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                                ticketCableVo.setSysnamea(performanaceVoList.get(i+1).getSysname());
                                                ticketCableVo.setPorta(performanaceVoList.get(i+1).getPort());
                                                ticketCableVo.setSysnamez(performanaceVoList.get(i).getSysname());
                                                ticketCableVo.setPortz(performanaceVoList.get(i).getPort());
                                            }
                                            ticketCableVo.setIsRootRowSignalLine(true);

                                            ticketCableList.add(ticketCableVo);
                                        }
                                    }else{
                                        ticketCableVo = ticketCableVoObjectFactory.getObject();

                                        if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                            ticketCableVo.setSysnamea(performanaceVoList.get(i).getSysname());
                                            ticketCableVo.setPorta(performanaceVoList.get(i).getPort());
                                            ticketCableVo.setSysnamez(performanaceVoList.get(i+1).getSysname());
                                            ticketCableVo.setPortz(performanaceVoList.get(i+1).getPort());
                                        }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                            ticketCableVo.setSysnamea(performanaceVoList.get(i+1).getSysname());
                                            ticketCableVo.setPorta(performanaceVoList.get(i+1).getPort());
                                            ticketCableVo.setSysnamez(performanaceVoList.get(i).getSysname());
                                            ticketCableVo.setPortz(performanaceVoList.get(i).getPort());
                                        }
                                        ticketCableVo.setIsRootRowSignalLine(true);

                                        ticketCableList.add(ticketCableVo);
                                    }
                                }else{
                                    if(i == performanaceVoList.size() -1){
                                        break;
                                    }
                                    ticketCableVo = ticketCableVoObjectFactory.getObject();

                                    if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                        ticketCableVo.setSysnamea(performanaceVoList.get(i).getSysname());
                                        ticketCableVo.setPorta(performanaceVoList.get(i).getPort());
                                        ticketCableVo.setSysnamez(performanaceVoList.get(i+1).getSysname());
                                        ticketCableVo.setPortz(performanaceVoList.get(i+1).getPort());
                                    }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                        ticketCableVo.setSysnamea(performanaceVoList.get(i+1).getSysname());
                                        ticketCableVo.setPorta(performanaceVoList.get(i+1).getPort());
                                        ticketCableVo.setSysnamez(performanaceVoList.get(i).getSysname());
                                        ticketCableVo.setPortz(performanaceVoList.get(i).getPort());
                                    }

                                    ticketCableList.add(ticketCableVo);
                                }
                            }
                        }
                    }

                    LOGGER.info(ticketCableList);
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[TicketService] createTicket() error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }
}

class RoutenumAscendingPerformanaceData implements Comparator<RoadmPerformanceOrgVo> {

    @Override
    public int compare(RoadmPerformanceOrgVo o1, RoadmPerformanceOrgVo o2) {
        return new Integer(o1.getRouteNum()).compareTo(new Integer(o2.getRouteNum()));
    }
}

class RoutenumDescendingPerformanaceData implements Comparator<RoadmPerformanceOrgVo> {

    @Override
    public int compare(RoadmPerformanceOrgVo o1, RoadmPerformanceOrgVo o2) {
        return new Integer(o2.getRouteNum()).compareTo(new Integer(o1.getRouteNum()));
    }
}

