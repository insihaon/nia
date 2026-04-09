package com.ipsdn_opt.app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipsdn_opt.app.model.AdjRouter;
import com.ipsdn_opt.app.model.AnalysisE2e;
import com.ipsdn_opt.app.model.AnalysisFactors;
import com.ipsdn_opt.app.model.AnalysisLink;
import com.ipsdn_opt.app.model.AnalysisOptPathSet;
import com.ipsdn_opt.app.model.AnalysisOptRoute;
import com.ipsdn_opt.app.model.AnalysisPath;
import com.ipsdn_opt.app.model.ChangedRoute;
import com.ipsdn_opt.app.model.ChangedRouteFactors;
import com.ipsdn_opt.app.model.ConfigMetricHis;
import com.ipsdn_opt.app.model.DbChangedMetric;
import com.ipsdn_opt.app.model.DbOptRouteForNms;
import com.ipsdn_opt.app.model.DistanceCost;
import com.ipsdn_opt.app.model.E2eNode;
import com.ipsdn_opt.app.model.E2eNodeInfo;
import com.ipsdn_opt.app.model.Interface;
import com.ipsdn_opt.app.model.Link;
import com.ipsdn_opt.app.model.LinkFactor;
import com.ipsdn_opt.app.model.LinkFactorAvg;
import com.ipsdn_opt.app.model.LinkForCalc;
import com.ipsdn_opt.app.model.LinkInfo;
import com.ipsdn_opt.app.model.LinkMetricHis;
import com.ipsdn_opt.app.model.LinkTraffic;
import com.ipsdn_opt.app.model.LinkTrafficAvg;
import com.ipsdn_opt.app.model.MetricInterface;
import com.ipsdn_opt.app.model.NodeFactor;
import com.ipsdn_opt.app.model.NodeFactorAvg;
import com.ipsdn_opt.app.model.OptPathForNms;
import com.ipsdn_opt.app.model.OptRoute;
import com.ipsdn_opt.app.model.OptRouteForNms;
import com.ipsdn_opt.app.model.OptRoutePathSet;
import com.ipsdn_opt.app.model.OptRunDebug;
import com.ipsdn_opt.app.model.OptRunHis;
import com.ipsdn_opt.app.model.OspfRoute;
import com.ipsdn_opt.app.model.OspfRouteForNms;
import com.ipsdn_opt.app.model.OspfRouteOnly;
import com.ipsdn_opt.app.model.Path;
import com.ipsdn_opt.app.model.RequestMetricConfig;
import com.ipsdn_opt.app.model.Response;
import com.ipsdn_opt.app.model.RoutePath;
import com.ipsdn_opt.app.model.RoutePathForNms;
import com.ipsdn_opt.app.model.RoutePathSet;
import com.ipsdn_opt.app.model.Router;
import com.ipsdn_opt.app.model.Settings;
import com.ipsdn_opt.app.model.ShellCommand;
import com.ipsdn_opt.app.model.TestOrderbyParent;
import com.ipsdn_opt.app.model.VerifyOptRoute;
import com.ipsdn_opt.app.repository.ConfigMetricHisRepository;
import com.ipsdn_opt.app.repository.E2eNodeRepository;
import com.ipsdn_opt.app.repository.InterfaceRepository;
import com.ipsdn_opt.app.repository.LinkFactorAvgRepository;
import com.ipsdn_opt.app.repository.LinkFactorRepository;
import com.ipsdn_opt.app.repository.LinkFactorRouteAvgRepository;
import com.ipsdn_opt.app.repository.LinkRepository;
import com.ipsdn_opt.app.repository.LinkTrafficAvgRepository;
import com.ipsdn_opt.app.repository.LinkTrafficRepository;
import com.ipsdn_opt.app.repository.NodeFactorAvgRepository;
import com.ipsdn_opt.app.repository.NodeFactorRepository;
import com.ipsdn_opt.app.repository.OptRouteRepository;
import com.ipsdn_opt.app.repository.OptRunDebugRepository;
import com.ipsdn_opt.app.repository.OptRunHisRepository;
import com.ipsdn_opt.app.repository.OspfBackupDateRepository;
import com.ipsdn_opt.app.repository.OspfDbRouterBackupRepository;
import com.ipsdn_opt.app.repository.OspfDbRouterRepository;
import com.ipsdn_opt.app.repository.OspfRouteBackupRepository;
import com.ipsdn_opt.app.repository.OspfRouteForNmsRepository;
import com.ipsdn_opt.app.repository.OspfRouteOnlyRepository;
import com.ipsdn_opt.app.repository.OspfRouteRepository;
import com.ipsdn_opt.app.repository.RoutePathRepository;
import com.ipsdn_opt.app.repository.RoutePathSetRepository;
import com.ipsdn_opt.app.repository.SettingsRepository;
import com.ipsdn_opt.app.repository.TestOrderbyParentRepository;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice.Local;

@Service
@Slf4j
public class OptimizeRouteSvc {
    private static Integer INF = 32767; //DB SMALLINT 최대값(32767)
    private OptRunHis runhis;

    private Settings settings;
    private List<Interface> interfaces;
    private List<Link> links;
    // private HashMap<Long, List<RoutePath>> mapRoutePathSets;
    private HashMap<Long, Interface> ifMap;
    private HashMap<Long, Link> linkMap;

    @Autowired
    SettingsRepository settingsRepository;
    @Autowired
    LinkFactorRepository linkFactorRepository;
    @Autowired
    LinkTrafficRepository linkTrafficRepository;
    @Autowired
    NodeFactorRepository nodeFactorRepository;
    @Autowired
    LinkFactorAvgRepository linkFactorAvgRepository;
    @Autowired
    NodeFactorAvgRepository nodeFactorAvgRepository;
    @Autowired
    LinkTrafficAvgRepository linkTrafficAvgRepository;
    @Autowired
    OptRunHisRepository optRunHisRepository;
    @Autowired
    OspfRouteRepository ospfRouteRepository;
    @Autowired
    E2eNodeRepository e2eNodeRepository;
    @Autowired
    LinkRepository linkRepository;
    @Autowired
    InterfaceRepository interfaceRepository;
    @Autowired
    OptRunDebugRepository optRunDebugRepository;
    @Autowired
    OptRouteRepository optRouteRepository;
    @Autowired
    RoutePathRepository routePathRepository;
    @Autowired
    RoutePathSetRepository routePathSetRepository;
    @Autowired
    RoutingTableSvc routingTableSvc;
    @Autowired
    TestOrderbyParentRepository testOrderbyParentRepository;
    @Autowired
    LinkFactorRouteAvgRepository linkFactorRouteAvgRepository;
    @Autowired
    OspfRouteOnlyRepository ospfRouteOnlyRepository;
    @Autowired
    OspfRouteBackupRepository ospfRouteBackupRepository;
    @Autowired
    OspfDbRouterBackupRepository ospfDbRouterBackupRepository;
    @Autowired
    OspfBackupDateRepository ospfBackupDateRepository;
    @Autowired
    ConfigMetricHisRepository configMetricHisRepository;
    @Autowired
    OspfRouteForNmsRepository ospfRouteForNmsRepository;
    @Autowired
    RestSvc restSvc;
    @Autowired
    EntityManager em;

