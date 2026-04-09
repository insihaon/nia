package com.nia.data.linkage.ipsdn.common;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.io.*;

public class UtlFileReaderWriter {
    private final static Logger LOGGER = Logger.getLogger(UtlFileReaderWriter.class);

    public static boolean isFile(String fileName){
        File fileYd = new File(fileName);
        return fileYd.isFile();
    }

    public static String fileReader(String fileName){
        StringBuilder fileData = new StringBuilder();

        try (FileReader rw = new FileReader(fileName)) {
            try (BufferedReader br = new BufferedReader(rw)) {
                String readLine = null ;

                while( ( readLine =  br.readLine()) != null ){
                    fileData.append(readLine);
                }
            }
        } catch (IOException ie) {
            LOGGER.error("[UtlFileReaderWriter] fileReader("+fileName+") error : " + ExceptionUtils.getStackTrace(ie));
        }
        return fileData.toString();
    }

    public static void fileWriter(String fileName, String fileData){
        try (
                FileWriter fw = new FileWriter(fileName, true);
                BufferedWriter bw = new BufferedWriter(fw);
        ){
            bw.write(fileData);
            bw.newLine();
        }catch (IOException ie){
            LOGGER.error("[UtlFileReaderWriter] fileWriter("+fileName+") error : " + ExceptionUtils.getStackTrace(ie));
        }
    }

    public static void fileDataClear(String fileName){
        try {
            new FileOutputStream(fileName).close();
        }catch (IOException ie){
            LOGGER.error("[UtlFileReaderWriter] fileDataClear("+fileName+") error : " + ExceptionUtils.getStackTrace(ie));
        }
    }
}
