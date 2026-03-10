package com.kt.ipms.legacy.cmn.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.kt.framework.exception.FrameworkException;
import com.kt.ipms.legacy.cmn.exception.ProhibitionExtensionException;

public class ExtensionCheckCommonsMultipartResolver extends CommonsMultipartResolver {

	
	private String prohibitionExtension = null; 

	public void setProhibitionExtension(String prohibitionExtension) {
	    this.prohibitionExtension = prohibitionExtension;
	}
	
	private boolean isProhibitionExtension(String extension) {
		String pExtension = "," + prohibitionExtension + ",";
		String tExtension = "," + extension + ",";
		
		if (0 <= pExtension.indexOf(tExtension)) {
			return true;
		}
		
		return false;
	}
	

	@Override
	protected MultipartParsingResult parseRequest(HttpServletRequest request)
			throws MultipartException {
		
		String encoding = determineEncoding(request);
		FileUpload fileUpload = prepareFileUpload(encoding);
		
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> fileItems = ((ServletFileUpload)fileUpload).parseRequest(request);
			
			// File 확장자체크 로직 추가. - Start
			for (FileItem fileItem : fileItems) {
				if (!fileItem.isFormField()) {
					int index = fileItem.getName().lastIndexOf(".");
				    if (0 <= index) {
				    	String extension = fileItem.getName().substring(index + 1);
				    	
				    	// 금지 확장자 일 경우.
						if (isProhibitionExtension(extension)) {
							throw new ProhibitionExtensionException(prohibitionExtension);
						}
					}
				}
			}
			// File 확장자체크 로직 추가. - End
			
			return parseFileItems(fileItems, encoding);
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new FrameworkException("CMN.E0000022", ex);
		} catch (FileUploadException ex) {
			throw new FrameworkException("CMN.E0000022", ex);
		}
	}
	
	
	

}
