package com.codej.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import com.codej.base.controller.BaseController;
import com.codej.base.dto.AppDto;
import com.codej.base.dto.response.UploadFileResponse;
import com.codej.base.property.FileStorageProperties;
import com.codej.base.utils.DateUtil;
import com.codej.web.service.FileStorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FileController extends BaseController {

    @Autowired
	private AppDto appDto;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(
        @RequestPart(value = "file", required = true) MultipartFile file) {
        String dirName = DateUtil.getCurrentDate_YYMMDD();
        String fileName = fileStorageService.storeFile(file, dirName);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(dirName + "/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, 
                                    fileDownloadUri, 
                                    fileDownloadUri.replace("/downloadFile/", fileStorageProperties.getAccessUrl().replace("**", "")),
                                    file.getContentType(), 
                                    file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(
            @RequestPart(value = "files", required = true) MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/downloadFile/{dirName:.+}")
    public ModelAndView downloadFolder(@PathVariable String dirName, HttpServletRequest request) {
        String url = appDto.getErrorRedirect();
		if (url.length() > 0) {
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(url);
			return new ModelAndView(redirectView);
		}

        // 404 페이지 리다이렉트
        return new ModelAndView("error/index");
    }

    @RequestMapping(value = "/downloadFile/{dirName:.+}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String dirName, @PathVariable String fileName, HttpServletRequest request) throws Exception {
        Resource resource = fileStorageService.loadFileAsResource(dirName + "/" + fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType+";charset=UTF-8"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
}
