package com.nia.engine.singleton;

import java.util.ArrayList;
import java.util.Iterator;

import com.nia.engine.vo.BasicAlarmVo;
import com.nia.engine.vo.RCATicket;
import org.apache.log4j.Logger;

public class SingletoneEngineData {

    private final Logger LOGGER = Logger.getLogger(SingletoneEngineData.class);

    private ArrayList<RCATicket> rcaTicketList = new ArrayList<RCATicket>();
    private ArrayList<BasicAlarmVo> clearAlarmList = new ArrayList<BasicAlarmVo>();

	private static SingletoneEngineData one;
    private SingletoneEngineData() {
    }
    public synchronized static SingletoneEngineData getInstance() {
        if(one==null) {
            one = new SingletoneEngineData();
        }
        return one;
    }
    
	public void addTicket(RCATicket rcaTicket){
        rcaTicketList.add(rcaTicket);
    }

    public synchronized void removeRcaTicket(String ticketId){
        Iterator<RCATicket> itr;
        RCATicket rcaTicket;
        if(rcaTicketList.size() > 0){
            itr = rcaTicketList.iterator();
            while( itr.hasNext() ) {
                rcaTicket = itr.next();
                if(ticketId.equals(rcaTicket.getTicketId())){
                    //rcaTicketList.remove(itr);
                    itr.remove();
                }
            }
        }
    }

    public ArrayList<RCATicket> getRcaTicketList() {
        return rcaTicketList;
    }

    public ArrayList<BasicAlarmVo> getClearAlarmList() {
        return clearAlarmList;
    }

    public RCATicket getParentTicket(String ticketId){
        Iterator<RCATicket> itr;
        RCATicket rcaTicket = null;

        if(rcaTicketList.size() > 0){
            itr = rcaTicketList.iterator();
            while( itr.hasNext() ) {
                rcaTicket = itr.next();
                if(ticketId.equals(rcaTicket.getTicketId())){
                    break;
                }
            }
        }
        return rcaTicket;
    }
}
