package com.nia.ip.sdn.syslog.linkage.vo.syslog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;


@Component
@Scope(value = "prototype")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyslogDataVo implements Serializable {

    private int collectSeq;
    private FieldsVo fields;
    private String name;
    private TagsVo tags;
    private Timestamp timestamp;


}
