package com.ipsdn_opt.app.service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipsdn_opt.app.model.CpuMemUsageCheck;
import com.ipsdn_opt.app.model.CpuUsage;
import com.ipsdn_opt.app.model.DbCheckLinkCollecting;
import com.ipsdn_opt.app.model.DbCheckNodeCollecting;
import com.ipsdn_opt.app.model.DbCheckTrafficCollecting;
import com.ipsdn_opt.app.model.DbLinkFactorForNms;
import com.ipsdn_opt.app.model.DbLinkTrafficForNms;
import com.ipsdn_opt.app.model.DbNodeFactorForNms;
import com.ipsdn_opt.app.model.Factor;
import com.ipsdn_opt.app.model.Interface;
import com.ipsdn_opt.app.model.InterfaceOrg;
import com.ipsdn_opt.app.model.Link;
import com.ipsdn_opt.app.model.LinkCollectCheck;
import com.ipsdn_opt.app.model.LinkFactor;
import com.ipsdn_opt.app.model.LinkFactorForUI;
import com.ipsdn_opt.app.model.LinkTraffic;
import com.ipsdn_opt.app.model.MappedNmsInterface;
import com.ipsdn_opt.app.model.MemUsage;
import com.ipsdn_opt.app.model.Node;
import com.ipsdn_opt.app.model.NodeCollectCheck;
import com.ipsdn_opt.app.model.NodeFactor;
import com.ipsdn_opt.app.model.NodeUsageRate;
import com.ipsdn_opt.app.model.Probe;
import com.ipsdn_opt.app.model.Response;
import com.ipsdn_opt.app.model.TrafficCollectCheck;
import com.ipsdn_opt.app.model.UsageRate;
import com.ipsdn_opt.app.model.DbUsageRate;
import com.ipsdn_opt.app.repository.InterfaceOrgRepository;
import com.ipsdn_opt.app.repository.InterfaceRepository;
import com.ipsdn_opt.app.repository.LinkFactorForUIRepository;
import com.ipsdn_opt.app.repository.LinkRepository;
import com.ipsdn_opt.app.repository.LinkTrafficRepository;
import com.ipsdn_opt.app.repository.NodeFactorRepository;
import com.ipsdn_opt.app.repository.NodeRepository;
import com.ipsdn_opt.app.repository.ProbeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CollectSvc {
    @Autowired
    ProbeRepository probeRepository;
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    LinkFactorForUIRepository linkFactorForUIRepository;
    @Autowired
    NodeFactorRepository nodeFactorRepository;
    @Autowired
    InterfaceRepository interfaceRepository;
    @Autowired
    LinkRepository linkRepository;
    @Autowired
    LinkTrafficRepository linkTrafficRepository;
    @Autowired
    InterfaceOrgRepository interfaceOrgRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    RestSvc restSvc;
    @Autowired
    EntityManager em;

    public Response linkUpdateFromIpsdn() {
        List<Link> links = linkRepository.findAll();
        links.forEach(l -> {
            l.setUsaged(false);
        });
        Query qry = em.createNativeQuery("call findLinkFromIpsdn();");
        List<Object[]> results = qry.getResultList();
        for(Object[] row : results) {
            long sif_id = ((BigInteger)row[0]).longValue();
            long rif_id = ((BigInteger)row[1]).longValue();
            boolean isExist = false;
            for(Link l : links) {
                if(l.getSif_id()==sif_id && l.getRif_id()==rif_id) {
                    l.setUsaged(true);
                    isExist = true;
                }
            }
            if(!isExist) {
                Link link = new Link(sif_id, rif_id, null, true);
                links.add(link);
            }
        }
        linkRepository.saveAll(links);
        return new Response(true, "Link is Updated from IP-SDN.", links);
    }
    public Response interfaceUpdateFromIpsdn() {
        List<InterfaceOrg> interfaces = interfaceOrgRepository.findAll();
        interfaces.forEach(intf -> {
            intf.setUsaged(false);
        });
        Query qry = em.createNativeQuery("call findInterfaceFromIpSdn();");
        List<Object[]> results = qry.getResultList();
        for(Object[] row : results) {
            long if_id = ((BigInteger)row[0]).longValue();
            String ifname = row[1].toString();
            long node_id = ((BigInteger)row[2]).longValue();
            String ipaddr = row[3]==null?null:row[3].toString();
            boolean isExist = false;
            for(InterfaceOrg intf : interfaces) {
                if(intf.getId()==if_id) {
                    intf.setUsaged(true);
                    isExist = true;
                }
            }
            if(!isExist) {
                InterfaceOrg intf = new InterfaceOrg();
                intf.setId(if_id);
                intf.setIfname(ifname);
                intf.setNode_id(node_id);
                intf.setIpaddr(ipaddr);
                interfaces.add(intf);
            }
        }
        interfaceOrgRepository.saveAll(interfaces);
        interfaceOrgRepository.updateVlanInterfaceFromIpsdn();
        interfaceOrgRepository.updateAgencyInterfaceFromIpsdn();
        return new Response(true, "Link is Updated from IP-SDN.", interfaces);
    }
    public Response latencyCheck(long node_id, Long destNode_id) {
        JSONObject result;
        try {
            LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            Probe probe = probeRepository.findProbe4ColbyNodeid(node_id);
            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);
            String url = "http://" + probe.getIpaddr() + ":" + probe.getPort().toString() + "/ipsdn/opt/probe/";
            if(destNode_id==null)
                url += "linklatency?measured_time=" + currentDateTime;
            else {
                url += "e2elatency?measured_time=" + currentDateTime + "&destnode_id=" + destNode_id.toString();
            }
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(resultMap.getBody());
            result = new JSONObject(jsonString);
        }
        catch(Exception e) {
            return new Response(false, "Probe API(Latency Check) Call Fail. ", e.getMessage());
        }
        List<Object> jData = null;
        if(result.getBoolean("status")) jData = result.getJSONArray("data").toList();
        return new Response(result.getBoolean("status"), result.getString("message"), jData);
    }


    public Response latencyCheckAllNode(LocalDateTime currentDateTime) {
        List<Response> responses = new ArrayList<>();
        List<Node> nodes = nodeRepository.findAll();
        // List<Node> nodes = nodeRepository.findSomeNodes();
        List<Thread> threads = new ArrayList<>();
        for(Node node : nodes) {
            Thread linkThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject result;
                    try {
                        Probe probe = probeRepository.findProbe4ColbyNodeid(node.getId());
                        HttpHeaders header = new HttpHeaders();
                        HttpEntity<?> entity = new HttpEntity<>(header);
                        String url = "http://" + probe.getIpaddr() + ":" + probe.getPort().toString() + "/ipsdn/opt/probe/linklatency?measured_time=" + currentDateTime;
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
                        ObjectMapper mapper = new ObjectMapper();
                        String jsonString = mapper.writeValueAsString(resultMap.getBody());
                        result = new JSONObject(jsonString);
                        List<Object> jData = null;
                        if(result.getBoolean("status")) jData = result.getJSONArray("data").toList();
                        responses.add(
                            new Response(
                                true,
                                String.format("Node(%s) API(Latency Check) Call Complete", node.getNodename()),
                                jData
                            )
                        );
                    } catch(Exception e) {
                        responses.add(
                            new Response(
                                false,
                                String.format("Node(%s) API(Latency Check) Call Fail. => %s", node.getNodename(), e.getMessage()),
                                null
                            )
                        );
                    }
                }
            });
            threads.add(linkThread);
            linkThread.start();
        }
        for(Thread thread : threads) {
            try {
                thread.join();
            }
            catch(Exception e) {
                return new Response(false, "Probe API Call Thread Create Fail.", e.getMessage());
            }
        }
        return new Response(true, "All Node Latency Measuring Complete.", responses);
    }

    public Response getLinkTrafficFromControllerDB() {
        Query qry = em.createNativeQuery("call findLinkTrafficForOpt();", LinkTraffic.class);
        List<LinkTraffic> remoteData = qry.getResultList();
        List<LinkTraffic> toSaveData = new ArrayList<>();
        remoteData.forEach(r -> {
            toSaveData.add(new LinkTraffic(r.getIf_id(), r.getTxbitrate(), r.getRxbitrate(), r.getTxpktrate(), r.getRxpktrate(), r.getMeasureddatetime()));
        });
        linkTrafficRepository.saveAll(toSaveData);
        return new Response(true, "Link Traffic Load Complete.", null);
    }
    public Response requestLinkTraffic(LocalDateTime currentDateTime) {
        List<Interface> interfaces = interfaceRepository.findInterfaceAll();
        List<Node> nodes = nodeRepository.findAll();
        List<Link> links = linkRepository.findAll();
        HashMap<Long, Interface> ifMap = new HashMap<>();
        HashMap<Long, Node> nodeMap = new HashMap<>();
        interfaces.forEach(intf -> {
            ifMap.put(intf.getId(), intf);
        });
        nodes.forEach(node -> {
            nodeMap.put(node.getId(), node);
        });
        List<JSONObject> jObjList = new ArrayList<>();
        HashMap<Long, JSONObject> mapJobj = new HashMap<>();
        for(Link link : links) {
            Interface intf = ifMap.get(link.getSif_id());
            Node node = nodeMap.get(intf.getNode_id());
            if(node==null) continue;
            JSONObject jObj = mapJobj.get(node.getId());
            if(jObj==null) {
                jObj = new JSONObject();
                jObj.put("nodeName", node.getNodename());
                jObj.put("interfaceTraffic", new JSONArray());
                jObjList.add(jObj);
                mapJobj.put(node.getId(), jObj);
            }
            JSONObject jif = new JSONObject();
            jif.put("interfaceName", intf.getIfname());
            jObj.getJSONArray("interfaceTraffic").put(jif);
        }
        
        Query qry = em.createNativeQuery("call findAgencyInterface();");
        List<Object> results = qry.getResultList();
        for(Object row : results) {
            long if_id = ((BigInteger)row).longValue();
            Interface intf = ifMap.get(if_id);
            Node node = nodeMap.get(intf.getNode_id());
            if(node==null) continue;
            JSONObject jObj = mapJobj.get(node.getId());
            if(jObj==null) {
                jObj = new JSONObject();
                jObj.put("nodeName", node.getNodename());
                jObj.put("interfaceTraffic", new JSONArray());
                jObjList.add(jObj);
                mapJobj.put(node.getId(), jObj);
            }
            JSONObject jif = new JSONObject();
            jif.put("interfaceName", intf.getIfname());
            jObj.getJSONArray("interfaceTraffic").put(jif);
        }

        JSONArray reqJarrNew = new JSONArray();
        JSONArray reqJarrOld = new JSONArray();
        jObjList.forEach(jObj -> {
            String model = jObj.getString("nodeName").split("-")[1];
            if(model.startsWith("S9")) reqJarrNew.put(jObj);
            else reqJarrOld.put(jObj);
        });

        List<Object> oTraffic = new ArrayList<>();
        boolean isSuccess = false;
        String msg = "Traffic Read Complete.";
        for(int i=0; i<2; i++) {
            if(i==0 && reqJarrOld.length()==0) continue;
            if(i==1 && reqJarrNew.length()==0) continue;
            JSONArray reqJarr = null;
            if(i==0) reqJarr = reqJarrOld;
            else reqJarr = reqJarrNew;

            JSONObject result;
            HttpHeaders reqHeaders = new HttpHeaders();

            if(i==0) {
                for(String cookie : restSvc.getCookies()) {
                    reqHeaders.add("Cookie", cookie);
                }
            }
            else {
                reqHeaders.add("Authorization", restSvc.getToken());
            }
            reqHeaders.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<String>(reqJarr.toString(), reqHeaders);
            String hostAddrPort = i==0 ? restSvc.getHostAddrPort() : restSvc.getNewHostAddrPort();
            String url = hostAddrPort + "/ipsdn/services/stat/linktraffic";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(resultMap.getBody());
                result = new JSONObject(jsonString);
            }
            catch(Exception e) {
                return new Response(false, "Link Traffic Read Error From Controller. ", e.getMessage());
            }
            
            List<LinkTraffic> linkTraffics = linkTrafficFromJson(result.getJSONArray("data"), currentDateTime);
            AddNmsInterface(linkTraffics);
            try {
                linkTrafficRepository.saveAll(linkTraffics);
            }catch(Exception e) {
                System.out.println("링크트래픽 저장 Error => " + e.getMessage());
            }
            if(result.getBoolean("status")) {
                List<Object> jData = result.getJSONArray("data").toList();
                oTraffic.addAll(jData);
                isSuccess = true;
            }
            else {
                msg = result.getString("message");
            }
        }
        
        return new Response(isSuccess, msg, oTraffic);
    }
    public List<LinkTraffic> linkTrafficFromJson(JSONArray jArr, LocalDateTime currentDateTime) {
        List<LinkTraffic> linktraffics = new ArrayList<>();
        String sqlString = "call findNodeInterfaceAll();";
        Query query = em.createNativeQuery(sqlString);
        List<Object[]> interfaces = query.getResultList();
        HashMap<String, Long> ifMap = new HashMap<>();
        interfaces.forEach(intf -> {
            ifMap.put(intf[0].toString(), Long.parseLong(intf[1].toString()));
        });
        
        for(int i=0; i<jArr.length(); i++) {
            JSONObject jObj = jArr.getJSONObject(i);
            JSONArray ifArr = jObj.getJSONArray("interfaceTraffic");
            for(int j=0; j<ifArr.length(); j++) {
                JSONObject jif = ifArr.getJSONObject(j);
                String ifName = String.format("%s %s", jObj.getString("nodeName"), jif.getString("interfaceName"));
                Long if_id = ifMap.get(ifName);
                Integer txBitRate = null;
                Integer rxBitRate = null;
                Integer txPktRate = null;
                Integer rxPktRate = null;
                try {
                    if(!jif.get("txBitRate").toString().equals("null")) txBitRate = jif.getInt("txBitRate");
                    if(!jif.get("rxBitRate").toString().equals("null")) rxBitRate = jif.getInt("rxBitRate");
                    if(!jif.get("txPktRate").toString().equals("null")) txPktRate = jif.getInt("txPktRate");
                    if(!jif.get("rxPktRate").toString().equals("null")) rxPktRate = jif.getInt("rxPktRate");
                }catch(Exception e) {
                    log.info(String.format("%s %s", jObj.getString("nodeName"), jif.getString("interfaceName")));
                    log.info("====> " + e.getMessage());
                    log.info("====> " + jif);
                }
                linktraffics.add(new LinkTraffic(if_id, txBitRate, rxBitRate, txPktRate, rxPktRate, currentDateTime));
            }
        }
        return linktraffics;
    }
    public void AddNmsInterface(List<LinkTraffic> linkTraffics) {
        // Query qry = em.createNativeQuery("call findMappedNmsInterface();", MappedNmsInterface.class);
        Query qry = em.createNativeQuery("call temp_findMappedNmsInterface();", MappedNmsInterface.class);
        List<MappedNmsInterface> nmsInterfaces = qry.getResultList();
        HashMap<Long, MappedNmsInterface> mapNmsInterface = new HashMap<>();
        nmsInterfaces.forEach(ni -> {
            mapNmsInterface.put(ni.getIf_id(),ni);
        });
        linkTraffics.forEach(lf -> {
            MappedNmsInterface ni = mapNmsInterface.get(lf.getIf_id());
            if(ni==null) return;
            lf.setNms_interface_name(ni.getNms_interface_name());
        });
    }

    public Response measuredFactors(String nodename) {
        List<Factor> factors = new ArrayList<>();
        List<Node> nodes;
        List<LinkFactorForUI> linkfactors;

        if(nodename==null) {
            nodes = nodeRepository.findAll();
        }
        else {
            Node node = nodeRepository.findNodeByNodename(nodename);
            if(node==null)
                return new Response(false, "Error: " + nodename + " is not registed node.", null);
            nodes = new ArrayList<>();
            nodes.add(node);
        }
        for(Node node : nodes) {
            linkfactors = linkFactorForUIRepository.findLinkFactorForUIByNodeid(node.getId());
            linkfactors.forEach(l -> {
                l.setJitter(l.getJitter());
                l.setLatency(l.getLatency());
            });
            NodeFactor nodeFactor = nodeFactorRepository.findNodeFactorByNodeid(node.getId());
            factors.add(new Factor(node.getNodename(), nodeFactor, linkfactors));
        }
        return new Response(true, "Measured Factors Read Complete.", factors);
    }
    
    public Response viewMeasuredFactors(String nodename) {
        return new Response(true, "Measured Factors Read Complete.", null);
    }

    public Response requestFactorMatrix_bak(String factorName) {
        List<LinkFactorForUI> linkfactors = linkFactorForUIRepository.findLinkFactorForMatrix();
        List<Node> nodes = nodeRepository.findAll();
        HashMap<String, LinkFactorForUI> mapLinkFactor = new HashMap<>();
        linkfactors.forEach(lf -> {
            String key = String.format("%s -> %s", lf.getSrcnodename(), lf.getDestnodename());
            mapLinkFactor.put(key, lf);
        });
        List<String> results = new ArrayList<>();
        String row = String.format("|%10.10s|", "");
        String line = "------------";
        for(Node n : nodes) {
            row += String.format("%-10.10s|", n.getNodename());
            line += String.format("%11.11s", "-----------");
        }
        results.add(row);
        results.add(line);
        for(Node sn : nodes) {
            row = String.format("|%-10.10s|",sn.getNodename());
            for(Node rn : nodes) {
                String key = String.format("%s -> %s", sn.getNodename(), rn.getNodename());
                LinkFactorForUI lf = mapLinkFactor.get(key);
                if(lf==null) row = row + String.format("%5s%-5s|","", "X");
                else {
                    if(factorName==null) row = row + String.format("%10.4f|", lf.getLatency());
                    else if(factorName.toLowerCase().equals("jitter")) row = row + String.format("%10.4f|", lf.getJitter());
                    else if(factorName.toLowerCase().equals("lost")) row = row + String.format("%10d|", lf.getLost());
                }
            }
            results.add(row);
        }
        return new Response(true, "Measured Factors Read Complete.", results);
    }
    public Response requestFactorMatrix(String factorName) {
        List<LinkFactorForUI> linkfactors = linkFactorForUIRepository.findLinkFactorForMatrix();
        List<Node> nodes = nodeRepository.findAll();
        HashMap<String, LinkFactorForUI> mapLinkFactor = new HashMap<>();
        linkfactors.forEach(lf -> {
            String key = String.format("%s -> %s", lf.getSrcnodename(), lf.getDestnodename());
            mapLinkFactor.put(key, lf);
        });
        List<String[]> results = new ArrayList<>();
        String[] cols = new String[nodes.size()+1];
        cols[0] = "";
        for(int i=0; i<nodes.size(); i++) {
            cols[i+1] = nodes.get(i).getNodename();
        }
        results.add(cols.clone());
        for(int i=0; i<nodes.size(); i++) {
            Node sn = nodes.get(i);
            for(int j=0; j<nodes.size(); j++) {
                if(j==0) cols[j] = sn.getNodename();
                Node rn = nodes.get(j);
                String key = String.format("%s -> %s", sn.getNodename(), rn.getNodename());
                LinkFactorForUI lf = mapLinkFactor.get(key);
                if(lf==null) cols[j+1] = "-";
                else {
                    if(factorName==null) cols[j+1] = String.format("%.4f", lf.getLatency());
                    else if(factorName.toLowerCase().equals("latency")) cols[j+1] = String.format("%.4f", lf.getLatency());
                    else if(factorName.toLowerCase().equals("jitter")) cols[j+1] = String.format("%.4f", lf.getJitter());
                    else if(factorName.toLowerCase().equals("hops")) cols[j+1] = String.format("%d", lf.getHops());
                }
            }
            results.add(cols.clone());
        }
        return new Response(true, "Measured Factors Read Complete.", results);
    }
    
    public Response conversionNmsId() {
        nodeRepository.updateNmsId();
        return new Response(true, "NMS Node-iD, Interface-ID Conversion Complete.", null);
    }

    public Response checkCollecting(LocalDate measuredDate) {

        List<CpuMemUsageCheck> cpuMemUsageCheckList = checkCpuMemoryUsage(measuredDate);
        List<NodeCollectCheck> nodeCollectCheckList = checkNodeFactorCollecting(measuredDate);
        List<LinkCollectCheck> linkCollectCheckList = checkLinkFactorCollecting(measuredDate);
        List<TrafficCollectCheck> trafficCollectCheckList = checkLinkTrafficCollecting(measuredDate);
        String cpuMemUsageMailContent = makeMailContentForCpuMemUsage(cpuMemUsageCheckList, measuredDate);
        String nodeFactorMailContent = makeMailContentForNodeFactor(nodeCollectCheckList, measuredDate);
        String linkFactorMailContent = makeMailContentForLinkFactor(linkCollectCheckList, measuredDate);
        String linkTrafficMailContent = makeMailContentForLinkTraffic(trafficCollectCheckList, measuredDate);
        sendMailAlarm(cpuMemUsageMailContent, nodeFactorMailContent, linkFactorMailContent, linkTrafficMailContent, measuredDate);

        return new Response(true, "NodeFactor Read Complete", null);
    }
    public List<NodeCollectCheck> checkNodeFactorCollecting(LocalDate measuredDate) {
        Query qry= em.createNativeQuery("call findCollectedNodeFactor(:measureddate); ", DbCheckNodeCollecting.class)
                .setParameter("measureddate",measuredDate);
        List<DbCheckNodeCollecting> dbCollected = qry.getResultList();
        HashMap<Long, NodeCollectCheck> mapNodeCheck = new HashMap<>();
        List<NodeCollectCheck> nodeCollectCheckList = new ArrayList<>();
        dbCollected.forEach(dc -> {
            NodeCollectCheck ncc = mapNodeCheck.get(dc.getNode_id());
            if(ncc==null) {
                ncc = new NodeCollectCheck();
                ncc.setNode_id(dc.getNode_id());
                ncc.setNodename(dc.getNodename());
                nodeCollectCheckList.add(ncc);
                mapNodeCheck.put(dc.getNode_id(), ncc);
            }
            NodeFactor nf = new NodeFactor();
            nf.setCpuusage(dc.getCpuusage());
            nf.setMemusage(dc.getMemusage());
            nf.setMeasureddatetime(dc.getMeasureddatetime());
            ncc.getNodeFactors().add(nf);
        });
        
        for(int i=0; i<nodeCollectCheckList.size(); i++) {
            NodeCollectCheck ncc = nodeCollectCheckList.get(i);
            int fatalCount = 0;
            int errCount = 0;
            for(int j=0; j<ncc.getNodeFactors().size(); j++) {
                NodeFactor nf = ncc.getNodeFactors().get(j);
                if(nf.getCpuusage()==null || nf.getMemusage()==null || nf.getCpuusage()<0 || nf.getMemusage()<0) {
                    errCount++;
                    if(j==fatalCount) { //앞에서부터(최근시간대부터) 연속으로 측정값이 없을 경우에만
                        fatalCount++;
                    }
                }
            }
            ncc.setFatalErrors(fatalCount);
            ncc.setErrCount(errCount);
            if(errCount==0) {
                nodeCollectCheckList.remove(ncc);
                i--;
            }
            else {
                for(int j=0; j<ncc.getNodeFactors().size(); j++) {
                    NodeFactor nf = ncc.getNodeFactors().get(j);
                    if(nf.getCpuusage()!=null && nf.getMemusage()!=null && nf.getCpuusage()>=0 && nf.getMemusage()>=0) {
                        ncc.getNodeFactors().remove(nf);
                        j--;
                    }
                };
            }
        }
        return nodeCollectCheckList;
    }
    public List<LinkCollectCheck> checkLinkFactorCollecting(LocalDate measuredDate) {
        Query qry= em.createNativeQuery("call findCollectedLinkFactor(:measureddate); ", DbCheckLinkCollecting.class)
                .setParameter("measureddate",measuredDate);
        List<DbCheckLinkCollecting> dbCollected = qry.getResultList();
        HashMap<Long, LinkCollectCheck> mapLinkCheck = new HashMap<>();
        List<LinkCollectCheck> linkCollectCheckList = new ArrayList<>();
        dbCollected.forEach(dc -> {
            LinkCollectCheck lcc = mapLinkCheck.get(dc.getLink_id());
            if(lcc==null) {
                lcc = new LinkCollectCheck();
                lcc.setLink_id(dc.getLink_id());
                lcc.setLink_name(dc.getLink_name());
                linkCollectCheckList.add(lcc);
                mapLinkCheck.put(dc.getLink_id(), lcc);
            }
            LinkFactor lf = new LinkFactor();
            lf.setLatency(dc.getLatency());
            lf.setJitter(dc.getJitter());
            lf.setLost(dc.getLost());
            lf.setMeasureddatetime(dc.getMeasureddatetime());
            lcc.getLinkFactors().add(lf);
        });
        
        for(int i=0; i<linkCollectCheckList.size(); i++) {
            LinkCollectCheck lcc = linkCollectCheckList.get(i);
            int fatalCount = 0;
            int errCount = 0;
            for(int j=0; j<lcc.getLinkFactors().size(); j++) {
                LinkFactor lf = lcc.getLinkFactors().get(j);
                if(lf.getLatency()==null || lf.getJitter()==null) {
                    errCount++;
                    if(j==fatalCount) { //앞에서부터(최근시간대부터) 연속으로 측정값이 없을 경우에만
                        fatalCount++;
                    }
                }
            }
            lcc.setFatalErrors(fatalCount);
            lcc.setErrCount(errCount);
            if(errCount==0) {
                linkCollectCheckList.remove(lcc);
                i--;
            }
            else {
                for(int j=0; j<lcc.getLinkFactors().size(); j++) {
                    LinkFactor lf = lcc.getLinkFactors().get(j);
                    if(lf.getLatency()!=null && lf.getJitter()!=null) {
                        lcc.getLinkFactors().remove(lf);
                        j--;
                    }
                };
            }
        }
        return linkCollectCheckList;
    }
    public List<TrafficCollectCheck> checkLinkTrafficCollecting(LocalDate measuredDate) {
        Query qry= em.createNativeQuery("call findCollectedLinkTraffic(:measureddate); ", DbCheckTrafficCollecting.class)
                .setParameter("measureddate",measuredDate);
        List<DbCheckTrafficCollecting> dbCollected = qry.getResultList();
        HashMap<Long, TrafficCollectCheck> mapTrafficCheck = new HashMap<>();
        List<TrafficCollectCheck> trafficCollectCheckList = new ArrayList<>();
        dbCollected.forEach(dc -> {
            TrafficCollectCheck tcc = mapTrafficCheck.get(dc.getIf_id());
            if(tcc==null) {
                tcc = new TrafficCollectCheck();
                tcc.setIf_id(dc.getIf_id());
                tcc.setIfname(dc.getIfname());
                tcc.setNode_id(dc.getNode_id());
                tcc.setNodename(dc.getNodename());
                trafficCollectCheckList.add(tcc);
                mapTrafficCheck.put(dc.getIf_id(), tcc);
            }
            LinkTraffic lt = new LinkTraffic();
            lt.setTxbitrate(dc.getTxbitrate());
            lt.setRxbitrate(dc.getRxbitrate());
            lt.setTxpktrate(dc.getTxpktrate());
            lt.setRxpktrate(dc.getRxpktrate());
            lt.setMeasureddatetime(dc.getMeasureddatetime());
            tcc.getLinkTraffics().add(lt);
        });
        
        for(int i=0; i<trafficCollectCheckList.size(); i++) {
            TrafficCollectCheck tcc = trafficCollectCheckList.get(i);
            int fatalCount = 0;
            int errCount = 0;
            LocalDateTime existTime = measuredDate.atTime(23, 59).plusMinutes(1);
            for(int j=0; j<tcc.getLinkTraffics().size(); j++) {
                LinkTraffic lt = tcc.getLinkTraffics().get(j);
                LocalDateTime stdDt = existTime.minusMinutes(1);
                existTime = lt.getMeasureddatetime();
                if(j==0) {
                    tcc.setOmitedTime_start(measuredDate.atTime(23, 59));
                    tcc.setOmitedTime_end(measuredDate.atTime(0, 0));
                    tcc.setFatal_omitedTime_start(measuredDate.atTime(23, 59));
                    tcc.setFatal_omitedTime_end(measuredDate.atTime(0, 0));
                }
                if(lt.getMeasureddatetime()==null) {
                    errCount = 1440;
                    fatalCount = 1440;
                    tcc.setOmitedTime_start(measuredDate.atTime(0, 0));
                    tcc.setOmitedTime_end(measuredDate.atTime(23, 59));
                    tcc.setFatal_omitedTime_start(measuredDate.atTime(0, 0));
                    tcc.setFatal_omitedTime_end(measuredDate.atTime(23, 59));
                    break;
                }
                else {
                    //기준시간
                    Duration duration = Duration.between(stdDt, lt.getMeasureddatetime());
                    if(j==0) { //최초 측정시간대부터 연속으로 빠진 경우만 확인
                        if(duration.getSeconds()>60) {
                            fatalCount = (int)(duration.getSeconds() / 60); //누락된 분수(minute count)
                            tcc.setFatal_omitedTime_start(lt.getMeasureddatetime().plusMinutes(1));
                        }
                    }
                    if(duration.getSeconds()>60) {
                        errCount += (int)(duration.getSeconds() / 60); //누락된 분수(minute count)
                        System.out.println(String.format("[id=%d] errCount: %d, stddate:%s, trfdate:%s, existTime:%s", 
                            tcc.getIf_id(), errCount, stdDt.toString(), lt.getMeasureddatetime().toString(), existTime.toString()));
                        LocalDateTime omitedTime = lt.getMeasureddatetime().plusMinutes(1);
                        if(omitedTime.isBefore(tcc.getFatal_omitedTime_start()) &&
                            omitedTime.isBefore(tcc.getOmitedTime_start())) {
                            tcc.setOmitedTime_start(omitedTime);
                        }
                        if(omitedTime.isBefore(tcc.getFatal_omitedTime_start()) &&
                            omitedTime.isAfter(tcc.getOmitedTime_end())) {
                            tcc.setOmitedTime_end(omitedTime);
                        }
                    }
                }
            }
            LocalDateTime mustbeDateTime = measuredDate.atTime(0, 0,0);
            Duration duration = Duration.between(mustbeDateTime, existTime);
            if(duration.getSeconds()>60) {
                errCount += (int)(duration.getSeconds() / 60); //누락된 분수(minute count)
                tcc.setOmitedTime_start(mustbeDateTime);
                if(existTime.plusMinutes(1).isBefore(tcc.getFatal_omitedTime_start()) &&
                    existTime.plusMinutes(1).isAfter(tcc.getOmitedTime_end())) {
                    tcc.setOmitedTime_end(existTime.plusMinutes(1));
                }
            }
            tcc.setFatalErrors(fatalCount);
            tcc.setErrCount(errCount);
            if(errCount==0) {
                trafficCollectCheckList.remove(tcc);
                i--;
            }
            else {
                tcc.getLinkTraffics().removeAll(tcc.getLinkTraffics());
            }
        }
        return trafficCollectCheckList;
    }
    public List<CpuMemUsageCheck> checkCpuMemoryUsage(LocalDate sourceDate) {
        Query qry= em.createNativeQuery("call findCpuUsage(:sourcedate); ", CpuUsage.class)
                .setParameter("sourcedate",sourceDate);
        List<CpuUsage> cpuUsages = qry.getResultList();
        List<CpuMemUsageCheck> cpuMemUsageCheckList = new ArrayList<>();
        HashMap<Long, CpuMemUsageCheck> mapChecks = new HashMap<>();
        cpuUsages.forEach(cu -> {
            CpuMemUsageCheck cmuc = mapChecks.get(cu.getNode_id());
            if(cmuc==null) {
                cmuc = new CpuMemUsageCheck(cu.getNode_id(), cu.getNodename());
                mapChecks.put(cu.getNode_id(), cmuc);
                cpuMemUsageCheckList.add(cmuc);
            }
            cmuc.getCpuUsages().add(cu);
        });
        qry= em.createNativeQuery("call findMemUsage(:sourcedate); ", MemUsage.class)
                .setParameter("sourcedate",sourceDate);
        List<MemUsage> memUsages = qry.getResultList();
        memUsages.forEach(mu -> {
            CpuMemUsageCheck cmuc = mapChecks.get(mu.getNode_id());
            if(cmuc==null) {
                cmuc = new CpuMemUsageCheck(mu.getNode_id(), mu.getNodename());
                mapChecks.put(mu.getNode_id(), cmuc);
                cpuMemUsageCheckList.add(cmuc);
            }
            cmuc.getMemUsages().add(mu);
        });

        cpuMemUsageCheckList.forEach(cmuc -> {
            int increase_count = 0;

            for(int i=0; i<cmuc.getCpuUsages().size()-1; i++) {
                if(i>=0 && i<=4) {
                    CpuUsage curr = cmuc.getCpuUsages().get(i);
                    CpuUsage prev = cmuc.getCpuUsages().get(i+1); // 시간역순으로 있으므로
                    if(curr.getCpuusage()>prev.getCpuusage()) increase_count++;
                    continue;
                }
            }
            if(cmuc.getCpuUsages().size()>=3) {
                if((cmuc.getCpuUsages().get(0).getCpuusage() >= cmuc.getCpuUsages().get(2).getCpuusage()) && increase_count>=3) 
                    cmuc.setCpuWarning(true);
            }

            increase_count = 0;
            for(int i=0; i<cmuc.getMemUsages().size()-1; i++) {
                if(i>=0 && i<=4) {
                    MemUsage curr = cmuc.getMemUsages().get(i);
                    MemUsage prev = cmuc.getMemUsages().get(i+1); // 시간역순으로 있으므로
                    if(curr.getMemusage()>prev.getMemusage()) increase_count++;
                    continue;
                }
            }
            if(cmuc.getCpuUsages().size()>=3) {
                if((cmuc.getMemUsages().get(0).getMemusage() >= cmuc.getMemUsages().get(2).getMemusage()) && increase_count>=3) 
                    cmuc.setMemWarning(true);
            }
        });
        return cpuMemUsageCheckList;
    }

    public String makeMailContentForCpuMemUsage(List<CpuMemUsageCheck> cpuMemUsageCheckList, LocalDate sourceDate) {
        String content = "";
        for(CpuMemUsageCheck cmuc : cpuMemUsageCheckList) {
            if(cmuc.isCpuWarning()) {
                content += String.format("%s :\n", cmuc.getNodeName());
                content += "    최근 몇일동안 CPU 사용량이 증가하고 있습니다.\n";
                for(int i=0; i<cmuc.getCpuUsages().size()-1; i++) {
                    Float curr = cmuc.getCpuUsages().get(i).getCpuusage();
                    Float prev = cmuc.getCpuUsages().get(i+1).getCpuusage();
                    String curDate = cmuc.getCpuUsages().get(i).getSourcedate().toString();
                    String bullet = "";
                    if(curr==null || prev==null) continue;
                    if(curr>prev) bullet = "(▲)";
                    else if(curr<prev) bullet = "(↓)";
                    content += String.format("    %s : %3f%% %s\n", curDate, curr, bullet);
                }
                CpuUsage last = cmuc.getCpuUsages().get(cmuc.getCpuUsages().size() - 1);
                content += String.format("    %s : %3f%%\n", last.getSourcedate(), last.getCpuusage());
            };
            if(cmuc.isMemWarning()) {
                content += String.format("%s :\n", cmuc.getNodeName());
                content += "    최근 몇일동안 메모리 사용량이 증가하고 있습니다.\n";
                for(int i=0; i<cmuc.getMemUsages().size()-1; i++) {
                    Float curr = cmuc.getMemUsages().get(i).getMemusage();
                    Float prev = cmuc.getMemUsages().get(i+1).getMemusage();
                    String curDate = cmuc.getMemUsages().get(i).getSourcedate().toString();
                    String bullet = "";
                    if(curr==null || prev==null) continue;
                    if(curr>prev) bullet = "(▲)";
                    else if(curr<prev) bullet = "(↓)";
                    content += String.format("    %s : %3f%% %s\n", curDate, curr, bullet);
                }
                MemUsage last = cmuc.getMemUsages().get(cmuc.getMemUsages().size() - 1);
                content += String.format("    %s : %3f%%\n\n", last.getSourcedate(), last.getMemusage());
            };
        }
        return content;
    }
    public String makeMailContentForNodeFactor(List<NodeCollectCheck> nodeCollectCheckList, LocalDate measuredDate) {

        String content = "";
        for(NodeCollectCheck ncc : nodeCollectCheckList) {
            if(ncc.getErrCount()<100 && ncc.getFatalErrors()<3) continue;
            String fatal_content = "";
            String err_content = "";
            int errCount = 0;
            if(ncc.getErrCount()>0) {
                fatal_content += String.format("%s :\n", ncc.getNodename());
            }
            if(ncc.getFatalErrors()>=3) {
                fatal_content += String.format("    SNMP(CPU/MEM사용량) 자료가 수집되고 있지 않습니다.(%d개째 미수집중)\n", ncc.getFatalErrors());
                String start_time = "";
                String last_time = "";
                for(int i=0; i<ncc.getNodeFactors().size(); i++) {
                    NodeFactor nf = ncc.getNodeFactors().get(i);
                    if(i==0) last_time = nf.getMeasureddatetime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    else if(i==(ncc.getFatalErrors()-1)) {
                        start_time = nf.getMeasureddatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        break;
                    }
                }
                fatal_content += String.format("    %s ~ %s\n\n", start_time, last_time);
            }
            if((ncc.getErrCount()-ncc.getFatalErrors())>0) {
                err_content += String.format("    아래 시간대에 자료가 수집되지 않은 적이 있습니다. (미수집 수량: %d)\n", ncc.getErrCount()-ncc.getFatalErrors());
                String start_time = "";
                String last_time = "";
                for(int i=0; i<ncc.getNodeFactors().size(); i++) {
                    if(i<ncc.getFatalErrors()) continue; //Fatal Error는 위에서 처리했음
                    NodeFactor nf = ncc.getNodeFactors().get(i);
                    errCount++;
                    if(errCount==1) {
                        last_time = nf.getMeasureddatetime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    }
                    if(errCount==(ncc.getErrCount()-ncc.getFatalErrors())) {
                        start_time = nf.getMeasureddatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    }
                }
                err_content += String.format("    %s ~ %s\n\n", start_time, last_time);
            }
            content += fatal_content + err_content;
        }
        return content;
    }
    public String makeMailContentForLinkFactor(List<LinkCollectCheck> linkCollectCheckList, LocalDate measuredDate) {

        String content = "";
        for(LinkCollectCheck lcc : linkCollectCheckList) {
            if(lcc.getErrCount()<100 && lcc.getFatalErrors()<3) continue;
            String fatal_content = "";
            String err_content = "";
            int errCount = 0;
            if(lcc.getErrCount()>0) {
                fatal_content += String.format("%s, link_id=%d :\n", lcc.getLink_name(), lcc.getLink_id());
            }
            if(lcc.getFatalErrors()>=3) {
                fatal_content += String.format("    twamp(Latency/Jitter/Lost) 자료가 수집되고 있지 않습니다.(%d개째 미수집중)\n", lcc.getFatalErrors());
                String start_time = "";
                String last_time = "";
                for(int i=0; i<lcc.getLinkFactors().size(); i++) {
                    LinkFactor lf = lcc.getLinkFactors().get(i);
                    if(i==0) last_time = lf.getMeasureddatetime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    else if(i==(lcc.getFatalErrors()-1)) {
                        start_time = lf.getMeasureddatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        break;
                    }
                }
                fatal_content += String.format("    %s ~ %s\n\n", start_time, last_time);
            }
            if((lcc.getErrCount()-lcc.getFatalErrors())>0) {
                err_content += String.format("    아래 시간대에 자료가 수집되지 않은 적이 있습니다. (미수집 수량: %d)\n", lcc.getErrCount()-lcc.getFatalErrors());
                String start_time = "";
                String last_time = "";
                for(int i=0; i<lcc.getLinkFactors().size(); i++) {
                    if(i<lcc.getFatalErrors()) continue; //Fatal Error는 위에서 처리했음
                    LinkFactor lf = lcc.getLinkFactors().get(i);
                    errCount++;
                    if(errCount==1) {
                        last_time = lf.getMeasureddatetime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    }
                    if(errCount==(lcc.getErrCount()-lcc.getFatalErrors())) {
                        start_time = lf.getMeasureddatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    }
                }
                err_content += String.format("    %s ~ %s\n\n", start_time, last_time);
            }
            content += fatal_content + err_content;
        }
        return content;
    }
    public String makeMailContentForLinkTraffic(List<TrafficCollectCheck> trafficCollectCheckList, LocalDate measuredDate) {

        String content = "";
        for(TrafficCollectCheck tcc : trafficCollectCheckList) {
            if(tcc.getErrCount()<100 && tcc.getFatalErrors()<3) continue;
            String fatal_content = "";
            String err_content = "";
            if(tcc.getErrCount()>0) {
                fatal_content += String.format("%s (%s, id=%d)\n", tcc.getNodename(), tcc.getIfname(), tcc.getIf_id());
            }
            if(tcc.getFatalErrors()>=3) {
                fatal_content += String.format("    트래픽 자료가 수집되고 있지 않습니다.(%d개째 미수집중)\n", tcc.getFatalErrors());
                fatal_content += String.format("    %s ~ %s\n\n", 
                    tcc.getFatal_omitedTime_start().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    tcc.getFatal_omitedTime_end().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
            if((tcc.getErrCount()-tcc.getFatalErrors())>0) {
                err_content += String.format("    아래 시간대에 자료가 수집되지 않은 적이 있습니다. (미수집 수량: %d)\n", tcc.getErrCount()-tcc.getFatalErrors());
                err_content += String.format("    %s ~ %s\n\n",
                    tcc.getOmitedTime_start().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    tcc.getOmitedTime_end().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
            content += fatal_content + err_content;
        }
        return content;
    }
    public void sendMailAlarm(String cpuMemUsageMailContent, String nodeFactorMailContent, String linkFactorMailContent, String linkTrafficMailContent, LocalDate measuredDate) {
        String content = cpuMemUsageMailContent + nodeFactorMailContent + linkFactorMailContent + linkFactorMailContent;
        String head_content = "본 메일은 노드의 자원상태와 지표측정자료 수집이 정상적이지 않은 경우, 자동 발송됩니다.\n";
        head_content += "수집자료의 검토대상은 전일 0시부터 23시59분까지입니다.\n\n";
        if((content.length())>0) {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("noti@koren.kr");
            simpleMailMessage.setTo("sbs@koren.kr", "sejin@koren.kr", "england@koren.kr");
            simpleMailMessage.setSubject(String.format("노드의 자원상태 및 지표측정자료수집 (%s)", measuredDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            content = "";
            if(cpuMemUsageMailContent.length()>0)
                content += "[CPU 및 메모리 사용량 증가]====================================================\n\n" + cpuMemUsageMailContent;
            if(nodeFactorMailContent.length()>0)
                content += "[CPU/MEM사용량 수집자료 (NMS)]=================================================\n\n" + nodeFactorMailContent;
            if(linkFactorMailContent.length()>0)
                content += "[Latency/Jitter/Lost 수집자료 (twamp)]=========================================\n\n" + linkFactorMailContent;
            if(linkTrafficMailContent.length()>0)
                content += "[트래픽 수집자료 (Netconf)]====================================================\n\n" + linkTrafficMailContent;
            simpleMailMessage.setText(head_content+content);
            javaMailSender.send(simpleMailMessage);
        }
    }
    public Response getLinkTraffic(LocalDateTime start_time, LocalDateTime end_time) {
        Query qry = em.createNativeQuery("call findLinkTrafficForNms(:start_time, :end_time);", DbLinkTrafficForNms.class)
                .setParameter("start_time", start_time)
                .setParameter("end_time", end_time);
        List<DbLinkTrafficForNms> linkTraffics = qry.getResultList();
        return new Response(true, "Link Traffic Read Complete.", linkTraffics);
    }
    public Response getLinkFactor(LocalDateTime start_time, LocalDateTime end_time) {
        Query qry = em.createNativeQuery("call findLinkFactorForNms(:start_time, :end_time);", DbLinkFactorForNms.class)
                .setParameter("start_time", start_time)
                .setParameter("end_time", end_time);
        List<DbLinkFactorForNms> linkFactors = qry.getResultList();
        return new Response(true, "Link Factor Read Complete.", linkFactors);
    }
    public Response getNodeFactor(LocalDateTime start_time, LocalDateTime end_time) {
        Query qry = em.createNativeQuery("call findNodeFactorForNms(:start_time, :end_time);", DbNodeFactorForNms.class)
                .setParameter("start_time", start_time)
                .setParameter("end_time", end_time);
        List<DbNodeFactorForNms> linkFactors = qry.getResultList();
        return new Response(true, "Node Factor Read Complete.", linkFactors);
    }

    public Response getNodeFactorForSeries(String factorName, LocalDate toDate) {
        Query qry =em.createNativeQuery("call findNodeFactorForSeries(:factorname, :todate); ", DbUsageRate.class)
                    .setParameter("factorname", factorName)
                    .setParameter("todate", toDate);
        List<DbUsageRate> dbusagerates = qry.getResultList();

        HashMap<Long, NodeUsageRate> mapNodeRate = new HashMap<>();
        List<NodeUsageRate> nodeUsageRates = new ArrayList<>();
        dbusagerates.forEach(du -> {
            NodeUsageRate nu = mapNodeRate.get(du.getNode_id());
            if(nu==null) {
                nu = new NodeUsageRate(du.getNode_id(), du.getNodename());
                nodeUsageRates.add(nu);
                mapNodeRate.put(du.getNode_id(), nu);
            }
            nu.getUsageRates().add(new UsageRate(du.getSourcedate(), du.getUsagerate()));
        });
        
                    
        return new Response(true, "노드 " + factorName + " 사용량 Read Complete.", nodeUsageRates);
    }
}
