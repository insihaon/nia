package com.ipsdn_opt.app.component.ssh;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class FrrShellWorker {
    InputStream is;
    PrintStream ps;
    int nRead = 0;

    public String sendCommandForVtysh(String cmd, long time_out, String prompt) {
        cmd = cmd.format("vtysh -c '%s'", cmd);
        ps.println(cmd);
        String result = getOutputMessage(cmd, nRead, time_out, prompt);
        //result = result.replaceAll("\r\n", "\n");

        return result;
    }
    public void skipOutputMessage(int nRead, long time_out, String prompt) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        String strBuf = "";
        // String ret = "";

        try {
            int bufferOffset = 0;
            long elapse_time = 0;
            long start = System.currentTimeMillis();
            while (!strBuf.contains(prompt) && elapse_time < time_out && bufferOffset < buffer.length) {
                int readLength = java.lang.Math.min(is.available(),buffer.length-bufferOffset);
                nRead = is.read(buffer, bufferOffset, readLength);
                baos.write(buffer, 0, nRead);
                strBuf = baos.toString();
                long end = System.currentTimeMillis();
                elapse_time = end - start;
                if (nRead == -1) break;
                bufferOffset += nRead;
            }
            if(elapse_time > time_out) {
                throw new Exception("TimeOut (There is no prompt.)");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private String getOutputMessage(String cmd, int nRead, long time_out, String prompt) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        String strBuf = "";
        String result = "";

        try {
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
                throw new Exception("TimeOut (There is no prompt.)");
            }
            strBuf = baos.toString();
            result = strBuf.toString();
            //result = strBuf.substring(strBuf.indexOf(cmd)+cmd.length(), strBuf.length());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
