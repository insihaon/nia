package com.kt.ipms.legacy.cmn.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.codej.base.property.FileStorageProperties;
import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.DateUtils;
import com.kt.ipms.legacy.cmn.service.ExcelCheckService;

@Component
public class ExcelUtil {
	
	@Lazy @Autowired
	private FileSystemResource fileSystemResource;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private ExcelCheckService excelCheckService;

	@Autowired
    private FileStorageProperties fileStorageProperties;
	
	private final DecimalFormat bigDecimalFormat = new DecimalFormat("#,##0.00########");
	
	private final DecimalFormat bigIntegerFormat = new DecimalFormat("#,##0");
	
	public String createExcelFile(List<?> voList, List<String> mappingList, HttpServletRequest request) {
		Date now = new Date();
		BufferedOutputStream bos = null;
		StringBuilder fileName = new StringBuilder();
		PrintLogUtil.printLog("parse excel start now = "+now.toString());
		try {
			if (mappingList == null || mappingList.size() == 0) {
				excelCheckService.updateExcelDown("N");
				throw new ServiceException("CMN.HIGH.00050");
			} else if (voList != null && voList.size() > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				excelCheckService.updateExcelDown("N");
				throw new ServiceException("CMN.INFO.00051");
			}
			//워크북, Sheet 준비
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(CommonCodeUtil.EXCEL_SHEET_NAME);
			List<String> headerList = new ArrayList<String>();
			List<String> keyList = new ArrayList<String>();
			for (int i=0; i < mappingList.size(); i++) {
				StringTokenizer st = new StringTokenizer(mappingList.get(i), "|");
				headerList.add(st.nextToken());
				keyList.add(st.nextToken());
			}
			/** SET EXCEL HEADER COLUMNS **/ 
			setHeaderColumns(sheet, getHeaderCellStyle(workbook), headerList);
			
			/** SET EXCEL BODY COLUMNS **/
			setBodyColumns(sheet, getBodyCellStyle(workbook), voList, keyList);
			
			/** EXCEL FILE CREATE **/
			
			fileName.append(DateUtils.getCurrentDateTime());
			fileName.append("_EXCEL_FILE_");
			fileName.append(jwtUtil.getUserId(request));
			fileName.append(".xlsx");

			// File excelFile = fileUtil.getFile(fileSystemResource.getPath() + File.separator + fileName.toString());
			File excelFile = fileUtil.getFile(fileStorageProperties.getExcelUploadDir() + File.separator + fileName.toString());
			
			if((fileName.indexOf(".."))!= -1 || (fileName.indexOf("./"))!=-1 || (fileName.indexOf(".\\"))!=-1 || (fileName.indexOf(":"))!=-1 || fileName.indexOf("xlsx") == -1 ){
				excelCheckService.updateExcelDown("N");
				throw new ServiceException("CMN.HIGH.00050");
			}
		  
			bos = new BufferedOutputStream(new FileOutputStream(excelFile));
			
			workbook.write(bos);
			bos.flush();
		} catch (ServiceException e) {
			excelCheckService.updateExcelDown("N");
			throw e;
		} catch (Exception e) {
			excelCheckService.updateExcelDown("N");
			throw new ServiceException("CMN.HIGH.00050");
		} finally {
			excelCheckService.updateExcelDown("N");
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		excelCheckService.updateExcelDown("N");
		now = new Date();
		PrintLogUtil.printLog("parse excel end now = "+now.toString());
		return fileName.toString();
	}
	
	private void setHeaderColumns(XSSFSheet sheet, XSSFCellStyle cellStyle, List<String> headerList) {
		XSSFRow row = sheet.createRow(0);
		for (int colIdx=0; colIdx < headerList.size(); colIdx++) {
			XSSFCell cell = row.createCell(colIdx);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(headerList.get(colIdx));
		}
		for (int colIdx=0; colIdx < headerList.size(); colIdx++) {
			sheet.autoSizeColumn(colIdx);
			sheet.setColumnWidth(colIdx, (sheet.getColumnWidth(colIdx))+512 );
		}
	}
	
	private void setHeaderColumns(XSSFSheet sheet, XSSFCellStyle cellStyle, List<String> headerList, List<String> impoList) {
		XSSFRow row = sheet.createRow(0);
		for (int colIdx=0; colIdx < headerList.size(); colIdx++) {
			XSSFCell cell = row.createCell(colIdx);
			XSSFCellStyle tempCellStyle = (XSSFCellStyle) cellStyle.clone();
			
			String impoValue = impoList.get(colIdx);
			if(impoValue.equals("1")){
				tempCellStyle.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
			}else if(impoValue.equals("2")){
				tempCellStyle.setFillForegroundColor(new HSSFColor.PALE_BLUE().getIndex());
			}else if(impoValue.equals("3")){
				tempCellStyle.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//셀에 색깔 채우기   
			}
			
			cell.setCellStyle(tempCellStyle);
			cell.setCellValue(headerList.get(colIdx));
		}
		for (int colIdx=0; colIdx < headerList.size(); colIdx++) {
			sheet.autoSizeColumn(colIdx);
			sheet.setColumnWidth(colIdx, (sheet.getColumnWidth(colIdx))+512 );
		}
	}
	
	private void setBodyColumns(XSSFSheet sheet, XSSFCellStyle cellStyle, List<?> voList, List<String> keyList) {
		if (voList == null || voList.size() == 0) {
			XSSFRow row = sheet.createRow(1);
			XSSFCell cell = row.createCell(0);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("조회 결과 목록이 존재하지 않습니다.");
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, keyList.size()-1));
		} else {
			for (int rowIdx=0; rowIdx < voList.size(); rowIdx++) {
				XSSFRow row = sheet.createRow(rowIdx+1);
				for (int colIdx=0; colIdx < keyList.size(); colIdx++) {
					XSSFCell cell = row.createCell(colIdx);
					cell.setCellStyle(cellStyle);
					String methodName = keyList.get(colIdx);
					cell.setCellValue(getStringVoMemberValue(voList.get(rowIdx), methodName));
				}
				if (rowIdx == (voList.size()-1)) {
					for (int colIdx=0; colIdx < keyList.size(); colIdx++) {
						sheet.autoSizeColumn(colIdx);
						sheet.setColumnWidth(colIdx, (sheet.getColumnWidth(colIdx))+512);
					}
				}
			}	
		}
		
	}
	
	private XSSFCellStyle getHeaderCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);                     //스타일인스턴스의 속성 셑팅
		cellStyle.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//셀에 색깔 채우기   
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		XSSFFont font = workbook.createFont();
		font.setBoldweight( (short)600);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	private XSSFCellStyle getBodyCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);  //스타일인스턴스의 속성 셑팅           
		cellStyle.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//셀에 색깔 채우기   
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		XSSFFont font = workbook.createFont(); //폰트 조정 인스턴스 생성   
		font.setBoldweight( (short)100);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	private String getStringVoMemberValue(Object voObj, String methodName) {
		String result = null;
		try {
			Method method = voObj.getClass().getMethod(methodName);
			Object value = method.invoke(voObj);
			if (value instanceof Date) {
				result = DateUtils.parseDate((Date)value, "yyyy-MM-dd HH:mm:ss");
			} else if (value instanceof BigInteger){
				result = bigIntegerFormat.format((BigInteger) value);
			} else if (value instanceof BigDecimal){
				bigDecimalFormat.setParseBigDecimal(true);
				result = bigDecimalFormat.format((BigDecimal) value);
			} else {
				result = value.toString();	
			}
			
		} catch (Exception e) {
			result = "";
		}
		return result;
	}
	
	/**
	 * Excel Parse
	 * @param filePath
	 * @param fileName
	 * @param getColumns
	 * @return
	 * @throws IOException 
	 */
	public List<Map<String,Object>> parseExcel(String filePath, String fileName, List<String> getColumns) throws IOException { //codeeyes-Critical-File 자원 해제 검사

		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		String line = null;
		FileInputStream file = null;
		try {
			
			String path = filePath+"/" + fileName;
			
			file = new FileInputStream(path);
						
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			
			String value = "";
			String cellName = "";
			
			int rowIndex = 0;
			int columnIndex = 0;

			XSSFSheet sheet = workbook.getSheetAt(0);		// 시트 수
			
			int rows = sheet.getPhysicalNumberOfRows();		// 행의 수
			
			for(rowIndex=0; rowIndex<=rows; rowIndex++) {
				Map<String,Object> map = new LinkedHashMap<String, Object>();
				
				XSSFRow row = sheet.getRow(rowIndex);
				
				if(row != null && rowIndex > 0) {
				
					int cells = row.getPhysicalNumberOfCells();	//   셀의 수
					
					for(columnIndex=0; columnIndex<=cells; columnIndex++) {
						
						XSSFCell cell = row.getCell(columnIndex);
		
						cellName = CellReference.convertNumToColString(columnIndex);
						
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
									value=cell.getBooleanCellValue()+"";
									break;
								case Cell.CELL_TYPE_ERROR:
									value=cell.getErrorCellValue()+"";
									break;
								default:
									break;
							}
							
							map.put(cellName, value);
							
						}
					}
					resultList.add(map);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			file.close();
		}
		
		return resultList;
	}
	
	
	/**
	 * 시설정보  parse
	 * @param filePath
	 * @param fileName
	 * @param cols
	 * @return
	 * @throws IOException 
	 */
	public List<Map<String,Object>> parseNmsHostIp(String filePath, String fileName, String[] cols) throws IOException {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		String line = null;
		BufferedReader in = null;
		try {
			
			String path = filePath+"/" + fileName;
			
//			BufferedReader in = new BufferedReader(new FileReader(path));
			
			in = new BufferedReader(new InputStreamReader(new FileInputStream(path),"EUC-KR"));
			
			int cnt = 0;
			while ( (line = in.readLine()) != null) {
				
				//cnt +=1;
				//if(cnt > 500) break;
				String[] strArr = line.split(",");
				LinkedHashMap<String,Object> map = new LinkedHashMap<String, Object>();
				
				map.put(cols[0], strArr[0]);
				map.put(cols[1], strArr[1]);
				map.put(cols[2], strArr[4]);
				map.put(cols[3], strArr[5]);
				
				resultList.add(map);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			in.close();
		}
		
		return resultList;
		
	}
	
	public String createIpUploadExcelFile(List<List<?>> voList, List<List<String>> mappingList, List<String> sheetName, HttpServletRequest request) {
		BufferedOutputStream bos = null;
		StringBuilder fileName = new StringBuilder();
		try {
			if (mappingList == null || mappingList.size() == 0) {
				throw new ServiceException("CMN.HIGH.00050");
			} else if (voList != null && voList.size() > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			
			//워크북, Sheet 준비
			XSSFWorkbook workbook = new XSSFWorkbook();
			
			for (int j=0;j<mappingList.size();j++){
				XSSFSheet sheet = workbook.createSheet(sheetName.get(j));
				
				List<String> headerList = new ArrayList<String>();
				List<String> keyList = new ArrayList<String>();
				List<String> impoList = new ArrayList<String>();
			
				for (int i=0; i < mappingList.get(j).size(); i++) {
					StringTokenizer st = new StringTokenizer(mappingList.get(j).get(i), "|");
					headerList.add(st.nextToken());
					keyList.add(st.nextToken());
					
					if(st.hasMoreElements()){
						impoList.add(st.nextToken());
					}else{
						impoList.add("0");
					}
				}
				
				/** SET EXCEL HEADER COLUMNS **/ 
				setHeaderColumns(sheet, getHeaderCellStyle(workbook), headerList, impoList);
				
				/** SET EXCEL BODY COLUMNS **/
				setBodyColumns(sheet, getBodyCellStyle(workbook), voList.get(j), keyList);
				
			}
			
			/** EXCEL FILE CREATE **/
			fileName.append(DateUtils.getCurrentDateTime());
			fileName.append("_EXCEL_FILE_");
			fileName.append(jwtUtil.getUserId(request));
			fileName.append(".xlsx");
			
			// File excelFile = fileUtil.getFile(fileSystemResource.getPath() + File.separator + fileName.toString());
			File excelFile = fileUtil.getFile(fileStorageProperties.getExcelUploadDir() + File.separator + fileName.toString());
			bos = new BufferedOutputStream(new FileOutputStream(excelFile));
			
			workbook.write(bos);
			bos.flush();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00050");
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileName.toString();
	}
	
	public String createIpUploadTextFile(HttpServletRequest request) {
		StringBuilder fileName = new StringBuilder();
		BufferedWriter br = null;
		String retStr = "------------------------------\n************주의사항************\n1. 양식 절대 수정 금지\n2. 양식 바로 다음줄 부터 작성 (10번째줄부터 바로 작성)\n3. 예시처럼 작성\n4. 참고 데이터를 확인하려면 엑셀양식 다운도르 후 (참고) 시트 확인\n예시) CL0008|CT0005|SA1049|10.119.206.152/30|10.119.200.0/21|10.119.206.153|R00459|175.207.36.112|LOC_E6100|GGSWX9115|Pyoungtong-SSE112-2|02017772-2485|TenGi0/24|-||수원교육지원청|-\n------------------------------\n(필수)서비스망유형CD|(필수)IP생성유형코드|(필수)IP할당유형코드|(필수)IP블록|(필수)IP블록 입력범위 |(필수)게이트웨이|수용국코드|장비대표IP|장치모델명|시설표준코드|장비별칭|전용번호|인터페이스명|국축스위치시리얼IP|가입자축스위치시리얼IP|수용회선명|비고";
		try{
			fileName.append(DateUtils.getCurrentDateTime());
			fileName.append("_TEXT_FILE_");
			fileName.append(jwtUtil.getUserId(request));
			fileName.append(".txt");

			// File textFile = fileUtil.getFile(fileSystemResource.getPath() + File.separator + fileName.toString());
			File textFile = fileUtil.getFile(fileStorageProperties.getExcelUploadDir() + File.separator + fileName.toString());
			br = new BufferedWriter(new FileWriter(textFile, true));
			br.write(retStr);
			br.flush();
			br.close(); 
		}catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00050");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileName.toString();
	}
	
}
