package com.codej.nia.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codej.base.controller.BaseController;
import com.codej.base.utils.EncryptUtil;
import com.codej.web.annotation.EncryptResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
public class NiaTestController extends BaseController {

  @GetMapping(value = "/nia/ex1")
  @ResponseBody
	
  public ModelMap getEx1() throws Exception {
    ModelMap model = new ModelMap();
    return model;
  }

  @PostMapping(value = "/nia/ex1")
  @ResponseBody
  @EncryptResponse
  public ModelMap postEx1() throws Exception {
    return getEx1();

    /* 
      // 요청 예제
      fetch("http://localhost:8070/ipms/ex1", {
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          encrypt: 'GpLFqAJUEginXTnHmhg=', // encrypt : true
        }),
        method: "POST",
        mode: "cors",
        credentials: "omit",
      }); 
    */
  }

  @PostMapping(value = "/nia/ex2")
  @ResponseBody
	
  public ResponseEntity<?> postEx2() throws Exception {
    ModelMap model = getEx1();
    return new ResponseEntity<>(encrypt(model), HttpStatus.OK);
  }

  public static String encrypt(Object result) {
    Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss") // 원하는 형식 설정
        .create();
    String json = gson.toJson(result);
    return EncryptUtil.encrypt(json);
  }
}
