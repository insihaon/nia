package com.nia.korenrca.system;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Natives {
    private static final Logger logger = LogManager.getLogger(Natives.class);
    private static final byte[] buf = new byte['?'];
    private static File extractionDirOverride = null;
    private static File extractionDir = null;

    public static void extractNativeLib(String sysName, String name) throws IOException {
        extractNativeLib(sysName, name, false, true);
    }

    public static void extractNativeLib(String sysName, String name, boolean load) throws IOException {
        extractNativeLib(sysName, name, load, true);
    }

    public static void extractNativeLib(String sysName, String name, boolean load, boolean warning) throws IOException {
        String fullname;
        String path;
        extractionDir = null;
        if (!name.contains(".")) {
            fullname = System.mapLibraryName(name);
            path = sysName + "/" + fullname;
        } else {
            fullname = name;
            path = sysName + "/" + fullname;
        }
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        if (url == null) {
            if (!warning) {
                logger.log(Level.WARN, String.format("Cannot locate native library: %s/%s", new Object[]{sysName, fullname}));
            }
            return;
        }
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        File targetFile = new File(getExtractionDir(sysName), fullname);
        OutputStream out = null;
        try {
            if (targetFile.exists()) {
                logger.log(Level.INFO, String.format("Not copying library %s. Latest already extracted.", fullname));
                return;
            }
            out = new FileOutputStream(targetFile);
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            in = null;
            out.close();
            out = null;

            targetFile.setLastModified(conn.getLastModified());
        } catch (FileNotFoundException ex) {
            if (ex.getMessage().contains("used by another process")) {
                return;
            }
            throw ex;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }

            if (load) {
                try {
                    System.load(targetFile.getAbsolutePath());
                } catch (UnsatisfiedLinkError ex) {
                    logger.log(Level.INFO, ex.getMessage(), ex);
                }
            }
        }

        logger.log(Level.INFO, String.format("Copied %s to %s", new Object[]{fullname, targetFile}));
    }

    public static File getExtractionDir(String folderPath) {
        if (extractionDirOverride != null) {
            return extractionDirOverride;
        }
        if (extractionDir == null) {
            File workingFolder = new File(folderPath).getAbsoluteFile();
            if (!workingFolder.canWrite()) {
                setStorageExtractionDir();
            } else {
                try {
                    File file = new File(workingFolder.getAbsolutePath() + File.separator + ".testwrite");
                    file.createNewFile();
                    file.delete();
                    extractionDir = workingFolder;
                } catch (Exception e) {
                    setStorageExtractionDir();
                }
            }
        }
        return extractionDir;
    }

    private static void setStorageExtractionDir() {
        logger.log(Level.WARN, "Working directory is not writable. Using home directory instead.");
        extractionDir = new File("").getAbsoluteFile();
    }

    public static void extractNativeLibs() throws IOException {
        extractNativeLib("config", "config.properties");
        extractNativeLib("config", "rca.jks");
        extractNativeLib("config", "tl_key.p12");

        extractNativeLib("file-access/chrome", "ChromeStandaloneSetup.exe");
        extractNativeLib("file-access/chrome", "ChromeStandaloneSetup64.exe");
    }
}
