package com.nia.korenrca.server.http.handler;

import com.nia.korenrca.server.http.route.HttpRouteMatcher;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadHandler {

    public static Router create(Router router) {
        router.post("/upload").handler(ctx -> {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String usagePath = ctx.queryParams().get("usage") == null ? "" : ctx.queryParams().get("usage");
            if(!usagePath.equals("")){
                File configDirectory = new File(HttpRouteMatcher.ACCESS_PATH + "/" + usagePath);
                if (!configDirectory.exists()) {
                    configDirectory.mkdir();
                }
            }
            String newPath = HttpRouteMatcher.ACCESS_PATH + "/" + (usagePath.equals("") ? "" : (usagePath + "/")) + formatter.format(date);
            JsonArray fileInfoList = new JsonArray();

            File configDirectory = new File(newPath);
            if (!configDirectory.exists()) {
                configDirectory.mkdir();
            }

            for (FileUpload f : ctx.fileUploads()) {
                String newFileName = newPath + "/" +  f.fileName();
                Integer subNum = 2;
                while (new File(newFileName).isFile()) {
                    String strExtension = newFileName.substring(newFileName.lastIndexOf("."));
                    newFileName = newFileName.substring(0, newFileName.lastIndexOf("."));
                    if(subNum > 2){
                        newFileName = newFileName.substring(0, newFileName.lastIndexOf("_"));
                    }
                    newFileName += ("_" + subNum.toString() + strExtension);
                    subNum++;
                }
                fileInfoList.add(new JsonObject().put("filePath", newFileName.replace(HttpRouteMatcher.ACCESS_PATH + "/","")).put("fileSize",f.size()));
                File uploadedFile = new File(f.uploadedFileName());
                uploadedFile.renameTo(new File(newFileName));
                try {
                    uploadedFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new File(f.uploadedFileName()).delete();

                System.out.println("Filename: " + f.fileName());
                System.out.println("Size: " + f.size());
            }

            ctx.response().end(fileInfoList.toBuffer());
        });

        return router;
    }
}
