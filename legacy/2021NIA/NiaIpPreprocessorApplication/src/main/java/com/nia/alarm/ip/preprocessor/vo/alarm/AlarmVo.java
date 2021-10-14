
package com.nia.alarm.ip.preprocessor.vo.alarm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.alarm.ip.preprocessor.common.UtlDateHelper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component()
@Data
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlarmVo implements Serializable {
    private int intErrIdx;
    private int moduleSrl;
    private String strResID;
    private String strResName;
    private String strIfID;
    private String strType;
    private String strLevel;
    private String strTitle;
    private String strContent;
    private String strGroupID;
    private String strGroupID1;
    private String strGroupID2;
    private String strGroupID3;
    private String strGroupID4;
    private String strGroupID5;
    private String strGroupID6;
    private String strGroupID7;
    private String strGroupID8;
    private String strGroupID9;
    private String strGroupID10;
    private Timestamp dateRaiseDate;
    private Timestamp dateClearDate;
    private Timestamp dateRegDate;
    private String strClearCheck;
    private String strMemo;
    private String strAcknownCheck;
    private int strAcknownCheckMemberSrl;

    public void setDateRaiseDate(String dateRaiseDate) {
        this.dateRaiseDate = UtlDateHelper.stringToTimestamp2(dateRaiseDate.substring(0,14));
    }

    public void setDateClearDate(String dateClearDate) {
        if(StringUtils.isNotEmpty(dateClearDate)){
            this.dateClearDate = UtlDateHelper.stringToTimestamp2(dateClearDate.substring(0,14));
        }
    }

    public void setDateRegDate(String dateRegDate) {
        this.dateRegDate = UtlDateHelper.stringToTimestamp2(dateRegDate.substring(0,14));
    }

    @JsonProperty("dateRaiseDate2")
    public void setDateRaiseDate2(long dateRaiseDate) {
        this.dateRaiseDate = UtlDateHelper.longToTimestamp(dateRaiseDate);
    }

    @JsonProperty("dateClearDate2")
    public void setDateClearDate2(long dateClearDate) {
        if(dateClearDate != 0){
            this.dateClearDate = UtlDateHelper.longToTimestamp(dateClearDate);
        }
    }

    @JsonProperty("dateRegDate2")
    public void setDateRegDate2(long dateRegDate) {
        this.dateRegDate = UtlDateHelper.longToTimestamp(dateRegDate);
    }
}
