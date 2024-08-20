package com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.CommonCodeService;
import com.kt.ipms.legacy.cmn.service.IpCommonService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.IpCalculateUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.service.AssignMgmtService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.service.AssignMgmtTxService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadListVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadSubListVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadSubVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.log4kt.utils.StringUtil;



@Component
@Transactional
public class IpUploadMgmtService {

	@Autowired
	private IpUploadMgmtTxService ipUploadMgmtTxService;
	
	@Autowired
	private AssignMgmtTxService assignMgmtTxService;
	
	@Autowired
	protected CommonCodeService commonCodeService;
	
	@Autowired
	private IpCalculateUtil ipCalculateUtil;
	
	@Autowired
	private IpCommonService ipCommonService;
	
	@Autowired
	private AllocMgmtService allocMgmtService;
	
	@Autowired
	private AssignMgmtService assignMgmtService;

	@Transactional(readOnly = true)
	public TbIpUploadListVo selectListPageIpUpload(TbIpUploadVo searchVo) {
		TbIpUploadListVo resultListVo = null;
		
		try{
			
			List<TbIpUploadVo> resultList = ipUploadMgmtTxService.selectListPageIpUpload(searchVo);
			int totalCount = ipUploadMgmtTxService.countListPageIpUpload(searchVo);
			
			resultListVo = new TbIpUploadListVo();
			resultListVo.setTbIpUloadVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois목록"});
		}
		
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListTbIpHostMst(TbIpUploadVo searchVo, TbLvlBasVo tbLvlBasVo) {
		List<IpAllocOperMstVo> retList = ipUploadMgmtTxService.selectListTbIpHostMst(searchVo);
		return retList;
	}

	public void insertTbIpUploadMst(TbIpUploadVo insertVo) {
		ipUploadMgmtTxService.insertTbIpUploadMst(insertVo); 
	}

	public List<Map<String,Object>> parseUploadFile(MultipartHttpServletRequest request, TbIpUploadVo insertVo, File convFile) throws IOException {
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		FileInputStream fis = null;
		try{
			
			// 엑셀 읽기
			fis = new FileInputStream(convFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			
			String value = "";
			String cellName = "";
			
			List<String> getColumns = new ArrayList<String>();
			getColumns.add("A");
			getColumns.add("B");
			getColumns.add("C");
			getColumns.add("D");
			getColumns.add("E");
			getColumns.add("F");
			getColumns.add("G");
			getColumns.add("H");
			getColumns.add("I");
			getColumns.add("J");
			getColumns.add("K");
			getColumns.add("L");
			getColumns.add("M");
			getColumns.add("N");
			getColumns.add("O");
			getColumns.add("P");
			getColumns.add("Q");
			
//			List<String> setColumns = new ArrayList<String>(Arrays.asList("(필수)서비스망유형CD","(필수)IP생성유형코드","(필수)IP할당유형코드","(필수)IP블록","(필수)IP블록 입력범위 ","(필수)게이트웨이","수용국코드","장비대표IP",	"장치모델명","시설표준코드","장비별칭","전용번호","인터페이스명","국축스위치시리얼IP","가입자축스위치시리얼IP","수용회선명",	"비고"));
			List<String> setColumns = new ArrayList<String>(Arrays.asList("ssvcLineTypeCd","sipCreateTypeCd","sassignTypeCd","pipprefix","pipprefixRange","sgatewayip","sicisofficescode","ssubscmstip","smodelname","ssubscnnescode","ssubscnealias","sllnum","ssubsclgipportdescription","ssubsclgipportip","ssubscrouterserialip","sconnalias","scomment"));
			
			int rowIndex = 0;
			int columnIndex = 0;
			int getRowIndex = 1;
	
			XSSFSheet sheet = workbook.getSheetAt(0);		// 시트 수
	
			int rows = sheet.getPhysicalNumberOfRows();		// 행의 수
			
			for(rowIndex=0; rowIndex<=rows; rowIndex++) {
				Map<String,Object> map = new LinkedHashMap<String, Object>();
				
				XSSFRow row = sheet.getRow(rowIndex);
				
				if(row != null && rowIndex > 0) {
				
					//int cells = row.getPhysicalNumberOfCells();	//   셀의 수
					int cells = 17;
					int getCells = cells - setColumns.size();
					
					for(columnIndex=0; columnIndex<=cells; columnIndex++) {
						
						XSSFCell cell = row.getCell(columnIndex);
		
						cellName = CellReference.convertNumToColString(columnIndex);
	
						if(rowIndex<getRowIndex){
							continue;
						}
	
						if(cell == null) {
							continue;		// 셀이 빈값일 경우를 위한 null check
						} else {
							
							// 추출대상 커럼인지 확인 
							if(!getColumns.contains(cellName)) {
								continue;
							}
							
							// 셀 타입 별로 내용 읽기
							switch(cell.getCellType()) {
								case Cell.CELL_TYPE_FORMULA:
									value=cell.getCellFormula();
									break;
								case Cell.CELL_TYPE_NUMERIC:
									value=cell.getNumericCellValue()+"";
									break;
								case Cell.CELL_TYPE_STRING:
									value=cell.getStringCellValue()+"";
									break;
								case Cell.CELL_TYPE_BLANK:
									value=cell.getStringCellValue()+"";
									break;
								case Cell.CELL_TYPE_ERROR:
									value=cell.getErrorCellValue()+"";
									break;
								default:
									break;
							}
							
							map.put(setColumns.get(columnIndex-getCells), value);
	
						}
						
					}
					
					resultList.add(map);
					
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00000");
		}finally{
			fis.close();
		}
		
		return resultList;
		
	} 
	
	public List<Map<String,Object>> validSetUploadFile(MultipartHttpServletRequest request, TbIpUploadVo insertVo, List<Map<String,Object>> parseList, List<CommonCodeVo> sipCreateTypeCds, List<CommonCodeVo> sipVersionTypeCds, List<CommonCodeVo> svcLineTypeCds, List<CommonCodeVo> sLvlSubvCds, List<CommonCodeVo> sassignTypeCds) {
		
		try{
			
			//Step1. 해당 계위 정보로 필요 유효성 검증 코드값 로드 ( 서비스망 유형 CD , 생성유형코드 , 할당유형코드 , 수용국코드)
			IpAllocMstComplexVo ipAllocMstComplexVo = new IpAllocMstComplexVo();
			String pipPattern = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\/([0-9]{1,2})";
			String ipPattern = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";
			
			int defaultBit = 0; // 비교 기준 bitmask
			int compareBit = 32;
			int maxCount = 0;
			
			String defaultIpBlock = ""; // 비교 기준 Ipblock
			BigInteger defaultStartIp = new BigInteger("0"); // 비교 기준  시작 IP 
			BigInteger defaultEndIp = new BigInteger("0"); // 비교 기준 끝 IP
			
			String defaultipBlockRange = ""; // IP블록입력범위
			
			// 블록 범위 체크
			for(int i=0;i<parseList.size();i++){
				Map<String,Object> validMap = parseList.get(i);
				String sValue = validMap.get("pipprefixRange").toString();
				
				if(StringUtil.isNullorBlank(sValue)){ // null
					PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00055 IP블록입력범위");
					throw new ServiceException("APP.HIGH.00055", new String[]{"IP블록입력범위","입력하셔야"}); // IP블록입력범위 를 입력하셔야 합니다.
				}else{
					if(Pattern.matches(pipPattern, sValue)){ // cidr 규칙 확인
					
						if(i==0){ // 처음값으로 IP블록입력범위 설정
							defaultipBlockRange = sValue;
						}else{
							if(!defaultipBlockRange.equals(sValue)){
								PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00055 IP블록입력범위");
								throw new ServiceException("APP.HIGH.00056", new String[]{"IP블록입력범위","일치하지"}); // IP블록입력범위가 일치하지 않습니다.
							}
						}
						
						// 맨 마지막에서 비교 기준 설정
						if(i==parseList.size()-1){				
							
							String[] splitStringRange = sValue.split("/");
							String sIpBlockRange = splitStringRange[0];
							String sBitmaskRange = splitStringRange[1];
							int iBitmaskRange = Integer.parseInt(sBitmaskRange);
							
							defaultIpBlock = sIpBlockRange;
							defaultStartIp = ipCalculateUtil.getStartIpAddressDecimal(sIpBlockRange, iBitmaskRange, "CV0001");
							defaultEndIp = ipCalculateUtil.getEndIpAddressDecimal(sIpBlockRange, iBitmaskRange, "CV0001");
							defaultBit = iBitmaskRange;
							maxCount = (int)Math.pow(2, 32-defaultBit);
							
						}
						
					}else{
						PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 IP 블럭입력범위 형식");
						throw new ServiceException("APP.HIGH.00056", new String[]{"IP 블록입력범위 형식","맞지"}); // PIP PREFIX 형식가 맞지 않습니다.
					}
				}
			}
			
			
			for(int i =0 ;i<parseList.size();i++){
				Map<String,Object> validMap = parseList.get(i);
				validMap.put("ssvcLineTypeCd", insertVo.getSsvcLineTypeCd());
				validMap.put("ssvcGroupCd", insertVo.getSsvcGroupCd());
				validMap.put("ssvcObjCd", insertVo.getSsvcObjCd());
								
				String sIpBlock = "";
				String sBitmask = "";
				String sIpCreateType = "";
				
				BigInteger tempStartIp = new BigInteger("0");
				BigInteger tempEndIp = new BigInteger("0");
				int iBitmask = 0;
				
				for(Map.Entry<String, Object> entry : validMap.entrySet()){

					String sKey = entry.getKey();
					String sValue = entry.getValue().toString();
					
					if(sKey.equals("ssvcLineTypeCd")){
						
						if(StringUtil.isNullorBlank(sValue)){ // null
							PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00055 서비스망유형CD");
							throw new ServiceException("APP.HIGH.00055", new String[]{"서비스망유형CD","입력하셔야"}); // 서비스망유형CD 를 입력하셔야 합니다.
						}else{ // 코드목록
							boolean testFlag = false;
							
							for(int j=0;j<svcLineTypeCds.size();j++){
								if(svcLineTypeCds.get(j).getCode().equals(sValue)){
									testFlag = true;
									break;
								}
							}
							
							if(!testFlag){
								PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 서비스망유형CD");
								throw new ServiceException("APP.HIGH.00056", new String[]{"서비스망유형CD","일치하지"}); // 서비스망유형CD 가 일치하지 않습니다.
							}
						}
						
					}else if(sKey.equals("sipCreateTypeCd")){
						
						if(StringUtil.isNullorBlank(sValue)){ // null
							PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00055 IP생성유형코드");
							throw new ServiceException("APP.HIGH.00055", new String[]{"IP생성유형코드","입력하셔야"}); // IP생성유형코드 를 입력하셔야 합니다.
						}else{ // 코드목록
							boolean testFlag = false;
							
							for(int j=0;j<sipCreateTypeCds.size();j++){
								if(sipCreateTypeCds.get(j).getCode().equals(sValue)){
									testFlag = true;
									break;
								}
							}
							
							if(!testFlag){
								PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 서비스망유형CD");
								throw new ServiceException("APP.HIGH.00056", new String[]{"IP생성유형코드","일치하지"}); // 서비스망유형CD 가 일치하지 않습니다.
							}else{
								sIpCreateType = sValue;
							}
							
						}
						
					}else if(sKey.equals("sassignTypeCd")){
						
						if(StringUtil.isNullorBlank(sValue)){ // null
							PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00055 IP할당유형코드");
							throw new ServiceException("APP.HIGH.00055", new String[]{"IP할당유형코드","입력하셔야"}); // IP생성유형코드 를 입력하셔야 합니다.
						}else{ // 코드목록 
							boolean testFlag = false;
							
							for(int j=0;j<sassignTypeCds.size();j++){
								if(sassignTypeCds.get(j).getCode().equals(sValue)){
									testFlag = true;
									break;
								}
							}
							
							if(!testFlag){
								PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 IP할당유형코드");
								throw new ServiceException("APP.HIGH.00056", new String[]{"IP할당유형코드","일치하지"}); // IP생성유형코드 가 일치하지 않습니다.
							}
						}
						
					}else if(sKey.equals("pipprefix")){
						if(StringUtil.isNullorBlank(sValue)){ // null
							PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00055 PIP_PREFIX");
							throw new ServiceException("APP.HIGH.00055", new String[]{"PIP_PREFIX","입력하셔야"}); // PIP_PREFIX 를 입력하셔야 합니다.
						}else{ // pip 형식 체크
							if(Pattern.matches(pipPattern, sValue)){
								// cidr 규칙 확인
								String[] splitString = sValue.split("/");
								sIpBlock = splitString[0];
								sBitmask = splitString[1];
								iBitmask = Integer.parseInt(sBitmask);
								
								String bBitmask = ipCalculateUtil.getBinaryIpAddress(ipCalculateUtil.getDecimalToStringFull(new BigInteger(ipCalculateUtil.getNetMaskDecimal(iBitmask,"CV0001").toString()),"CV0001"),"CV0001");
								String bIpAddress = ipCalculateUtil.getBinaryIpAddress(sIpBlock, "CV0001");
								
								String sOrString = ipCalculateUtil.StringOrCalculater(bBitmask, bIpAddress);
								
								if(!bBitmask.equals(sOrString)){
									PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 PIP_PREFIX 형식");
									throw new ServiceException("APP.HIGH.00056", new String[]{"PIP_PREFIX 형식","맞지"}); // PIP_PREFIX 형식이 일치하지 않습니다.
								}
								
								// IP 블럭이 기준점 넘어가는지 체크
								tempStartIp = ipCalculateUtil.getStartIpAddressDecimal(sIpBlock, iBitmask, "CV0001");
								tempEndIp = ipCalculateUtil.getEndIpAddressDecimal(sIpBlock, iBitmask, "CV0001");
								
								if(tempStartIp.compareTo(defaultEndIp)==1 || tempEndIp.compareTo(defaultStartIp) == -1){
									PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 IP 블럭 범위");
									throw new ServiceException("APP.HIGH.00056", new String[]{"IP 블럭 범위","일치하지"}); // IP 블럭 범위가 일치하지 않습니다.
								}
																	
								int tempBit = compareBit-iBitmask;
								maxCount = maxCount - (int)Math.pow(2, tempBit);
								
								if(maxCount<0){
									PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00055 IP Block 총합");
									throw new ServiceException("APP.HIGH.00055", new String[]{"IP Block 총합","초과"}); // BitMask 범위 를 초과 합니다.
								}

								if(Math.abs(iBitmask-defaultBit)>10){
									PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00055 업로드 가능한 Bitmask 범위");
									throw new ServiceException("APP.HIGH.00056", new String[]{"업로드 가능한 Bitmask 범위","맞지"}); // 업로드 가능한 Bitmask 범위가 맞지않습니다
								}										

							}else{ // 형식에러
								PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 IP 블럭 형식");
								throw new ServiceException("APP.HIGH.00056", new String[]{"IP 블록 형식","맞지"}); // PIP PREFIX 형식가 맞지 않습니다.
							}
						}
						
					}else if(sKey.equals("sgatewayip")){
						if(StringUtil.isNullorBlank(sValue)){ // null
							PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00055 게이트웨이IP");
							throw new ServiceException("APP.HIGH.00055", new String[]{"게이트웨이IP","입력하셔야"}); // 게이트웨이IP 를 입력하셔야 합니다.
						}else{ // ip 형식 체크
							if(Pattern.matches(ipPattern, sValue)){
								
								BigInteger startIp = ipCalculateUtil.getStartIpAddressDecimal(sIpBlock, Integer.parseInt(sBitmask), "CV0001"); 
								BigInteger endIp = ipCalculateUtil.getEndIpAddressDecimal(sIpBlock, Integer.parseInt(sBitmask), "CV0001");
								BigInteger gatewayIp = ipCalculateUtil.getStringToDecimal(sValue, "CV0001");
								
								if(gatewayIp.compareTo(startIp)== -1 || gatewayIp.compareTo(endIp)== 1){ // gateway 범위 체크
									PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 게이트웨이IP 범위");
									throw new ServiceException("APP.HIGH.00056", new String[]{"게이트웨이IP 범위","맞지"}); // 게이트웨이IP가 맞지 않습니다.
								}
							}else{ // 형식에러
								PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 게이트웨이IP");
								throw new ServiceException("APP.HIGH.00056", new String[]{"게이트웨이IP","맞지"}); // 게이트웨이IP가 맞지 않습니다.
							}
						}
					}else if(sKey.equals("sicisofficescode")){
						boolean testFlag = false;
						
						for(int j=0;j<sLvlSubvCds.size();j++){
							if(sLvlSubvCds.get(j).getCode().equals(sValue)){
								testFlag = true;
								break;
							}
						}
						
						if(!testFlag){
							PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 수용국코드");
							throw new ServiceException("APP.HIGH.00056", new String[]{"수용국코드","일치하지"}); // 서비스망유형CD 가 일치하지 않습니다.
						}
						
					}
					
				}
								
				validMap.put("sfirst_addr", ipCalculateUtil.getIpAddressFromDecimal(tempStartIp, "CV0001"));
				validMap.put("slast_addr", ipCalculateUtil.getIpAddressFromDecimal(tempEndIp, "CV0001"));
				validMap.put("nfirst_addr", tempStartIp);
				validMap.put("nlast_addr", tempEndIp);
				validMap.put("nbitmask", iBitmask);
				validMap.put("sIpBlock",sIpBlock);
				
			} // end for

		} catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessageKey(),e.getArgs());
		} catch (Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return parseList;
		
	}

	public void insertIpUploadSub(MultipartHttpServletRequest request,List<Map<String, Object>> parseList, TbIpUploadVo insertVo) {
		
		try{
						
			for(int i=0;i<parseList.size();i++){
			
				Map<String,Object> retMap = parseList.get(i);
								
				BigInteger nLvlMstSeq = ipUploadMgmtTxService.selectNlvlMstSeq(retMap);
				insertVo.setNlvlMstSeq(nLvlMstSeq);
				
				retMap.put("mst_seq", insertVo.getSeq());
				retMap.put("screate_id",insertVo.getScreateId());
				retMap.put("smodify_id",insertVo.getScreateId());
				retMap.put("nlvl_mst_seq",insertVo.getNlvlMstSeq());
				
				// IP SUB 테이블에 등록
				ipUploadMgmtTxService.insertIpUploadSub(retMap);
			}
			
			// Step2. 분할 대상중 빈 IP 채우기
			List<Map<String,Object>> compareList = ipUploadMgmtTxService.selectCompareList(insertVo);

			// 분할 목록만 가져오기
			List<Map<String,Object>> divList = new ArrayList<Map<String,Object>>();
			for(int i=0;i<compareList.size();i++){
				Map<String,Object> tempMap = compareList.get(i);
				if(tempMap.get("div_target").equals("Y")){
					divList.add(tempMap);
				}
			}
			
			List<TbIpAssignMstVo> mergeList = new ArrayList<TbIpAssignMstVo>(); // 병합함수용 목록
			List<BigInteger> blankList = new ArrayList<BigInteger>();
			List<BigInteger> tempBlankList = new ArrayList<BigInteger>();
			List<BigInteger> delList = new ArrayList<BigInteger>();
			
			BigInteger nFirstAddr = new BigInteger("0"); // IPMS
			BigInteger nLastAddr = new BigInteger("0");
			BigInteger nUpFirstAddr = new BigInteger("0"); //UP 
			BigInteger nUpLastAddr = new BigInteger("0");
			BigInteger nUpFinalAddr = new BigInteger("0");
			boolean changeFlag = false;
			
			
			for(int i=0;i<divList.size();i++){
				Map<String,Object> tempMap = divList.get(i);
				
				if(nFirstAddr.compareTo(BigInteger.ZERO)==0){
					nFirstAddr = new BigInteger(tempMap.get("ipms_nfirst_addr").toString()); // IPMS
					nLastAddr = new BigInteger(tempMap.get("ipms_nlast_addr").toString());
					// changeFlag = false;
					
					BigInteger forvar = nLastAddr.subtract(nFirstAddr);
					int iForVar = forvar.intValue();
					
					String ipmsPipPrefix = tempMap.get("ipms_pip_perfix").toString();
					String[] splitPipPrefix = ipmsPipPrefix.split("/");
					int sBitmaskRange = Integer.parseInt(splitPipPrefix[1]);
					
					if(sBitmaskRange <16){
						String alertMessage = "분할 또는 할당 대상 IP Block 의 Bitmask 가 너무 큽니다. 16bit 부터 가능합니다. 대상 IP Block : ("+ipmsPipPrefix+")";
						throw new ServiceException("CMN.INFO.00054", new String[]{alertMessage}); // 분할 또는 할당 대상 IP Block 의 Bitmask 가 너무 큽니다. 
					}
					
					
					if(tempBlankList.isEmpty()){
						for(int j=0;j<=iForVar;j++){
							BigInteger addInteger = nFirstAddr.add(new BigInteger(Integer.toString(j)));
							blankList.add(addInteger);
						}
					}
					
				}else if(nFirstAddr.compareTo(new BigInteger(tempMap.get("ipms_nfirst_addr").toString()))!=0){

					nFirstAddr = new BigInteger(tempMap.get("ipms_nfirst_addr").toString());
					nLastAddr = new BigInteger(tempMap.get("ipms_nlast_addr").toString());
					
					BigInteger forvar = nLastAddr.subtract(nFirstAddr);
					int iForVar = forvar.intValue();
					
					if(tempBlankList.isEmpty()){
						for(int j=0;j<=iForVar;j++){
							BigInteger addInteger = nFirstAddr.add(new BigInteger(Integer.toString(j)));
							blankList.add(addInteger);
						}
					}
				}
			
				nUpFirstAddr = new BigInteger(tempMap.get("up_nfirst_addr").toString()); //UP 
				nUpLastAddr = new BigInteger(tempMap.get("up_nlast_addr").toString());
				
/**
//				Upload 되는 block 제거
//				BigInteger forvar2 = nUpLastAddr.subtract(nUpFirstAddr);
//				int iForVar2 = forvar2.intValue();
//				
//				for(int j=0;j<=tempBlankList.size();j++){
//					if(tempBlankList.get(j).compareTo(nUpFirstAddr)==0){
//
//						for(int k=0;k<=iForVar2;k++){
//							tempBlankList.remove(j);
//						}
//						break;
//					}
//				}
//				nUpFinalAddr = nUpLastAddr;
//				blankList.addAll(tempBlankList);
* 
*/
				
				BigInteger forvar2 = nUpLastAddr.subtract(nUpFirstAddr);
				int iForVar2 = forvar2.intValue();
				
				for(int k=0;k<=iForVar2;k++){
					BigInteger delInt = nUpFirstAddr.add(new BigInteger(Integer.toString(k)));
					delList.add(delInt);
				}

			} // 
			
			for(int i=0;i<delList.size();i++){
				blankList.remove(delList.get(i));
			}
						
			for(int i=0;i<blankList.size();i++){

				String ipBlock = ipCalculateUtil.getIpAddressFromFullBinary(ipCalculateUtil.getDecimalToStringBinary(blankList.get(i), "CV0001"), "CV0001");
				
				TbIpAssignMstVo tbIpAssignMstVo = new TbIpAssignMstVo();
				tbIpAssignMstVo.setPipPrefix(ipBlock+"/32");
				tbIpAssignMstVo.setNbitmask(32);
				tbIpAssignMstVo.setNfirstAddr(blankList.get(i));
				tbIpAssignMstVo.setNlastAddr(blankList.get(i));
				tbIpAssignMstVo.setSfirstAddr(ipBlock);
				tbIpAssignMstVo.setSipVersionTypeCd("CV0001");
				
				mergeList.add(tbIpAssignMstVo);
			}

			TbIpAssignMstVo resultVo = null;
			List<TbIpAssignMstVo> endResultVos = new ArrayList<TbIpAssignMstVo>();
			
			while(true){
				
				if(mergeList.size()==0){
					break;
				}
				
				List<TbIpAssignMstVo> tempList = new ArrayList<TbIpAssignMstVo>();
				
				if(mergeList.size()<=1){
					endResultVos.add(mergeList.get(0));
					break;
				}
				
				TbIpAssignMstVo tempParam1 = mergeList.get(0);
				TbIpAssignMstVo tempParam2 = mergeList.get(1);
				
				tempList.add(tempParam1);
				tempList.add(tempParam2);
				
				boolean isMergeSuccess = ipCommonService.getMergeIpBlockMstInfo(tempList);

				if (isMergeSuccess) {
					resultVo = tempList.get(0);
					mergeList.add(resultVo);
					mergeList.remove(tempParam1);
					mergeList.remove(tempParam2);
				}else{
					endResultVos.add(tempParam1);
					mergeList.remove(tempParam1);
				}
				tempList.clear();
				
			}
						
			for(int i=0;i<endResultVos.size();i++){
				TbIpAssignMstVo tempVo = endResultVos.get(i);
				Map<String,Object> tempMap = new HashMap<String,Object>();
				tempMap.put("mst_seq", insertVo.getSeq());
				tempMap.put("pipprefix",tempVo.getPipPrefix());
				tempMap.put("ssvcLineTypeCd",parseList.get(0).get("ssvcLineTypeCd"));
				tempMap.put("nlvl_mst_seq",insertVo.getNlvlMstSeq());
				tempMap.put("sipCreateTypeCd",parseList.get(0).get("sipCreateTypeCd"));
				tempMap.put("sIpBlock",tempVo.getSipBlock());
				tempMap.put("nbitmask",tempVo.getNbitmask());
				tempMap.put("sfirst_addr",tempVo.getSfirstAddr());
				tempMap.put("slast_addr",tempVo.getSlastAddr());
				tempMap.put("nfirst_addr",tempVo.getNfirstAddr());
				tempMap.put("nlast_addr",tempVo.getNlastAddr());
				tempMap.put("screate_id",insertVo.getScreateId());
				tempMap.put("smodify_id",insertVo.getScreateId());
				 
				ipUploadMgmtTxService.insertIpUploadSubBlank(tempMap);
			}
								
			
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessageKey(),e.getArgs());
		} catch (Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00000");
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void createServiceVo(MultipartHttpServletRequest request,List<Map<String, Object>> parseList, TbIpUploadVo insertVo) {
		
		try{
			
			List<Map<String,Object>> compareList = ipUploadMgmtTxService.selectCompareList(insertVo); 
						
			//Step1. 해제 기본 값 세팅
			// List<List<Map<String,Object>>> targetDelList = new ArrayList<List<Map<String,Object>>>();
			List<Map<String,Object>> targetDelList = new ArrayList<Map<String,Object>>();
			int tempDelGroup = -1;
			
			//Step1-1. 해제 그룹 정리
			for(int i=0;i<compareList.size();i++){
				Map<String,Object> retMap = compareList.get(i);
				if(retMap.get("cancel_target").toString().equals("Y")){
					// int tGroupSeq = Integer.parseInt(retMap.get("group_seq").toString());
					int tassignSeq = Integer.parseInt(retMap.get("nip_assign_mst_seq").toString());
					
					if(retMap.get("merge_target").equals("Y")){ // 병합
						if(tempDelGroup == -1){ // 맨처음 대상
							tempDelGroup = tassignSeq;
							targetDelList.add(retMap);
						}else{
							if(tempDelGroup != tassignSeq){
								tempDelGroup = tassignSeq;
								targetDelList.add(retMap);
							}
						}
					}else if(retMap.get("div_target").equals("Y")){ // 분할
						if(tempDelGroup == -1){ // 맨처음 대상
							tempDelGroup = tassignSeq;
							targetDelList.add(retMap);
						}else{
							if(tempDelGroup != tassignSeq){
								tempDelGroup = tassignSeq;
								targetDelList.add(retMap);
							}
						}
					}else{
						
						if(tempDelGroup == -1){ // 맨처음 대상
							tempDelGroup = tassignSeq;
							targetDelList.add(retMap);
						}else{
							if(tempDelGroup != tassignSeq){
								tempDelGroup = tassignSeq;
								targetDelList.add(retMap);
							}
						}
						
					}
					
				}
								
			}
			
			//Step1. 해제
			for(int i=0;i<targetDelList.size();i++){ 
				
				Map<String,Object> retMap = targetDelList.get(i);
				PrintLogUtil.printLog("$$$$$$$$$$$$$$$$$$$$$$$$Cancel");
				
				BigInteger nipAllocMstSeq = new BigInteger(retMap.get("nip_alloc_mst_seq").toString());
				BigInteger nipAssignMstSeq = new BigInteger(retMap.get("nip_assign_mst_seq").toString());
				
				IpAllocMstComplexVo ipAllocMstComplexVo = new IpAllocMstComplexVo();			
											 ipAllocMstComplexVo.setSmodifyId(insertVo.getSmodifyId());
										 	 ipAllocMstComplexVo.setScreateId(insertVo.getScreateId());
										 	 ipAllocMstComplexVo.setMenuType("Upload");
										 	 
				List<IpAllocOperMstVo> destIpAllocMstVos = new ArrayList<IpAllocOperMstVo>();
				
				IpAllocOperMstVo destIpAllocMstVo = new IpAllocOperMstVo();
										destIpAllocMstVo.setNipAllocMstSeq(nipAllocMstSeq);
										destIpAllocMstVo.setNipAssignMstSeq(nipAssignMstSeq);
										destIpAllocMstVo.setSmodifyId(insertVo.getSmodifyId());
										destIpAllocMstVo.setScreateId(insertVo.getScreateId());
										
				IpAllocOperMstVo srcIpAllocMstVo = new IpAllocOperMstVo();
										srcIpAllocMstVo.setNipAllocMstSeq(nipAllocMstSeq);
										srcIpAllocMstVo.setNipAssignMstSeq(nipAssignMstSeq);
										srcIpAllocMstVo.setSmodifyId(insertVo.getSmodifyId());
										srcIpAllocMstVo.setScreateId(insertVo.getScreateId());
					
				destIpAllocMstVos.add(destIpAllocMstVo);
				ipAllocMstComplexVo.setDestIpAllocMstVos(destIpAllocMstVos);
				ipAllocMstComplexVo.setSrcIpAllocMstVo(srcIpAllocMstVo);
				
				allocMgmtService.deleteListAllocIPMst(ipAllocMstComplexVo);
					
			}
			
			compareList = ipUploadMgmtTxService.selectCompareList(insertVo);
			
			//Step2. 병합 기본 값 세팅
			List<List<Map<String,Object>>> targetMergeList = new ArrayList<List<Map<String,Object>>>();
			List<Map<String,Object>> tempMergeList = new ArrayList<Map<String,Object>>();
			int tempMergeGroup = -1;
			
			//Step2-1. 병합 그룹 정리
			for(int i=0;i<compareList.size();i++){
				Map<String,Object> retMap = compareList.get(i);
				if(retMap.get("merge_target").toString().equals("Y")){
					int tGroupSeq = Integer.parseInt(retMap.get("group_seq").toString());
					if(tempMergeGroup == -1){ // 맨처음 대상
						tempMergeGroup = tGroupSeq;
						tempMergeList.add(retMap);
					}else if(tempMergeGroup == tGroupSeq){
						tempMergeList.add(retMap);
					}else{
						if(tempMergeList.isEmpty()){
							tempMergeGroup = tGroupSeq;
							tempMergeList.add(retMap);
						}else{
							List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempMergeList);						
							targetMergeList.add(copyList);
							tempMergeList.clear();
							tempMergeGroup = tGroupSeq;
							tempMergeList.add(retMap);
						}
					}
				}else{
					if(!tempMergeList.isEmpty()){
						List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempMergeList);
						targetMergeList.add(copyList);
						tempMergeList.clear();
					}
				}
				
				if(i==compareList.size()-1){ // 마지막일경우 처리
					if(!tempMergeList.isEmpty()){
						List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempMergeList);
						targetMergeList.add(copyList);
						tempMergeList.clear();
					}
				}
				
			}
			
			//Step2-2. 병합 실행
			for(int i=0;i<targetMergeList.size();i++){
				
				List<Map<String,Object>> targetList = targetMergeList.get(i);
				
				
				TbIpAssignMstComplexVo tbIpAssignMstComplexVo = new TbIpAssignMstComplexVo();
				TbIpAssignMstVo srcIpAssignMstVo = new TbIpAssignMstVo();
				List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();
				
				for(int j=0;j<targetList.size();j++){
					
					Map<String,Object> targetMap = targetList.get(j);
					PrintLogUtil.printLog("$$$$$$$$$$$$$$$$$$$$$$$$Merge");
					if(j==0){
						tbIpAssignMstComplexVo.setScreateId(insertVo.getScreateId());
						tbIpAssignMstComplexVo.setSmodifyId(insertVo.getSmodifyId());
						
						srcIpAssignMstVo.setScreateId(insertVo.getScreateId());
						srcIpAssignMstVo.setSmodifyId(insertVo.getSmodifyId());
						srcIpAssignMstVo.setNlvlMstSeq(new BigInteger(targetMap.get("nlvl_mst_seq").toString()));
						srcIpAssignMstVo.setPipPrefix(targetMap.get("up_pip_prefix").toString());
						srcIpAssignMstVo.setSassignLevelCd("IA0004");
						srcIpAssignMstVo.setSassignTypeCd(targetMap.get("sassign_type_cd").toString());
						srcIpAssignMstVo.setSipVersionTypeCd("CV0001");
						srcIpAssignMstVo.setSsvcLineTypeCd(targetMap.get("ssvc_line_type_cd").toString());
						srcIpAssignMstVo.setSsvcGroupCd(targetMap.get("ssvc_group_cd").toString());
						srcIpAssignMstVo.setSsvcObjCd(targetMap.get("ssvc_obj_cd").toString());
					}
					
					TbIpAssignMstVo destIpAssignMstVo = new TbIpAssignMstVo();
											destIpAssignMstVo.setSmodifyId(insertVo.getSmodifyId());
											destIpAssignMstVo.setScreateId(insertVo.getScreateId());
											destIpAssignMstVo.setNipAssignMstSeq(new BigInteger(targetMap.get("nip_assign_mst_seq").toString()));
											
					destIpAssignMstVos.add(destIpAssignMstVo);
				}
				
				// TbIpAssignMstVo
				tbIpAssignMstComplexVo.setSrcIpAssignMstVo(srcIpAssignMstVo);
				tbIpAssignMstComplexVo.setDestIpAssignMstVos(destIpAssignMstVos);
				tbIpAssignMstComplexVo.setMenuType("Upload");
				
				
				assignMgmtService.insertMrgAsgnIPMst(tbIpAssignMstComplexVo);
				
			}
			
			compareList = ipUploadMgmtTxService.selectCompareList(insertVo);
			// Step3 . 분할 기본 값 세팅
			List<List<Map<String,Object>>> targetDivList = new ArrayList<List<Map<String,Object>>>();
			List<Map<String,Object>> tempDivList = new ArrayList<Map<String,Object>>();
			int tempDivGroup = -1;
			
			//Step3-1. 분할 그룹 정리
			for(int i=0;i<compareList.size();i++){
				Map<String,Object> retMap = compareList.get(i);
				if(retMap.get("div_target").toString().equals("Y")){
					int tGroupSeq = Integer.parseInt(retMap.get("group_seq").toString());
					if(tempDivGroup == -1){ // 맨처음 대상
						tempDivGroup = tGroupSeq;
						tempDivList.add(retMap);
					}else if(tempDivGroup == tGroupSeq){
						tempDivList.add(retMap);
					}else{
						if(tempDivList.isEmpty()){
							tempDivGroup = tGroupSeq;
							tempDivList.add(retMap);
						}else{
							List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempDivList);						
							targetDivList.add(copyList);
							tempDivList.clear();
							tempDivGroup = tGroupSeq;
							tempDivList.add(retMap);
						}
						
					}
				}else{
					if(!tempDivList.isEmpty()){
						List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempDivList);
						targetDivList.add(copyList);
						tempDivList.clear();
					}
				}
				
				if(i==compareList.size()-1){ // 마지막일경우 처리
					if(!tempDivList.isEmpty()){
						List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempDivList);
						targetDivList.add(copyList);
						tempDivList.clear();
					}
				}
			}
			