    public Response calculateDayData(LocalDate fromDate) {
        Settings settings = settingsRepository.findAll().get(0);
        //fromDate = LocalDate.of(2022, 9, 7); //시작일 00시부터
        LocalDate toDate = fromDate.plusDays(1);  //종료일 00시 이전까지 (종료일 미포함)

        // Link Factor 평균값/중간값 산출
        List<LinkFactor> linkfactors = linkFactorRepository.findLinkFactorByDateRange(fromDate, toDate);
        HashMap<Long, LinkFactorForSort> lfMap = new HashMap<>();
        List<LinkFactorAvg> linkfactorsAvg = new ArrayList<>();
        linkfactors.forEach(lf -> {
            LinkFactorForSort lfs = lfMap.get(lf.getLink_id());
            if(lfs==null) {
                lfs = new LinkFactorForSort(lf.getLink_id());
                lfMap.put(lf.getLink_id(), lfs);
            }
            lfs.setAddFactor(lf.getLatency(), lf.getJitter(), lf.getLost());
        });
        // sortLinkFactorFile(false, lfMap);
        for(Long key : lfMap.keySet()) {
            LinkFactorForSort lfs = lfMap.get(key);

            Collections.sort(lfs.getLatencys());
            Collections.sort(lfs.getJitters());
            Collections.sort(lfs.getLosts());

            LinkFactorAvg lfa = new LinkFactorAvg(lfs.getLink_id());
            lfa.setSourcedate(fromDate);
            linkfactorsAvg.add(lfa);

            linkFactorAverage(lfs, lfa, settings.getAvgrange(), settings.getMedposition());
            // System.out.println(String.format("link: %d Latency [getRange=%d%%, count=%d, start=%d, end=%d] total=%f average=%f", 
            //         lfs.getLink_id(), settings.getAvgrange(), lfs.getLatencys().size(), start, end, total, avg));
        }
        // sortLinkFactorFile(true, lfMap);

        // 노드팩터 평균값/중간값 산출
        List<NodeFactor> nodefactors = nodeFactorRepository.findNodeFactorByDateRange(fromDate, toDate);
        HashMap<Long, NodeFactorForSort> nodeMap = new HashMap<>();
        List<NodeFactorAvg> nodefactorsAvg = new ArrayList<>();
        nodefactors.forEach(nf -> {
            NodeFactorForSort nfs = nodeMap.get(nf.getNode_id());
            if(nfs==null) {
                nfs = new NodeFactorForSort(nf.getNode_id());
                nodeMap.put(nf.getNode_id(), nfs);
            }
            nfs.setAddFactor(nf.getCpuusage(), nf.getMemusage());
        });

        //sortNodeFactorFile(false, nodeMap);
        for(Long key : nodeMap.keySet()) {
            NodeFactorForSort nfs = nodeMap.get(key);

            Collections.sort(nfs.getCpuUsages());
            Collections.sort(nfs.getMemUsages());

            NodeFactorAvg nfa = new NodeFactorAvg(nfs.getNode_id());
            nfa.setSourcedate(fromDate);
            nodefactorsAvg.add(nfa);

            nodeFactorAverage(nfs, nfa, settings.getAvgrange(), settings.getMedposition());
            // System.out.println(String.format("link: %d Latency [getRange=%d%%, count=%d, start=%d, end=%d] total=%f average=%f", 
            //         lfs.getLink_id(), settings.getAvgrange(), lfs.getLatencys().size(), start, end, total, avg));
        }
        //sortNodeFactorFile(true, nodeMap);

        // Link Traffic 평균값/중간값 산출
        List<LinkTraffic> linktraffics = linkTrafficRepository.findLinkTrafficByDateRange(fromDate, toDate);
        HashMap<Long, LinkTrafficForSort> ltMap = new HashMap<>();
        List<LinkTrafficAvg> linktrafficAvg = new ArrayList<>();
        linktraffics.forEach(lt -> {
            LinkTrafficForSort lts = ltMap.get(lt.getIf_id());
            if(lts==null) {
                lts = new LinkTrafficForSort(lt.getIf_id());
                ltMap.put(lt.getIf_id(), lts);
            }
            lts.setAddTraffic(lt.getTxbitrate(), lt.getTxpktrate());
        });
        // sortLinkTrafficFile(false, ltMap);
        for(Long key : ltMap.keySet()) {
            LinkTrafficForSort lts = ltMap.get(key);

            Collections.sort(lts.getTxBitrates());
            Collections.sort(lts.getTxPktrates());

            LinkTrafficAvg lta = new LinkTrafficAvg(lts.getIf_id());
            lta.setSourcedate(fromDate);
            linktrafficAvg.add(lta);

            linkTrafficAverage(lts, lta, settings.getAvgrange(), settings.getMedposition());
            // System.out.println(String.format("link: %d Latency [getRange=%d%%, count=%d, start=%d, end=%d] total=%f average=%f", 
            //         lfs.getLink_id(), settings.getAvgrange(), lfs.getLatencys().size(), start, end, total, avg));
        }
        // sortLinkTrafficFile(true, ltMap);

        linkFactorAvgRepository.deleteLinkFactorAvgBySourcedate(fromDate);
        linkFactorAvgRepository.saveAll(linkfactorsAvg);

        nodeFactorAvgRepository.deleteNodeFactorAvgBySourcedate(fromDate);
        nodeFactorAvgRepository.saveAll(nodefactorsAvg);

        linkTrafficAvgRepository.deleteLinkTrafficAvgBySourcedate(fromDate);
        linkTrafficAvgRepository.saveAll(linktrafficAvg);
        return new Response(true, "Link/Node Factors Day-data Read Complete.", linkfactorsAvg);
    }
    public void linkFactorAverage(LinkFactorForSort lfs, LinkFactorAvg lfa, Integer avgRange, Integer medPosition) {
        lfa.setAvgrange(avgRange);
        lfa.setMedposition(medPosition);

        //Latency 평균값/중간값 산출
        int start = (lfs.getLatencys().size() - (int)(lfs.getLatencys().size()*(avgRange/100.)))/2;
        int end = lfs.getLatencys().size() - start - 1;
        double total = 0;
        for(int i=start; i<end; i++) {
            total += lfs.getLatencys().get(i);
        }
        Double avg = null;
        if((end-start+1)>0) avg = total / (end - start + 1);
        lfa.setLatency_avg(avg);

        int pos = (int)Math.ceil(lfs.getLatencys().size()*(medPosition/100.)) - 1;
        lfa.setLatency_med(lfs.getLatencys().get(pos));

        //Jitter 평균값/중간값 산출
        start = (lfs.getJitters().size() - (int)(lfs.getJitters().size()*(avgRange/100.)))/2;
        end = lfs.getJitters().size() - start - 1;
        total = 0;
        for(int i=start; i<end; i++) {
            total += lfs.getJitters().get(i);
        }
        avg = null;
        if((end-start+1)>0) avg = total / (end - start + 1);
        lfa.setJitter_avg(avg);

        pos = (int)Math.ceil(lfs.getJitters().size()*(medPosition/100.)) - 1;
        if(pos>=0) lfa.setJitter_med(lfs.getJitters().get(pos));
        else lfa.setJitter_med(null);

        //Lost 평균값/중간값 산출
        start = (lfs.getLosts().size() - (int)(lfs.getLosts().size()*(avgRange/100.)))/2;
        end = lfs.getLosts().size() - start - 1;
        total = 0;
        for(int i=start; i<end; i++) {
            total += lfs.getLosts().get(i);
        }
        avg = null;
        if((end-start+1)>0) avg = total / (end - start + 1);
        lfa.setLost_avg((Integer)avg.intValue());

        pos = (int)Math.ceil(lfs.getJitters().size()*(medPosition/100.)) - 1;
        if(pos>=0) lfa.setLost_med(lfs.getLosts().get(pos));
        else lfa.setLost_med(null);
    }
    public void nodeFactorAverage(NodeFactorForSort nfs, NodeFactorAvg nfa, Integer avgRange, Integer medPosition) {
        nfa.setAvgrange(avgRange);
        nfa.setMedposition(medPosition);

        //CPU사용율 평균값/중간값 산출
        int start = (nfs.getCpuUsages().size() - (int)(nfs.getCpuUsages().size()*(avgRange/100.)))/2;
        int end = nfs.getCpuUsages().size() - start - 1;
        float total = 0;
        for(int i=start; i<end; i++) {
            total += nfs.getCpuUsages().get(i);
        }
        Float avg = null;
        if((end-start+1)>0) avg = total / (end - start + 1);
        nfa.setCpuusage_avg(avg);

        int pos = (int)Math.ceil(nfs.getCpuUsages().size()*(medPosition/100.)) - 1;
        if(pos>=0) nfa.setCpuusage_med(nfs.getCpuUsages().get(pos));
        else nfa.setCpuusage_med(null);

        //Memory사용율 평균값/중간값 산출
        start = (nfs.getMemUsages().size() - (int)(nfs.getMemUsages().size()*(avgRange/100.)))/2;
        end = nfs.getMemUsages().size() - start - 1;
        total = 0;
        for(int i=start; i<end; i++) {
            total += nfs.getMemUsages().get(i);
        }
        avg = null;
        if((end-start+1)>0) avg = total / (end - start + 1);
        nfa.setMemusage_avg(avg);

        pos = (int)Math.ceil(nfs.getMemUsages().size()*(medPosition/100.)) - 1;
        if(pos>=0) nfa.setMemusage_med(nfs.getMemUsages().get(pos));
        else nfa.setMemusage_med(null);
    }
    public void linkTrafficAverage(LinkTrafficForSort lts, LinkTrafficAvg lta, Integer avgRange, Integer medPosition) {
        lta.setAvgrange(avgRange);
        lta.setMedposition(medPosition);

        // txBitRate 평균값/중간값 산출
        int start = (lts.getTxBitrates().size() - (int)(lts.getTxBitrates().size()*(avgRange/100.)))/2;
        int end = lts.getTxBitrates().size() - start - 1;
        Long total = 0L;
        for(int i=start; i<end; i++) {
            total += lts.getTxBitrates().get(i)/1000;
        }
        Integer avg = null;
        if((end-start+1)>0) avg = (int)(total / (end - start + 1));
        lta.setTxbitrate_avg(avg*1000);

        int pos = (int)Math.ceil(lts.getTxBitrates().size()*(medPosition/100.)) - 1;
        if(pos>=0) lta.setTxbitrate_med(lts.getTxBitrates().get(pos));
        else lta.setTxbitrate_med(null);

        //txPktRate 평균값/중간값 산출
        start = (lts.getTxPktrates().size() - (int)(lts.getTxPktrates().size()*(avgRange/100.)))/2;
        end = lts.getTxPktrates().size() - start - 1;
        total = 0L;
        for(int i=start; i<end; i++) {
            total += lts.getTxPktrates().get(i);
        }
        avg = null;
        if((end-start+1)>0) avg = (int)(total / (end - start + 1));
        lta.setTxpktrate_avg(avg);

        pos = (int)Math.ceil(lts.getTxPktrates().size()*(medPosition/100.)) - 1;
        if(pos>=0) lta.setTxpktrate_med(lts.getTxPktrates().get(pos));
        else lta.setTxpktrate_med(null);
    }
    public void sortLinkFactorFile(boolean isSort, HashMap<Long, LinkFactorForSort> lfMap) {
        try {
            File file;
            if(isSort)
                file = new File("after_sort.txt");
            else
                file = new File("before_sort.txt");
            FileWriter fw = new FileWriter(file);
            for(Long key : lfMap.keySet()) {
                LinkFactorForSort lfs = lfMap.get(key);
                fw.write(String.format("============= Latency (%d) ==============\n", lfs.getLink_id()));
                int cnt=0;
                for(Double l : lfMap.get(key).getLatencys()) {
                    fw.write(String.format("[%4d] value=%f\n", ++cnt, l));
                }
                fw.write(String.format("============= Jitter (%d) ==============\n", lfs.getLink_id()));
                cnt=0;
                for(Double l : lfMap.get(key).getJitters()) {
                    fw.write(String.format("[%4d] value=%f\n", ++cnt, l));
                }
                fw.write(String.format("============= Lost (%d) ==============\n", lfs.getLink_id()));
                cnt=0;
                for(Integer l : lfMap.get(key).getLosts()) {
                    fw.write(String.format("[%4d] value=%d\n", ++cnt, l));
                }
            }
            fw.flush();
            fw.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void sortNodeFactorFile(boolean isSort, HashMap<Long, NodeFactorForSort> nodeMap) {
        try {
            File file;
            if(isSort)
                file = new File("after_sort_nodefactors.txt");
            else
                file = new File("before_sort_nodefactors.txt");
            FileWriter fw = new FileWriter(file);
            for(Long key : nodeMap.keySet()) {
                NodeFactorForSort nfs = nodeMap.get(key);
                fw.write(String.format("============= CPU USAGE (%d) ==============\n", key));
                int cnt=0;
                for(float l : nfs.getCpuUsages()) {
                    fw.write(String.format("[%4d] value=%f\n", ++cnt, l));
                }
                fw.write(String.format("============= MEMORY USAGE (%d) ==============\n", key));
                cnt=0;
                for(float l : nfs.getMemUsages()) {
                    fw.write(String.format("[%4d] value=%f\n", ++cnt, l));
                }
            }
            fw.flush();
            fw.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void sortLinkTrafficFile(boolean isSort, HashMap<Long, LinkTrafficForSort> ltMap) {
        try {
            File file;
            if(isSort)
                file = new File("after_sort.txt");
            else
                file = new File("before_sort.txt");
            FileWriter fw = new FileWriter(file);
            for(Long key : ltMap.keySet()) {
                LinkTrafficForSort lts = ltMap.get(key);
                fw.write(String.format("============= txBitRate (%d) ==============\n", key));
                int cnt=0;
                for(Integer l : lts.getTxBitrates()) {
                    fw.write(String.format("[%4d] value=%d\n", ++cnt, l));
                }
                fw.write(String.format("============= txPktRate (%d) ==============\n", key));
                cnt=0;
                for(Integer l : lts.getTxPktrates()) {
                    fw.write(String.format("[%4d] value=%d\n", ++cnt, l));
                }
            }
            fw.flush();
            fw.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public Response optimizeRoute(LocalDate sourceDate, Long faultLink_id, Long faultNode_id) {
        reloadRoutingTable();
        settings = settingsRepository.findAll().get(0);
        runhis = optRunHisRepository.findOptRunHisBySourcedate(sourceDate);
        if(runhis==null) {
            runhis = new OptRunHis(sourceDate, settings.getOptsource(), settings.getCpulimit(), settings.getMemlimit(), settings.getLostlimit());
            runhis = optRunHisRepository.save(runhis);
        }
        else {
            runhis.setRundatetime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            optRunHisRepository.save(runhis);
            existOptRouteDelete();
        }
        interfaces = interfaceRepository.findInterfaceAll();
        ifMap = new HashMap<>();
        interfaces.forEach(intf -> {
            ifMap.put(intf.getId(), intf);
        });
        links = linkRepository.findLinkAllUsaged(faultLink_id);

        linkMap = new HashMap<>();
        links.forEach(l -> {
            linkMap.put(l.getId(), l);

        });
        boolean isFault = false;
        if(faultLink_id!=null || faultNode_id!=null) isFault = true;
        List<LinkFactorAvg> linkfactors = metricModify(sourceDate, isFault);
        //장애링크의 forward, backward링크 모두 INF설정
        if(faultLink_id!=null) {
            Link fl = linkMap.get(faultLink_id);
            if(fl==null) {
                return new Response(false, String.format("장애링크의 ID가 잘못되었습니다. 링크ID (%d)는 존재하지 않습니다.", faultLink_id), null);
            }
            linkfactors.forEach(lf -> {
                Link l = linkMap.get(lf.getLink_id());
                if((l.getSif_id()==fl.getSif_id() && l.getRif_id()==fl.getRif_id()) ||
                   (l.getRif_id()==fl.getSif_id() && l.getSif_id()==fl.getRif_id()) ) lf.setModifymetric(INF);
            });
        }
        if(faultNode_id!=null) {
            
            linkfactors.forEach(lf -> {
                Link l = linkMap.get(lf.getLink_id());
                Interface sif = ifMap.get(l.getSif_id());
                Interface rif = ifMap.get(l.getRif_id());
                if(sif.getNode_id()==faultNode_id || rif.getNode_id()==faultNode_id) lf.setModifymetric(INF);
            });
        }
        
        List<LinkForCalc> calcLinks = arraySettingForAlgorithm(sourceDate, linkfactors);
        HashMap<Long, Link> linkMapForSinterface = new HashMap<>();
        links.forEach(l -> {
            linkMapForSinterface.put(l.getSif_id(), l);
        });

        Response response = routingTableSvc.dijkstraAlgorithm(calcLinks, linkMapForSinterface);
        if(!response.isStatus()) return response;
        response = optimizedPathDbUpdate((List<Router>)response.getData(), linkfactors, faultLink_id, faultNode_id);
        return response;
    }
    public void existOptRouteDelete() {
        optRunDebugRepository.deleteOptRunDebugByRunhisId(runhis.getId());
        optRouteRepository.deleteOptRouteByRunhisId(runhis.getId());
    }
    public List<LinkFactorAvg> metricModify(LocalDate sourceDate, boolean isFault) {
        // 필요자료 DB가져오기 및 매핑테이블 만들기
        List<LinkFactorAvg> linkfactors = linkFactorAvgRepository.findLinkFactorAvgBySourcedate(sourceDate);
        List<LinkTrafficAvg> linktraffics = linkTrafficAvgRepository.findLinkTrafficAvgBySourcedate(sourceDate);
        HashMap<Long, LinkTrafficAvg> ltMap = new HashMap<>();
        linktraffics.forEach(lt -> {
            ltMap.put(lt.getIf_id(), lt);
        });
        List<NodeFactorAvg> nodefactors = nodeFactorAvgRepository.findNodeFactorAvgBySourcedate(sourceDate);
        HashMap<Long, NodeFactorAvg> nfMap = new HashMap<>();
        nodefactors.forEach(nf -> {
            nfMap.put(nf.getNode_id(), nf);
        });
        linkfactors.forEach(lf -> {

            Link l = linkMap.get(lf.getLink_id());
            if(l==null) return;
            // 장애로 인한 우회경로 산정시에는 장애링크외에는 기존 메트릭 유지
            if(isFault) lf.setModifymetric(l.getOspfmetric());

            //System.out.println(l.getId());
            LinkTrafficAvg lt = ltMap.get(l.getSif_id());
            if(settings.getOptsource().equals("AVG")) {
                lf.setLatency(lf.getLatency_avg());
                lf.setJitter(lf.getJitter_avg());
                lf.setLost(lf.getLost_avg()==null?0:lf.getLost_avg());
                if(lt==null) {
                    OptimzeDBLog(runhis.getId(), String.format("Traffic Average of Link-%d is not calculated for %s.", l.getId(), sourceDate.toString()));
                }
                else lf.setLinktrf(lt.getTxbitrate_avg()); //단위: Mbps
            }
            else {
                lf.setLatency(lf.getLatency_med());
                lf.setJitter(lf.getJitter_med());
                lf.setLost(lf.getLost_med()==null?0:lf.getLost_med());
                if(lt==null) {
                    OptimzeDBLog(runhis.getId(), String.format("Traffic Average of Link-%d is not calculated for %s.", l.getId(), sourceDate.toString()));
                }
                else lf.setLinktrf(lt.getTxbitrate_med()); //단위: Mbps
            }
        });
        if(isFault) return linkfactors; // 장애링크외에는 메트릭 조정 없음


        nodefactors.forEach(nf -> {
            if(settings.getOptsource().equals("AVG")) {
                // 측정되지 않거나 해당일 오류발생 노드는 반영하지 않음. (0)
                nf.setCpuusage(nf.getCpuusage_avg()==null?0:nf.getCpuusage_avg());
                nf.setMemusage(nf.getMemusage_avg()==null?0:nf.getMemusage_avg());
            }
            else {
                nf.setCpuusage(nf.getCpuusage_med()==null?0:nf.getCpuusage_med());
                nf.setMemusage(nf.getMemusage_med()==null?0:nf.getMemusage_med());
            }
        });

        // 전체링크 평균 구하기
        double jitterAvg = 0d;
        Integer trafficAvg = 0;
        int jcnt = 0, tcnt = 0;
        for(LinkFactorAvg lf : linkfactors) {
            if(lf.getJitter()!=null) {
                jitterAvg += lf.getJitter();
                jcnt++;
            }
            if(lf.getLinktrf()!=null) {
                trafficAvg += lf.getLinktrf();
                tcnt++;
            }
        }
        if(jcnt>0) jitterAvg = jitterAvg / jcnt;
        if(tcnt>0) trafficAvg = trafficAvg / tcnt;
        Integer metricAvg = metricAverage(links);

        linkFactorRouteAvgRepository.updateRouteAverage(sourceDate, jitterAvg, trafficAvg);

        // 메트릭값 수정하기
        for(LinkFactorAvg lf : linkfactors) {
            Link l = linkMap.get(lf.getLink_id());

            if(l==null) continue;
            lf.setModifymetric(l.getOspfmetric());
            Interface sif = ifMap.get(l.getSif_id());
            Interface rif = ifMap.get(l.getRif_id());
            NodeFactorAvg snf = nfMap.get(sif.getNode_id());
            NodeFactorAvg rnf = nfMap.get(rif.getNode_id());
            Float snode_cpuusage = 0f, snode_memusage = 0f, rnode_cpuusage = 0f, rnode_memusage = 0f;
            if(snf==null) OptimzeDBLog(runhis.getId(), String.format("Factor-average of Node-%d is not calculated for %s", sif.getNode_id(), sourceDate.toString()));
            else {
                snode_cpuusage = snf.getCpuusage();
                snode_memusage = snf.getMemusage();
            }
            if(rnf==null) OptimzeDBLog(runhis.getId(), String.format("Factor-average of Node-%d is not calculated for %s", rif.getNode_id(), sourceDate.toString()));
            else {
                rnode_cpuusage = rnf.getCpuusage();
                rnode_memusage = rnf.getMemusage();
            }
            
            if(snode_cpuusage > settings.getCpulimit()) {
                lf.setModifymetric(INF);
                OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] S-CPU: %f > %f (limit)", sif.getNode_id(), rif.getNode_id(), l.getId(), snode_cpuusage, settings.getCpulimit()));
                continue;
            }
            else if(snode_memusage > settings.getMemlimit()) {
                lf.setModifymetric(INF);
                OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] S-MEM: %f > %f (limit)", sif.getNode_id(), rif.getNode_id(), l.getId(), snode_memusage, settings.getMemlimit()));
                continue;  
            }
            else if(rnode_cpuusage > settings.getCpulimit()) {
                lf.setModifymetric(INF);
                OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] R-MEM: %f > %f (limit)", sif.getNode_id(), rif.getNode_id(), l.getId(), rnode_cpuusage, settings.getCpulimit()));
                continue;  
            }
            else if(rnode_memusage > settings.getMemlimit()) {
                lf.setModifymetric(INF);
                OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] R-MEM: %f > %f (limit)", sif.getNode_id(), rif.getNode_id(), l.getId(), rnode_memusage, settings.getMemlimit()));
                continue;  
            }
            else if(lf.getLost() > settings.getLostlimit()) {
                lf.setModifymetric(INF);
                OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] Lost: %d > %d (limit)", sif.getNode_id(), rif.getNode_id(), l.getId(), lf.getLost(), settings.getLostlimit()));
                continue;  
            }
            String speed = l.getSpeed();
            String unit = speed.substring(speed.length()-1,speed.length());
            String digit = speed.substring(0, speed.length()-1);
            if(!digit.matches("[+-]?\\d*(\\.\\d+)?")) {
                OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] speed: %s => Not Numeric.", sif.getNode_id(), rif.getNode_id(), l.getId(), l.getSpeed()));
                continue;
            }
            Integer linkSpeed = Integer.parseInt(digit);
            if(unit.equals("g")) linkSpeed = linkSpeed * 1000;
            else if(unit.equals("t")) linkSpeed = linkSpeed * 1000000;
            else if(unit.equals("m")) ;
            else {
                OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] speed: %s => traffic unit error", sif.getNode_id(), rif.getNode_id(), l.getId(), l.getSpeed()));
                continue;
            }
            if(lf.getLinktrf()!=null && lf.getLinktrf() > (linkSpeed * 0.8)) {
                lf.setModifymetric(INF);
                OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] traffic: %d > %d (80%% of Link-Speed)", sif.getNode_id(), rif.getNode_id(), l.getId(), lf.getLinktrf(), Math.round(linkSpeed * 0.8)));
                continue;
            }

