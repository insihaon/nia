package com.kt.ipms.legacy.opermgmt.uploadmgmt.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.codej.web.controller.FileController;
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.service.UploadMgmtService;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadListVo;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadVo;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadZipcodeVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class UploadMgmtController  extends CommonController {
	
	@Autowired
	private UploadMgmtService uploadMgmtService;

	@Autowired
	private FileController fileController;
	
	@RequestMapping(value = "/opermgmt/uploadmgmt/uploadView.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap uploadView(@RequestBody TbUploadVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbUploadListVo resultListVo = uploadMgmtService.selectListPageUpload(searchVo);
		return createResultList(resultListVo.getTbUploadVo(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/uploadmgmt/uploadView.do", method = RequestMethod.POST)
	public String uploadView(@ModelAttribute("searchVo") TbUploadVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
				TbUploadVo searchVoClone = new TbUploadVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = uploadViewModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/uploadmgmt/uploadView.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/uploadmgmt/uploadView";
	}
	private ModelMap uploadViewModel(@ModelAttribute("searchVo") TbUploadVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUploadListVo resultListVo = null;
		
		try{
			setPagination(searchVo);
			resultListVo = uploadMgmtService.selectListPageUpload(searchVo);
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("resultListVo",resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/uploadmgmt/upload.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public int fileUpload(ModelMap model, MultipartHttpServletRequest request, HttpServletResponse response)  {
		MultipartFile file =  request.getFile("file");
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = file.getOriginalFilename();
		
		/* Sparrow - PATH_TRAVERSAL Start */
		fileName = fileName.replaceAll("/","");
		fileName = fileName.replaceAll("\\\\","");
		fileName = fileName.replaceAll("&","");
		/* Sparrow - PATH_TRAVERSAL End */
		
		File convFile = new File(fileName);
		Date max_date = null;
		Date temp_max_date = null;
		try {
			if(uploadMgmtService.selectmaxdate() != null){
				max_date = transFormat.parse(uploadMgmtService.selectmaxdate());
				temp_max_date = max_date;
			}
			// System.out.println(max_date);  //Codeeyes-Critical-sysout
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		int result = 0;
		int temp_result = 1;
		BufferedReader bufReader = null;
		try {
			file.transferTo(convFile);
			// UploadFileResponse fileResponse = fileController.uploadFile(file);
            bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(convFile),"UTF-8"));
            String line = "";
            int cnt = 0;
            while((line = bufReader.readLine()) != null){
            	if(cnt != 0){
            		if(!line.toString().equals("")){
            			String[] line_array = line.toString().split("\\|");
                        TbUploadZipcodeVo tbUploadZipcodeVo = new TbUploadZipcodeVo();
        				tbUploadZipcodeVo.setZipcode(line_array[0]);
        				tbUploadZipcodeVo.setSido(line_array[1]);
        				tbUploadZipcodeVo.setSigungu(line_array[2]);
        				tbUploadZipcodeVo.setTown(line_array[3]);
        				tbUploadZipcodeVo.setRoadnameid(line_array[4]);
        				tbUploadZipcodeVo.setRoadname(line_array[5]);
        				tbUploadZipcodeVo.setBldg_mainno(line_array[7]);
        				tbUploadZipcodeVo.setBldg_subno(line_array[8]);
        				tbUploadZipcodeVo.setBldgmgmtid(line_array[9]);
        				tbUploadZipcodeVo.setBldgname(line_array[11]);
        				tbUploadZipcodeVo.setRi(line_array[14]);
        				tbUploadZipcodeVo.setLegal_dong(line_array[13]);
        				tbUploadZipcodeVo.setDong(line_array[15]);
        				tbUploadZipcodeVo.setJibun_bonbun(line_array[17]);
        				tbUploadZipcodeVo.setJibun_bubun(line_array[18]);
        				tbUploadZipcodeVo.setOpcode(line_array[19]);
                		try {
        					Date temp_date = transFormat.parse(line_array[22]);
        					
        					if(max_date != null){
        						if(max_date.compareTo(temp_date) < 1){
        							//temp_date 가 큼 ---> insert or max_date = temp_date
        							if(temp_max_date.compareTo(temp_date) < 0){
        								temp_max_date = temp_date;
        							}
        							uploadMgmtService.insertTbNewZipcode(tbUploadZipcodeVo);
        						}
        					}else{
        						//max_date = null >>>> data 없음 >>>> insert 
        						if(cnt == 1){
        							temp_max_date = temp_date;
        						}else{
        							if(temp_max_date.compareTo(temp_date) < 0){
        								temp_max_date = temp_date;
        							}
        						}
        						uploadMgmtService.insertTbNewZipcode(tbUploadZipcodeVo);
        					}
        					
        				} catch (ParseException e) {
        					// TODO Auto-generated catch block
        					temp_result = 0;
        					convFile.delete();
        					e.printStackTrace();
        				}
            		}
            	}
            	cnt++;
            }            
            bufReader.close();
            convFile.delete();
            //upload 내용 db 저장
            String status = "";
            //upload file 상태 db 저장
            if(temp_result == 1){
            	status = "success";
            }else{
            	status = "fail";
            }
            
            TbUploadVo tbUploadVo = new TbUploadVo();
            HttpSession session = request.getSession(false);
            LoginInfoVo sessionVo = (LoginInfoVo) session.getAttribute("user");
            tbUploadVo.setFile_name(file.getOriginalFilename());
            tbUploadVo.setRegist_id(sessionVo.getSuserId());
            tbUploadVo.setStatus(status);
            tbUploadVo.setMax_date(temp_max_date);
            result = uploadMgmtService.insertTbUploadVo(tbUploadVo);
            
		} catch (IllegalStateException e) {
			// System.out.println(e);  //Codeeyes-Critical-sysout
			e.printStackTrace();
		} catch (IOException e) {
			// System.out.println(e);  //Codeeyes-Critical-sysout
			e.printStackTrace();
		}finally{
			try {
				bufReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
}
