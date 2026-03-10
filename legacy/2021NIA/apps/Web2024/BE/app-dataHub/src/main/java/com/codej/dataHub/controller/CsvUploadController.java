package com.codej.dataHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codej.base.dto.response.SingleResponse;
import com.codej.dataHub.service.CsvService;
import com.codej.web.service.ResponseService;

@RestController
public class CsvUploadController {

    @Autowired
    ResponseService responseService;
    
    @Autowired
    CsvService csvService;


    @PostMapping("/upload_meta")
    public SingleResponse<Object> uploadMeta(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return responseService.createSingleResponse("파일이 없습니다.");
        }

        try {
            csvService.saveDataFromCsv(file);
            return responseService.createSingleResponse(true);
        } catch (Exception e) {
            return responseService.createSingleResponse(e.toString());
        }
    }
}