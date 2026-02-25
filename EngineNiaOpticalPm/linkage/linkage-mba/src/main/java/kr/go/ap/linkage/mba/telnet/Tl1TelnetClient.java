//package com.nia.linkage.mba.telnet;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.net.telnet.*;
//
//import java.io.*;
//import java.nio.charset.Charset;
//import java.time.Duration;
//
//@Slf4j
//public class Tl1TelnetClient implements Closeable {
//
//    private final TelnetClient telnet = new TelnetClient();
//    private final Charset charset;
//    private InputStream in;
//    private OutputStream out;
//    private final int readBufSize;
//    private Duration readTimeout = Duration.ofSeconds(15);
//
//    // TL1 기본 프롬프트 패턴들 (필요 시 변경)
//    private String loginPrompt = "LOGIN:";
//    private String passwordPrompt = "PASSWORD:";
//    private String shellPrompt = "TL1>"; // 로그인 성공 후 프롬프트
//
//    public Tl1TelnetClient(Charset charset, int readBufSize) {
//        this.charset = charset;
//        this.readBufSize = readBufSize;
//    }
//
//    public void setReadTimeout(Duration timeout) { this.readTimeout = timeout; }
//    public void setPrompts(String login, String password, String shell) {
//        this.loginPrompt = login;
//        this.passwordPrompt = password;
//        this.shellPrompt = shell;
//    }
//
//    public void connect(String host, int port, int connectTimeoutMillis) throws IOException, InvalidTelnetOptionException {
//        // 터미널 타입/에코/SGA 설정
//        telnet.addOptionHandler(new TerminalTypeOptionHandler("VT100", false, false, true, false));
//        telnet.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));
////        telnet.addOptionHandler(new EchoOptionHandler(false, false, true, true)); // 1안
//      telnet.addOptionHandler(new EchoOptionHandler(true,  false, true, true));  // 2안
//
//
//        telnet.setReaderThread(true);
//        telnet.setConnectTimeout(connectTimeoutMillis);
//        telnet.registerSpyStream(new PrintStream("/home/codej8888/server/logs/linkage/mba/linkageMba/telnet.spy"));
//        telnet.connect(host, port);
//        in = telnet.getInputStream();
//        out = telnet.getOutputStream();
//    }
//
//    public void login(String id, String password) throws IOException {
//        sendLine("");
//
//        readUntil(loginPrompt);
//
//        sendLine(id);
//
//        readUntil(passwordPrompt);
//
//        sendLine(password);
//
//        // 로그인 성공 프롬프트 대기
//        readUntil(shellPrompt);
//    }
//
//    public void logout(String id) throws IOException {
//        sendLine("canc-user:::1:"+id+";");
//    }
//
//
//    public String sendCommandAndRead(String command) throws IOException {
//        sendLine(command);
//        return readUntil(shellPrompt);
//    }
//
//    /** 라인 전송 (CRLF 필수: \r\n) */
//    private void sendLine(String s) throws IOException {
//        String line = s + "\r\n";
//        out.write(line.getBytes(charset));
//        out.flush();
//
////        OutputStream output;
////        output = new PrintStream(out);
////        output.write(line.getBytes(StandardCharsets.US_ASCII));
////        output.flush();
//    }
//
//    /** readUntil: 지정된 패턴(프롬프트)이 나올 때까지 읽어서 문자열 반환 */
//    private String readUntil(String token) throws IOException {
//        long deadline = System.nanoTime() + readTimeout.toNanos();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buf = new byte[readBufSize];
//
//        while (System.nanoTime() < deadline) {
//            if (in.available() == 0) {
//                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
//                continue;
//            }
//            int n = in.read(buf);
//            if (n < 0) break;
//            baos.write(buf, 0, n);
//
//            String s = baos.toString(charset.name());
//            log.info("readUntil : " + s);
//            if (s.contains(token)) {
//                return s;
//            }
//        }
//        throw new IOException("Timeout while waiting for token: " + token);
//    }
//
//    public boolean isConnected() {
//        return telnet.isConnected();
//    }
//
//    @Override
//    public void close() throws IOException {
//        try { if (telnet.isConnected()) telnet.disconnect(); } catch (Exception ignored) {}
//    }
//}
