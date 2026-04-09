package com.ipsdn_opt.probe.component.ssh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ipsdn_opt.probe.model.CommandServer;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.Session;

import net.bytebuddy.asm.Advice.Local;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

public class ShellWorker {
    Session session;
    ChannelShell channel;
    InputStream is;
    PrintStream ps;
    int nRead = 0;
    private String prompt;
    private LocalDateTime lastWorkTime;
    private Boolean commandTerminated;

    // public ShellWorker(String prompt) {
    //     this.prompt = prompt;
    // }
    public ShellWorker(CommandServer cmdServer) throws Exception {
        JSch jsch = new JSch();
        lastWorkTime = LocalDateTime.now();

        try {
            session = jsch.getSession(cmdServer.getLoginid(), cmdServer.getIpaddr(), cmdServer.getSshport());
            session.setPassword(cmdServer.getPassword());
			session.setConfig("StrictHostKeyChecking", "no");
            

        } catch (JSchException e) {
            throw e;
        }
		commandTerminated = false;
    }
    public ChannelShell getSession() throws Exception {
        try {
            if ( !session.isConnected() ) {
                session.connect(10000);
            }

            if ( channel == null || !channel.isConnected() ) {
                channel = (ChannelShell) session.openChannel("shell");
                is = channel.getInputStream();
            
                channel.connect(5000);
                ps = new PrintStream(channel.getOutputStream(), true);
                Thread.sleep(1000);

                skipOutputMessage(0, 5000);

            }
        } catch(Exception e) {
            throw e;
        }
		return channel;
	}
    public String sendCommand(String cmd, long time_out) throws Exception {
        lastWorkTime = LocalDateTime.now();
        ps.println(cmd);
        String result = "";
        try {
            result = getOutputMessage(time_out);
        } catch(Exception e) {
            throw e;
        }
        //result = result.replaceAll("\r\n", "\n");
        return result;
    }
    public void skipOutputMessage(int nRead, long time_out) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        // String ret = "";

        try {
            String strBuf = "";
            long elapse_time = 0;
            long start = System.currentTimeMillis();
            while(!strBuf.contains(prompt) && elapse_time < time_out) {
                int readLength = java.lang.Math.min(is.available(),buffer.length);
                nRead = is.read(buffer, 0, readLength);
                baos.write(buffer, 0, nRead);
                strBuf = baos.toString();
                long end = System.currentTimeMillis();
                elapse_time = end - start;
                if (nRead == -1) break;
            }
            if(elapse_time > time_out) {
                throw new Exception("TimeOut (There is no prompt.)");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public String getOutputMessage(long time_out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        String strBuf = "";
        String result = "";

        try {
            // long elapse_time = 0;
            // long start = System.currentTimeMillis();
            // while(!strBuf.contains(prompt) && elapse_time < time_out) {
            //     System.out.println("elapse_time:" + elapse_time + ", nRead=" + nRead + ", buffer=" + buffer);
            //     nRead = this.is.read(buffer, 0, buffer.length);
            //     System.out.println("read.");
            //     baos.write(buffer, 0, nRead);
            //     strBuf = baos.toString();
            //     long end = System.currentTimeMillis();
            //     elapse_time = end - start;
            // }
            long elapse_time = 0;
            long start = System.currentTimeMillis();
            while (!strBuf.contains(prompt) && elapse_time < time_out) {
                int readLength = java.lang.Math.min(is.available(),buffer.length);
                nRead = is.read(buffer, 0, readLength);
                baos.write(buffer, 0, nRead);
                strBuf = baos.toString();
                long end = System.currentTimeMillis();
                elapse_time = end - start;
                if (nRead == -1) break;
            }

            if(elapse_time > time_out) {
                throw new Exception("TimeOut : There is not the prompt.(" + prompt + ")");
            }
            strBuf = baos.toString();
            result = strBuf.toString();
            result = result.replace(prompt, "");
            //result = strBuf.substring(strBuf.indexOf(cmd)+cmd.length(), strBuf.length());
        }
        catch(Exception e) {
            throw e;
        }
        return result;
    }
    public LocalDateTime getLastWorkTime() {
        return lastWorkTime;
    }
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    public void close() {
		try {
			//if(is!=null) is.read();
            if(is!=null) is.close();
			if(ps!=null) ps.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

		if (channel!=null && channel.isConnected()) {
			channel.disconnect();
		}

        if (session!=null && session.isConnected()){
            session.disconnect();
		}
    }

}
