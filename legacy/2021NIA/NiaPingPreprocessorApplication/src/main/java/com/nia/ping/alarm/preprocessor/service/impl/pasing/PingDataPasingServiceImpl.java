package com.nia.ping.alarm.preprocessor.service.impl.pasing;

import com.nia.ping.alarm.preprocessor.common.UtlDateHelper;
import com.nia.ping.alarm.preprocessor.service.pasing.PingDataPasingService;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service("PingDataPasingService")
public class PingDataPasingServiceImpl implements PingDataPasingService {

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PingAlarmVo> pingDataVoObjectFactory;

    @Override
    public PingAlarmVo pingDataPasing(String pingData) {
        String[] pingDataArr1;
        String[] pingDataArr2;
        String[] dataArr;
        String yyyyMMddHH;
        String mm;
        String currentTime = null;

        Calendar cal = Calendar.getInstance();
        Timestamp time = null;

        PingAlarmVo pingDataVo = null;

        if(StringUtils.isNotEmpty(pingData)){
            pingDataArr1= pingData.split(" ");
            pingDataArr2 = (pingDataArr1[0]+","+pingDataArr1[1]).split(",");

            pingDataVo = pingDataVoObjectFactory.getObject();

            for(String data : pingDataArr2){
                if(data.contains("=")){
                    dataArr = data.split("=");

                    switch (dataArr[0]){
                        case "host" :
                            pingDataVo.setHost(dataArr[1]);
                            break;
                        case "url" :
                            pingDataVo.setUrl(dataArr[1]);
                            break;
                        case "packets_received" :
                            pingDataVo.setPacketsReceived(dataArr[1]);
                            break;
                        case "percent_packet_loss" :
                            pingDataVo.setPercentPacketLoss(Integer.parseInt(dataArr[1]));
                            break;
                        case "packets_transmitted" :
                            pingDataVo.setPacketsTransmitted(dataArr[1]);
                            break;
                        case "result_code" :
                            pingDataVo.setResultCode(dataArr[1]);
                            break;
                        case "ttl" :
                            pingDataVo.setTtl(dataArr[1]);
                            break;
                    }


                    cal.setTimeInMillis(UtlDateHelper.getCurrentTime().getTime());

//                    cal.add(Calendar.HOUR, -10);
                    time = new Timestamp(cal.getTime().getTime());

                    yyyyMMddHH = (time+"").substring(0,14);
                    mm = (time+"").substring(14,16);

                    mm = (Math.round(Integer.parseInt(mm)/5)*5)+"";

                    currentTime = yyyyMMddHH+mm+":00";


                    pingDataVo.setAlarmtime(UtlDateHelper.stringToTimestamp(currentTime));
                }
            }
        }
        return pingDataVo;
    }
}
