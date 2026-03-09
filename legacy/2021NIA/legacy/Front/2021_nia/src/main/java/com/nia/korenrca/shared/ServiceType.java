package com.nia.korenrca.shared;

public enum ServiceType {
  // @formatter:off
  Main("main"),
  RCA("rca"),
  ;
  // @formatter:on

  private String service;

  ServiceType(String service) {
    this.service = service;
  }

  public String getService() {
    return service;
  }

  public static ServiceType fromURI(String service) {
    for (ServiceType requestMethodType : ServiceType.values()) {
      if (requestMethodType.getService().equals(service)) {
        return requestMethodType;
      }
    }
    return null;
  }
}
