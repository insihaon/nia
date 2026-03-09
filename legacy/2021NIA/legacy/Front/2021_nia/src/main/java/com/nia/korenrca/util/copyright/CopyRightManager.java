package com.nia.korenrca.util.copyright;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class CopyRightManager {

    public static String packagePath = "";
    public static String sourceRoot = "D:\\Work\\KTRCA\\workspace\\rca_ui";
    public static String duplicationString = "NMS version 1.0";     // 카피라이트 작성된 파일 스킵을 위한 첫번째 라인 TEXT
    public static String fileNameEndFilterString = "Factory.java";
    public static ArrayList<String> fileErrorList = new ArrayList<String>();
    public static ArrayList<String> alreadyCopyWriteFileList = new ArrayList<String>();
    public static int factoryCount = 0;
    public static int successCount = 0;
    public static Date date = new Date();

    public static void main(String[] args) throws IOException {
        System.out.println("CopyRight Init Start...");

        String currentDir = System.getProperty("user.dir");
        packagePath = System.getProperty("sun.java.command");

        if (packagePath.indexOf("CopyRightManager") != -1) {
            packagePath = packagePath.substring(0, packagePath.indexOf("CopyRightManager") - 1);
        }
        packagePath = currentDir + "\\src\\main\\java\\" + packagePath.replace('.', '\\') + '\\';

        System.out.println("## SourceRoot : " + sourceRoot);
        System.out.println("## PackagePath : " + packagePath);
        run();
    }

    /**
     * 작업실행
     *
     * @param
     * @return
     * @throws IOException
     */
    public static void run() throws IOException {

        System.out.println("CopyRight Work Start...");

        File file = new File(sourceRoot);
        findFactoryFile(file);
        logResult();
    }

    /**
     * factory 찾음
     *
     * @param 
     * @return
     * @throws IOException
     */
    public static void findFactoryFile(File file) {
        File[] filelist = file.listFiles();

        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].isDirectory()) {
                findFactoryFile(filelist[i]);
            } else {
                String fileName = filelist[i].getName();
                if (fileName.endsWith(fileNameEndFilterString)) {
                    writeCopyRightMessage(filelist[i]);
                    System.out.println(filelist[i].getPath());
                    factoryCount++;
                }
            }
        }
    }

    /**
     * File 접근유무, CopyRight가 써있는지 확인
     *
     * @param
     * @return
     * @throws IOException
     */
    public static void writeCopyRightMessage(File factoryFile) {
        File file = factoryFile;

        // 읽을수잇는지, 쓸수있는지 확인
        if (file.canRead() && file.canWrite()) {

            // 이미 써있는지 확인
            if (isTargetFile(file)) {
                // 파일에 저작권 작성
                try {
                    writeFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                alreadyCopyWriteFileList.add(file.getPath());
            }
        } else {
            fileErrorList.add(file.getPath());
        }
    }

    /**
     * 해당 File이 이미 써져있는지 확인 및 menuID를 가져옴
     *
     * @param
     * @return
     * @throws IOException
     */
    public static boolean isTargetFile(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                String value = reader.readLine();

                if (value.indexOf(duplicationString) != -1) {
                    return false;
                }
            }
            reader.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return false;
        }
    }

    /**
     * 해당 File에 CopyRight 작성
     *
     * @param
     * @return
     * @throws IOException
     */
    public static void writeFile(File file) throws IOException {
        PrintStream ps = null;
        Scanner scan = null;

        try {
            // 쓰기용
            ps = new PrintStream(new FileOutputStream(file.getAbsoluteFile(), true));

            // 읽기용
            Locale local = new Locale("KO", "KR");
            scan = new Scanner(new FileInputStream(packagePath + "CopyRightFormat.txt"));
            scan.useLocale(local);

            StringBuilder copyright = new StringBuilder();
            StringBuilder totalSourcecode = new StringBuilder();

            // CopyRightFormat 내용 복사
            int i = 0;
            while (scan.hasNext()) {
                ps.println();
                /*if (i == 8) {
                    // 8번째 라인. 작성일 입력
                    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
                    copyright.append(scan.nextLine() + dateFormat.format(date) + "\n");
                } else*/ {
                    copyright.append(scan.nextLine() + "\n");
                }
                i++;
            }

            // Factory파일 내용 복사
            int ch;
            String data = "";
            FileReader fr = new FileReader(file);
            do {
                ch = fr.read();
                if (ch != -1) {
                    data += ((char)ch);
                }
            } while (ch != -1);
            fr.close();

            // 파일 내용지우기
            PrintWriter pw = new PrintWriter(file);
            pw.write("");
            pw.flush();
            pw.close();

            // CopyRight + Factory내용 한것을 입력하기
            totalSourcecode.append(copyright).append("\n").append(data.trim());
            ps.print(totalSourcecode);
            ps.flush();
            successCount++;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * 작업결과 출력 및 로그파일 작성
     *
     * @param
     * @return
     * @throws IOException
     */
    public static void logResult() throws IOException {

        File file = new File(packagePath + "Log.txt");
        PrintStream ps = null;
        String temp = "";

        // Log파일 존재여부 확인
        if (!file.exists()) {
            // 없으니 생성함.
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (!file.canRead() || !file.canWrite()) {
                file.delete();
                file.createNewFile();
            }
        }

        try {
            ps = new PrintStream(new FileOutputStream(file, false));

            if (factoryCount > 0) {
                temp = "####################### factoryCount Result #######################";
                System.out.println(temp);
                ps.println();
                ps.print(temp);

                temp = "factoryCount => " + factoryCount;
                System.out.println(temp);
                ps.println();
                ps.print(temp);
                ps.println();
                ps.println();
            }

            if (successCount > 0) {
                temp = "\n\n####################### Success Result #######################";
                System.out.println(temp);
                ps.println();
                ps.print(temp);

                temp = "SuccessCount => " + successCount;
                System.out.println(temp);
                ps.println();
                ps.print(temp);
                ps.println();
                ps.println();
            }

            if (alreadyCopyWriteFileList.size() > 0) {
                temp = "####################### alreadyCopyWriteFileList Result #######################";
                System.out.println(temp);
                ps.println();
                ps.print(temp);

                for (int i = 0; i < alreadyCopyWriteFileList.size(); i++) {
                    temp = (i + 1) + ": " + alreadyCopyWriteFileList.get(i);
                    System.out.println(temp);
                    ps.println();
                    ps.print(temp);
                }
                ps.println();
                ps.println();
            }

            if (fileErrorList.size() > 0) {
                temp = "####################### fileErrorList Result #######################";
                System.out.println(temp);
                ps.println();
                ps.print(temp);

                for (int i = 0; i < fileErrorList.size(); i++) {
                    temp = (i + 1) + ": " + fileErrorList.get(i);
                    System.out.println(temp);
                    ps.println();
                    ps.print(temp);
                }
                ps.println();
                ps.println();
            }

            temp = "\n\n####################### CopyRight End #######################";
            System.out.println(temp);
            ps.println();
            ps.print(temp);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (ps != null) {
                ps.flush();
                ps.close();
            }
        }
    }
}
