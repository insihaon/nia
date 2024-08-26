package com.kt.ipms.legacy.test.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.controller.BaseController;
import com.codej.base.dto.model.ResultMap;
import com.kt.ipms.legacy.cmn.service.CommonCodeService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.test.service.TestService;

@RestController
public class TestController extends BaseController{

    @Autowired
	private TestService service;

    @Autowired
	protected CommonCodeService commonCodeService;


    @GetMapping(value = "/now1")
    public ResponseEntity<?> now1() throws Exception {
        return new ResponseEntity<>("now1 = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), HttpStatus.OK);
    }

    @GetMapping(value = "/test")
    public ResponseEntity<?> test() throws Exception {
        ResultMap result = service.SELECT_NOW(null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/test2")
    public List<CommonCodeVo> test2() throws Exception {
        return commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_SEQ_CD, null);
    }
}
