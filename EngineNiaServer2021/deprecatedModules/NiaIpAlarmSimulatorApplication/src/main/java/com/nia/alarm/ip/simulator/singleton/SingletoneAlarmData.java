package com.nia.alarm.ip.simulator.singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.nia.alarm.ip.simulator.vo.AlarmVo;

public class SingletoneAlarmData {
	private final Logger LOGGER = Logger.getLogger(SingletoneAlarmData.class);
	
	private List<AlarmVo> alarmList = new ArrayList<AlarmVo>();
	private BlockingQueue<AlarmVo> alarmQue = new LinkedBlockingQueue<AlarmVo>();
	
    private SingletoneAlarmData() {}
    
    private static class SingleTonHolder{
    	private static final SingletoneAlarmData instance = new SingletoneAlarmData();
    }
    
    public static SingletoneAlarmData getInstance() {
        return SingleTonHolder.instance;
    }
    
    public void addAlarmList(AlarmVo alarmVo) {
    	alarmList.add(alarmVo);
	}
    
    public synchronized void alarmListModify(String gb, AlarmVo alarmVo, ArrayList<AlarmVo> list){
    	if(gb.equals("I")){
    		alarmList.add(alarmVo);
    	}else if(gb.equals("D")){
    		alarmList.remove(alarmVo);
    	}else if(gb.equals("A")){
            alarmList.addAll(list);
        }
    }
    
    public void alarmQueOffer(AlarmVo alarmVo){
    	alarmQue.offer(alarmVo);
    }

    public AlarmVo alarmQuePeek(){
        return alarmQue.peek();
    }
    
    public AlarmVo alarmPoll(){
    	return alarmQue.poll();
    }
    
	public BlockingQueue<AlarmVo> getAlarmQue() {
		return alarmQue;
	}

	public List<AlarmVo> getAlarmList() {
		return alarmList;
	}
    
}
