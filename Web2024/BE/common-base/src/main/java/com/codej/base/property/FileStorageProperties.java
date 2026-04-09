package com.codej.base.property;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.codej.base.utils.FileUtil;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    @Value("${file.access-url:/external/**}")
    private String accessUrl;
    @Value("${file.excel-upload-dir:excelFiles}")
    private String excelUploadDir;

    public String getUploadDir() {
        String dir = FileUtil.combine(FileUtil.getCurrentDir(), "uploads");
        if (uploadDir != null) {
            dir = uploadDir;
        }

        if (!Files.exists(Paths.get(dir))) {
            FileUtil.mkDirs(dir);
        }
        return dir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getExcelUploadDir() {
        String uploadPath = "uploads";
        if(uploadDir != null) {
            uploadPath = uploadDir;
        }
        String dir = FileUtil.combine(FileUtil.getCurrentDir(), uploadPath, excelUploadDir);
        if (!Files.exists(Paths.get(dir))) {
            FileUtil.mkDirs(dir);
        }
        return dir;
    }

}
