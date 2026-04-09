package kr.go.ap.core.primary.nia.dto.pm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PmModelSendDataDto implements Serializable {
    private String tid;
    private String sysname;
    private String ptpname;
    @JsonProperty("in_out")
    private String inOut;
}
