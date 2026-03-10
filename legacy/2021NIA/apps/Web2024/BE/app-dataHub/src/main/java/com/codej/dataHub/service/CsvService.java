package com.codej.dataHub.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codej.dataHub.mapper.db2nd.DataHubMapper;
import com.codej.web.security.AuthUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CsvService {

    @Autowired(required = false)
    DataHubMapper dataHubMapper;

    @Autowired
    AuthUserService authUserService;

    public void saveDataFromCsv(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            // 첫 번째 라인을 무시합니다.
            String line = reader.readLine();
            String uid = authUserService.getUserUid();
            while ((line = reader.readLine()) != null) {
                // Assuming CSV file structure matches the fields in YourEntity class
                try {
                    String[] values = line.split(",");
                    int column = 0;

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("table_nm", values[column++]);
                    map.put("column_nm", values[column++]);
                    map.put("data_type", values[column++]);
                    map.put("metadata_desc", values[column++]);
                    map.put("update_user", uid);
                    
                    // MyBatis를 사용하여 데이터베이스에 삽입
                    dataHubMapper.INSERT_DATA_SET_LIST(map);
                } catch (Exception e) {
                    log.error("{}", line, e.toString());
                }
            }
        }
    }
}