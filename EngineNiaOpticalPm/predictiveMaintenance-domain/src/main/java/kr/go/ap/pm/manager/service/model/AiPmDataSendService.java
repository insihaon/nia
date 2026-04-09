package kr.go.ap.pm.manager.service.model;

import kr.go.ap.core.primary.nia.dto.pm.model.PmModelSendDataDto;
import kr.go.ap.core.primary.nia.entity.pm.optical.RoadmPerformanceDailyEntity;
import kr.go.ap.core.repository.primary.jpa.pm.optical.RoadmPerformanceDailyRepository;
import kr.go.ap.core.repository.primary.mapper.predictive.pm.PmPreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiPmDataSendService {

    private final PmPreMapper pmPreMapper;
    private final RestTemplate restTemplate;

    @Value("${client.base-url}")
    private String baseUrl;

    public void sendPmData(){

        List<PmModelSendDataDto> pmModelSendDataDtoList;
        try {
            pmModelSendDataDtoList = pmPreMapper.selectPmModelSendDataList();

            if(!pmModelSendDataDtoList.isEmpty()){
                for(PmModelSendDataDto pmModelSendDataDto : pmModelSendDataDtoList){
                    sendData(pmModelSendDataDto);
                }
            }
        }catch (NullPointerException | IndexOutOfBoundsException e){
            log.error("sendPmData : ", e);
        }
    }

    private String sendData(PmModelSendDataDto pmModelSendDataDto){
        String url = baseUrl + "/api/pmData";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<PmModelSendDataDto> entity = new HttpEntity<>(pmModelSendDataDto, headers);
        ResponseEntity<String> resp = null;

        try {
            resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            log.info("remote status={}, body={}", resp.getStatusCodeValue(), resp.getBody());
            return resp.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("Remote error status={}, body={}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Remote call failed", e);
            throw e;
        }
    }
}

