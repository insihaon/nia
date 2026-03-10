package com.ipsdn_opt.app.component.ssh;

import java.io.IOException;
import java.io.PrintStream;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class FrrShellManager {
    Session session;
    ChannelShell channel;
    FrrShellWorker sw;

    public FrrShellWorker shellConnect(String loginid, String passwd, String ipaddr, int port) {
        JSch jsch = new JSch();
        sw = new FrrShellWorker();

        try {
            session = jsch.getSession(loginid, ipaddr, port);
            session.setPassword(passwd);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(10000);
            channel = (ChannelShell) session.openChannel("shell");
            try {
                sw.is = channel.getInputStream();
                channel.connect(5000);
                sw.ps = new PrintStream(channel.getOutputStream(), true);

            }
            catch(IOException e) {
                e.printStackTrace();
            }

        }
        catch(JSchException e) {
            e.printStackTrace();
        }
        return sw;
    }
    public void shellClose() {
        try {
            sw.is.close();
            sw.ps.close();
            if(channel.isConnected()) channel.disconnect();
            if(session.isConnected()) session.disconnect();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
