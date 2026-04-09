package com.ipsdn_opt.probe.service;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

import org.springframework.stereotype.Service;

import com.ipsdn_opt.probe.component.ssh.ShellManager;
import com.ipsdn_opt.probe.component.ssh.ShellWorker;
import com.ipsdn_opt.probe.model.LinkFactor;
import com.ipsdn_opt.probe.model.LinkNode;
import com.ipsdn_opt.probe.model.NetIp;
import com.ipsdn_opt.probe.model.Node;
import com.ipsdn_opt.probe.model.NodeFactor;
import com.ipsdn_opt.probe.model.Probe;
import com.ipsdn_opt.probe.model.ProbeActivity;
import com.ipsdn_opt.probe.model.RegexTest;
import com.ipsdn_opt.probe.model.Response;
import com.ipsdn_opt.probe.model.CommandServer;
import com.ipsdn_opt.probe.model.IpStatus;
import com.ipsdn_opt.probe.repository.LinkFactorRepository;
import com.ipsdn_opt.probe.repository.LinkNodeRepository;
import com.ipsdn_opt.probe.repository.NodeFactorRepository;
import com.ipsdn_opt.probe.repository.NodeRepository;
import com.ipsdn_opt.probe.repository.ProbeActivityRepository;
import com.ipsdn_opt.probe.repository.ProbeRepository;
import com.ipsdn_opt.probe.repository.CommandServerRepository;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.net.util.SubnetUtils;

@Service
@Slf4j
public class measureSvc {
    @Autowired
    private ApplicationArguments applicationArguments;
    @Autowired
    private ProbeRepository probeRepository;
    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private LinkNodeRepository linkNodeRepository;
    @Autowired
    private LinkFactorRepository linkFactorRepository;
    @Autowired
    private CommandServerRepository commandServerRepository;
    @Autowired
    private ProbeActivityRepository probeActivityRepository;
    @Autowired
    private NodeFactorRepository nodeFactorRepository;
    @Autowired
    ShellManager sm;
    @Autowired
    EntityManager em;

    public void argumentPrint() {
        List<String> optionValues = applicationArguments.getOptionValues("probe.number");
        System.out.println("probe number : " + optionValues.get(0));
    }

    public Response owampCheck() {
        Response response = new Response(false, "", null);
        long probeNumber = Long.parseLong(applicationArguments.getOptionValues("probe.number").get(0));
        Probe probe = probeRepository.findById(probeNumber).get();
        CommandServer server = probe.getCommnadserver();
        if(server!=null) {
            server.setPassword(commandServerRepository.findPasswordById(server.getId()));
        }
        JSch jsch = new JSch();
        Session session = null;
        Channel channel = null;
        Integer passedSec = null;
        if(server==null) return new Response(false, String.format("Server of owampd is not defined for probe %d (%s:%d).", probe.getId(), probe.getIpaddr(), probe.getPort()), null);
        try {
            ShellWorker sw = sm.getShellWorker(server, RandomStringUtils.randomAlphanumeric(10));
            sw.getSession();
            /*
            Query qry = em.createNativeQuery("select timestampdiff(second, measureddatetime, now()) secdiff "
                    + "from probeactivity where probe_id=:probe_id "
                    + "order by id desc limit 1 ");
            passedSec = ((BigInteger) qry.setParameter("probe_id", probe.getId()).getSingleResult()).intValue();

            if(probe.isAutomeasurement() && passedSec > (probe.getMeasurementperiod()*3)) {
                session = jsch.getSession(server.getLoginid(), server.getIpaddr(), server.getSshport());
                session.setPassword(server.getPassword());
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                channel = session.openChannel("exec");
                ChannelExec channelExec = (ChannelExec) channel;
                channelExec.setCommand("pkill -9 twamp; /usr/local/bin/twampd -U root -f");
                channelExec.connect();
                response.setStatus(true);
                response.setMessage("The twamp is rerunned. (" + passedSec.toString() +  "sec have passed since last measurement of the probe.)");
                response.setData(null);
            }
            else {
                response.setStatus(true);
                response.setMessage("The twamp is not rerunned (Probably twamp is currently running or auto-measurement is off).");
                response.setData(null);

            }
            */
            sw.close();
        }
        catch(Exception e) {
            response.setStatus(false);
            response.setMessage("Remote Execute Fail. -> " + e.getMessage() + ", " + e.getCause() );
            response.setData(server.getLoginid() + "@" + server.getIpaddr() + ":" + server.getSshport());
        }
        finally {
            if(channel!=null) channel.disconnect();
            if(session!=null) session.disconnect();
        }
        return response;
    }

