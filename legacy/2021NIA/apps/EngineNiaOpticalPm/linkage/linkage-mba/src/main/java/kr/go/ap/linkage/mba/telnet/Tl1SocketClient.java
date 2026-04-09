package kr.go.ap.linkage.mba.telnet;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
public class    Tl1SocketClient implements Closeable {

    private final Charset charset;
    private final int readBufSize;
    private Duration readTimeout = Duration.ofSeconds(15);

    private String loginPrompt = "LOGIN:";
    private String passwordPrompt = "PASSWORD:";
    private String shellPrompt = "TL1>";

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public Tl1SocketClient(Charset charset, int readBufSize) {
        this.charset = charset;
        this.readBufSize = readBufSize;
    }

    public void setReadTimeout(Duration timeout) { this.readTimeout = timeout; }

    public void setPrompts(String login, String password, String shell) {
        this.loginPrompt = login;
        this.passwordPrompt = password;
        this.shellPrompt = shell;
    }

    public void connect(String host, int port, int connectTimeoutMillis) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), connectTimeoutMillis);
        socket.setSoTimeout((int) readTimeout.toMillis());
        socket.setTcpNoDelay(true);

        in  = new BufferedInputStream(socket.getInputStream(), readBufSize);
        out = new BufferedOutputStream(socket.getOutputStream(), 1024);

        CRLF(out); out.flush();
    }

    public void login(String id, String password) throws IOException {
        readUntil(loginPrompt);
        writeASCII(out, id); CRLF(out); out.flush();

        readUntil(passwordPrompt);
        writeASCII(out, password); CRLF(out); out.flush();

        readUntil(shellPrompt);
    }

    public void logout(String id) throws IOException {
        log.info("client logout");
        sendCommandAndRead("canc-user:::1:" + id + ";");
    }

    public String sendCommandAndRead(String command) throws IOException {
        sendLine(command);
        return readUntil(shellPrompt);
    }

    private void sendLine(String s) throws IOException {
        writeASCII(out, s);
        CRLF(out);
        out.flush();
    }

    private String readUntil(String token) throws IOException {
        long deadline = System.nanoTime() + readTimeout.toNanos();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8 * 1024);
        byte[] buf = new byte[readBufSize];
        int n = 0;
        String s = null;
        int avail;

        while (System.nanoTime() < deadline) {

            try {
                avail = in.available();
            } catch (IOException e) {
                avail = 1;
            }
            if (avail == 0) {
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
            }
            if (in.available() > 0) {
                n = in.read(buf);
                if (n < 0) break;
                baos.write(buf, 0, n);

                s = baos.toString(charset.name());
                log.info("readUntil : {}", s);
                if (s.contains(token)) {
                    return s;
                }
            }
        }
        throw new IOException("Timeout while waiting for token: " + token);
    }

    private static void writeASCII(OutputStream out, String s) throws IOException {
        out.write(s.getBytes(StandardCharsets.US_ASCII));
    }

    private static void CR(OutputStream out) throws IOException { out.write(0x0D); }
    private static void LF(OutputStream out) throws IOException { out.write(0x0A); }
    private static void CRLF(OutputStream out) throws IOException { CR(out);    LF(out); }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    @Override
    public void close() throws IOException {
        log.info("client close");
        try { if (out != null) out.flush(); } catch (Exception ignored) {}
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}
    }
}
