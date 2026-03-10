package com.codej.dataHub.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.codej.base.dto.model.ResultMap;
import com.codej.base.exception.CServiceException;
import com.codej.dataHub.controller.DataHubApiController;
import com.codej.dataHub.mapper.db2nd.DataHubMapper;
import com.codej.web.controller.KongApiController;
import com.codej.web.service.MainService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class DataHubService extends MainService {
    @Autowired(required = false)
    protected DataHubMapper dataHubMapper;

    @Autowired(required = false)
    protected KongApiController kongApiController;

    @Autowired
    @Lazy
    protected DataHubApiController DataHubApiController;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    @Value("${myconf.api-server.kong-use:false}")
    private Boolean kongApiUse;


    public List<ResultMap> SELECT_API_LIST(HashMap<String, Object> param) throws Exception {
        param.remove("TOTAL_COUNT");
        List<ResultMap> resultList = dataHubMapper.SELECT_API_LIST(param);
        return resultList;
    }

    /***
     * 연동시스템 > 삭제 
     *  deleteUser("SITE_A")
     */
    public int DELETE_LINK_SYSTEM_LIST_PROC(HashMap<String, Object> param) throws CServiceException {
        int result = -1;
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            String system_id = param.get("system_id").toString();
            if(system_id == null) {
                throw new CServiceException(String.format("BadRequest DELETE_LINK_SYSTEM_LIST_PROC, %s", param.toString()));
            }
            
            // if (kongApiUse) {
            //     kongApiController.deleteUser(system_id); // KONG API 를 이용한 USER 삭제
            //     log.info("KONG deleteUser: name={}", system_id);
            // }
            
            result = dataHubMapper.DELETE_LINK_SYSTEM_LIST(param);
            transactionManager.commit(txStatus);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            log.error(e.toString());
            throw new CServiceException("Error in DELETE_LINK_SYSTEM_LIST_PROC");  
        }
        return result;
    }

    /***
     * 연동시스템 > 등록
     *  apiKey =  addUser("SITE_A")
     */
    public int INSERT_LINK_SYSTEM_LIST_PROC(HashMap<String, Object> param) throws CServiceException {
        int result = -1;
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            String system_nm = (String) param.get("system_nm");
            if(system_nm == null) {
                throw new CServiceException(String.format("BadRequest INSERT_LINK_SYSTEM_LIST_PROC, %s", param.toString()));
            }

            int systemId = dataHubMapper.SELECT_LINK_SYSTEM_NEXTVAL(null);
            param.put("system_id", systemId);
            
            if (kongApiUse) {
                String api_key = kongApiController.addUser(String.valueOf(systemId)); // KONG API 를 이용한 USER 등록
                log.info("KONG addUser: name={}, api_key={}", systemId, api_key);
                param.put("api_key", api_key);
            }

            result = dataHubMapper.INSERT_LINK_SYSTEM_LIST(param);
            transactionManager.commit(txStatus);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            log.error(e.toString());
            throw new CServiceException("Error in INSERT_LINK_SYSTEM_LIST_PROC");
        }
        return result;
    }

    /***
     * 권한관리 > 승인/회수
     *  - 승인: 
     *      addService("ALARM") // 최초 1 회만 요청하면 되지만, 타이밍을 알 수 없고, 중복 요청을 해도 상관 없기에 여기서 로직 처리함
     *      addLinkUserToService("SITE_A", "ALARM")
     *  - 회수: deleteLinkUserToService("SITE_A", "ALARM");
     */
    public int UPDATE_API_AUTH_HIST_PROC(HashMap<String, Object> param) throws CServiceException {
        int result = -1;
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            String status_cd = (String) param.get("status_cd");
            String api_id = param.get("api_id").toString();
            String api_url = (String) param.get("api_url");
            String system_id = param.get("system_id").toString();

            log.info("UPDATE_API_AUTH_HIST_PROC: status_cd={}, api_id={}, api_url={}, system_id={},", status_cd, api_id, api_url, system_id);
            
            switch (status_cd) {
                case "GRANT":   /* 승인 */ 
                    if (kongApiUse) {
                        INSERT_API_SET_LIST_PROC(param);

                        log.info("KONG addLinkUserToService: system_id={}, api_id={}", system_id, api_id);
                        String aclGroupName = kongApiController.addLinkUserToService(system_id, api_id); // KONG API 를 이용한 링크 추가
                        log.info("KONG addLinkUserToService: aclGroupName={}", aclGroupName);
                    }
                    break;
                case "REVOKE":  /* 회수 */
                    if (kongApiUse) {
                        log.info("KONG deleteLinkUserToService: system_id={}, api_id={}", system_id, api_id);
                        String aclGroupName = kongApiController.deleteLinkUserToService(system_id, api_id); // KONG API 를 이용한 링크 삭제
                        log.info("KONG deleteLinkUserToService: aclGroupName={}", aclGroupName);
                    }
                    break;
                default:
                    throw new CServiceException(String.format("BadRequest UPDATE_API_AUTH_HIST_PROC, %s", param.toString()));
            }

            result = dataHubMapper.UPDATE_API_AUTH_HIST(param);
            transactionManager.commit(txStatus);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            log.error(e.toString());
            throw new CServiceException("Error in UPDATE_API_AUTH_HIST_PROC");
        }
        return result;
    }

    /***
     * 권한관리 > 신청
     *  KONG API 처리 로직 없음
     */
    public int INSERT_API_AUTH_HIST_PROC(HashMap<String, Object> param) throws CServiceException {
        int result = -1;
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Integer api_id =  (Integer)param.get("api_id");
            String status_cd = (String) param.get("status_cd");

            switch (status_cd) {
                case "APPLY":   /* 신청 */
                    break;
                default:
                    throw new CServiceException(String.format("BadRequest INSERT_API_AUTH_HIST_PROC, %s", param.toString()));
            }

            result = dataHubMapper.INSERT_API_AUTH_HIST(param);
            transactionManager.commit(txStatus);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            log.error(e.toString());
            throw new CServiceException("Error in INSERT_API_AUTH_HIST_PROC");
        }
        return result;
    }

    /***
     * 데이터셋 > 생성 요청 
     *  KONG API 처리 로직 없음
     */
    public int INSERT_DATA_REQ_PROC(HashMap<String, Object> param) throws CServiceException {
        int result = -1;
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            HashMap<String, Object> info = (HashMap<String, Object>) param.get("info");
            HashMap<String, Object> values = (HashMap<String, Object>) param.get("values");
    
            if (info == null || values == null) {
                throw new CServiceException(String.format("BadRequest INSERT_DATA_REQ_PROC, %s", param.toString()));
            }

            // dataset_id 시퀀스 생성
            int dataset_id = dataHubMapper.SELECT_DATASET_REQ_COL_MST(null);
            
            info.put("dataset_id", dataset_id);    
            result = dataHubMapper.INSERT_DATA_SET_AUTH_LIST(info);
            
            values.put("dataset_id", dataset_id); 
            int result2 = dataHubMapper.INSERT_DATA_SET_VALUE_LIST(values);
    
            if (result > 0 && result2 > 0) {
                transactionManager.commit(txStatus);
            } else {
                throw new CServiceException(String.format("Fail INSERT_DATA_REQ_PROC, %s", param.toString()));
            }
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            log.error(e.toString());
            throw new CServiceException("Error in INSERT_DATA_REQ_PROC");
        }
        return result;
    }

     /***
     * API오류이력 > 재처리 요청 
     *  KONG API 처리 로직 없음
     */
    public int UPDATE_API_RETRY_PROC(HashMap<String, Object> param) throws CServiceException {
        int result = -1;
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // Assuming "proc_serial" is the key to store the result
                int serialNum = (Integer) param.get("serialNum");
                String targetUrl = (String) param.get("targetUrl");
                String requestParam = (String) param.get("requestParam");
                
                param.put("serialNum", serialNum);    
                param.put("targetUrl", targetUrl);
                param.put("key", requestParam);  

                DataHubApiController.postHubResendApi(param); 
               
                param.put("proc_serial", serialNum);
                param.put("retry_proc", 'T');
                

                result = dataHubMapper.UPDATE_API_PROC_RETRY_STATUS(param);

            if (result > 0) {
                transactionManager.commit(txStatus);
            } else {
                throw new CServiceException(String.format("Fail UPDATE_API_RETRY_PROC, %s", param.toString()));
            }
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            log.error(e.toString());
            throw new CServiceException("Error in UPDATE_API_RETRY_PROC");
        }

        return result;
    }
    

    

    /***
     * 데이터셋 > 조회 > 요청
     */
    // public String REQUEST_API_DATA_SET_PROC(HashMap<String, Object> param) throws Exception {
    //     TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
    //     try {
    //        String apiUrl = (String) param.get("apiUrl");
    //         HashMap<String, Object> jsonData = (HashMap<String, Object>) param.get("jsonData");

    //         String response = ApiUtil.requestApiDataSetService(apiUrl, jsonData);
    //         transactionManager.commit(txStatus);

    //         return response;
    //     } catcxdsch (Exception e) {
    //         // 예외 처리 로직 추가
    //         e.printStackTrace(); // 예외를 콘솔에 출력하는 예시
    //         transactionManager.rollback(txStatus); // 롤백 추가
    //     }
    //     return null;
    // }

    
    /***
     * 데이터셋 > 승인/반려 
     *  KONG API 처리 로직 없음
     * 
     *  - 승인: addService("ALARM") 
     *      * 데이터셋 승인이 되는 시점에 API 가 생성된게 아니다 
     *      * 따라서 addService 를 할 수 없다. 대신  "권한관리 > 승인" 시점에 해야 한다
     */
    public int UPDATE_DATA_SET_HIST_LIST_PROC(HashMap<String, Object> param) throws CServiceException {
        int result = -1;
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            String status_cd = (String) param.get("status_cd");
            switch (status_cd) {
                case "G":   /* 승인 */ 
                    break;
                case "R":  /* 반려 */
                    break;
                default:
                    throw new CServiceException(String.format("BadRequest UPDATE_DATA_SET_HIST_LIST_PROC, %s", param.toString()));
            }

            result = dataHubMapper.UPDATE_DATA_SET_HIST_LIST(param);
            transactionManager.commit(txStatus);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            log.error(e.toString());
            throw new CServiceException("Error in UPDATE_DATA_SET_HIST_LIST_PROC");
        }
        return result;
    }

    
    public Object INSERT_API_SET_LIST_PROC(HashMap<String, Object> param) throws Exception {
        int result = -1;
        // TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // try {
            String api_id = param.get("api_id").toString();
            String api_url = (String) param.get("api_url");

            log.info("INSERT_API_SET_LIST_PROC: api_id={}, api_url={}", api_id, api_url);
            String kong_host = kongApiController.addService(api_id, api_url); // KONG API 를 이용한 Service 추가
            log.info("INSERT_API_SET_LIST_PROC: kong_host={}", kong_host);

            if (kong_host == null) {
                // 이미 기존에 등록된 서비스 일 경우
                log.info("KONG addService: 이미 등록된 서비스 입니다.");
            } else {
                param.put("api_id", api_id);
                param.put("kong_host", kong_host);
                dataHubMapper.UPDATE_API_MST(param);

                // ON-DEMAND or BATCH or BOTH 에 따라서 TB_API_MST 에 INSERT 한다
                // 2023.09.27 현재는 ON-DEMAND 만 고려 
                // String[] mode = new String[]{"ON-DEMAND"/* , "BATCH" */};
                // for (int i = 0; i < mode.length; i++) {
                //     // kong_host 값 설정
                    
                //     int apiId = dataHubMapper.SELECT_API_MST_NEXTVAL(null);
                //     param.put("api_id", apiId);
                //     param.put("kong_host", kong_host);
                //     param.put("exec_mode_cd", "B");   // BATCH
                //     int rows = dataHubMapper.INSERT_API_MST(param);

                //     // 2023.09.27 현재는 dataset_id 가 넘어오지 않기 때문에 아래 코드는 실행 되지 않음
                //     // String dataset_id = param.get("dataset_id").toString();
                //     // if (rows > 0 && dataset_id != null) {
                //     //     dataHubMapper.UPDATE_DATA_SET_HIST_LIST(param);
                //     //     dataHubMapper.INSERT_DATASET_MAPP_MST(param);
                //     // }
                // }
            }

            // 커밋 전에 성공적으로 처리되었음을 표시
        //     transactionManager.commit(txStatus);
        // } catch (Exception e) {
        //     // 예외 처리 로직 추가
        //     transactionManager.rollback(txStatus); // 롤백 추가
        //     log.error(e.toString());
        // }
        return result;
    }
    
    
}
