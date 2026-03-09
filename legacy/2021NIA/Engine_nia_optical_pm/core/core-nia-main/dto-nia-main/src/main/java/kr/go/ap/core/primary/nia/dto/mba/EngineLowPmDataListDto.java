package kr.go.ap.core.primary.nia.dto.mba;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EngineLowPmDataListDto implements Serializable {

    private List<OpticalPerformanceDto> lowPmDataDtoList;
    private List<OpticalPerformanceDto> lowPmHistDataDtoList;
}