    public Response e2eLatency(LocalDateTime measuredTime, Long destNode_id) {
        long probeNumber = Long.parseLong(applicationArguments.getOptionValues("probe.number").get(0));
        Probe srcProbe = probeRepository.findById(probeNumber).get();
        CommandServer commandServer = srcProbe.getCommnadserver();
        
        List<Node> nodes = nodeRepository.findAll();
        HashMap<Long, Node> nodeMap = new HashMap<>();
        nodes.forEach(node -> {
            nodeMap.put(node.getId(), node);
        });

        Probe destProbe = probeRepository.findProbebyNodeid(destNode_id);
        List<LinkNode> links = linkNodeRepository.findOspfPathLink(srcProbe.getNode_id(), destNode_id);

        JSONObject result = new JSONObject();
        result.put("measuredTime", measuredTime);
        result.put("srcProbe", srcProbe.getPingip());
        result.put("destProbe", destProbe.getPingip());
        result.put("latency", destProbe.getId());
        List<String> paths = new ArrayList<>();
        for(LinkNode link : links) {
            paths.add(String.format("%s(%s) -> %s(%s)", nodeMap.get(link.getSnode_id()).getNodename(),
                link.getSifname(), nodeMap.get(link.getRnode_id()).getNodename(), link.getRifname()));
        }
        result.put("paths", paths);
        return new Response(true, "Remote Probe Latency Measuring Complete.", result.toMap());
    }
    