            // 측정되지 않거나 해당일 오류발생 지표의 경우 평균값으로 적용
            double latencyDev = 0d, jitterDev = 0d;
            //Integer trafficDev = 0;
            double latencyDef = 0d, jitterDef = 0d;
            //Integer trafficDef = 0;
            if(lf.getLatency()!=null)
                latencyDef = (lf.getLatency() - l.getLatencystd());
            if(lf.getJitter()!=null)
                jitterDef = (lf.getJitter() - jitterAvg);
            // if(lf.getLinktrf()!=null) 
            //     trafficDef = (lf.getLinktrf() - trafficAvg);

            if(Math.abs(latencyDef) > settings.getLatencylimit()) latencyDev = latencyDef / l.getLatencystd();
            if(Math.abs(jitterDef) > settings.getJitterlimit()) jitterDev = jitterDef / jitterAvg;
            // if(Math.abs(trafficDef) > settings.getTrafficlimit()) trafficDev = trafficDef / trafficAvg;
            float addMetric = (float)((latencyDev * settings.getLatencyrefrate()) + (jitterDev * settings.getJitterrefrate())); // + (trafficDev * settings.getTrafficrefrate()));

            if(Math.abs(addMetric) <= settings.getMetriclimit()) {
                OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] 증감율: |%3.2f| < %3.2f (limit)", sif.getNode_id(), rif.getNode_id(), l.getId(), addMetric, settings.getMetriclimit()));
                lf.setModifymetric(l.getOspfmetric());
                continue;
            }
            lf.setModifymetric((int)(l.getOspfmetric() * (1 + addMetric)));
            OptimzeDBLog(runhis.getId(), String.format("메트릭변경 [%d -> %d / %d] 증감율: %3.2f (Latency: %3.2f, Jitter: %3.2f)", sif.getNode_id(), rif.getNode_id(), l.getId(), addMetric, latencyDev, jitterDev));

