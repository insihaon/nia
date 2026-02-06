package com.nia.data.linkage.ip.sflow.common;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Vector;

@Component
public class SFTPSession {
    private final Logger LOGGER = Logger.getLogger(SFTPSession.class);

    private Session session = null;
    private Channel channel = null;

    @Value("${spring.ftp.host}")
    private String host = null;

    @Value("${spring.ftp.port}")
    private int port = 0;

    @Value("${spring.ftp.user}")
    private String user = null;

    @Value("${spring.ftp.password}")
    private String pw = null;

    private ChannelSftp channelSftp;

    public void init(){

        try{
            JSch jsch = new JSch();

            session = jsch.getSession(user, host, port);

            // 3. 패스워드를 설정한다.
            session.setPassword(pw);

            // 4. 세션과 관련된 정보를 설정한다.
            java.util.Properties config = new java.util.Properties();
            // 4-1. 호스트 정보를 검사하지 않는다.
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // 5. 접속한다.
            session.connect();

            // 6. sftp 채널을 연다.
            channel = session.openChannel("sftp");
            channel.connect();

            // 8. 채널을 SFTP용 채널 객체로 캐스팅한다
            channelSftp = (ChannelSftp) channel;
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> SFTPSession init()  error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }

// 단일 파일 업로드
public void upload(String dir, File file) {
    FileInputStream in = null;

    try { //파일을 가져와서 inputStream에 넣고 저장경로를 찾아 put
        in = new FileInputStream(file);
        channelSftp.cd(dir);
        channelSftp.put(in, file.getName());
    } catch (SftpException se) {
        LOGGER.error(">>>>>>>>>>>> SFTPSession upload()  error : "+ ExceptionUtils.getStackTrace(se)+" <<<<<<<<<<<<<<<<");
    } catch (FileNotFoundException fe) {
        LOGGER.error(">>>>>>>>>>>> SFTPSession upload()  error : "+ ExceptionUtils.getStackTrace(fe)+" <<<<<<<<<<<<<<<<");
    } finally {
        try {
            in.close();
        } catch (IOException ioe) {
            LOGGER.error(">>>>>>>>>>>> SFTPSession upload()  error : "+ ExceptionUtils.getStackTrace(ioe)+" <<<<<<<<<<<<<<<<");
        }
    }
}

    /**
     * 단일 파일 다운로드
     *
     * @param dir
     *            파일 위치(서버)
     * @param downloadFileName
     *            다운로드할 파일
     * @param path
     *            저장될 공간
     */

    public boolean download(String dir, String downloadFileName, String path) {

        boolean result = true;
        InputStream in = null;
        FileOutputStream out = null;

        try {
            channelSftp.cd(dir);
            in = channelSftp.get(downloadFileName);
            LOGGER.info(">>>>>>>>>>>> SFTPSession download() downloadFileName : "+ downloadFileName+" <<<<<<<<<<<<<<<<");
        } catch (SftpException e) {
            LOGGER.error(">>>>>>>>>>>> SFTPSession download()  error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
            disconnection();
            result = false;
        }

        try {
            out = new FileOutputStream(new File(path));
            int data;
            byte b[] = new byte[2048];
            while((data = in.read(b, 0, 2048)) != -1) {
                out.write(b, 0, data);
                out.flush();
            }
        } catch (IOException e) {
            LOGGER.error(">>>>>>>>>>>> SFTPSession download()  error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
            result = false;
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                LOGGER.error(">>>>>>>>>>>> SFTPSession download()  error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
            }
        }
        return result;
    }

    public void fileDelete(String path, String fileName){
        try {
            channelSftp.cd(path);
            channelSftp.rm(fileName);
        }catch (SftpException e){
            LOGGER.error(">>>>>>>>>>>> SFTPSession fileDelete()  error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }

    /**
     * 인자로 받은 경로의 파일 리스트를 리턴한다.
     * @param path
     * @return
     */
    public Vector<ChannelSftp.LsEntry> getFileList(String path) {

    	Vector<ChannelSftp.LsEntry> list = null;
    	try {
    		channelSftp.cd(path);
    		//System.out.println(" pwd : " + channelSftp.pwd());
    		list = channelSftp.ls(".");
		} catch (SftpException e) {
			e.printStackTrace();
			return null;
		}
    	return list;
    }

    public void disconnection() {
        try {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
            if(channel != null){
                channel.disconnect();
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> SFTPSession disconnection()  error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }
}
