package com.kt.ipms.legacy.linkmgmt.batchmgmt.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.service.IpCommonService;
import com.kt.ipms.legacy.cmn.util.ExcelUtil;
import com.kt.ipms.legacy.cmn.util.MobileHostUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogVo;

@Component
@Transactional
public class ExcelParseBatchService {

	@Autowired
	private ExcelUtil excelUtil;
	
@Lazy @Autowired
	private ConfigPropertieService configPropertieService;
	
@Lazy @Autowired
	private ExcelParseBatchTxService excelParseBatchTxService;
	
@Lazy @Autowired
	private IpCommonService ipCommonService;
	
@Lazy @Autowired
	private TbBatchLogTxService tbBatchLogTxService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIpHostMstBatch() {
		
		Random random = new Random();
		random.setSeed(Calendar.getInstance().getTimeInMillis());
		TbBatchLogVo insertLogVo = new TbBatchLogVo();
		insertLogVo.setSifId("NMS_IPM_BT_0004");
		insertLogVo.setSsysNm("IPMS");
		insertLogVo.setStbNm("TB_IP_HOST_MST");
		insertLogVo.setSbatchEndYn("N");
		insertLogVo.setSstepStatus("START");
		insertLogVo.setScomment("EXCEL PARSE PROCESSING");
		BigInteger npid = new BigInteger(String.valueOf(random.nextInt(65536)), 10);
		insertLogVo.setNpid(npid);
		
		int allCnt = 0;
		
		try {
			tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
	        String date = formatter.format(new java.util.Date());
	        
			String filePath = configPropertieService.getString("Excel.fileUrl");
			String configFileName = configPropertieService.getString("Excel.fileName");
			String configFilePrefix = configPropertieService.getString("Excel.filePreFix");
			
			String filePreFix = "";
			if(!configFilePrefix.equals("null")) {
				filePreFix = "." + configFilePrefix;
			}
			
			//Codeeyes-Urgent-String 추가 시 String Buffer 사용
			StringBuffer sb = new StringBuffer();
			sb.append(date);
			sb.append("000000");
			String strDate = sb.toString();
//			date = date + "000000";  // 20210325000000			
			
			String fileName = configFileName + strDate + filePreFix;  
			
			List<String> columns = new ArrayList<String>();
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			
			columns.add("A");		// 장비보유네트워크운용본부 -> 수용국
			columns.add("B"); 	// 장비구분	-> 용도
			columns.add("E");		// 장비명		-> 장비명(호스트명)
			columns.add("F");		// 아이피		-> Host IP
			
			// 엑셀 데이터 읽기
			// resultList = excelUtil.parseExcel(filePath, fileName, columns);
			
			// csv 데이터 읽기
			String[] colArr = {"A", "B", "E", "F"};
			resultList = excelUtil.parseNmsHostIp(filePath, fileName, colArr);
			
			int insert = 0;
			int update = 0;
			if(resultList.size() > 0) {

				allCnt = resultList.size();
				Map<String,Object> checkMap = new LinkedHashMap<String, Object>();
				
				Iterator it = resultList.iterator();
				while(it.hasNext()) {
					
					Map<String,Object> data = (Map<String, Object>) it.next();
					
					// 데이터 중복 제거
					if(!checkMap.containsKey(data.get("F"))) {		// Host IP
						
						checkMap.put((String) data.get("F"),data.get("F")); //중복제거된 값이 key, value 로 들어감.
						
						// 수용국 데이터 맵핑
						Map<String,Object> searchMap = new LinkedHashMap<String, Object>();
						
//						String a = new String(data.get("A").toString().getBytes("EUC-KR"), "UTF-8");
//						String b = new String(data.get("B").toString().getBytes("EUC-KR"), "UTF-8");
						String a = data.get("A").toString();
						String b = data.get("B").toString();
						 
						String lvlNm = MobileHostUtil.findLvlNm(a, b);
						
						searchMap.put("lvlNm", lvlNm);
						// 맵핑된 수용국 명으로 수용국 테이블에서 코드 조회
						Map<String,Object> lvl = null;
						lvl = excelParseBatchTxService.selectLvlNm(searchMap);
						
						if(null != lvl) {
							
							// 시설 정보 등록

							String ipBlock = data.get("F").toString();
							
							// IP 주소 형식
							//if(Pattern.matches("((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])([.](?!$)|$)){4}", ipBlock)){
							if(ipBlock.contains(".") || ipBlock.contains("/")) {
								
								String subnetMask = "/32";   // 디폴트 (기존 화면 등록시와 동일)
//								String e = new String(data.get("E").toString().getBytes("EUC-KR"), "UTF-8");
								String e = data.get("E").toString();
								
								String modelNm = e;
								String comment = data.get("B").toString();
								
								if(ipBlock.contains("/")) {
									String[] arr = ipBlock.split("/"); 
									ipBlock = arr[0]; 
									subnetMask = "/" + arr[1];
								}
							
								TbIpHostMstVo tbIpHostMstVo = new TbIpHostMstVo();
								
								tbIpHostMstVo.setSipHostTypeCd("HT0010"); // N/W INFRA-수작업  (기존 화면 등록시와 동일)
								tbIpHostMstVo.setSprorityYn("Y"); // 대표여부_YN (기존 화면 등록시와 동일)
								tbIpHostMstVo.setSipHostNm(modelNm); // IP_HOST_명
								tbIpHostMstVo.setSrssofficescode(lvl.get("slvl_cd").toString()); // 수용국코드
								tbIpHostMstVo.setSipBlock(ipBlock);
								tbIpHostMstVo.setSnetmask(subnetMask);
								tbIpHostMstVo.setPipHostInet(ipBlock + subnetMask);
								tbIpHostMstVo.setPipPrefix(ipBlock + subnetMask);
								// tbIpHostMstVo.setSmodelname(modelNm);
								tbIpHostMstVo.setScomment(comment);
								tbIpHostMstVo.setSipVersionTypeCd("CV0001"); // IPv4
								tbIpHostMstVo.setScreateId("Batch");
								tbIpHostMstVo.setSmodifyId("Batch");
								
								int checkCnt = 0;
								TbIpHostMstVo checkVo = new TbIpHostMstVo();
								checkVo.setSipHostTypeCd("HT0010");
								checkVo.setSipBlock(ipBlock);
								checkCnt = excelParseBatchTxService.countIpHostMst(checkVo);
								
								ipCommonService.setBaseIpBlockMstInfo(tbIpHostMstVo);
								
								// sipHostTypeCd = 'HT0010'  인 것들은 IP 중복되면 안됨
								if(checkCnt > 0){
									excelParseBatchTxService.updateIpHostMst(tbIpHostMstVo);
									update +=1;
								} else {
									excelParseBatchTxService.insertIpHostMst(tbIpHostMstVo);
									insert +=1;
								}
								
							}
						}
					} 
				}
				
			}
			
			insertLogVo.setSbatchEndYn("Y");
			insertLogVo.setScomment("EXCEL PARSE COLLECTION SUCCESS // 전체 " + allCnt +  " , 입력 " + insert  + " , 수정 " + update);
			insertLogVo.setSstepStatus("PROCESS SUCCESS");
			
			tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
			
		} catch (ServiceException e) {
			PrintLogUtil.printError(e);
			insertLogVo.setScomment("EXCEL PARSE COLLECTION ERROR");
			insertLogVo.setSstepStatus("PROCESS ERROR");
			insertLogVo.setSbatchEndYn("Y");
			if (insertLogVo.getNinputSeq() != null) {
				tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
			} else {
				tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			}

		} catch (Exception e) {
			PrintLogUtil.printError(e);
			insertLogVo.setScomment("EXCEL PARSE COLLECTION ERROR");
			insertLogVo.setSstepStatus("PROCESS ERROR");
			insertLogVo.setSbatchEndYn("Y");
			if (insertLogVo.getNinputSeq() != null) {
				tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
			} else {
				tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			}
		}
		
	}
}