/*
            // 2022.10.21 평균메트릭값에서 가감조정으로 시도
            latencyDev = latencyDef / l.getLatencystd();
            jitterDev = jitterDef / jitterAvg;
            Integer modifyMetric = (int)Math.round(((latencyDev * settings.getLatencyrefrate()) + (jitterDev * settings.getJitterrefrate()) + 1) * metricAvg);
            Integer difMetric = modifyMetric - l.getOspfmetric();
            // 
            // if(Math.abs(difMetric*1.0/l.getOspfmetric()) <= settings.getMetriclimit()) {
            //     OptimzeDBLog(runhis.getId(), String.format("[%d -> %d / %d] 증감율: |%3.2f| < %3.2f (limit)", sif.getNode_id(), rif.getNode_id(), l.getId(), difMetric*1.0/l.getOspfmetric(), settings.getMetriclimit()));
            //     lf.setModifymetric(l.getOspfmetric());
            //     continue;
            // }
            lf.setModifymetric(modifyMetric);
            OptimzeDBLog(runhis.getId(), String.format("메트릭변경 [%d -> %d / %d] 증감율: %3.2f (Latency: %3.2f, Jitter: %3.2f)", sif.getNode_id(), rif.getNode_id(), l.getId(), difMetric*1.0/l.getOspfmetric(), latencyDev, jitterDev));
            // 새로운 시도 끝
*/            

        }

        return linkfactors;
    }
    public List<LinkForCalc> arraySettingForAlgorithm(LocalDate sourceDate, List<LinkFactorAvg> linkfactors) {
        List<LinkForCalc> calcLinks = new ArrayList<>();
        HashMap<Long, LinkForCalc> calcLinkMap = new HashMap<>();
        HashMap<Long, LinkFactorAvg> lfMap = new HashMap<>();
        linkfactors.forEach(lf -> {
            lfMap.put(lf.getLink_id(), lf);
        });
        links.forEach(l -> {
            LinkFactorAvg lf = lfMap.get(l.getId());

            // 측정된 링크는 수정메트릭 측정되지 않은 링크는 OSPF메트릭 적용
            int metric = l.getOspfmetric();
            if(lf!=null && lf.getModifymetric()!=null) metric = lf.getModifymetric();

            Interface sif = ifMap.get(l.getSif_id());
            Interface rif = ifMap.get(l.getRif_id());
            long hwif_id = sif.getId();
            if(sif.getHwif_id()!=null) hwif_id = sif.getHwif_id();
            //metricModify(runhis, l, lf, snf, rnf, lt);
            LinkForCalc calcLink = calcLinkMap.get(sif.getNode_id());
            if(calcLink==null) {
                calcLink = new LinkForCalc(sif.getNode_id());
                calcLinkMap.put(sif.getNode_id(), calcLink);
                calcLinks.add(calcLink);
            }
            calcLink.getAdjRouters().add(new AdjRouter(rif.getNode_id(), sif.getId(), hwif_id, metric));
        });
        return calcLinks;
    }
    public void OptimzeDBLog(long runhis_id, String message) {
        optRunDebugRepository.save(new OptRunDebug(runhis_id, message));
    }
    public Integer metricAverage(List<Link> links) {
        List<Integer> metrics = new ArrayList<>();
        links.forEach(l -> {
            metrics.add(l.getOspfmetric());
        });
        Collections.sort(metrics);
        int fivePercent = (int)(metrics.size() * 0.05); // 중간90%의 평균값
        if(fivePercent==0 && metrics.size()>=3) fivePercent = 1;
        Integer metricAvg = 0;
        for(int i=fivePercent; i<metrics.size()-fivePercent; i++) {
            metricAvg += metrics.get(i);
        }
        metricAvg = metricAvg/(metrics.size()-2*fivePercent);
        return metricAvg;
    }
    public Response optimizedPathDbUpdate(List<Router> routers, List<LinkFactorAvg> linkfactors, Long faultLink_id, Long faultNode_id) {
        List<E2eNode> e2enodes = e2eNodeRepository.findAllE2eNodeUsaged();
        List<OptRoute> optRoutes = new ArrayList<>();
        List<OspfRoute> ospfRoutes = ospfRouteRepository.findAll();
        HashMap<Long, OspfRoute> mapOspfRoute = new HashMap<>();
        ospfRoutes.forEach(ospf -> {
            mapOspfRoute.put(ospf.getE2enode_id(), ospf);
        });
        // List<RoutePath> routePaths = routePathRepository.findAll(Sort.by("pathser"));
        List<RoutePathSet> routePathSets = routePathSetRepository.findAll();
        HashMap<Long, RoutePathSet> mapRoutePathSets = new HashMap<>();
        // routePaths.forEach(rp -> {
        //     List<RoutePath> rplist = mapRoutePathSets.get(rp.getRoutepathset_id());
        //     if(rplist==null) {
        //         rplist = new ArrayList<>();
        //         mapRoutePathSets.put(rp.getRoutepathset_id(), rplist);
        //     }
        //     rplist.add(rp);
        // });
        routePathSets.forEach(rps -> {
            mapRoutePathSets.put(rps.getId(), rps);
        });
        HashMap<Long, Router> routerMap = new HashMap<>();
        routers.forEach(r -> {
            routerMap.put(r.getNode_id(), r);
        });
        HashMap<Long, LinkFactorAvg> lfMap = new HashMap<>();
        linkfactors.forEach(lf -> {
            lfMap.put(lf.getLink_id(), lf);
        });


        /* 장애링크(노드)에 대한 경로산정인 경우도 바뀐 경로는 모두 적용 2022.10.18
        // 장애링크의 forward, backward링크를 거쳐가는 OSPF경로의 E2E에 대해서만 저장
        HashMap<Long, Boolean> mapOptE2e = new HashMap<>();
        if(faultLink_id!=null) {
            Link fl = linkMap.get(faultLink_id);
            ospfRoutes.forEach(or -> {
                RoutePathSet rps = or.getRoutePathSet();
                rps.getRoutePaths().forEach(rp -> {
                    Link l = linkMap.get(rp.getLink_id());
                    if((fl.getSif_id()==l.getSif_id() && fl.getRif_id()==l.getRif_id()) ||
                        fl.getRif_id()==l.getSif_id() && fl.getSif_id()==l.getRif_id()) mapOptE2e.put(or.getE2enode_id(), true);
                });
            });
        }
        // 장애노드를 거쳐가는 OSPF경로의 E2E에 대해서만 저장
        if(faultNode_id!=null) {
            ospfRoutes.forEach(or -> {
                RoutePathSet rps = or.getRoutePathSet();
                rps.getRoutePaths().forEach(rp -> {
                    Link l = linkMap.get(rp.getLink_id());
                    Interface sif = ifMap.get(l.getSif_id());
                    Interface rif = ifMap.get(l.getRif_id());
                    if(sif.getNode_id()==faultNode_id || rif.getNode_id()==faultNode_id) mapOptE2e.put(or.getE2enode_id(), true);
                });
            });
        }
        */
        for(E2eNode en : e2enodes) {
            /* 장애링크(노드)에 대한 경로산정인 경우도 바뀐 경로는 모두 적용 2022.10.18
            Boolean isSave = mapOptE2e.get(en.getId()); 
            if((faultLink_id!=null || faultNode_id!=null) && (isSave==null || !isSave)) continue;
            */
            Router r = routerMap.get(en.getSnode_id());
            if(r==null) continue;
            DistanceCost d = r.getDestMap().get(en.getRnode_id());
            if(d==null) continue;
            int pathser = 0;
            List<RoutePath> opt_rplist = new ArrayList<>();
            boolean isFault = false;
            for(Path path : d.getPaths()) {
                Link l = path.getLink();

                /* 장애링크(노드)에 대한 경로산정인 경우도 바뀐 경로는 모두 적용 2022.10.18
                if(faultNode_id!=null) {
                    Interface sif = ifMap.get(l.getSif_id());
                    Interface rif = ifMap.get(l.getRif_id());
                    if(sif.getNode_id()==faultNode_id || rif.getNode_id()==faultNode_id) isFault = true;
                }
                if(faultLink_id!=null) {
                    if(l.getId()==faultLink_id) isFault = true;
                }
                */
                if(isFault) continue;
                RoutePath rp = new RoutePath(++pathser, l.getId());
                opt_rplist.add(rp);
            }
            if(isFault) continue; //산정루트에 장애노드/링크가 포함되어 있으면 SKIP
            if(opt_rplist.size()==0) continue;
            // 기존 루트경로세트에 있는지 확인
            RoutePathSet rps = routingTableSvc.findRoutePathSet(opt_rplist, mapRoutePathSets);
            if(rps==null) { //없으면 새로 추가
                rps = new RoutePathSet(opt_rplist);
                routePathSets.add(rps);
            }
            // 최적경로가 OSPF경로와 다른지 확인하여 다른 경우에만 저장
            OspfRoute ospf = mapOspfRoute.get(en.getId());
            if(ospf==null) {
                OptimzeDBLog(runhis.getId(), String.format("Ospf path is not exist in E2E [%d -> %d].", en.getSnode_id(), en.getRnode_id()));
                continue;
            }
            if(ospf.getRoutePathSet().getId()!=rps.getId()) {
                OptRoute or = new OptRoute(runhis.getId(), en.getId());
                or.setRoutePathSet(rps);
                optRoutes.add(or);
            }
            else { // 경로변경이 없는 경우에는 메트릭은 기존 메트릭으로 원복
                rps.getRoutePaths().forEach(rp -> {
                    Link l =  linkMap.get(rp.getLink_id());
                    LinkFactorAvg lf = lfMap.get(rp.getLink_id());
                    if(lf!=null) lf.setModifymetric(l.getOspfmetric());
                });
            }
        }
        routePathSetRepository.saveAll(routePathSets);
        optRouteRepository.saveAll(optRoutes);
        linkfactors.forEach(lf -> {
            linkFactorAvgRepository.updateMetricOfLinkFactorAvgById(lf.getId(), lf.getModifymetric());
        });

        // 현재의 OSPF 스냅샷
        optRouteRepository.snapshotOspf(runhis.getId());

        return new Response(true, "Route Optimize Complete.", null);
    }
    public Response optimizedPathCheck(Boolean isFactors, Boolean isInterface, LocalDate sourceDate) {

        List<RoutePathSet> routePathSets = routePathSetRepository.findAll();
        HashMap<Long, RoutePathSet> mapRps = new HashMap<>();
        routePathSets.forEach(rps -> {
            mapRps.put(rps.getId(), rps);
        });

        Query qry= em.createNativeQuery("call findE2eNodeInfo(); ", E2eNodeInfo.class);
        List<E2eNodeInfo> e2enodes = qry.getResultList();
        //HashMap<Long, E2eNodeInfo> mapE2e = new HashMap<>();
        HashMap<String, E2eNodeInfo> mapE2eNodename = new HashMap<>();

        e2enodes.forEach(e2e -> {
            // mapE2e.put(e2e.getId(), e2e);
            String strE2e = String.format("%d -> %d",e2e.getSnode_id(), e2e.getRnode_id());
            mapE2eNodename.put(strE2e, e2e);
        });

      

        qry= em.createNativeQuery("call findLinkInfo(); ", LinkInfo.class);
        List<LinkInfo> links = qry.getResultList();
        HashMap<Long, LinkInfo> mapLink = new HashMap<>();
        links.forEach(l -> {
            mapLink.put(l.getId(), l);
        });

        qry= em.createNativeQuery("call findChangedRoute(:sourcedate); ", ChangedRoute.class)
                .setParameter("sourcedate",sourceDate);
        List<ChangedRoute> changedRoutes = qry.getResultList();
        HashMap<Long, AnalysisOptRoute> mapOptRoute = new HashMap<>();

        HashMap<Long, AnalysisE2e> mapAnalE2e = new HashMap<>();
        List<AnalysisE2e> analE2es = new ArrayList<>();
        for(ChangedRoute cr : changedRoutes) {
            AnalysisOptRoute forward = mapOptRoute.get(cr.getE2enode_id());
            // String strE2e = String.format("%d -> %d", cr.getRnode_id(), cr.getSnode_id());
            // E2eNodeInfo opposite_e2e = mapE2eNodename.get(strE2e);
            // if(opposite_e2e==null) log.info("E2E " + strE2e + " is not exist.");
            // AnalysisOptRoute backward = mapOptRoute.get(opposite_e2e.getId());
            if(forward==null) {
                forward = new AnalysisOptRoute(cr.getSnode_id(), cr.getSnodename(), cr.getRnode_id(), cr.getRnodename());
                mapOptRoute.put(cr.getE2enode_id(), forward);
                // analRoutes.add(aor);
                RoutePathSet ospf_rps = mapRps.get(cr.getOspf_routepathset_id());
                for(RoutePath rp : ospf_rps.getRoutePaths()) {
                    LinkInfo li = mapLink.get(rp.getLink_id());
                    AnalysisPath ap = new AnalysisPath(rp.getPathser(), rp.getLink_id(), li.getSnode_id(), li.getSnodename(), li.getRnode_id(), li.getRnodename());
                    forward.getOspfPaths().add(ap);
                }
                AnalysisE2e anale2e = new AnalysisE2e(String.format("%s <-> %s", cr.getSnodename(), cr.getRnodename()));
                analE2es.add(anale2e);
                anale2e.setForward(forward);
                mapAnalE2e.put(cr.getE2enode_id(), anale2e);
            }
            RoutePathSet opt_rps = mapRps.get(cr.getOpt_routepathset_id());
            AnalysisOptPathSet aops = new AnalysisOptPathSet(cr.getOptcnt());
            forward.getOptimized_path().add(aops);
            for(RoutePath rp : opt_rps.getRoutePaths()) {
                LinkInfo li = mapLink.get(rp.getLink_id());
                AnalysisPath ap = new AnalysisPath(rp.getPathser(), rp.getLink_id(), li.getSnode_id(), li.getSnodename(), li.getRnode_id(), li.getRnodename());
                aops.getOptPaths().add(ap);
            }
        }
        
        // 링크의 월별 산정지표 시작
        HashMap<Long, AnalysisLink> mapAnalLink = new HashMap<>();
        if(isFactors) {
            qry = em.createNativeQuery("call checkChangedRouteFactors(:sourcedate);", ChangedRouteFactors.class)
                        .setParameter("sourcedate", sourceDate);
            List<ChangedRouteFactors> changedRouteFactors = qry.getResultList();
            changedRouteFactors.forEach(crf -> {
                if(crf.getOspfmetric()==crf.getModifymetric()) return; //메트릭변화가 없으면 볼 필요가 없잖아
                AnalysisLink al = mapAnalLink.get(crf.getLink_id());
                LinkInfo li = mapLink.get(crf.getLink_id());
                if(li==null) return; // 링크테이블에 없는 링크ID? 그럴리는 없겠지만..
                if(al==null) {
                    al = new AnalysisLink(crf.getLink_id(), li.getSnodename() + " -> " + li.getRnodename());
                    mapAnalLink.put(crf.getLink_id(), al);
                }
                boolean isExist = false;
                for(AnalysisFactors af: al.getFactors()) {
                    if(af.getSourceDate().compareTo(crf.getSourcedate())==0) {
                        isExist = true;
                        break;
                    }
                }
                if(isExist) return;

                double latency = 0, jitter = 0;
                Integer traffic = 0;
                if(crf.getOptsource().equals("AVG")) {
                    latency = crf.getLatency_avg();
                    jitter = crf.getJitter_avg();
                    traffic = crf.getTraffic_avg();
                }
                else {
                    latency = crf.getLatency_med();
                    jitter = crf.getJitter_med();
                    traffic = crf.getTraffic_med();
                }
                al.getFactors().add(
                    new AnalysisFactors(
                        crf.getSourcedate(), 
                        crf.getLatency_standard(), latency,
                        crf.getJitter_route_avg(), jitter, 
                        crf.getTraffic_route_avg(), traffic, 
                        crf.getOspfmetric(), crf.getModifymetric()
                    )
                );
            });
        } //월별 산정지표 할당 끝

        e2eBackwardAssign(analE2es, mapE2eNodename, mapAnalE2e, mapRps, mapLink);

        analE2es.forEach(ae2e -> {
            for(int fb=0; fb<2; fb++) {
                AnalysisOptRoute aor;
                if(fb==0) aor = ae2e.getForward();
                else aor = ae2e.getBackward();
                Collections.sort(aor.getOspfPaths());
                for(int i=0; i<aor.getOspfPaths().size(); i++) {
                    AnalysisPath ap = aor.getOspfPaths().get(i);
                    if(isFactors) {
                        AnalysisLink al = mapAnalLink.get(ap.getLink_id());
                        if(al!=null) aor.getLinkFactors().add(al);
                    }
                    String send = "";
                    String receive = "";
                    if(i==0)
                        send = ap.getSendNodeName();
                    else {
                        send = aor.getOspf_path();

                        AnalysisPath prev = aor.getOspfPaths().get(i-1);
                        if(prev.getReceiveNode_id()!=ap.getSendNode_id()) {
                            log.info(String.format("OSPF [%s] Path Order Error. : (%d) %s, (%d) %s", aor.getDirection(), prev.getPathser(), prev.getPaths(), ap.getPathser(), ap.getPaths()));
                            break;
                        }
                    }
                    receive = ap.getReceiveNodeName();
                    if(isInterface) {
                        LinkInfo li = mapLink.get(ap.getLink_id());
                        if(i==0) {
                            send = send + ":" + li.getSifname();
                            if(li.getVsif_id()!=null)
                                send = send + "(" + li.getVsifname() + ")";
                        }
                        else {
                            send = send + "," + li.getSifname();
                            if(li.getVsif_id()!=null)
                                send = send + "(" + li.getVsifname() + ")";
                        }

                        receive = receive + ":" + li.getRifname();
                    }
                    aor.setOspf_path(send + " -> " + receive);
                }

                if(aor.getOptimized_path()==null) continue;
                aor.getOptimized_path().forEach(aop -> {
                    Collections.sort(aop.getOptPaths());
                    for(int i=0; i<aop.getOptPaths().size(); i++) {
                        AnalysisPath ap = aop.getOptPaths().get(i);

                        String send = "";
                        String receive = "";
                        if(i==0)
                            send = ap.getSendNodeName();
                        else {
                            send = aop.getPaths();
    
                            AnalysisPath prev = aop.getOptPaths().get(i-1);
                            if(prev.getReceiveNode_id()!=ap.getSendNode_id()) {
                                log.info(String.format("Optimize [%s] Path Order Error. : (%d) %s, (%d) %s", aor.getDirection(), prev.getPathser(), prev.getPaths(), ap.getPathser(), ap.getPaths()));
                                break;
                            }
                        }
                        receive = ap.getReceiveNodeName();
                        if(isInterface) {
                            LinkInfo li = mapLink.get(ap.getLink_id());
                            if(i==0) {
                                send = send + ":" + li.getSifname();
                                if(li.getVsif_id()!=null)
                                    send = send + "(" + li.getVsifname() + ")";
                            }
                            else {
                                send = send + "," + li.getSifname();
                                if(li.getVsif_id()!=null)
                                    send = send + "(" + li.getVsifname() + ")";
                            }
    
                            receive = receive + ":" + li.getRifname();
                        }
                        aop.setPaths(send + " -> " + receive);
                    }
                });
            }
        });

        symmetricCheck(analE2es);

        return new Response(true, "Changed Route Read Complete.", analE2es);
    }

    public Response findLinkfactors(long link_id, LocalDate measuredDate) {
        Query qry = em.createNativeQuery("call findLinkFactors1Link1Day(:link_id, :sourcedate);")
                .setParameter("link_id", link_id)
                .setParameter("sourcedate", measuredDate);
        List<Object[]> results = qry.getResultList();
        List<String> factors = new ArrayList<>();
        factors.add(String.format("%-19s %11s %11s %-6s %6s %6s", "측정일자", "Latency(ms)", "Jitter(ms)", "보정", "Lost", "Hops"));
        factors.add("===================================================================");
        for(Object[] row : results) {
            factors.add(String.format("%-19s %11.4f %11.4f %6s %6s %6s", row[1].toString(), Float.parseFloat(row[2].toString()), Float.parseFloat(row[3].toString()), row[4].toString(), row[5].toString(), row[6].toString()));
        }
        return new Response(true, "The response is complete for requested link factors of the date.", factors);
    }
    public void e2eBackwardAssign(List<AnalysisE2e> analE2es, HashMap<String, E2eNodeInfo> mapE2eNodename, HashMap<Long, AnalysisE2e> mapAnalE2e, HashMap<Long, RoutePathSet> mapRps, HashMap<Long, LinkInfo> mapLink) {
        List<OspfRouteOnly> ospfRoutes = ospfRouteOnlyRepository.findAll();
        HashMap<Long, OspfRouteOnly> mapOspfRoute = new HashMap<>();
        ospfRoutes.forEach(or -> {
            mapOspfRoute.put(or.getE2enode_id(), or);
        });


        for(int i=0; i<analE2es.size(); i++) {
            AnalysisE2e analE2e = analE2es.get(i);
            if(analE2e.isRemove()) continue;
            AnalysisOptRoute forward = analE2e.getForward();
            String strE2e = String.format("%d -> %d", forward.getDestNode_id(), forward.getSrcNode_id());
            E2eNodeInfo opposite_e2e = mapE2eNodename.get(strE2e);
            if(opposite_e2e==null) log.info("E2E " + strE2e + " is not exist.");
            AnalysisE2e bwAnalE2e = mapAnalE2e.get(opposite_e2e.getId());
            AnalysisOptRoute backward;
            if(bwAnalE2e==null) {
                backward = new AnalysisOptRoute(opposite_e2e.getSnode_id(), opposite_e2e.getSnodename(), opposite_e2e.getRnode_id(), opposite_e2e.getRnodename());
                OspfRouteOnly or = mapOspfRoute.get(opposite_e2e.getId());
                if(or==null) log.info("OSPF E2E " + opposite_e2e.getSnodename() + " -> " + opposite_e2e.getRnodename() + " is not exist.");
                RoutePathSet ospf_rps = mapRps.get(or.getRoutepathset_id());
                for(RoutePath rp : ospf_rps.getRoutePaths()) {
                    LinkInfo li = mapLink.get(rp.getLink_id());
                    AnalysisPath ap = new AnalysisPath(rp.getPathser(), rp.getLink_id(), li.getSnode_id(), li.getSnodename(), li.getRnode_id(), li.getRnodename());
                    backward.getOspfPaths().add(ap);
                }
            }
            else {
                backward = bwAnalE2e.getForward();
                mapAnalE2e.remove(opposite_e2e.getId());
                bwAnalE2e.setRemove(true);
            }
            analE2e.setBackward(backward);
         }

         for(int i=0; i<analE2es.size(); i++) {
            if(analE2es.get(i).isRemove()) {
                analE2es.remove(i);
                i--;
            }
         }
    }
    public void symmetricCheck(List<AnalysisE2e> analE2es) {
        analE2es.forEach(e2e -> {
            if(e2e.getForward().getOspfPaths().size()!=e2e.getBackward().getOspfPaths().size()) {
                e2e.setOspf_path_symmetric(false);
            }
            else {
                String fwd = "";
                String bwdReverse = "";
                for(int i=0; i<e2e.getForward().getOspfPaths().size(); i++) {
                    AnalysisPath fwpath = e2e.getForward().getOspfPaths().get(i);
                    AnalysisPath bwpath = e2e.getBackward().getOspfPaths().get(i);

                    if(i==0) {
                        fwd = String.format("%d -> %d", fwpath.getSendNode_id(), fwpath.getReceiveNode_id());
                        bwdReverse = String.format("%d -> %d", bwpath.getReceiveNode_id(), bwpath.getSendNode_id());
                    }
                    else {
                        fwd = String.format("%s -> %d", fwd, fwpath.getReceiveNode_id());
                        bwdReverse = String.format("%d -> %s", bwpath.getReceiveNode_id(), bwdReverse);
                    }
                }
                if(!fwd.equals(bwdReverse)) {
                    e2e.setOspf_path_symmetric(false);
                }
            }

            if(e2e.getBackward().getOptimized_path()==null) return;

            e2e.getForward().getOptimized_path().forEach(optRps -> {
                String fwd = "";
                for(int i=0; i<optRps.getOptPaths().size(); i++) {
                    AnalysisPath ap = optRps.getOptPaths().get(i);
                    if(i==0) {
                        fwd = String.format("%d -> %d", ap.getSendNode_id(), ap.getReceiveNode_id());
                    }
                    else {
                        fwd = String.format("%s -> %d", fwd, ap.getReceiveNode_id());
                    }
                }

                List<AnalysisOptPathSet> bwdRpsList = e2e.getBackward().getOptimized_path();
                if(bwdRpsList==null) return;
                String bwdReverse = "";
                for(AnalysisOptPathSet bwdRps : bwdRpsList) {
                    for(int i=0; i<bwdRps.getOptPaths().size(); i++) {
                        AnalysisPath ap = bwdRps.getOptPaths().get(i);
                        if(i==0) {
                            bwdReverse = String.format("%d -> %d", ap.getReceiveNode_id(), ap.getSendNode_id());
                        }
                        else {
                            bwdReverse = String.format("%d -> %s", ap.getReceiveNode_id(), bwdReverse);
                        }
                    }
                }
                if(fwd.equals(bwdReverse)) e2e.setOptimized_path_symmetric(true);
            });
        });
    }

    public Response configOptimizedPath(LocalDate sourceDate) {
        Settings settings = settingsRepository.findAll().get(0);
        if(!settings.getConfigmetric()) return new Response(false, "변경루트의 실망 적용은 운용자와 협의부탁합니다.", null);
        LocalDateTime configDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Query qry = em.createNativeQuery("call findChangedMetricLink(:sourcedate);", DbChangedMetric.class)
                .setParameter("sourcedate", sourceDate);
        List<DbChangedMetric> dbChangedMetrics = qry.getResultList();
        JSONObject jBody = requestMetricModify(dbChangedMetrics, false);

        if(jBody.getBoolean("status")) {
            ConfigMetricHis configMetricHis = new ConfigMetricHis();
            dbChangedMetrics.forEach(dbrow -> {
                configMetricHis.setRunhis_id(dbrow.getRunhis_id());
                configMetricHis.getLinkMetricHistories().add(new LinkMetricHis(dbrow.getLink_id(), dbrow.getOrgmetric(), dbrow.getModifymetric()));
            });
            configMetricHis.setConfigdatetime(configDateTime);
            configMetricHisRepository.deleteConfigMetricHisByRunhisId(configMetricHis.getRunhis_id());
            configMetricHisRepository.save(configMetricHis);

            optRouteRepository.updateOptimizedRouteApply(configMetricHis.getRunhis_id(), true);
            reloadRoutingTable();
        }

        return new Response(jBody.getBoolean("status"), jBody.getString("message"), null);
    }
    public Response configMetricRestore(LocalDate sourceDate) {
        Settings settings = settingsRepository.findAll().get(0);
        if(!settings.getConfigmetric()) return new Response(false, "변경루트의 실망 적용은 운용자와 협의부탁합니다.", null);
        Query qry = em.createNativeQuery("call findConfigMetricHistory(:sourcedate);", DbChangedMetric.class)
                .setParameter("sourcedate", sourceDate);
        List<DbChangedMetric> dbChangedMetrics = qry.getResultList();
        if(dbChangedMetrics.size()<=0) return new Response(false, "No Config-data to restore.", null);
        JSONObject jBody = requestMetricModify(dbChangedMetrics, true);
        if(jBody.getBoolean("status")) {
            configMetricHisRepository.deleteConfigMetricHisByRunhisId(dbChangedMetrics.get(0).getRunhis_id());

            optRouteRepository.updateOptimizedRouteApply(dbChangedMetrics.get(0).getRunhis_id(), false);
            reloadRoutingTable();
        }

        return new Response(jBody.getBoolean("status"), jBody.getString("message"), null);
    }
    public JSONObject requestMetricModify(List<DbChangedMetric> dbChangedMetrics, boolean isRestore) {
        
        List<RequestMetricConfig> reqMetrics = new ArrayList<>();
        HashMap<Long, RequestMetricConfig> mapReqMeric = new HashMap<>();
        dbChangedMetrics.forEach(dbrow -> {
            RequestMetricConfig reqMetric = mapReqMeric.get(dbrow.getSnode_id());
            if(reqMetric==null) {
                reqMetric = new RequestMetricConfig(dbrow.getSnode_id());
                mapReqMeric.put(dbrow.getSnode_id(), reqMetric);
                reqMetrics.add(reqMetric);
            }
            String ifname = dbrow.getSifname();
            if(dbrow.getVif_id()!=null) {
                ifname = dbrow.getVifname();
            }
            Integer metric = dbrow.getModifymetric();
            if(isRestore) metric = dbrow.getOrgmetric();
            reqMetric.getInterfaces().add(new MetricInterface(ifname, metric));
        });
        return netconfMetricModify(reqMetrics);
    }
    public JSONObject netconfMetricModify(List<RequestMetricConfig> reqMetrics) {
        JSONObject jBody = new JSONObject();
        try {
            jBody = restSvc.requestToRestServer(restSvc.getHostAddrPort() + "/ipsdn/services/config/metric", "POST", reqMetrics);
        }
        catch(Exception e) {
            jBody.put("status", false);
            jBody.put("message", String.format("Metric Config Error => %s", e.getMessage()));
            return jBody;
        }
        return jBody;
    }
    public Response requestNetconfMetricModify(List<RequestMetricConfig> reqMetrics) {
        JSONObject jBody = netconfMetricModify(reqMetrics);
        return new Response(jBody.getBoolean("status"), jBody.getString("message"), null);
    }
    public void reloadRoutingTable() {
        List<String> cmd = new ArrayList<>();
        cmd.add("show ip ospf database json");
        ShellCommand shcmd = new ShellCommand("frr", cmd, 5000);
        routingTableSvc.collectRoutingTable(shcmd);
        routingTableSvc.calculateOspfPath();
    }

    public Response getCurrentPath(Long e2enode_id) {
        List<OspfRouteForNms> curRoutes = null;
        if(e2enode_id==null) curRoutes = ospfRouteForNmsRepository.findAll();
        else curRoutes = ospfRouteForNmsRepository.findOspfRouteByE2eNodeId(e2enode_id);
        HashMap<Long, OspfRouteForNms> mapRoutes = new HashMap<>();
        curRoutes.forEach(cr -> {
            mapRoutes.put(cr.getRoutepathset_id(), cr);
        });
        Query qry = em.createNativeQuery("call findOspfRoutePath(:e2enode_id);", RoutePathForNms.class)
                .setParameter("e2enode_id", e2enode_id);

        List<RoutePathForNms> curPaths = qry.getResultList();
        curPaths.forEach(cp -> {
            OspfRouteForNms route = mapRoutes.get(cp.getRoutepathset_id());
            if(route==null) return;
            route.getCurrent_paths().add(cp);
        });

        return new Response(true, "Current Route Paths Read Complete.", curRoutes);
    }
    public Response getOptimizedPath(Long e2enode_id, LocalDate firstDate, LocalDate lastDate) {
        Query qry = em.createNativeQuery("call findOptRouteForNms(:e2enode_id, :firstdate, :lastdate);", DbOptRouteForNms.class)
                .setParameter("e2enode_id", e2enode_id)
                .setParameter("firstdate", firstDate)
                .setParameter("lastdate", lastDate);
        List<DbOptRouteForNms> dbOptRoutes = qry.getResultList();

        HashMap<Long, OptRouteForNms> mapOptRoutes = new HashMap<>();
        List<OptRouteForNms> optRoutes = new ArrayList<>();
        dbOptRoutes.forEach(dbrow -> {
            OptRouteForNms or = mapOptRoutes.get(dbrow.getE2enode_id());
            if(or==null) {
                or = new OptRouteForNms(dbrow.getE2enode_id());
                optRoutes.add(or);
                mapOptRoutes.put(or.getE2enode_id(), or);
            }
            OptPathForNms op = or.getMapOptPath().get(dbrow.getRoutepathset_id());
            if(op==null) {
                op = new OptPathForNms(dbrow.getRoutepathset_id(), dbrow.getCount(), dbrow.getUsaged());
                or.getOptimized_paths().add(op);
                or.getMapOptPath().put(op.getRoutepathset_id(), op);
            }
            op.getPaths().add(
                new RoutePathForNms(
                    dbrow.getPathser(),
                    dbrow.getLink_id(),
                    dbrow.getSend_node_id(),
                    dbrow.getSend_node_name(),
                    dbrow.getSend_interface_id(),
                    dbrow.getSend_interface_name(),
                    dbrow.getReceive_node_id(),
                    dbrow.getReceive_node_name(),
                    dbrow.getReceive_interface_id(),
                    dbrow.getReceive_interface_name(),
                    dbrow.getSend_strresid(),
                    dbrow.getSend_strifid(),
                    dbrow.getReceive_strresid(),
                    dbrow.getReceive_strifid()
                )
            );
        });

        return new Response(true, "Optimized Route Path Read Complete.", optRoutes);
    }

    public Response optimizedPathVerify(LocalDate sourceDate) {

        List<RoutePathSet> routePathSets = routePathSetRepository.findAll();
        HashMap<Long, RoutePathSet> mapRps = new HashMap<>();
        routePathSets.forEach(rps -> {
            mapRps.put(rps.getId(), rps);
        });

        Query qry= em.createNativeQuery("call findE2eNodeInfo(); ", E2eNodeInfo.class);
        List<E2eNodeInfo> e2enodes = qry.getResultList();
        //HashMap<Long, E2eNodeInfo> mapE2e = new HashMap<>();
        HashMap<String, E2eNodeInfo> mapE2eNodename = new HashMap<>();

        e2enodes.forEach(e2e -> {
            // mapE2e.put(e2e.getId(), e2e);
            String strE2e = String.format("%d -> %d",e2e.getSnode_id(), e2e.getRnode_id());
            mapE2eNodename.put(strE2e, e2e);
        });

      

        qry= em.createNativeQuery("call findLinkInfo(); ", LinkInfo.class);
        List<LinkInfo> links = qry.getResultList();
        HashMap<Long, LinkInfo> mapLink = new HashMap<>();
        links.forEach(l -> {
            mapLink.put(l.getId(), l);
        });

        List<OspfRouteForNms> curRoutes = ospfRouteForNmsRepository.findAll();
        HashMap<Long, OspfRouteForNms> mapCurRoutes = new HashMap<>();
        curRoutes.forEach(cr -> {
            mapCurRoutes.put(cr.getE2enode_id(), cr);
            RoutePathSet cur_rps = mapRps.get(cr.getRoutepathset_id());
            for(RoutePath rp : cur_rps.getRoutePaths()) {
                LinkInfo li = mapLink.get(rp.getLink_id());
                RoutePathForNms rpn = new RoutePathForNms(
                    rp.getPathser(), rp.getLink_id(), li.getSnode_id(), li.getSnodename(), li.getSif_id(), li.getSifname(),
                     li.getRnode_id(), li.getRnodename(), li.getRif_id(), li.getRifname(), li.getSend_strresid(), li.getSend_strifid(), li.getReceive_strresid(), li.getReceive_strifid());
                cr.getCurrent_paths().add(rpn);
            }
        });

        qry= em.createNativeQuery("call findChangedRoute(:sourcedate); ", ChangedRoute.class)
                .setParameter("sourcedate",sourceDate);
        List<ChangedRoute> changedRoutes = qry.getResultList();
        HashMap<Long, VerifyOptRoute> mapOptRoute = new HashMap<>();

        List<VerifyOptRoute> verifyOptRoutes = new ArrayList<>();
        for(ChangedRoute cr : changedRoutes) {
            VerifyOptRoute vrte = mapOptRoute.get(cr.getE2enode_id());
            // String strE2e = String.format("%d -> %d", cr.getRnode_id(), cr.getSnode_id());
            // E2eNodeInfo opposite_e2e = mapE2eNodename.get(strE2e);
            // if(opposite_e2e==null) log.info("E2E " + strE2e + " is not exist.");
            // AnalysisOptRoute backward = mapOptRoute.get(opposite_e2e.getId());
            if(vrte==null) {
                vrte = new VerifyOptRoute(cr.getE2enode_id(), cr.getSnode_id(), cr.getSnodename(), cr.getRnode_id(), cr.getRnodename());
                mapOptRoute.put(cr.getE2enode_id(), vrte);
                // analRoutes.add(aor);
                RoutePathSet ospf_rps = mapRps.get(cr.getOspf_routepathset_id());
                for(RoutePath rp : ospf_rps.getRoutePaths()) {
                    LinkInfo li = mapLink.get(rp.getLink_id());
                    AnalysisPath ap = new AnalysisPath(rp.getPathser(), rp.getLink_id(), li.getSnode_id(), li.getSnodename(), li.getRnode_id(), li.getRnodename());
                    vrte.getOspfPaths().add(ap);
                }
                verifyOptRoutes.add(vrte);
            }
            RoutePathSet opt_rps = mapRps.get(cr.getOpt_routepathset_id());
            AnalysisOptPathSet aops = new AnalysisOptPathSet(cr.getOptcnt());
            vrte.getOptimized_path().add(aops);
            for(RoutePath rp : opt_rps.getRoutePaths()) {
                LinkInfo li = mapLink.get(rp.getLink_id());
                AnalysisPath ap = new AnalysisPath(rp.getPathser(), rp.getLink_id(), li.getSnode_id(), li.getSnodename(), li.getRnode_id(), li.getRnodename());
                aops.getOptPaths().add(ap);
            }
        }
        

        verifyOptRoutes.forEach(vrte -> {

            Collections.sort(vrte.getOspfPaths());
            for(int i=0; i<vrte.getOspfPaths().size(); i++) {
                AnalysisPath ap = vrte.getOspfPaths().get(i);
                String send = "";
                String receive = "";
                if(i==0)
                    send = ap.getSendNodeName();
                else {
                    send = vrte.getPrevious_path();

                    AnalysisPath prev = vrte.getOspfPaths().get(i-1);
                    if(prev.getReceiveNode_id()!=ap.getSendNode_id()) {
                        log.info(String.format("OSPF [%s] Path Order Error. : (%d) %s, (%d) %s", vrte.getE2e(), prev.getPathser(), prev.getPaths(), ap.getPathser(), ap.getPaths()));
                        break;
                    }
                }
                receive = ap.getReceiveNodeName();
                vrte.setPrevious_path(send + " -> " + receive);
            }
            OspfRouteForNms cur_rte = mapCurRoutes.get(vrte.getE2enode_id());
            Collections.sort(cur_rte.getCurrent_paths());
            for(int i=0; i<cur_rte.getCurrent_paths().size(); i++) {
                RoutePathForNms cp = cur_rte.getCurrent_paths().get(i);
                String send = "";
                String receive = "";
                if(i==0)
                    send = cp.getSend_node_name();
                else {
                    send = vrte.getCurrent_path();

                    RoutePathForNms prev = cur_rte.getCurrent_paths().get(i-1);
                    if(prev.getReceive_node_id()!=cp.getSend_node_id()) {
                        log.info(String.format("OSPF [%s] Path Order Error. : (%d) %s, (%d) %s", vrte.getE2e(), prev.getPathser(), prev.getPaths(), cp.getPathser(), cp.getPaths()));
                        break;
                    }
                }
                receive = cp.getReceive_node_name();
                vrte.setCurrent_path(send + " -> " + receive);
            }

            if(vrte.getOptimized_path()==null) return;
            vrte.getOptimized_path().forEach(aop -> {
                Collections.sort(aop.getOptPaths());
                for(int i=0; i<aop.getOptPaths().size(); i++) {
                    AnalysisPath ap = aop.getOptPaths().get(i);

                    String send = "";
                    String receive = "";
                    if(i==0)
                        send = ap.getSendNodeName();
                    else {
                        send = aop.getPaths();

                        AnalysisPath prev = aop.getOptPaths().get(i-1);
                        if(prev.getReceiveNode_id()!=ap.getSendNode_id()) {
                            log.info(String.format("Optimize [%s] Path Order Error. : (%d) %s, (%d) %s", vrte.getE2e(), prev.getPathser(), prev.getPaths(), ap.getPathser(), ap.getPaths()));
                            break;
                        }
                    }
                    receive = ap.getReceiveNodeName();
                    aop.setPaths(send + " -> " + receive);
                }
            });
        });

        return new Response(true, "Changed Route Read Complete.", verifyOptRoutes);
    }

}

