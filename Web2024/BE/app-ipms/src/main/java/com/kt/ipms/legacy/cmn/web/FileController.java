package com.kt.ipms.legacy.cmn.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.ExcelCheckService;
import com.kt.ipms.legacy.cmn.util.FileUtil;
import com.kt.ipms.legacy.cmn.vo.FileVo;

@Controller("legacyFileController")
public class FileController extends CommonController {
	
	@Autowired
	private FileUtil fileUtil;
	
	@Lazy @Autowired
	private FileSystemResource fileSystemResource;
	
	@Autowired
	private ExcelCheckService excelCheckService;

	@RequestMapping(value = "/downloadExcelFile.excel", method = RequestMethod.POST)
	public void downloadExcelFile(@ModelAttribute("fileVo") FileVo fileVo, 
		HttpServletRequest request, HttpServletResponse response) {
        if((request.getParameter("fileName").indexOf(".."))!= -1 || (request.getParameter("fileName").indexOf("./"))!=-1 || (request.getParameter("fileName").indexOf(".\\"))!=-1 || (request.getParameter("fileName").indexOf(":"))!=-1){
        	excelCheckService.updateExcelDown("N");
        	throw new ServiceException("CMN.HIGH.00000");
		}
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File excelFile = null;
		try {
			
			String filePath = fileSystemResource.getPath() + File.separator +  fileVo.getFileName();
			if (fileUtil.isExist(filePath)) {
				//IE8 SSL 다운로드 불가 현상 조치-START
				response.setHeader("Cache-Control", "");
				response.setHeader("Pragma", "");
				
				response.setContentType("Content-type:application/octet-stream;");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileVo.getFileName() + "\";");
				response.setHeader("Content-Transfer-Encoding", "binary");
				
				excelFile = fileUtil.getFile(filePath);
				bis = new BufferedInputStream(new FileInputStream(excelFile));
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] buf = new byte[2048]; // buffer size 2K.
				int read = 0;
				while ((read = bis.read(buf)) != -1) {
					bos.write(buf, 0, read);
				}
				bos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (excelFile != null) {
				excelFile.delete();
			}
		}
	}
}
