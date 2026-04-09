package com.nia.engine.mapper;

import com.nia.engine.vo.engineToAi.EngineToAiAnoVo;
import com.nia.engine.vo.engineToAi.EngineToAiNoxVo;
import com.nia.engine.vo.selfProcess.AutoTreatProcessVo;
import com.nia.engine.vo.selfProcess.MailAttVo;
import com.nia.engine.vo.selfProcess.MailEquipAndPortVo;
import com.nia.engine.vo.selfProcess.TicketAlCurrentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AutoProcessMapper {

    AutoTreatProcessVo selectAutoProcess(String num);

    AutoTreatProcessVo selectAutoMailingProcess(String ticketId);

    void insertAutoProcess(HashMap<String, String> map);

    void updateAutoProcessTicket(HashMap<String, String> map);

    void updateAutoProcessSyslog(HashMap<String, String> map);

    TicketAlCurrentVo selectTicketAlCurrent(String ticketId);

    List<AutoTreatProcessVo> selectCommandtoTicketList();

    List<AutoTreatProcessVo> selectCommandtoSyslogList();

    EngineToAiAnoVo selectEngineToAiAno(String ticketId);

    EngineToAiNoxVo selectEngineToAiNox(String ticketId);

    void insertAutoProcessSop(HashMap<String, String> map);

    void updateSyslogAlarmState(HashMap<String, String> map);

    void updateAiResult(HashMap<String, String> map);
    void updateAiFTTResult(HashMap<String, String> map);

    void insertSyslogSop(HashMap<String, String> map);

    void updateAutoProcessFTT(String ticketId);

    void updateFTTSop(HashMap<String, String> map);

    void updateSop(HashMap<String, String> map);

    MailAttVo selectMailingAtt(HashMap<String, String> map);

    MailAttVo selectMailingAttTest(HashMap<String, String> map);

    MailEquipAndPortVo selectRequestEquipAndPort(String ticketId);

    String selectParentTicket(String ticketId);

}
