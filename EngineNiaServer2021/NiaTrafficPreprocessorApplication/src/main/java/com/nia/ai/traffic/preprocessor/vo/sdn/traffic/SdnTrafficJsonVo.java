package com.nia.ai.traffic.preprocessor.vo.sdn.traffic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Data
@ToString
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SdnTrafficJsonVo implements Serializable {

    private String pattern;
    private SdnTrafficListVo data;
}
