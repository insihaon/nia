package com.kt.ipms.legacy.cmn.util;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class FileUtil {

	public File getFile(String filePath) throws IOException {
		if (!isExist(filePath)) {
			return createNewFile(filePath);
		}
		return new File(filePath);
	}
	
	public File createNewFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.canWrite()) {
			if (filePath.lastIndexOf("\\") != -1) {
				String dirPath = filePath.substring(0, filePath.lastIndexOf("\\"));
				File dir = new File(dirPath);
				dir.mkdirs();
			} else if (filePath.lastIndexOf("/") != -1) {
				String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
				File dir = new File(dirPath);
				dir.mkdirs();
			}
		}
		file.createNewFile();
		return file;
	}
	
	public boolean isExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
}
