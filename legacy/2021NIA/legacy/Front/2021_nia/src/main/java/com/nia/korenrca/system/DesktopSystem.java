package com.nia.korenrca.system;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;


public class DesktopSystem {
    private static Logger logger = LogManager.getLogger(DesktopSystem.class);

    protected boolean initialized = false;
    public static boolean IsDevelopmentMode = false;
    public static boolean IsAuthPassMode = false;
    public static String AppPath = "";

    public void initialize() {
        if (this.initialized) {
            return;
        }

        this.initialized = true;
        IsDevelopmentMode = isDevelopmentMode();
        if (IsDevelopmentMode) {
            AppPath = new File("").getAbsolutePath() + "/app/";
        }

        try {
            System.setProperty("file.encoding", "UTF-8");
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
        }

        try {
            createConfigDirectory();
            createChromeDirectory();
            Natives.extractNativeLibs();
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Error while copying native libraries", ex);
        }
    }

    private void createConfigDirectory() {
        File configDirectory = new File("config");
        if (!configDirectory.exists()) {
            configDirectory.mkdir();
        }
    }
    private void createChromeDirectory() {
        File configDirectory = new File("file-access/chrome");
        if (!configDirectory.exists()) {
            configDirectory.mkdir();
        }
    }

    public static boolean isDevelopmentMode() {
        String pkgname = DesktopSystem.class.getPackage().getName();

        File directory = null;
        String relPath = pkgname.replace('.', '/');
        // System.out.println("ClassDiscovery: Package: " + pkgname + " becomes Path:" + relPath);
        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
        // System.out.println("ClassDiscovery: Resource = " + resource);
        if (resource == null) {
            throw new RuntimeException("No resource for " + relPath);
        }

        try {
            directory = new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(pkgname + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
        } catch (IllegalArgumentException e) {
            directory = null;
        }
        // System.out.println("ClassDiscovery: Directory = " + directory);

        return directory != null && directory.exists();
    }
}
