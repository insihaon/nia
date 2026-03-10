package com.nia.engine.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.nia.engine.common.UtlDateHelper;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class TmpClusterObject implements Serializable{
	private static final long serialVersionUID = 6932989118226110129L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TmpClusterObject.class);

    private String tmpClusterSeq;
    private Timestamp startTime;
    private Timestamp endTime;
}