			// Step3-2. 분할 실행
			for(int i=0;i<targetDivList.size();i++){
				
				List<Map<String,Object>> targetList = targetDivList.get(i);
				TbIpAssignMstComplexVo tbIpAssignMstComplexVo = new TbIpAssignMstComplexVo();
				TbIpAssignMstVo srcIpAssignMstVo = new TbIpAssignMstVo();
				List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();
				
				for(int j=0;j<targetList.size();j++){
					Map<String,Object> targetMap = targetList.get(j);
					
					if(j==0){
						
						tbIpAssignMstComplexVo.setSmodifyId(insertVo.getSmodifyId());
						tbIpAssignMstComplexVo.setScreateId(insertVo.getScreateId());
						
						srcIpAssignMstVo.setSmodifyId(insertVo.getSmodifyId());
						srcIpAssignMstVo.setScreateId(insertVo.getScreateId());
						srcIpAssignMstVo.setNipAssignMstSeq(new BigInteger(targetMap.get("nip_assign_mst_seq").toString()));
						srcIpAssignMstVo.setNlvlMstSeq(new BigInteger(targetMap.get("nlvl_mst_seq").toString()));
						srcIpAssignMstVo.setSassignLevelCd(targetMap.get("sassign_level_cd").toString());
						srcIpAssignMstVo.setSipVersionTypeCd("CV0001");
					}
					
					TbIpAssignMstVo destIpAssignMstVo = new TbIpAssignMstVo();
											destIpAssignMstVo.setSmodifyId(insertVo.getSmodifyId());
											destIpAssignMstVo.setScreateId(insertVo.getScreateId());
											destIpAssignMstVo.setPipPrefix(targetMap.get("up_pip_prefix").toString());
											destIpAssignMstVo.setSipVersionTypeCd("CV0001");
											
						destIpAssignMstVos.add(destIpAssignMstVo);
					
				}
				
				tbIpAssignMstComplexVo.setSrcIpAssignMstVo(srcIpAssignMstVo);
				tbIpAssignMstComplexVo.setDestIpAssignMstVos(destIpAssignMstVos);				
				tbIpAssignMstComplexVo.setMenuType("Upload");
				
				assignMgmtService.insertListDivAsgnIPMst(tbIpAssignMstComplexVo);
				
			}
			
			
			List<String> allocTargetList = ipUploadMgmtTxService.selectAllocTargetList(insertVo);
			//Step4. 서비스 변경
			for(int i=0;i<parseList.size();i++){
				Map<String,Object> retMap = parseList.get(i);

				if(allocTargetList.contains(retMap.get("pipprefix"))){
					
					List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();
					
					BigInteger nAssignMstSeq = ipUploadMgmtTxService.selectAssignMstSeq(retMap);
						
					TbIpAssignMstVo tbIpAssignMstVo = new TbIpAssignMstVo();
					tbIpAssignMstVo.setNipAssignMstSeq(nAssignMstSeq);
					tbIpAssignMstVo.setSassignLevelCd("IA0004");		// 서비스배정(미할당)
					tbIpAssignMstVo.setPipPrefix(retMap.get("pipprefix").toString());
					tbIpAssignMstVo.setSipVersionTypeCd("CV0001");
					tbIpAssignMstVo.setScreateId(insertVo.getScreateId());
					tbIpAssignMstVo.setSmodifyId(insertVo.getSmodifyId());
					tbIpAssignMstVo.setSassignTypeCd(retMap.get("sassignTypeCd").toString());
					tbIpAssignMstVo.setTypeFlag("svcassign"); 		// 배정-서비스배정
					
//					tbIpAssignMstVo.setsGubun("ROUTMGMT");		// 일반 배정과 비교하기 위함
					tbIpAssignMstVo.setMenuType("Upload");
					tbIpAssignMstVo.setScreateId(insertVo.getScreateId());
					
					destIpAssignMstVos.add(tbIpAssignMstVo);
						
					assignMgmtTxService.processUpdateAsgnIPMst(destIpAssignMstVos);
					
				}
					
			}
				
	
			// Step5. 할당
			for(int i=0;i<parseList.size();i++){
				
				Map<String,Object> retMap = parseList.get(i);

				if(allocTargetList.contains(retMap.get("pipprefix"))){

					BigInteger nLvlMstSeq = ipUploadMgmtTxService.selectNlvlMstSeq(retMap);
					BigInteger nAssignMstSeq = ipUploadMgmtTxService.selectAssignMstSeq(retMap);
					
					IpAllocMstComplexVo ipAllocMstComplexVo = new IpAllocMstComplexVo();			
					List<IpAllocOperMstVo> destIpAllocMstVos = new ArrayList<IpAllocOperMstVo>();
					
					IpAllocOperMstVo destIpAllocMstVo = new IpAllocOperMstVo();
											destIpAllocMstVo.setSsvcLineTypeCd(retMap.get("ssvcLineTypeCd").toString());
											destIpAllocMstVo.setSipCreateTypeCd(retMap.get("sipCreateTypeCd").toString());
											destIpAllocMstVo.setSassignTypeCd(retMap.get("sassignTypeCd").toString());
											destIpAllocMstVo.setNipmsSvcSeq(new BigInteger("0")); // 하드코딩
											destIpAllocMstVo.setNipAllocMstCnt(new BigInteger("0")); // 검증필요
											destIpAllocMstVo.setSgatewayip(retMap.get("sgatewayip").toString());
											destIpAllocMstVo.setSllnum(retMap.get("sllnum").toString()); 
											destIpAllocMstVo.setNlvlMstSeq(nLvlMstSeq); // 조회?
											destIpAllocMstVo.setNipAssignMstSeq(nAssignMstSeq);
											destIpAllocMstVo.setScreateId(insertVo.getScreateId());
											destIpAllocMstVo.setSmodifyId(insertVo.getSmodifyId());
											if(retMap.get("sconnalias").toString() != null){
												destIpAllocMstVo.setSconnalias(retMap.get("sconnalias").toString());
											}
											if(retMap.get("ssubscnnescode").toString() != null){
												destIpAllocMstVo.setSsubscnescode(retMap.get("ssubscnnescode").toString());
											}
											if(retMap.get("ssubscrouterserialip").toString() != null){
												destIpAllocMstVo.setSsubscrouterserialip(retMap.get("ssubscrouterserialip").toString());
											}
											if(retMap.get("ssubsclgipportip").toString() != null){
												destIpAllocMstVo.setSsubsclgipportip(retMap.get("ssubsclgipportip").toString());
											}
											
												
					IpAllocOperMstVo srcIpAllocMstVo = new IpAllocOperMstVo();
											srcIpAllocMstVo.setSicisofficescode(retMap.get("sicisofficescode").toString());
											srcIpAllocMstVo.setSofficecode(retMap.get("sicisofficescode").toString());
											srcIpAllocMstVo.setSsubsclgipportdescription(retMap.get("ssubsclgipportdescription").toString());
											srcIpAllocMstVo.setSsubscnealias(retMap.get("ssubscnealias").toString());
											srcIpAllocMstVo.setSsubscmstip(retMap.get("ssubscmstip").toString());
											srcIpAllocMstVo.setSmodelname(retMap.get("smodelname").toString());
											srcIpAllocMstVo.setSllnum(retMap.get("sllnum").toString()); 
											srcIpAllocMstVo.setScreateId(insertVo.getScreateId());
											srcIpAllocMstVo.setSmodifyId(insertVo.getSmodifyId());
											srcIpAllocMstVo.setScomment(retMap.get("scomment").toString());
										
					destIpAllocMstVos.add(destIpAllocMstVo);
					
					ipAllocMstComplexVo.setDestIpAllocMstVos(destIpAllocMstVos);
					ipAllocMstComplexVo.setSrcIpAllocMstVo(srcIpAllocMstVo);
					
					ipAllocMstComplexVo.setScreateId(insertVo.getScreateId());
					ipAllocMstComplexVo.setSmodifyId(insertVo.getSmodifyId());
					ipAllocMstComplexVo.setMenuType("Upload");
					
					allocMgmtService.insertListAllocIPMst(ipAllocMstComplexVo);
					ipUploadMgmtTxService.updateIpUploadSub(retMap);
				}
				
			}
			
			
			/**
			 * "pipprefix",
			 * "sgatewayip",
			 * "ssubscnnescode",
			 * "ssubsclgipportseq",
			 * "ssubsclgipportip",
			 * "ssubscrouterserialip",
			 * "sconnalias"
			 */
			
