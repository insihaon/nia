package com.nia.ip.sdn.sflow.linkage.vo.sflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SflowDataVo implements Serializable {
    private int collectSeq;
    private FieldsVo fields;
    private String name;
    private TagsVo tags;
    private int timestamp;
}
