package com.nia.rca.cluster.preprocessor.vo;

import com.nia.rca.cluster.preprocessor.common.UtlDateHelper;
import lombok.Data;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "prototype")
@Data
public class TmpClusterObject implements Serializable{
	private static final long serialVersionUID = 5185873915047878425L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TmpClusterObject.class);
	
	private String tmpClusterSeq;
	private Timestamp startTime;
	private Timestamp endTime;
	private Timestamp createTime;
    private Timestamp clusterStartTime;
    private Timestamp clusterEndTime;
	private List<ClusterObject> clusterList = new ArrayList<ClusterObject>();
	private boolean isStop = false;

    public boolean isClusterMaxTime(){
        long maxTime;

        try{
            if(!isStop){
                maxTime = (UtlDateHelper.getCurrentTime().getTime() - clusterStartTime.getTime())/1000;
               // LOGGER.info(">>>>>>>>>>[TmpClusterObject] isClusterMaxTime maxTime("+tmpClusterSeq+") : " + maxTime + " <<<<<<<<<<<<<<<<<");
                //450
                if(maxTime > 450){
                    isStop = true;
                    clusterEndTime = UtlDateHelper.getCurrentTime();
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[TmpClusterObject] isClusterMaxTime error() : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
        return isStop;
    }

    @Override
    public String toString() {
        return "TmpClusterObject \n" +
                "tmpClusterSeq=" + tmpClusterSeq + "\n"+
                "startTime=" + startTime + "\n"+
                "endTime=" + endTime + "\n"+
                "isStop=" + isStop ;
    }
}
