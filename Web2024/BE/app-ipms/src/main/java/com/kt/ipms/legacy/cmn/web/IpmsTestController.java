package com.kt.ipms.legacy.cmn.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codej.base.annotation.EncryptResponse;
import com.codej.base.utils.EncryptUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

@Controller
public class IpmsTestController extends CommonController {

  @GetMapping(value = "/ipms/ex1")
  @ResponseBody
  public ModelMap getEx1() throws Exception {
    CommonVo vo = new CommonVo();
    vo.setSearchWrd("KEYWORD");
    return createResult(vo, null);
  }

  @PostMapping(value = "/ipms/ex1")
  @ResponseBody
  @EncryptResponse
  public ModelMap postEx1() throws Exception {
    CommonVo vo = new CommonVo();
    vo.setSearchWrd("KEYWORD");
    return createResult(vo, null);
  }

  @GetMapping(value = "/ipms/ex2")
  @ResponseBody
  public ResponseEntity<?> getEx2() throws Exception {
    ModelMap model = getEx1();
    return new ResponseEntity<>(encrypt(model), HttpStatus.OK);
  }

  @PostMapping(value = "/ipms/ex2")
  @ResponseBody
  public ResponseEntity<?> postEx2() throws Exception {
    ModelMap model = getEx1();
    return new ResponseEntity<>(encrypt(model), HttpStatus.OK);

    /* 
      // 요청 예제
      fetch("http://localhost:8070/ipms/ex2", {
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          encrypt: true, // encrypt 값을 추가
        }),
        method: "POST",
        mode: "cors",
        credentials: "omit",
      }); 
    */
  }

  public static String encrypt(Object result) {
    Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss") // 원하는 형식 설정
        .create();
    String json = gson.toJson(result);
    return EncryptUtil.encrypt(json);
  }
}
