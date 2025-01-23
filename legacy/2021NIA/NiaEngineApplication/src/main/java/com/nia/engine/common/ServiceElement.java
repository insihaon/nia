package com.nia.engine.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public abstract class ServiceElement  implements Serializable {
    private Data data;
    private ArrayList<Data> dataList;
    protected String command;

    public ServiceElement() {
        super();
    }

    public ServiceElement(Data data) {
        super();
        this.data = data;
    }

    public ServiceElement(ArrayList<Data> dataList) {
        super();
        this.dataList = dataList;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Map<String, Object> getDataMap() {
        return data == null ? null : data.getMap();
    }

    public ArrayList<Data> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Data> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        // @formatter:off
        return (command != null ? "command=" + command + ",\n" : "");
        // @formatter:on
    }
}