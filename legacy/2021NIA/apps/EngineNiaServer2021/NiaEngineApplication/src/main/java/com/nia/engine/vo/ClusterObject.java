package com.nia.engine.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class ClusterObject implements Serializable {
	private static final long serialVersionUID = 5650560938655304970L;
    private final Logger LOGGER = Logger.getLogger(ClusterObject.class);

    private String clusterNo;
    private String tmpClusterNo;
    private List<BasicAlarmVo> basicAlarmtVoList;
}
