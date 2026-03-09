package com.nia.engine.common;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Response extends ServiceElement {
    private boolean succeeded;
    private String message;
    private JsonObject document;
    private String image_url;
    private String fileName;
    private ArrayList<String> fileNames;
    private ArrayList<String> realFileNames;
    private boolean trash;
    private byte[] bytes;

    public Response() {
        super();
    }

    public Response(int requestId) {
        super();
    }

    public Response(String command, boolean succeeded) {
        super();
        this.command = command;
        this.succeeded = succeeded;
    }

    public Response(String command, boolean succeeded, String message, Data data) {
        super(data);
        this.command = command;
        this.succeeded = succeeded;
        this.message = message;
    }

    public Response(String command, boolean succeeded, String message, ArrayList<Data> dataList) {
        super(dataList);
        this.command = command;
        this.succeeded = succeeded;
        this.message = message;
    }

    public Response(String command, String errorMessage) {
        super();
        this.command = command;
        this.succeeded = false;
        this.message = errorMessage;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        // @formatter:off
        return super.toString() + "\n" + "succeeded=" + succeeded + ",\n" + (message != null ? "\nmessage=" + message : "");
        // @formatter:on
    }

    public void setDocument(JsonObject document) {
        this.document = document;
    }

    public JsonObject getDocument() {
        return document;
    }





    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public ArrayList<String> getRealFilenames() {
        return this.realFileNames;
    }

    public void addFile(String filePath) {
        addFile(filePath, null);
    }

    public void addFile(String filePath, String fileName) {
        if (this.realFileNames == null) {
            this.realFileNames = new ArrayList<String>();

            if (fileName != null) {
                this.fileNames = new ArrayList<String>();
            }
        }

        this.realFileNames.add(filePath);
        if (fileName != null) {
            this.fileNames.add(fileName);
        }
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public boolean isTrash() {
        return trash;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }

}