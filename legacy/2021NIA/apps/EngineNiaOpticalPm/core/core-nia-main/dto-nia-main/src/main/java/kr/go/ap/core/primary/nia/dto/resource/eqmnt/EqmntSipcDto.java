package kr.go.ap.core.primary.nia.dto.resource.eqmnt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EqmntSipcDto implements Serializable {
    private String tid;
    private String systemName;
}
