package com.nia.ip.syslog.linkage.vo.syslog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyslogListVo implements Serializable {

    @JsonProperty("classify")
    private String classify;

    @JsonProperty("count")
    private String count;

    @JsonProperty("data")
    private List<SyslogVo> data;

    private String ydNum;
    private Timestamp insertTime;

    @JsonProperty("classify")
    public String getClassify() {
        return classify;
    }

    @JsonProperty("classify")
    public void setClassify(String classify) {
        switch (classify){
            case "1" :
                this.classify = "syslog";
                break;
            case "2" :
                this.classify = "traffic";
                break;
            case "3" :
                this.classify = "alarm";
                break;
            case "4" :
                this.classify = "flow";
                break;
        }
    }

    @JsonProperty("count")
    public String getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(String count) {
        this.count = count;
    }

    @JsonProperty("data")
    public List<SyslogVo> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<SyslogVo> data) {
        this.data = data;
    }

    public String getYdNum() {
        return ydNum;
    }

    public void setYdNum(String ydNum) {
        this.ydNum = ydNum;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }
}
