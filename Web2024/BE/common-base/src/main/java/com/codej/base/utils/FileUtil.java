package com.codej.base.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

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

	public static void DeleteOldFiles(String directory) {

        if(directory == null)
            return;

        Calendar cal = Calendar.getInstance();    // Calendar 객체 생성
        long todayMil = cal.getTimeInMillis();    // 현재 시간(밀리 세컨드)
        long oneDayMil = 24 * 60 * 60 * 1000;    // 일 단위

        Calendar fileCal = Calendar.getInstance();
        Date fileDate = null;

        File path = new File(directory);
        File[] list = path.listFiles();         // 파일 리스트 가져오기

        for (int j = 0; j < list.length; j++) {

            // 파일의 마지막 수정시간 가져오기
            fileDate = new Date(list[j].lastModified());

            // 현재시간과 파일 수정시간 시간차 계산(단위 : 밀리 세컨드)
            fileCal.setTime(fileDate);
            long diffMil = todayMil - fileCal.getTimeInMillis();

            // 시간으로 계산
            int diffDay = (int)(diffMil / (oneDayMil / 24));

            // 1시간이 지난 파일 삭제
            if (diffDay >= 1 && list[j].exists()) {
                list[j].delete();
//                System.out.println(list[j].getName() + " 파일을 삭제했습니다.");
            }
        }
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