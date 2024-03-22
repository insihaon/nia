package com.codej.base.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

	public static final String separator = "/";
	private static boolean isWindows = File.pathSeparator.equals(";");

	private FileUtil() {
	}

	public static String combine(Object... paths) {
		File file = new File(String.valueOf(paths[0]));

		for (int i = 1; i < paths.length; i++) {
			file = new File(file, String.valueOf(paths[i]));
		}

		String path = file.getPath();
		path = replaceSeparator(path);
		return path;
	}

	public static String replaceSeparator(String path) {
		return path != null && isWindows ? path.replace("\\", separator) : path;
	}

	public static File mkDirs(String in_path) {
		String path = in_path;
		File file = new File(path);

		if (file.isFile()) {
			path = replaceSeparator(file.getParent());
			file = file.getParentFile();
		}

		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("디렉토리를 생성했습니다. " + path);
				return file;
			} else {
				System.out.println("디렉토리를 생성하지 못했습니다. " + path);
			}
		}
		return file;
	}

	public static boolean existFile(String pathname) {
		try {
			File file = new File(pathname);
			return file != null && file.exists();
		} catch (Exception e) {
		}

		return false;
	}

	public static String getCurrentDir() {
		return System.getProperty("user.dir");
	}

	public static Path getCurrentPath() {
		return Paths.get(getCurrentDir());
	}

}