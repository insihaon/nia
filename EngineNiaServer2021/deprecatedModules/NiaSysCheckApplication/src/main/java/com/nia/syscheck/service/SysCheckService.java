package com.nia.syscheck.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface SysCheckService {

    void sysCheck() throws IOException, TimeoutException;

}