    public Response measureFactors(LocalDateTime measuredTime) {
        log.info("[NodeFactor Flow] measureSvc.measureFactors() 시작 - 측정시간: {}", measuredTime);
        long probeNumber = Long.parseLong(applicationArguments.getOptionValues("probe.number").get(0));
        Probe sendProbe = probeRepository.findById(probeNumber).get();
        log.info("[NodeFactor Flow] Probe 조회 완료 - Probe ID: {}, Node ID: {}", sendProbe.getId(), sendProbe.getNode_id());
        List<Node> nodes = nodeRepository.findAll();
        HashMap<Long, Node> nodeMap = new HashMap<>();
        nodes.forEach(node -> {
            nodeMap.put(node.getId(), node);
        });
        List<LinkNode> links = linkNodeRepository.findLinkNodeByNodeid(sendProbe.getNode_id());
        log.info("[NodeFactor Flow] LinkNode 조회 완료 - 총 {}개 링크", links.size());
        List<String> runMessages;
        try {
            log.info("[NodeFactor Flow] measureSvc.measureNow() 호출 시작");
            runMessages = measureNow(sendProbe, links, measuredTime);
            log.info("[NodeFactor Flow] measureSvc.measureNow() 완료 - 메시지 수: {}", runMessages.size());
        } catch(Exception e) {
            log.error("[NodeFactor Flow] measureSvc.measureNow() 실패: {}", e.getMessage(), e);
            return new Response(false, "Remote Measurement Command Execution Fail. -> " + e.getMessage(), null);
        }
        // JSONObject result = new JSONObject();
        // result.put("measuredTime", measuredTime);
        // result.put("sendIp", sendProbe.getPingip());
        // JSONArray jArr = new JSONArray();
        // for(LinkNode link : links) {
        //     JSONObject jObj = new JSONObject();
        //     jObj.put("sNode", nodeMap.get(link.getSnode_id()).getNodename());
        //     jObj.put("sendIf", link.getSifname());
        //     jObj.put("receiveNode", nodeMap.get(link.getRnode_id()).getNodename());
        //     jObj.put("receiveIf", link.getRifname());

        //     Probe receiveProbe = probeMap.get(link.getRnode_id());
        //     jObj.put("receiveIp", receiveProbe.getPingip());
        //     jObj.put("latency", receiveProbe.getId());
        //     jArr.put(jObj);
        // }
        //result.put("links", jArr);
        return new Response(true, "Remote Probe Latency Measuring Complete.", runMessages);
    }
    public List<String> measureNow(Probe sendProbe, List<LinkNode> links, LocalDateTime measureDateTime) throws Exception {
        log.info("[NodeFactor Flow] measureSvc.measureNow() 시작 - Probe ID: {}, 측정시간: {}, 링크 수: {}", 
            sendProbe.getId(), measureDateTime, links.size());
        CommandServer cmdServer = sendProbe.getCommnadserver();
        List<Probe> probes = probeRepository.findAll();
        HashMap<Long, Probe> probeMap = new HashMap<>();
        List<Node> nodes = nodeRepository.findAll();
        HashMap<Long, Node> nodeMap = new HashMap<>();
        List<LinkFactor> linkfactors = new ArrayList<>();
        probes.forEach(probe -> {
            probeMap.put(probe.getNode_id(), probe);
        });
        nodes.forEach(node -> {
            nodeMap.put(node.getId(), node);
        });

        if(cmdServer!=null) {
            cmdServer.setPassword(commandServerRepository.findPasswordById(cmdServer.getId()));
            log.info("[NodeFactor Flow] CommandServer 설정 완료 - Server: {}", cmdServer.getIpaddr());
        }
        else {
            log.error("[NodeFactor Flow] CommandServer가 정의되지 않음 - Probe ID: {}", sendProbe.getId());
            throw new Exception(String.format("Server of owampd is not defined for probe %d (%s:%d).", sendProbe.getId(), sendProbe.getIpaddr(), sendProbe.getPort()));
        }

        Node sendNode = nodeMap.get(sendProbe.getNode_id());
        log.info("[NodeFactor Flow] Node 정보 조회 완료 - Node: {}, MgmtAddr: {}", 
            sendNode.getNodename(), sendNode.getMgmtaddr());
        List<Thread> threads = new ArrayList<>();
        List<String> runMessages = new ArrayList<>();
        for(LinkNode link : links) {
            Thread linkThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Probe receiveProbe = probeMap.get(link.getRnode_id());
                    Node receiveNode = nodeMap.get(link.getRnode_id());
                    ShellWorker sw = null;
                    try {
                        sw = sm.getShellWorker(cmdServer, RandomStringUtils.randomAlphanumeric(10));
                        sw.getSession();
                        // String namespace = "";
                        // if(cmdServer.getNamespace()!=null) namespace = "ip netns exec " + cmdServer.getNamespace();
                        // String cmd = String.format("%s twping %s -c10 -b.0000001 -M", namespace, receiveProbe.getPingip());
                        String cmd = String.format("twping %s -c10 -i0.01 -b.0000001 -M", receiveProbe.getPingip());
                        String strRet = sw.sendCommand(cmd, 15000);
                        LinkFactor lf = shellReturnValueParsing(strRet, link, measureDateTime);
                        if(lf.getJitter()!=null && lf.getLatency()!=null) {
                            linkfactors.add(lf);
                            runMessages.add(
                                String.format("[%s->%s : %s->%s] Measuring of link-factors is completed.", 
                                    sendNode.getNodename(), receiveNode.getNodename(), sendProbe.getPingip(), receiveProbe.getPingip())
                            );
                        }
                        else {
                            runMessages.add(
                                String.format("[%s->%s : %s->%s] Measuring of link-factors is failed. (time-out)", 
                                    sendNode.getNodename(), receiveNode.getNodename(), sendProbe.getPingip(), receiveProbe.getPingip())
                            );
                        }
                    } catch(Exception e) {
                        runMessages.add(
                            String.format("From[%s] Receive Node [%s:%s] %s", cmdServer.getIpaddr(), receiveNode.getNodename(), receiveProbe.getPingip(), e.getMessage())
                        );
                        log.info(String.format("From[%s] Receive Node [%s:%s] %s", cmdServer.getIpaddr(), receiveNode.getNodename(), receiveProbe.getPingip(), e.getMessage()));
                    }
                    finally {
                        if(sw!=null) sw.close();
                    }

                }
            });
            threads.add(linkThread);
            linkThread.start();
        }

        NodeFactor nf = new NodeFactor(sendNode.getId(), measureDateTime);
        log.info("[NodeFactor Flow] NodeFactor 객체 생성 - Node ID: {}, 측정시간: {}", sendNode.getId(), measureDateTime);
        Map<String, String> factors = new HashMap<>();
        factors.put("cpu", "iso.3.6.1.4.1.36673.100.1.2.5.1.3.1");
        factors.put("memory", "iso.3.6.1.4.1.36673.100.1.2.4.1.2.1");
        log.info("[NodeFactor Flow] SNMP 측정 시작 - Node: {}, OID: CPU={}, Memory={}", 
            sendNode.getNodename(), factors.get("cpu"), factors.get("memory"));
        factors.forEach((key, value) -> {
            Thread nodeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ShellWorker sw = null;
                    try {
                        sw = sm.getShellWorker(cmdServer, RandomStringUtils.randomAlphanumeric(10));
                        sw.getSession();
                        String cmd = String.format("snmpwalk -v2c -c sdnkoren %s %s", sendNode.getMgmtaddr(), value);
                        log.info("[NodeFactor Flow] SNMP 명령 실행 - Node: {}, Factor: {}, Command: {}", 
                            sendNode.getNodename(), key, cmd);
                        
                        String strRet = sw.sendCommand(cmd, 7000);
                        log.debug("[NodeFactor Flow] SNMP 명령 결과 - Node: {}, Factor: {}, Result: {}", 
                            sendNode.getNodename(), key, strRet);
                        String strVal = strRet.split(":")[1];
                        strVal = strVal.split("\r\n")[0];
                        strVal = strVal.strip();
                        //if(!strVal.matches("[+-]?\\d*(\\.\\d+)")) { //[+-]?: + 또는 - 가 올수도 있고 안올수도 있고 // \d*: 0~9까지의 숫자가 0번이상 (\d+: 숫자가 한번이상) //(\.\d+): .과 한개이상숫자, .이 들어가면 숫자는 1개이상 반드시들어감 .과 뒤따른 숫자는 한그룹
                            //소수형이 아닌 경우
                        if(strVal.matches("[0-9]+[\\.]?[0-9]*")) {
                            //숫자값인 경우 (정수형, 실수형)
                            //지수형 [0-9]*[\.]?[0-9]*[Ee][-]?[0-9]+
                            float fVal;
                            if(key.equals("cpu")) {
                                fVal = Float.parseFloat(strVal)/10000;
                                nf.setCpuusage(fVal);
                                log.info("[NodeFactor Flow] CPU 값 파싱 완료 - Node: {}, 원본값: {}, 변환값: {}%", 
                                    sendNode.getNodename(), strVal, fVal);
                            }
                            else {
                                fVal = Float.parseFloat(strVal)/100;
                                nf.setMemusage(fVal);
                                log.info("[NodeFactor Flow] Memory 값 파싱 완료 - Node: {}, 원본값: {}, 변환값: {}%", 
                                    sendNode.getNodename(), strVal, fVal);
                            }
                            runMessages.add(String.format("Node [%s:%s] Measuring of node-factors(%s) is completed.", sendNode.getNodename(), sendNode.getMgmtaddr(), key));
                        }
                        else {
                            log.warn("[NodeFactor Flow] SNMP 값 파싱 실패 - Node: {}, Factor: {}, 값: {}", 
                                sendNode.getNodename(), key, strVal);
                            runMessages.add(String.format("Node [%s:%s] Measuring of node-factors is Fail. => %s", sendNode.getNodename(), sendNode.getMgmtaddr(), strVal));
                            log.info(String.format("Node [%s:%s] Measuring of node-factors is Fail. => %s", sendNode.getNodename(), sendNode.getMgmtaddr(), strVal));
                        }
                    } catch(Exception e) {
                        log.error("[NodeFactor Flow] SNMP 명령 실행 예외 - Node: {}, Factor: {}, Error: {}", 
                            sendNode.getNodename(), key, e.getMessage(), e);
                        runMessages.add(String.format("Error: From[%s] Node [%s:%s] %s => %s", cmdServer.getIpaddr(), key, sendNode.getNodename(), sendNode.getMgmtaddr(), e.getMessage()));
                    }
                    finally {
                        if(sw!=null) sw.close();
                    }
                }
            });
            threads.add(nodeThread);
            nodeThread.start();
        });
        for(Thread thread : threads) {
            try {
                thread.join();
            }
            catch(Exception e) {
                throw e;
            }
        }
        log.info("[NodeFactor Flow] DB 저장 시작 - NodeFactor: CPU={}%, Memory={}%", 
            nf.getCpuusage(), nf.getMemusage());
        nodeFactorRepository.save(nf);
        log.info("[NodeFactor Flow] DB INSERT 완료 - NodeFactor 저장 성공 (Node ID: {}, 측정시간: {})", 
            nf.getNode_id(), nf.getMeasureddatetime());
        linkFactorRepository.saveAll(linkfactors);
        ProbeActivity pa = new ProbeActivity(sendProbe.getId(), measureDateTime);
        probeActivityRepository.save(pa);

        return runMessages;
    }
    public LinkFactor shellReturnValueParsing(String strRet, LinkNode link, LocalDateTime measureDateTime) throws Exception {
        HashMap<String, Double> factors = new HashMap<>();
        String[] lines = strRet.split("\r\n");
        for(String l : lines) {
            String[] words = l.split("\t| ");
            for(int i=0; i<words.length; i++) {
                String word = words[i].strip();
                if(word.equals("LOST") || word.equals("MEDIAN_FWD") || word.equals("PDV_FWD") ||
                    word.equals("MAXTTL_FWD") || word.equals("MEDIAN_BCK") || word.equals("PDV_BCK") ||
                    word.equals("MEDIAN") || word.equals("PDV")) {
                    int validx = i + 1;
                    if(validx >= words.length) throw new Exception("Shell Return Value Error : " + l);
                    try {
                        factors.put(word, Double.parseDouble(words[validx]));
                    } catch(Exception e) {
                        throw new Exception("NonNumberic Value : " + l + "(" + words[validx] + ") : " + e);
                    }
                }
            }
        }

        LinkFactor lf = new LinkFactor(link.getId(), measureDateTime);
        Double val = factors.get("LOST");
        lf.setLost(val==null?null:(int)Math.round(val));
        val = factors.get("MAXTTL_FWD");
        lf.setHops(val==null?null:(255-(int)Math.round(val)));
        // 보정
        Double val1 = factors.get("MEDIAN_FWD");
        Double val2 = factors.get("MEDIAN_BCK");
        val = factors.get("MEDIAN");
        Double jit1 = factors.get("PDV_FWD");
        Double jit = factors.get("PDV");
        if(val1==null || val1<0 || val2==null || val2<0) {
            lf.setLatency(val==null?null:val/2);
            lf.setCorrected(true);
            lf.setJitter(jit/2);
        }
        else {
            lf.setLatency(val1);
            lf.setJitter(jit1);
        }

        // val1 = factors.get("PDV_FWD");
        // val2 = factors.get("PDV_BCK");
        // val = factors.get("PDV");
        // if(val1==null || val1<0 || val2==null || val2<0) {
        //     lf.setJitter(val==null?null:val/2);
        //     lf.setJittercorrected(true);
        // }
        // else lf.setJitter(val1);

        return lf;
    }

    public Response regexTest(RegexTest regex) {
        if(regex.getInputString()==null) regex.setInputString("");
        Boolean isMatches = regex.getInputString().matches(regex.getRegex());
        return new Response(true, "String '" + regex.getInputString() + "' for '" + regex.getRegex() + "' is ", isMatches);
    }

    public Response pingCheck(List<String> netipList) {
        List<IpStatus> ipStats = new ArrayList<>();
        List<String> runMessages = new ArrayList<>();
        Response response = new Response(true, "IP Alive Check Complete.", null);
        List<Thread> threads = new ArrayList<>();
        for(String netip : netipList) {
            SubnetUtils subnetUtils = new SubnetUtils(netip);
            for(String ip : subnetUtils.getInfo().getAllAddresses()) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean pingstat = false;
                        try {
                            InetAddress pingcheck = InetAddress.getByName(ip);
                            if(pingcheck.isReachable(1000)) pingstat = true;
                        }
                        catch(Exception e) {
                            runMessages.add("IP(" + ip + ") Alive Check Fail. => " + e.getMessage());
                        }
                        finally {
                            IpStatus istat = new IpStatus(ip, pingstat);
                            ipStats.add(istat);
                        }
                    }
                });
                threads.add(thread);
                thread.start();
            }
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            }
            catch(Exception e) {
                response.setStatus(false);;
                response.setMessage("IP Alive Check Thread Fail -> " + e.getMessage());
            }
        });
        
        if(runMessages.size()>0) response.setMessage(response.getMessage() + "(" + runMessages.toString() + ")");
        response.setData(ipStats);
        return response;
    }
}