class LinkFactorForSort {
    private long link_id;
    private List<Double> latencys = new ArrayList<>();
    private List<Double> jitters = new ArrayList<>();
    private List<Integer> losts = new ArrayList<>();
    public LinkFactorForSort(long link_id) {
        this.link_id = link_id;
    }
    public long getLink_id() {
        return this.link_id;
    }
    public List<Double> getLatencys() {
        return this.latencys;
    }
    public List<Double> getJitters() {
        return this.jitters;
    }
    public List<Integer> getLosts() {
        return this.losts;
    }
    public void setAddFactor(double latency, double jitter, int lost) {
        if(latency>=0) this.latencys.add(latency);
        if(jitter>=0) this.jitters.add(jitter);
        if(lost>=0) this.losts.add(lost);
    }
}
class NodeFactorForSort {
    private long node_id;
    private List<Float> cpuUsages = new ArrayList<>();
    private List<Float> memUsages = new ArrayList<>();
    public NodeFactorForSort(long node_id) {
        this.node_id = node_id;
    }
    public long getNode_id() {
        return this.node_id;
    }
    public List<Float> getCpuUsages() {
        return this.cpuUsages;
    }
    public List<Float> getMemUsages() {
        return this.memUsages;
    }
    public void setAddFactor(float cpuUsage, float memUsage) {
        if(cpuUsage>=0) this.cpuUsages.add(cpuUsage);
        if(memUsage>=0) this.memUsages.add(memUsage);
    }
}
class LinkTrafficForSort {
    private long if_id;
    private List<Integer> txBitrates = new ArrayList<>();
    private List<Integer> txPktrates = new ArrayList<>();
    public LinkTrafficForSort(long if_id) {
        this.if_id = if_id;
    }
    public long getIf_id() {
        return this.if_id;
    }
    public List<Integer> getTxBitrates() {
        return this.txBitrates;
    }
    public List<Integer> getTxPktrates() {
        return this.txPktrates;
    }
    public void setAddTraffic(Integer txBitrate, Integer txPktrate) {
        if(txBitrate==null) txBitrate = 0;
        if(txPktrate==null) txPktrate = 0;
        if(txBitrate>=0) this.txBitrates.add(txBitrate);
        if(txPktrate>=0) this.txPktrates.add(txPktrate);
    }
}

/* Sort Class
class LinkNodeForSort implements Comparable<LinkNodeForSort> {
    private int ser;
    private long node_id;

    public LinkNodeForSort(int ser, long node_id) {
        this.ser = ser;
        this.node_id = node_id;
    }
    @Override
    public int compareTo(LinkNodeForSort linkNodeForSort) {
        if(linkNodeForSort.ser < ser) return 1;
        else if(linkNodeForSort.ser > ser) return -1;
        return 0;
    }

    public int getSer() {
        return this.ser;
    }
    public long getNode_id() {
        return this.node_id;
    }
    public void setSer(int ser) {
        this.ser = ser;
    }
    public void setNode_id(long node_id) {
        this.node_id = node_id;
    }
}
*/