			/**
			 * ssvcLineTypeCd",
			 * "sipCreateTypeCd",
			 * "sassignTypeCd",
			 * "pipprefix",
			 * "sicisofficescode",
			 * "ssubscmstip",
			 * "smodelname",
			 * "sgatewayip",
			 * "ssubscnnescode",
			 * "ssubscnealias",
			 * "ssubsclgipportseq",
			 * "ssubsclgipportdescription",
			 * "ssubsclgipportip",
			 * "ssubscrouterserialip",
			 * "sconnalias"
			 */
			
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessageKey());
		} catch (Exception e){
			e.printStackTrace();
		}
		
		// return parseList;
		
	}

	// 성공여부 업데이트
	public void updateIpUploadMst(MultipartHttpServletRequest request, TbIpUploadVo insertVo) {
		ipUploadMgmtTxService.updateIpUploadMst(insertVo);
	}

	// 상세 페이지
	public TbIpUploadSubListVo selectUploadDetail(TbIpUploadVo searchVo) {
		TbIpUploadSubListVo retVo = new TbIpUploadSubListVo();
		List<TbIpUploadSubVo> retList = ipUploadMgmtTxService.selectUploadDetail(searchVo);
		retVo.setTotalCount(retList.size());
		retVo.setTbIpUploadSubVos(retList);
		return retVo;
	}

	/**
	 * MIG 병합 IP 조회
	 * @param searchVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> selectListMigIpBlock1(TbIpUploadVo searchVo) {
		List<Map<String, Object>> resultList = null;
		
		try{
			
			 resultList = ipUploadMgmtTxService.selectListMigIpBlock1(searchVo);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois목록"});
		}
		
		return resultList;
	}
	
	/**
	 * MIG 할당 IP 조회
	 * @param searchVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> selectListMigIpBlock2(TbIpUploadVo searchVo) {
		List<Map<String, Object>> resultList = null;
		
		try{
			
			 resultList = ipUploadMgmtTxService.selectListMigIpBlock2(searchVo);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois목록"});
		}
		
		return resultList;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void onlyMergeCancelCreateServiceVo(MultipartHttpServletRequest request,List<Map<String, Object>> parseList, TbIpUploadVo insertVo) {
		
		try{
			
			List<Map<String,Object>> compareList = ipUploadMgmtTxService.selectCompareList(insertVo); 
						
			//Step1. 해제 기본 값 세팅
			// List<List<Map<String,Object>>> targetDelList = new ArrayList<List<Map<String,Object>>>();
			List<Map<String,Object>> targetDelList = new ArrayList<Map<String,Object>>();
			int tempDelGroup = -1;
			
			//Step1-1. 해제 그룹 정리
			for(int i=0;i<compareList.size();i++){
				System.out.println("%%%%%%%%%%%%%"+i);
				Map<String,Object> retMap = compareList.get(i);
				if(retMap.get("cancel_target").toString().equals("Y")){
					// int tGroupSeq = Integer.parseInt(retMap.get("group_seq").toString());
					int tassignSeq = Integer.parseInt(retMap.get("nip_assign_mst_seq").toString());
					
					if(retMap.get("merge_target").equals("Y")){ // 병합
						if(tempDelGroup == -1){ // 맨처음 대상
							tempDelGroup = tassignSeq;
							targetDelList.add(retMap);
						}else{
							if(tempDelGroup != tassignSeq){
								tempDelGroup = tassignSeq;
								targetDelList.add(retMap);
							}
						}
					}else if(retMap.get("div_target").equals("Y")){ // 분할
						if(tempDelGroup == -1){ // 맨처음 대상
							tempDelGroup = tassignSeq;
							targetDelList.add(retMap);
						}else{
							if(tempDelGroup != tassignSeq){
								tempDelGroup = tassignSeq;
								targetDelList.add(retMap);
							}
						}
					}else{
						
						if(tempDelGroup == -1){ // 맨처음 대상
							tempDelGroup = tassignSeq;
							targetDelList.add(retMap);
						}else{
							if(tempDelGroup != tassignSeq){
								tempDelGroup = tassignSeq;
								targetDelList.add(retMap);
							}
						}
						
					}
					
				}
								
			}
			
			//Step1. 해제
			for(int i=0;i<targetDelList.size();i++){ 
				
				Map<String,Object> retMap = targetDelList.get(i);
				PrintLogUtil.printLog("$$$$$$$$$$$$$$$$$$$$$$$$Cancel");
				
				BigInteger nipAllocMstSeq = new BigInteger(retMap.get("nip_alloc_mst_seq").toString());
				BigInteger nipAssignMstSeq = new BigInteger(retMap.get("nip_assign_mst_seq").toString());
				
				IpAllocMstComplexVo ipAllocMstComplexVo = new IpAllocMstComplexVo();			
											 ipAllocMstComplexVo.setSmodifyId(insertVo.getSmodifyId());
										 	 ipAllocMstComplexVo.setScreateId(insertVo.getScreateId());
										 	 ipAllocMstComplexVo.setMenuType("Upload");
										 	 
				List<IpAllocOperMstVo> destIpAllocMstVos = new ArrayList<IpAllocOperMstVo>();
				
				IpAllocOperMstVo destIpAllocMstVo = new IpAllocOperMstVo();
										destIpAllocMstVo.setNipAllocMstSeq(nipAllocMstSeq);
										destIpAllocMstVo.setNipAssignMstSeq(nipAssignMstSeq);
										destIpAllocMstVo.setSmodifyId(insertVo.getSmodifyId());
										destIpAllocMstVo.setScreateId(insertVo.getScreateId());
										
				IpAllocOperMstVo srcIpAllocMstVo = new IpAllocOperMstVo();
										srcIpAllocMstVo.setNipAllocMstSeq(nipAllocMstSeq);
										srcIpAllocMstVo.setNipAssignMstSeq(nipAssignMstSeq);
										srcIpAllocMstVo.setSmodifyId(insertVo.getSmodifyId());
										srcIpAllocMstVo.setScreateId(insertVo.getScreateId());
					
				destIpAllocMstVos.add(destIpAllocMstVo);
				ipAllocMstComplexVo.setDestIpAllocMstVos(destIpAllocMstVos);
				ipAllocMstComplexVo.setSrcIpAllocMstVo(srcIpAllocMstVo);
				
				allocMgmtService.deleteListAllocIPMst(ipAllocMstComplexVo);
					
			}
			
			compareList = ipUploadMgmtTxService.selectCompareList(insertVo);
			
			//Step2. 병합 기본 값 세팅
			List<List<Map<String,Object>>> targetMergeList = new ArrayList<List<Map<String,Object>>>();
			List<Map<String,Object>> tempMergeList = new ArrayList<Map<String,Object>>();
			int tempMergeGroup = -1;
			
			//Step2-1. 병합 그룹 정리
			for(int i=0;i<compareList.size();i++){
				Map<String,Object> retMap = compareList.get(i);
				if(retMap.get("merge_target").toString().equals("Y")){
					int tGroupSeq = Integer.parseInt(retMap.get("group_seq").toString());
					if(tempMergeGroup == -1){ // 맨처음 대상
						tempMergeGroup = tGroupSeq;
						tempMergeList.add(retMap);
					}else if(tempMergeGroup == tGroupSeq){
						tempMergeList.add(retMap);
					}else{
						if(tempMergeList.isEmpty()){
							tempMergeGroup = tGroupSeq;
							tempMergeList.add(retMap);
						}else{
							List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempMergeList);						
							targetMergeList.add(copyList);
							tempMergeList.clear();
							tempMergeGroup = tGroupSeq;
							tempMergeList.add(retMap);
						}
					}
				}else{
					if(!tempMergeList.isEmpty()){
						List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempMergeList);
						targetMergeList.add(copyList);
						tempMergeList.clear();
					}
				}
				
				if(i==compareList.size()-1){ // 마지막일경우 처리
					if(!tempMergeList.isEmpty()){
						List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempMergeList);
						targetMergeList.add(copyList);
						tempMergeList.clear();
					}
				}
				
			}
			
			//Step2-2. 병합 실행
			for(int i=0;i<targetMergeList.size();i++){
				
				List<Map<String,Object>> targetList = targetMergeList.get(i);
				
				
				TbIpAssignMstComplexVo tbIpAssignMstComplexVo = new TbIpAssignMstComplexVo();
				TbIpAssignMstVo srcIpAssignMstVo = new TbIpAssignMstVo();
				List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();
				
				for(int j=0;j<targetList.size();j++){
					
					Map<String,Object> targetMap = targetList.get(j);
					PrintLogUtil.printLog("$$$$$$$$$$$$$$$$$$$$$$$$Merge");
					if(j==0){
						tbIpAssignMstComplexVo.setScreateId(insertVo.getScreateId());
						tbIpAssignMstComplexVo.setSmodifyId(insertVo.getSmodifyId());
						
						srcIpAssignMstVo.setScreateId(insertVo.getScreateId());
						srcIpAssignMstVo.setSmodifyId(insertVo.getSmodifyId());
						srcIpAssignMstVo.setNlvlMstSeq(new BigInteger(targetMap.get("nlvl_mst_seq").toString()));
						srcIpAssignMstVo.setPipPrefix(targetMap.get("up_pip_prefix").toString());
						srcIpAssignMstVo.setSassignLevelCd("IA0004");
						srcIpAssignMstVo.setSassignTypeCd(targetMap.get("sassign_type_cd").toString());
						srcIpAssignMstVo.setSipVersionTypeCd("CV0001");
						srcIpAssignMstVo.setSsvcLineTypeCd(targetMap.get("ssvc_line_type_cd").toString());
						srcIpAssignMstVo.setSsvcGroupCd(targetMap.get("ssvc_group_cd").toString());
						srcIpAssignMstVo.setSsvcObjCd(targetMap.get("ssvc_obj_cd").toString());
					}
					
					TbIpAssignMstVo destIpAssignMstVo = new TbIpAssignMstVo();
											destIpAssignMstVo.setSmodifyId(insertVo.getSmodifyId());
											destIpAssignMstVo.setScreateId(insertVo.getScreateId());
											destIpAssignMstVo.setNipAssignMstSeq(new BigInteger(targetMap.get("nip_assign_mst_seq").toString()));
											
					destIpAssignMstVos.add(destIpAssignMstVo);
				}
				
				// TbIpAssignMstVo
				tbIpAssignMstComplexVo.setSrcIpAssignMstVo(srcIpAssignMstVo);
				tbIpAssignMstComplexVo.setDestIpAssignMstVos(destIpAssignMstVos);
				tbIpAssignMstComplexVo.setMenuType("Upload");
				
				
				assignMgmtService.insertMrgAsgnIPMst(tbIpAssignMstComplexVo);
				
			}
			
			compareList = ipUploadMgmtTxService.selectCompareList(insertVo);
			// Step3 . 분할 기본 값 세팅
			List<List<Map<String,Object>>> targetDivList = new ArrayList<List<Map<String,Object>>>();
			List<Map<String,Object>> tempDivList = new ArrayList<Map<String,Object>>();
			int tempDivGroup = -1;
			
			//Step3-1. 분할 그룹 정리
			for(int i=0;i<compareList.size();i++){
				Map<String,Object> retMap = compareList.get(i);
				if(retMap.get("div_target").toString().equals("Y")){
					int tGroupSeq = Integer.parseInt(retMap.get("group_seq").toString());
					if(tempDivGroup == -1){ // 맨처음 대상
						tempDivGroup = tGroupSeq;
						tempDivList.add(retMap);
					}else if(tempDivGroup == tGroupSeq){
						tempDivList.add(retMap);
					}else{
						if(tempDivList.isEmpty()){
							tempDivGroup = tGroupSeq;
							tempDivList.add(retMap);
						}else{
							List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempDivList);						
							targetDivList.add(copyList);
							tempDivList.clear();
							tempDivGroup = tGroupSeq;
							tempDivList.add(retMap);
						}
						
					}
				}else{
					if(!tempDivList.isEmpty()){
						List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempDivList);
						targetDivList.add(copyList);
						tempDivList.clear();
					}
				}
				
				if(i==compareList.size()-1){ // 마지막일경우 처리
					if(!tempDivList.isEmpty()){
						List<Map<String,Object>> copyList = CloneUtil.createCloneViaListMap(tempDivList);
						targetDivList.add(copyList);
						tempDivList.clear();
					}
				}
			}
			
			// Step3-2. 분할 실행
			for(int i=0;i<targetDivList.size();i++){
				
				List<Map<String,Object>> targetList = targetDivList.get(i);
				TbIpAssignMstComplexVo tbIpAssignMstComplexVo = new TbIpAssignMstComplexVo();
				TbIpAssignMstVo srcIpAssignMstVo = new TbIpAssignMstVo();
				List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();
				
				for(int j=0;j<targetList.size();j++){
					Map<String,Object> targetMap = targetList.get(j);
					
					if(j==0){
						
						tbIpAssignMstComplexVo.setSmodifyId(insertVo.getSmodifyId());
						tbIpAssignMstComplexVo.setScreateId(insertVo.getScreateId());
						
						srcIpAssignMstVo.setSmodifyId(insertVo.getSmodifyId());
						srcIpAssignMstVo.setScreateId(insertVo.getScreateId());
						srcIpAssignMstVo.setNipAssignMstSeq(new BigInteger(targetMap.get("nip_assign_mst_seq").toString()));
						srcIpAssignMstVo.setNlvlMstSeq(new BigInteger(targetMap.get("nlvl_mst_seq").toString()));
						srcIpAssignMstVo.setSassignLevelCd(targetMap.get("sassign_level_cd").toString());
						srcIpAssignMstVo.setSipVersionTypeCd("CV0001");
					}
					
					TbIpAssignMstVo destIpAssignMstVo = new TbIpAssignMstVo();
											destIpAssignMstVo.setSmodifyId(insertVo.getSmodifyId());
											destIpAssignMstVo.setScreateId(insertVo.getScreateId());
											destIpAssignMstVo.setPipPrefix(targetMap.get("up_pip_prefix").toString());
											destIpAssignMstVo.setSipVersionTypeCd("CV0001");
											
						destIpAssignMstVos.add(destIpAssignMstVo);
					
				}
				
				tbIpAssignMstComplexVo.setSrcIpAssignMstVo(srcIpAssignMstVo);
				tbIpAssignMstComplexVo.setDestIpAssignMstVos(destIpAssignMstVos);				
				tbIpAssignMstComplexVo.setMenuType("Upload");
				
				assignMgmtService.insertListDivAsgnIPMst(tbIpAssignMstComplexVo);
				
			}
			
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessageKey());
		} catch (Exception e){
			e.printStackTrace();
		}
		
		// return parseList;
		
	}
	
	public List<Map<String,Object>> parseUploadTxtFile(MultipartHttpServletRequest request, TbIpUploadVo insertVo, File convFile) throws IOException {
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		BufferedReader bufReader = null;
		try{
			List<String> setColumns = new ArrayList<String>(Arrays.asList("ssvcLineTypeCd","sipCreateTypeCd","sassignTypeCd","pipprefix","pipprefixRange","sgatewayip","sicisofficescode","ssubscmstip","smodelname","ssubscnnescode","ssubscnealias","sllnum","ssubsclgipportdescription","ssubsclgipportip","ssubscrouterserialip","sconnalias","scomment"));
            //입력 버퍼 생성
            bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(convFile),"utf-8"));
            String line = "";
            int line_cnt = 0;
            while((line = bufReader.readLine()) != null){
            	line_cnt ++;
            	if(line_cnt >= 10){
            		String[] line_data = line.toString().split("[|]",-1);
            		if(line_data.length < 17){
            			PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>>>>>>>> APP.HIGH.00056 텍스트 양식");
						throw new ServiceException("APP.HIGH.00056", new String[]{"텍스트 양식의 형식","맞지"}); // 게이트웨이IP가 맞지 않습니다.
            		}
            		Map<String,Object> map = new LinkedHashMap<String, Object>();
            		for(int i=0 ; i<line_data.length ; i++){
                        map.put(setColumns.get(i), line_data[i]);
                    }
            		
            		resultList.add(map);
            	} 
            }   
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00000");
		}finally{
			bufReader.close();
		}
		return resultList;
	}
}
