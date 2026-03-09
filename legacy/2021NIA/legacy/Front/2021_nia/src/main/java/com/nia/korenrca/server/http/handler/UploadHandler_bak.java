package com.nia.korenrca.server.http.handler;

import com.nia.korenrca.server.http.route.HttpRouteMatcher;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadHandler_bak implements Handler<RoutingContext> {
    protected UploadHandler_bak() { super(); }

    public static Router create(Router router) {
        router.post("/upload").handler(new UploadHandler_bak());
        return router;
    }

    @Override
    public void handle(RoutingContext ctx){
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
            String newFilePath = newPath + "/" +  f.fileName();
            Integer subNum = 2;
            while (new File(newFilePath).isFile()) {
                String strExtension = newFilePath.substring(newFilePath.lastIndexOf("."));
                newFilePath = newFilePath.substring(0, newFilePath.lastIndexOf("."));
                if(subNum > 2){
                    newFilePath = newFilePath.substring(0, newFilePath.lastIndexOf("_"));
                }
                newFilePath += ("_" + subNum.toString() + strExtension);
                subNum++;
            }
            String tempFilePath = f.uploadedFileName() + newFilePath.substring(newFilePath.lastIndexOf("/") + 1);

            BufferedReader br = null;
            BufferedWriter bw = null;
            try {
                fileInfoList.add(new JsonObject().put("filePath", newFilePath.replace(HttpRouteMatcher.ACCESS_PATH + "/", "")).put("fileSize", f.size()));
                File uploadedFile = new File(f.uploadedFileName());
                uploadedFile.renameTo(new File(tempFilePath));

                br = new BufferedReader(new InputStreamReader(new FileInputStream(tempFilePath), "UTF-8"));
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFilePath), "UTF-8"));

                String s;
                while ((s = br.readLine()) != null) {
                    bw.write(s);
                    bw.newLine();
                }
                if (bw != null)
                    bw.close();
                if (br != null)
                    br.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    if (bw != null)
                        bw.close();
                    if (br != null)
                        br.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
                new File(tempFilePath).delete();
            }

            System.out.println("Filename: " + f.fileName());
            System.out.println("Size: " + f.size());
        }

        ctx.response().end(fileInfoList.toBuffer());
    }
}
