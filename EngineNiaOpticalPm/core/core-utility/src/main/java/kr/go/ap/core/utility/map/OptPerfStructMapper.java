package kr.go.ap.core.utility.map;

import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmCheckOpticalPerformanceEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.RoadmLowOpticalPerformanceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring"
        , unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE

)
public interface OptPerfStructMapper {

    @Mappings({
            @Mapping(target = "roadmCheckOpticalPerformanceKey.trunkId", source = "trunkId"),
            @Mapping(target = "roadmCheckOpticalPerformanceKey.tid", source = "tid"),
            @Mapping(target = "roadmCheckOpticalPerformanceKey.port", source = "port"),
            @Mapping(target = "roadmCheckOpticalPerformanceKey.ocrtime", source = "ocrtime"),
            @Mapping(target = "roadmCheckOpticalPerformanceKey.ptpname", source = "ptpname"),
            @Mapping(target = "roadmCheckOpticalPerformanceKey.inOut", source = "inOut"),
            @Mapping(target = "routenum", source = "routeNum")
    })
    RoadmCheckOpticalPerformanceEntity toCheckOptPerfEntity(OpticalPerformanceDto perfDto);


    @Mappings({
            @Mapping(target = "roadmLowOpticalPerformanceKey.trunkId", source = "trunkId"),
            @Mapping(target = "roadmLowOpticalPerformanceKey.tid", source = "tid"),
            @Mapping(target = "roadmLowOpticalPerformanceKey.port", source = "port"),
            @Mapping(target = "roadmLowOpticalPerformanceKey.ocrtime", source = "ocrtime"),
            @Mapping(target = "roadmLowOpticalPerformanceKey.ptpname", source = "ptpname"),
            @Mapping(target = "roadmLowOpticalPerformanceKey.inOut", source = "inOut"),
    })
    RoadmLowOpticalPerformanceEntity toLowOptPerfEntity(OpticalPerformanceDto perfDto);

}
