package com.ipsdn_opt.probe.model;

import lombok.Data;

@Data
public class IpStatus {
    private String ipAddress;
    private boolean status = false;

    public IpStatus() {
    }
    public IpStatus(String ipAddress, boolean status) {
        this.ipAddress = ipAddress;
        this.status = status;
    }
}
