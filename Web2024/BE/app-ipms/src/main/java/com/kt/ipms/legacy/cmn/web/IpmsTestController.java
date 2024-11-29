package com.kt.ipms.legacy.cmn.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codej.base.utils.EncryptUtil;
import com.codej.web.annotation.EncryptResponse;
import com.codej.web.vo.BaseVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;

@Controller
public class IpmsTestController extends CommonController {

  @GetMapping(value = "/ipms/ex1")
  @ResponseBody
	@EncryptResponse
  public ModelMap getEx1() throws Exception {
    CommonVo vo = new CommonVo();
    vo.setSearchWrd("KEYWORD");
    return createResult(vo, null);
  }

  @PostMapping(value = "/ipms/ex1")
  @ResponseBody
	@EncryptResponse
  public ModelMap postEx1(@RequestBody IpAllocOperMstVo searchVo, ModelMap model, HttpServletRequest request) throws Exception {
    CommonVo vo = new CommonVo();
    vo.setSearchWrd("KEYWORD");
    return createResult(vo, null);
  }

  @PostMapping(value = "/ipms/ex2")
  @ResponseBody
	@EncryptResponse
  public ResponseEntity<?> postEx2(@RequestBody HashMap<String, Object> param) throws Exception {
    ModelMap result = getEx1();
    return new ResponseEntity<>(encrypt(result), HttpStatus.OK);

    /* 
      // 요청 예제
      fetch("http://localhost:8070/ipms/ex2", {
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

  @PostMapping(value = "/ipms/ex3.model")
  @ResponseBody
  @EncryptResponse
  public ModelMap selectListIpBlockMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
      IpAllocOperMstListVo resultListVo = new IpAllocOperMstListVo();
      return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
  }

  @PostMapping(value = "/ipms/ex4.json")
	@ResponseBody
	@EncryptResponse
  public ResponseEntity<?> selectListIpBlockMstExcel(@RequestBody IpAllocOperMstVo searchVo,
			HttpServletRequest request) {
        FileVo resultVo = new FileVo();
        return new ResponseEntity<>(resultVo, HttpStatus.OK);
  }


  @PostMapping(value = "/ipms/ex5.json")
	@ResponseBody
	@EncryptResponse
  public BaseVo selectSearchtNeMst(@RequestBody IpAllocOperMstVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstListVo resultListVo = null;
    resultListVo = new IpAllocOperMstListVo();
    resultListVo.setSrchTypeFlag("This is SrchTypeFlag");
    resultListVo.setCommonMsg("성공했다");
    resultListVo.setTbRoutChkMstVo(new TbRoutChkMstVo());
    return resultListVo;
  }
  

  public static String encrypt(Object result) {
    Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss") // 원하는 형식 설정
        .create();
    String json = gson.toJson(result);
    return EncryptUtil.encrypt(json);
  }
}
