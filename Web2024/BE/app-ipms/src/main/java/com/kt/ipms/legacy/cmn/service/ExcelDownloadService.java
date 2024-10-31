package com.kt.ipms.legacy.cmn.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.codej.web.controller.FileController;

import com.kt.ipms.legacy.cmn.util.ExcelUtil;
import com.kt.framework.utils.StringUtils;
import com.kt.framework.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExcelDownloadService {

    @Autowired
	protected FileController fileController;
    @Autowired
	protected ExcelUtil excelUtil;

    @Value("${file.excel-upload-dir:excelFiles}")
    private String excelUploadDir;
    
    public ResponseEntity<Resource> generateAndDownloadExcel(List<?> voList, List<String> mappingList, HttpServletRequest request) {
        try {
            String fileName = excelUtil.createExcelFile(voList, mappingList, request);
            if(!StringUtils.hasText(fileName)) {
                throw new ServiceException("CMN.HIGH.00050");
            }
            return fileController.downloadFile(excelUploadDir, fileName, request);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

