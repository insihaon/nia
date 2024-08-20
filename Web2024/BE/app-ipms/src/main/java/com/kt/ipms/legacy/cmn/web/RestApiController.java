package com.kt.ipms.legacy.cmn.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.ipms.legacy.cmn.service.RestApiService;

/**
 * Rest API
 */

@Controller
public class RestApiController {

	@Autowired
	private RestApiService restApiService;
	
	/**
	 * (외부연동 API) IPMS 및 라우팅 IP 정보 수집
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/api/v1/getipinfo", method=RequestMethod.GET)
	@ResponseBody // json
	public Map<String,Object> getipinfo(HttpServletRequest request, HttpServletResponse response) {
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultMap = new LinkedHashMap<String, Object>();
				
		try { 
			
			String query = request.getQueryString();
			String[] params = query.split("&");
			
			if(params.length == 0) {
				
				resultMap.put("resultCd", "R00004");
				resultMap.put("resultMsg", "파라미터 형식 에러");
				
			} else {
				
				resultMap.put("resultCd", "R00001");
				resultMap.put("resultMsg", "연동 조회 성공");
				
				Map<String,Object> m = new HashMap<String, Object>();
				
				for(String s : params) {
					String key = s.split("=")[0];
					String value = s.split("=")[1];
					m.put(key, value);
				}
				
				resultList = restApiService.selectIpInfo(m);
				
				if(resultList.size() > 0) {
					
					// IPMS IP 정보
					resultMap.put("ipBlock", resultList.get(0).get("sipms_ip_block"));
					resultMap.put("subnet", resultList.get(0).get("nipms_ip_bitmask"));
					resultMap.put("useYn", resultList.get(0).get("sipms_use_yn")); // 'IA0005'(할당예약), 'IA0005'(할당)이면 Y(사용중)
					
					// detailList
					List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
					for(Map<String,Object> tmp : resultList) {
						Map<String,Object> map = new LinkedHashMap<String, Object>();	

						// 라우팅 IP 정보
						map.put("ipBlock", tmp.get("srouting_ip_block"));
						map.put("subnet", tmp.get("nrouting_ip_bitmask"));
						map.put("useYn", tmp.get("srouting_use_yn"));		
						
						detailList.add(map);
					}
					resultMap.put("detailList", detailList);
				} else {
					
					
					resultMap.put("resultCd", "R00003");
					resultMap.put("resultMsg", "IPMS 조회 결과 없음");
				}
			}
			
		} catch (Exception e) {
			resultMap.put("resultCd", "R00002");
			resultMap.put("resultMsg", "연동 조회 실패");
		}
		
		
		return resultMap;
	}
}
