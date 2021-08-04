package com.nia.tsdn.service.result.service;


import com.nia.tsdn.service.result.vo.ReservedResultVo;

public interface AsyncSendService {

    void sendServiceResult(ReservedResultVo reservedResultVo);
}
