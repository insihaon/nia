package kr.go.ap.linkage.mba.service;

import jakarta.annotation.PreDestroy;
import kr.go.ap.core.primary.nia.dto.linkage.model.ModelSendDto;
import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.EqmntEntity;
import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.EqmntSipcEntity;
import kr.go.ap.core.repository.primary.jpa.resource.eqmnt.EqmntRepository;
import kr.go.ap.core.repository.primary.jpa.resource.eqmnt.EqmntSipcRepository;
import kr.go.ap.core.repository.primary.mapper.common.CommonMapper;
import kr.go.ap.core.repository.primary.mapper.linkage.pm.PmLinkageMapper;
import kr.go.ap.linkage.mba.amqp.MbaAiModelPrdAmqp;
import kr.go.ap.linkage.mba.telnet.Tl1SocketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmsMmcHdlService {

    private final PmLinkageMapper pmMapper;
    //    private final EqmntMapper eqmntMapper;
    private final EqmntRepository eqmntRepository;
    private final EqmntSipcRepository eqmntSipcRepository;
    //    private final PmLinkageMapper pmLinkageMapper;
    private final CommonMapper commonMapper;
    private final ParsingHdlService parsingHdlService;
    private final MbaAiModelPrdAmqp mbaAiModelPrdAmqp;

    private volatile Tl1SocketClient client;
    private final ReentrantLock lock = new ReentrantLock();

    @Value("${spring.ems.roadm_d_ip}")
    private String roadmIp;

    @Value("${spring.ems.port_mmc}")
    private int port;

    @Value("${spring.ems.id}")
    private String id;

    @Value("${spring.ems.pw}")
    private String pw;


    public void pmCollectHdl() {
        try {
            sipcMmc();
            pmMmc();
        } catch (Exception e) {
            log.error("pmCollectHdl error", ExceptionUtils.getStackTrace(e));
        } finally {
            closeQuietly(this.client);
            this.client = null;
        }
    }

    private void sipcMmc() {
        log.info("sipcMmc");

        String cmd = null;
        String cmdResult = null;
        List<EqmntEntity> eqmntEntityList = null;

        Tl1SocketClient cli = null;
        try {
            eqmntEntityList = eqmntRepository.findByModelContaining("6300");
            if (!CollectionUtils.isEmpty(eqmntEntityList)) {
                cli = getClient();

                if (cli.isConnected()) {
                    for (EqmntEntity eqmntEntity : eqmntEntityList) {
                        cmd = "RTRV-SIPC:" + eqmntEntity.getTid() + "-SH1::::;";
                        cmdResult = cli.sendCommandAndRead(cmd);
                        parsingHdlService.parsingHdl("sipc", cmdResult);
                        Thread.sleep(2000);
                    }
                }
            }
        } catch (PersistenceException | IndexOutOfBoundsException | IOException | InterruptedException e) {
            log.error("sipcMmc error ", ExceptionUtils.getStackTrace(e));
            Thread.currentThread().interrupt();
        }
    }

    private void pmMmc() {
        log.info("pmMmc");

        String cmd = null;
        String cmdResult = null;
        String strOcrTime = null;
        List<EqmntSipcEntity> eqmntSipcEntityList = null;
        HashMap<String, String> map = null;

        Tl1SocketClient cli = null;
        ModelSendDto dto = null;

        try {
            Thread.sleep(10_000);

            cli = getClient();

            if (cli.isConnected()) {
                eqmntSipcEntityList = eqmntSipcRepository.findAll();
                if (!CollectionUtils.isEmpty(eqmntSipcEntityList)) {
                    for (EqmntSipcEntity equipSipc : eqmntSipcEntityList) {
                        cmd = "RTRV-PM:" + equipSipc.getTid() + "-" + equipSipc.getSystemName()
                                + "::::SIGNAL=AMPPWR,TYPE=15M,INTERVAL=CURR;";
                        cmdResult = cli.sendCommandAndRead(cmd);
//                        strOcrTime = parsingHdlService.parsingHdl("pmMmc", cmdResult);

//                        pmMmc는 case문에없음
                        strOcrTime = parsingHdlService.parsingHdl("pm", cmdResult);
                        Thread.sleep(3000);
                    }

                    pmMapper.callPmPrepro(strOcrTime);
                    map = new HashMap<>();
                    map.put("key", "emsPmKey");
                    commonMapper.updateLinkageYdKey(map);

                    dto = ModelSendDto.builder()
                            .ocrTime(strOcrTime)
                            .build();

                    mbaAiModelPrdAmqp.sendMessageCmd(dto);
                }
            }
        } catch (PersistenceException | IOException | InterruptedException e) {
//            log.error(e.toString());
            log.error("pmMmc error ", ExceptionUtils.getStackTrace(e));
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
        }
    }

    private Tl1SocketClient getClient() throws IOException {
        Tl1SocketClient c = this.client;

        if (c != null && isHealthy(c)) return c;

        lock.lock();

        try {
            c = this.client;
            if (c != null && isHealthy(c)) return c;

            closeQuietly(c);
            Tl1SocketClient nc = new Tl1SocketClient(StandardCharsets.US_ASCII, 4096);
            nc.connect(roadmIp, port, 15000);
            nc.setReadTimeout(Duration.ofSeconds(20));
            nc.login(id, pw);
            this.client = nc;
            return nc;
        } finally {
            lock.unlock();
        }
    }

    private boolean isHealthy(Tl1SocketClient c) {
        try {
            if (!c.isConnected()) return false;
            String resp = c.sendCommandAndRead("");
            return resp != null && resp.contains("TL1>");
        } catch (Exception e) {
            return false;
        }
    }

    private void closeQuietly(Tl1SocketClient c) {
        log.info("closeQuietly");

        if (c == null) return;

        try {
            if (c.isConnected()) c.logout(id);
        } catch (Exception ignore) {
        }

        try {
            c.close();
        } catch (Exception ignore) {
        }
    }

    @PreDestroy
    public void shutdown() {
        closeQuietly(this.client);
    }
}
