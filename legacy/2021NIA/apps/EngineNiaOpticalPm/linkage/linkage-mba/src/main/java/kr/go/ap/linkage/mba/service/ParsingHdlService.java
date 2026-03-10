package kr.go.ap.linkage.mba.service;

import kr.go.ap.core.primary.nia.entity.linkage.pm.PmEntity;
import kr.go.ap.core.primary.nia.entity.linkage.pm.key.PmKey;
import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.EqmntSipcEntity;
import kr.go.ap.core.repository.primary.jpa.linkage.pm.PmRepository;
import kr.go.ap.core.repository.primary.jpa.resource.eqmnt.EqmntSipcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParsingHdlService {

    //    private final EqmntMapper eqmntMapper;
//    private final EqmntRepository eqmntRepository;
    private final EqmntSipcRepository eqmntSipcRepository;
    private final PmRepository pmRepository;
//    private final PmLinkageMapper pmLinkageMapper;

    public String parsingHdl(String mmcGb, String mmcMsg) {
        log.info("parsingHdl : \n" + mmcMsg);

        final Pattern STATUS = Pattern.compile("(?m)^\\s*[A-Z]\\s+(COMPLD|DENY)\\b");
        final Pattern BLOCK_COMMENT = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        // "SH숫자:READY|DISABLE" (따옴표/쉼표/공백 허용)
        final Pattern SH_STATE = Pattern.compile(
                "\\\"?\\s*(SH\\d{1,3})\\s*:\\s*(ENABLE|DISABLE)?\\s*,?\\s*(READY|DISABLE)?\\s*,?\\s*\\\"?"
        );
        // 첫 줄에서 TID 추출
        final Pattern HOSTLINE = Pattern.compile("(?m)^\\s*([\\d.]+)-(SH\\d+)\\s+\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}");

        String strOcrTime = null;
        int quarter;
        Timestamp ocrTime;
        Timestamp now;
        LocalDateTime ldt;
        LocalDateTime rounded;

        List<EqmntSipcEntity> eqmntSipcEntityList = null;
        List<PmEntity> pmEntitiyList = null;
        String[] mmcMsgLineArr;
        HashMap<String, Object> paramObjMap;

        try {
            if (mmcMsg == null || mmcMsg.isEmpty()) return null;

            // CR/LF 정규화 (PM 응답에 ^M 포함)
            String normalized = mmcMsg.replace("\r", "").replace("^M", "");

            // 상태(COMPLD/DENY) 판별
            Matcher st = STATUS.matcher(normalized);
            String status = st.find() ? st.group(1) : null;

            switch (mmcGb) {
                case "sipc": {
                    String tid = extractTid(normalized, HOSTLINE);
                    if (tid == null) {
                        log.warn("RTRV-SIPC: TID not found in header line.");
                        return null;
                    }

                    eqmntSipcEntityList = new ArrayList<>();


                    if (!"COMPLD".equals(status)) {
                        String reason = extractFirstComment(normalized, BLOCK_COMMENT);
                        log.warn("RTRV-SIPC DENY: {}", reason == null ? "DENIED" : reason);

                        eqmntSipcEntityList.add(
                                EqmntSipcEntity.of(tid, "SH1")
                        );

                    } else {
                        String cleaned = BLOCK_COMMENT.matcher(normalized).replaceAll("");
                        cleaned = cleaned.replace(";", "");

                        Matcher m = SH_STATE.matcher(cleaned);

                        while (m.find()) {
                            String shelf = m.group(1);
                            String control = m.group(2) == null ? "" : m.group(2);
                            String state = m.group(3) == null ? "" : m.group(3);

                            log.info("{} control={} state={}", shelf, control, state);

                            // READY 인 것만 저장
                            if ("READY".equals(state)) {

                                eqmntSipcEntityList.add(
                                        EqmntSipcEntity.of(tid, shelf)
                                );
                            }
                        }

                        if (eqmntSipcEntityList.isEmpty()) {
                            log.info("parsingHdl RTRV-SIPC: READY shelves none. Use default SH1 for TID {}", tid);
                            eqmntSipcEntityList.add(
                                    EqmntSipcEntity.of(tid, "SH1")
                            );


                        }
                    }

                    if (!CollectionUtils.isEmpty(eqmntSipcEntityList)) {
                        eqmntSipcRepository.deleteByEqmntSipcKey_Tid(tid);
                        log.info("parsingHdl RTRV-SIPC size: {}", eqmntSipcEntityList.size());
                        eqmntSipcRepository.saveAll(eqmntSipcEntityList);
                    }

                    break;
                }

                case "pm": {
                    if (!"COMPLD".equals(status)) {
                        String reason = extractFirstComment(normalized, BLOCK_COMMENT);
                        log.warn("RTRV-PM DENY: {}", reason == null ? "DENIED" : reason);
                        return null;
                    }

                    pmEntitiyList = new ArrayList<>();

                    mmcMsgLineArr = normalized.split("\n");

                    String currentSysName = null;

                    for (String raw : mmcMsgLineArr) {
                        String line = raw.trim();
                        if (line.isEmpty() || ";".equals(line)) continue;

                        // 시스템명 추출 (SH 포함, -PM 제외)
                        if (line.contains("SH") && !line.contains("-PM")) {

                            // sysname = 앞의 토큰
                            String[] toks = line.split("\\s+");
                            if (toks.length > 0) {
                                currentSysName = toks[0];
                            }
                            continue;
                        }


                        if (line.startsWith("\"S.") || line.startsWith("S.")) {
                            // 따옴표 제거
                            String csv = line.replaceAll("^\"|\"$", "");
                            // 주석 제거
                            csv = BLOCK_COMMENT.matcher(csv).replaceAll("");

                            String[] arr = csv.split(",");

                            if (arr.length < 11) {
                                log.warn("RTRV-PM: unexpected columns: {}", csv);
                                continue;
                            }

                            // port, unit, tmper, rx/tx 값 매핑
                            String port = arr[0].replaceAll("\\s", "");            // S.1-ADD_IN 등
                            String unit = arr[1].split(":")[0];                    // MRSA-2A
                            String tmper = arr[2];                                 // 15M
                            double rxCur = parseDoubleSafe(arr[3]);
                            double rxMin = parseDoubleSafe(arr[4]);
                            double rxMax = parseDoubleSafe(arr[5]);
                            double rxAve = parseDoubleSafe(arr[6]);
                            double txCur = parseDoubleSafe(arr[7]);
                            double txMin = parseDoubleSafe(arr[8]);
                            double txMax = parseDoubleSafe(arr[9]);
                            double txAve = parseDoubleSafe(arr[10]);

                            PmEntity pmEntity = PmEntity.builder()
                                    .pmKey(PmKey.builder().sysname(currentSysName).port(port).build())
                                    .unit(unit)
                                    .tmper(tmper)
                                    .rxCur(rxCur)
                                    .rxMin(rxMin)
                                    .rxMax(rxMax)
                                    .rxAve(rxAve)
                                    .txCur(txCur)
                                    .txMin(txMin)
                                    .txMax(txMax)
                                    .txAve(txAve)
                                    .build();

                            // OCR 시간: 현재 시각을 15분 단위로 절삭
                            now = new Timestamp(System.currentTimeMillis());
                            ldt = now.toLocalDateTime();
                            quarter = (ldt.getMinute() / 15) * 15;
                            rounded = ldt.withMinute(quarter).withSecond(0).withNano(0);
                            ocrTime = Timestamp.valueOf(rounded);

                            pmEntity.setOcrtime(ocrTime);
                            strOcrTime = String.valueOf(ocrTime);

                            pmEntitiyList.add(pmEntity);
                        }
                    }

                    if (!CollectionUtils.isEmpty(pmEntitiyList)) {

                        pmRepository.saveAll(pmEntitiyList);
                        log.info("parsingHdl RTRV-PM size: {}", pmEntitiyList.size());
                    }
                    break;
                }

                default:
                    log.warn("Unknown mmcGb: {}", mmcGb);
            }

        } catch (NullPointerException | IllegalArgumentException | PersistenceException
                 | IndexOutOfBoundsException e) {
            log.error("parsingHdl error", e);
        }

        return strOcrTime;
    }

    private static String extractTid(String text, Pattern hostline) {
        Matcher h = hostline.matcher(text);
        if (!h.find()) return null;
        return h.group(1);
    }

    private static String extractFirstComment(String text, Pattern blockComment) {
        Matcher c = blockComment.matcher(text);
        return c.find() ? c.group(0).replaceAll("^/\\*|\\*/$", "").trim() : null;
    }

    private static double parseDoubleSafe(String s) {
        try {
            if (s == null) return Double.NaN;
            s = s.trim();
            if (s.equalsIgnoreCase("NA")) return Double.NaN;
            return Double.parseDouble(s);
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}


