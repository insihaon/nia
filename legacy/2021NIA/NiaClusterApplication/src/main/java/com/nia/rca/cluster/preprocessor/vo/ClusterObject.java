package com.nia.rca.cluster.preprocessor.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Component 
@Scope(value = "prototype")
@JsonInclude(Include.NON_NULL)
@Data
public class ClusterObject implements Serializable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterObject.class);
	
	private String clusterNo;
	private String tmpClusterNo;
	private List<String> equipList = new ArrayList<String>();
	private List<String> alarmTypeList = new ArrayList<String>();
	private List<String> trunkIdList = new ArrayList<String>();
	private ArrayList<BasicAlarmVo> alarmObjectList = new ArrayList<BasicAlarmVo>();
	private ClusterInfoVo clusterInfoVo;
	private Timestamp insertTime;
	private String alarmType;

	public void addAlarmNoList(BasicAlarmVo basicAlarmVo) {
        //LOGGER.info("addEquipList : " + sysName);
	    if(!findAlarmNo(basicAlarmVo)){
            this.alarmObjectList.add(basicAlarmVo);
        }
	}

	public void addEquipList(String pSysName) {
		String sysName;
        //LOGGER.info("addEquipList : " + sysName);
		sysName = pSysName.split("-")[0];
	    if(!findSysName(sysName)){
            this.equipList.add(sysName);
        }
	}

	public void addTrunkIdList(String trunkId) {
        //LOGGER.info("addEquipList : " + sysName);
	    if(!findTrunkId(trunkId)){
            this.trunkIdList.add(trunkId);
        }
	}

	public boolean findSysName(String pSysName){
		boolean isFind = false;
		try{
			if(equipList != null && equipList.size() > 0){

                isFind = equipList.stream().anyMatch(sysName -> pSysName.split("-")[0].equals(sysName));
			}
		}catch (Exception e) {
			LOGGER.error(">>>>>>>>>>[ClusterObject] findSysName("+pSysName+") error : " + ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<<");
		}
		return isFind;
	}

	public boolean findTrunkId(String pTrunkId){
		boolean isFind = false;
		try{
			if(trunkIdList != null && trunkIdList.size() > 0){

                isFind = trunkIdList.stream().anyMatch(trunkId -> pTrunkId.equals(trunkId));
			}
		}catch (Exception e) {
			LOGGER.error(">>>>>>>>>>[ClusterObject] findTrunkId("+pTrunkId+") error : " + ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<<");
		}
		return isFind;
	}

	public boolean findAlarmNo(BasicAlarmVo basicAlarmVo){
		boolean isFind = false;
		try{
			if(alarmObjectList != null && alarmObjectList.size() > 0){

                isFind = alarmObjectList.stream().anyMatch(alarmVo -> alarmVo.getAlarmno().equals(basicAlarmVo.getAlarmno()));
			}
		}catch (Exception e) {
			LOGGER.error(">>>>>>>>>>[ClusterObject] findAlarmNo("+basicAlarmVo.getAlarmno()+") error : " + ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<<");
		}
		return isFind;
	}
}
