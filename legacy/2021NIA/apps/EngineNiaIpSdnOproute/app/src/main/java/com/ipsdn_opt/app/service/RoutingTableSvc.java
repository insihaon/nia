package com.ipsdn_opt.app.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipsdn_opt.app.component.ssh.FrrShellManager;
import com.ipsdn_opt.app.component.ssh.FrrShellWorker;
import com.ipsdn_opt.app.model.AdjRouter;
import com.ipsdn_opt.app.model.BgpTable;
import com.ipsdn_opt.app.model.DistanceCost;
import com.ipsdn_opt.app.model.E2eNode;
import com.ipsdn_opt.app.model.Interface;
import com.ipsdn_opt.app.model.Link;
import com.ipsdn_opt.app.model.Node;
import com.ipsdn_opt.app.model.OspfDb;
import com.ipsdn_opt.app.model.OspfDbRouter;
import com.ipsdn_opt.app.model.LinkForCalc;
import com.ipsdn_opt.app.model.OspfRoute;
import com.ipsdn_opt.app.model.OspfPathSet;
import com.ipsdn_opt.app.model.Path;
import com.ipsdn_opt.app.model.Response;
import com.ipsdn_opt.app.model.RoutePath;
import com.ipsdn_opt.app.model.RoutePathSet;
import com.ipsdn_opt.app.model.Router;
import com.ipsdn_opt.app.model.RouterInfo;
import com.ipsdn_opt.app.model.ShellCommand;
import com.ipsdn_opt.app.model.ViewOspfPath;
import com.ipsdn_opt.app.model.ViewOspfPathSet;
import com.ipsdn_opt.app.repository.BgpTableRepository;
import com.ipsdn_opt.app.repository.E2eNodeRepository;
import com.ipsdn_opt.app.repository.InterfaceRepository;
import com.ipsdn_opt.app.repository.LinkRepository;
import com.ipsdn_opt.app.repository.NodeRepository;
import com.ipsdn_opt.app.repository.OspfDbRepository;
import com.ipsdn_opt.app.repository.OspfDbRouterRepository;
import com.ipsdn_opt.app.repository.OspfRouteRepository;
import com.ipsdn_opt.app.repository.RoutePathSetRepository;
import com.ipsdn_opt.app.repository.RouterInfoRepository;
import com.ipsdn_opt.app.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoutingTableSvc {
    @Autowired
    RouterInfoRepository routerInfoRepository;
    @Autowired
    OspfDbRepository ospfDbRepository;
    @Autowired
    OspfDbRouterRepository ospfDbRouterRepository;
    @Autowired
    BgpTableRepository bgpTableRepository;
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    InterfaceRepository interfaceRepository;
    @Autowired
    LinkRepository linkRepository;
    @Autowired
    OspfRouteRepository ospfRouteRepository;
    @Autowired
    E2eNodeRepository e2eNodeRepository;
    @Autowired
    RoutePathSetRepository routePathSetRepository;
    @Autowired
    EntityManager em;

    public List<Response> collectRoutingTable(ShellCommand shcmd) {
        RouterInfo ri = routerInfoRepository.findRouterInfoByRouterName(shcmd.getRouter_name());
        shcmd.setPrompt(ri.getPrompt());
        FrrShellManager sm = new FrrShellManager();
        FrrShellWorker sw = sm.shellConnect(ri.getLoginid(), ri.getPassword(), ri.getIpaddress(), ri.getPort());
        sw.skipOutputMessage(0, shcmd.getTime_out(), shcmd.getPrompt());

        List<Response> responses = new ArrayList<>();
        responses.add(getOspfDatabase(sw, shcmd));
        shcmd.getCommand().clear();
        shcmd.getCommand().add("show ip bgp community 9270:17000 json");
        responses.add(getBgpTable(sw, shcmd));

        sm.shellClose();

        return responses;
    }
    public Response getOspfDatabase(FrrShellWorker sw, ShellCommand shcmd) {
        String result = "";
        for(String cmd : shcmd.getCommand()) {
            result += sw.sendCommandForVtysh(cmd, shcmd.getTime_out(), shcmd.getPrompt());
        }
        String jsonString = result.substring(result.indexOf("{"), result.lastIndexOf("}")+1);
        JSONObject jObj = new JSONObject(jsonString);
        Object obj = Utils.jsonObjectSearch(jObj, "routerLinkStates");
        JSONArray jArr = new JSONArray();
        if(obj instanceof JSONObject) jArr.put(obj);
        else if(obj instanceof JSONArray) jArr = (JSONArray)obj;

        List<OspfDb> ospfDbs = new ArrayList<>();
        for(int i=0; i<jArr.length(); i++) {
            JSONObject jo = jArr.getJSONObject(i);
            if(jo.getString("lsId").equals("61.252.55.131")) continue;
            ospfDbs.add(new OspfDb(jo.getString("lsId"), jo.getString("advertisedRouter")));
        }

        ospfDbRouterRepository.deleteAll();
        ospfDbRepository.deleteAll();
        ospfDbRepository.saveAll(ospfDbs);

        List<OspfDbRouter> ospfDbRouters = new ArrayList<>();
        for(OspfDb od : ospfDbs) {
            String cmd = "show ip ospf database router " + od.getLinkstateid() + " json";
            result = sw.sendCommandForVtysh(cmd, shcmd.getTime_out(), shcmd.getPrompt());
            jsonString = result.substring(result.indexOf("{"), result.lastIndexOf("}")+1);
            jObj = new JSONObject(jsonString);
            JSONObject jLinks = (JSONObject) Utils.jsonObjectSearch(jObj, "routerLinks");
            Iterator<?> links = jLinks.keys();

            while(links.hasNext()) {
                String link = (String)links.next();
                JSONObject jLink = jLinks.getJSONObject(link);
                if(!jLink.has("neighborRouterId")) continue;
                if(jLink.getString("neighborRouterId").equals("61.252.55.131")) continue;
                OspfDbRouter ospfDbRouter = new OspfDbRouter(od.getLinkstateid(), jLink.getString("neighborRouterId"));
                ospfDbRouter.setIfnetaddr(jLink.getString("routerInterfaceAddress"));
                ospfDbRouter.setTos0metric(jLink.getInt("tos0Metric"));
                ospfDbRouters.add(ospfDbRouter);
            }

        }
        ospfDbRouterRepository.saveAll(ospfDbRouters);

        return new Response(true, "OSPF Database Load Complete.", null);
    }
    public Response getBgpTable(FrrShellWorker sw, ShellCommand shcmd) {
        String result = "";
        for(String cmd : shcmd.getCommand()) {
            result += sw.sendCommandForVtysh(cmd, shcmd.getTime_out(), shcmd.getPrompt());
        }
        String jsonString = result.substring(result.indexOf("{"), result.lastIndexOf("}")+1);
        JSONObject jObj = new JSONObject(jsonString);
        JSONObject jRoutes = (JSONObject) Utils.jsonObjectSearch(jObj, "routes");
        Iterator<?> routes = jRoutes.keys();
        List<BgpTable> bgpTables = new ArrayList<>();
        while(routes.hasNext()) {
            String route = (String)routes.next();
            JSONArray jArrRoute = jRoutes.getJSONArray(route);

            for(int i=0; i<jArrRoute.length(); i++) {
                JSONObject jRoute = jArrRoute.getJSONObject(i);
                if(!jRoute.has("nexthops")) continue;
                if(!jRoute.has("bestpath")) continue;
                if(!jRoute.has("valid")) continue;
                if(!jRoute.getBoolean("bestpath")) continue;
                if(!jRoute.getBoolean("valid")) continue;
                JSONArray jArrNexthops = jRoute.getJSONArray("nexthops");
                for(int j=0; j<jArrNexthops.length(); j++) {
                    JSONObject jNexthop = jArrNexthops.getJSONObject(j);
                    bgpTables.add(new BgpTable(route, jNexthop.getString("ip")));
                }
                
            }
        }
        bgpTableRepository.deleteAll();
        bgpTableRepository.saveAll(bgpTables);
        return new Response(true, "BGP Table Load Complete.", null);
    }

    public Response calculateOspfPath() {
        List<OspfDbRouter> ospfDbRouters = ospfDbRouterRepository.findAll();
        List<Link> links = linkRepository.findAll();
        HashMap<Long, Link> linkMap = new HashMap<>();
        links.forEach(l -> {
            linkMap.put(l.getSif_id(), l);
        });
        Response response = arraySettingForAlgorithm(ospfDbRouters, linkMap);
        if(!response.isStatus()) return response;
        List<LinkForCalc> calcLinks = (List<LinkForCalc>)response.getData();
        response = dijkstraAlgorithm(calcLinks, linkMap);
        if(!response.isStatus()) return response;
        response = ospfRouteDbUpdate((List<Router>)response.getData(), links);

        return response;
    }
    public Response arraySettingForAlgorithm(List<OspfDbRouter> ospfDbRouters, HashMap<Long, Link> linkMap) {
        List<Node> nodes = nodeRepository.findAll();
        List<Interface> interfaces = interfaceRepository.findInterfaceByNotNullAddress();
        HashMap<String, Node> nodeAddrMap = new HashMap<>();
        HashMap<Long, Node> nodeIdMap = new HashMap<>();

        nodes.forEach(node -> {
            nodeAddrMap.put(node.getLoopbackaddr(), node);
            nodeIdMap.put(node.getId(), node);
        });
        for(Interface i : interfaces) {
            // SubnetUtils ifnetaddr = new SubnetUtils(i.getIpaddr());
            // ifnetaddr.setInclusiveHostCount(true);
            // SubnetInfo ifnetInfo = ifnetaddr.getInfo();
            // String strAddr_start = ifnetInfo.getLowAddress();
            // String strAddr_end = ifnetInfo.getHighAddress();
            //System.out.println(String.format("%s: %s -> %s", i.getIpaddr(), strAddr_start, strAddr_end));
            String[] ifaddr = i.getIpaddr().split("/");
            Node node = nodeIdMap.get(i.getNode_id());
            node.getIfMap().put(ifaddr[0], i);
        }

        // 1. 인접리스트를 이용한 그래프 초기화
        List<LinkForCalc> calcLinks = new ArrayList<>();
        HashMap<String, LinkForCalc> calcLinkMap = new HashMap<>();
        for(OspfDbRouter odr : ospfDbRouters) {
            Node node = nodeAddrMap.get(odr.getLinkstateid());
            if(node == null) {
                log.info(String.format("LinkStateId('%s')에 해당하는 라우터가 없습니다.", odr.getLinkstateid()));
                continue;
            }

            LinkForCalc calcLink = calcLinkMap.get(odr.getLinkstateid());
            if(calcLink==null) {
                calcLink = new LinkForCalc(node.getId());
                calcLinkMap.put(odr.getLinkstateid(), calcLink);
                calcLinks.add(calcLink);
            }

            Node adjNode = nodeAddrMap.get(odr.getNrouteraddr());
            if(adjNode == null) {
                log.info(String.format("인접라우터('%s')에 해당하는 라우터가 없습니다.", odr.getNrouteraddr()));
                continue;
            }
            Interface adjIf = node.getIfMap().get(odr.getIfnetaddr()); //인접라우터로 연결된 자신의 I/F
            if(adjIf == null) {
                log.info(String.format("I/F주소 '%s'가 없습니다.", odr.getIfnetaddr()));
            }
            // String ifName = adjIf.getIfname();
            // if(adjIf.getHwifname()!=null) ifName = String.format("%s(%s)", ifName, adjIf.getHwifname());
            // ospfLink.getAdjRouters().add(new AdjRouter(adjNode.getNodename(), ifName, odr.getTos0metric()));
            long if_id = adjIf.getId();
            long hwif_id = if_id;
            if(adjIf.getHwif_id()!=null) hwif_id = adjIf.getHwif_id();
            calcLink.getAdjRouters().add(new AdjRouter(adjNode.getId(), if_id, hwif_id, odr.getTos0metric()));
            Link link = linkMap.get(hwif_id);
            if(link!=null) {
                link.setOspfmetric(odr.getTos0metric());
                if(if_id!=hwif_id) link.setVsif_id(if_id);
            }
        }

      
        return new Response(true, "Array Setting is completed", calcLinks);
    }
    public Response dijkstraAlgorithm(List<LinkForCalc> calcLinks, HashMap<Long, Link> linkMap) {

        List<Router> routers = new ArrayList<>();

        /* 라우터명으로 처리하는 로직
        for(OspfLinkInfo o : ospfLinkInfos) {
            if(sNode!=null) {
                if(!o.getRouterName().toLowerCase().equals(sNode.toLowerCase())) continue;
            }
            Router r = new Router(o.getRouterName());
            ospfLinkInfos.forEach(d -> {
                r.getDistances().add(new DistanceCost(d.getRouterName()));
            });
            routers.add(r);
        }

        HashMap<String, OspfLinkInfo> ospfLinkMap = new HashMap<>();
        ospfLinkInfos.forEach(r -> {
            ospfLinkMap.put(r.getRouterName(), r);
        });

        for(Router r : routers) {
            HashMap<String, DistanceCost> distMap = new HashMap<>();
            r.getDistances().forEach(d -> {
                distMap.put(d.getDestRouter(), d);
            });
            ospfLinkInfos.forEach(o -> {
                o.setVisited(false);
            });

            //시작하는 자기자신은 0으로 초기화
            distMap.get(r.getRouterName()).setTotCost(0);

            // 4. 다익스트라 알고리즘을 진행한다.
            // 모든 노드을 방문하면 종료하기 때문에, 노드의 개수 만큼만 반복을 한다.
            for (int i = 0; i <ospfLinkInfos.size(); i++) {
                // 4 - 1. 현재 거리 비용 중 최소인 지점을 선택한다.
                // 해당 노드가 가지고 있는 현재 비용.
                int nodeValue = Integer.MAX_VALUE;
                // 해당 노드의 인덱스(번호).
                String node = r.getRouterName();
                // 인덱스 0은 생각하지 않기 때문에 0부터 반복을 진행한다.
                for(OspfLinkInfo o : ospfLinkInfos) {
                    DistanceCost dist = distMap.get(o.getRouterName());
                    if(!o.isVisited() && dist.getTotCost() < nodeValue) {
                        nodeValue = dist.getTotCost();
                        node = dist.getDestRouter();
                    }
                }
                // 최종 선택된 노드를 방문처리 한다.
                ospfLinkMap.get(node).setVisited(true);

                // 4 - 2. 해당 지점을 기준으로 인접 노드의 최소 거리 값을 갱신한다.
                for(AdjRouter ar : ospfLinkMap.get(node).getAdjRouters()) {
                    DistanceCost adjDist = distMap.get(ar.getRouterName());
                    DistanceCost curDist = distMap.get(node);

                    if(adjDist.getTotCost() > curDist.getTotCost() + ar.getMetric()) {
                        adjDist.getPaths().clear();
                        curDist.getPaths().forEach(p -> {
                            adjDist.getPaths().add(p);
                        });
                        String addPath = ar.getRouterName();
                        if(ar.getInterfaceName().length()>0) addPath += "[" + ar.getInterfaceName() + "]";
                        adjDist.getPaths().add(addPath);
                        adjDist.setTotCost(curDist.getTotCost() + ar.getMetric());
                    }
                }
            }

            // 5. 최종 비용을 출력한다.
            // for (DistanceCost dist : r.getDistances()) {
            //     if (dist.getTotCost() == Integer.MAX_VALUE) {
            //         System.out.println(r.getRouterName() + "->" + dist.getDestRouter() + " : INF");
            //         dist.getPaths().forEach(p -> {
            //             System.out.println("        " + p);
            //         });
            //     } else {
            //         System.out.println(r.getRouterName() + "->" + dist.getDestRouter() + " : " + dist.getTotCost());
            //         dist.getPaths().forEach(p -> {
            //             System.out.println("        " + p);
            //         });
            //     }
            // }
        }
        for(Router r : routers) {
            if(rNode!=null) {
                for(int i=0; i<r.getDistances().size(); i++) {
                    DistanceCost d = r.getDistances().get(i);
                    if(!d.getDestRouter().toLowerCase().equals(rNode.toLowerCase())) {
                        r.getDistances().remove(d);
                        i--;
                    }
                }
            }
        }
*/        
        
        // ID로 처리하는 로직 - 전체 라우터 처리
        for(LinkForCalc o : calcLinks) {
            Router r = new Router(o.getNode_id());
            calcLinks.forEach(dest -> {
                r.getDistances().add(new DistanceCost(dest.getNode_id()));
            });
            routers.add(r);
        }

        HashMap<Long, LinkForCalc> calcLinkMap = new HashMap<>();
        calcLinks.forEach(r -> {
            calcLinkMap.put(r.getNode_id(), r);
        });  
        for(Router r : routers) {
            r.getDistances().forEach(d -> {
                r.getDestMap().put(d.getDestNode_id(), d);
            });
            calcLinks.forEach(l -> {
                l.setVisited(false);
            });

            //시작하는 자기자신은 0으로 초기화
            r.getDestMap().get(r.getNode_id()).setTotCost(0);

            // 4. 다익스트라 알고리즘을 진행한다.
            // 모든 노드을 방문하면 종료하기 때문에, 노드의 개수 만큼만 반복을 한다.
            for (int i = 0; i <calcLinks.size(); i++) {
                // 4 - 1. 현재 거리 비용 중 최소인 지점을 선택한다.
                // 해당 노드가 가지고 있는 현재 비용.
                int nodeValue = Integer.MAX_VALUE;
                // 해당 노드의 인덱스(번호).
                long node_id = r.getNode_id();
                // 인덱스 0은 생각하지 않기 때문에 0부터 반복을 진행한다.
                for(LinkForCalc o : calcLinks) {
                    DistanceCost dist = r.getDestMap().get(o.getNode_id());
                    if(!o.isVisited() && dist.getTotCost() < nodeValue) {
                        nodeValue = dist.getTotCost();
                        node_id = dist.getDestNode_id();
                    }
                }
                // 최종 선택된 노드를 방문처리 한다.
                calcLinkMap.get(node_id).setVisited(true);

                // 4 - 2. 해당 지점을 기준으로 인접 노드의 최소 거리 값을 갱신한다.
                for(AdjRouter ar : calcLinkMap.get(node_id).getAdjRouters()) {
                    DistanceCost adjDist = r.getDestMap().get(ar.getNode_id());
                    DistanceCost curDist = r.getDestMap().get(node_id);

                    if(adjDist.getTotCost() > curDist.getTotCost() + ar.getMetric()) {
                        adjDist.getPaths().clear();
                        curDist.getPaths().forEach(p -> {
                            adjDist.getPaths().add(p);
                        });
                        Link addLink = linkMap.get(ar.getHwinterface_id());
                        if(addLink==null) {
                            return new Response(false, String.format("I/F ID '%d'에 해당하는 링크는 없습니다.", ar.getHwinterface_id()), null);
                        }
                        // addPath.setOsif_id(ar.getInterface_id());
                        adjDist.getPaths().add(new Path(ar.getInterface_id(), addLink));
                        adjDist.setTotCost(curDist.getTotCost() + ar.getMetric());
                    }
                }
            }
        }

        return new Response(true, "OSPF알고리즘 수행완료.", routers);
    }
    public Response ospfRouteDbUpdate(List<Router> routers, List<Link> links) {
        List<E2eNode> e2enodes = e2eNodeRepository.findAllE2eNodeUsaged();
        List<OspfRoute> ospfRoutes = new ArrayList<>();
        List<RoutePathSet> routePathSets = routePathSetRepository.findAll();
        HashMap<Long, RoutePathSet> mapRoutePathSets = new HashMap<>();
        routePathSets.forEach(rps -> {
            mapRoutePathSets.put(rps.getId(), rps);
        });
        HashMap<Long, Router> routerMap = new HashMap<>();
        routers.forEach(r -> {
            routerMap.put(r.getNode_id(), r);
        });
        // [기존] OSPF경로세트 생성로직
        // for(E2eNode en : e2enodes) {
        //     Router r = routerMap.get(en.getSnode_id());
        //     if(r==null) continue;
        //     DistanceCost d = r.getDestMap().get(en.getRnode_id());
        //     if(d==null) continue;
        //     OspfPath op = new OspfPath(en.getId());
        //     int pathser = 0;
        //     for(Path path : d.getPaths()) {
        //         op.getOspfpathsets().add(new OspfPathSet(path.getSendif_id(), ++pathser, path.getLink().getId()));
        //     }
        //     ospfPaths.add(op);
        // }
        // ospfPathRepository.deleteAll();
        // ospfPathRepository.saveAll(ospfPaths);
        // linkRepository.saveAll(links);

        // [신규] OSPF경로세트 생성로직
        for(E2eNode en : e2enodes) {
            Router r = routerMap.get(en.getSnode_id());
            if(r==null) continue;
            DistanceCost d = r.getDestMap().get(en.getRnode_id());
            if(d==null) continue;
            int pathser = 0;
            List<RoutePath> ospf_rplist = new ArrayList<>();
            for(Path path : d.getPaths()) {
                RoutePath rp = new RoutePath(++pathser, path.getLink().getId());
                ospf_rplist.add(rp);
            }
            if(ospf_rplist.size()==0) continue;
            RoutePathSet rps = findRoutePathSet(ospf_rplist, mapRoutePathSets);
            if(rps==null) {
                rps = new RoutePathSet(ospf_rplist);
                routePathSets.add(rps);
            }

            OspfRoute or = new OspfRoute(en.getId());
            or.setRoutePathSet(rps);
            ospfRoutes.add(or);
        }
        routePathSetRepository.saveAll(routePathSets);
        ospfRouteRepository.deleteAll();
        ospfRouteRepository.saveAll(ospfRoutes);
        linkRepository.saveAll(links);
        return new Response(true, "OSPF경로저장 완료", null);
    }

    public Response getOspfPath(String sNode, String rNode) {
        // String sqlString = "select @seq::=@seq+1 as id, opath.sourceNode source_node, opath.destinationNode destination_node, "
        // + "opath.pathser, opath.path from ( "
        // + "    select sn.nodename sourceNode,rn.nodename destinationNode, op.pathser pathSer, "
	    // + "    concat(psn.nodename, '(', si.ifname, ') ->', prn.nodename, '(', ri.ifname, ')') path "
        // + "    from ospfpath op "
        // + "    inner join e2enode en on op.E2eNode_Id=en.id "
        // + "    inner join node sn on en.snode_id=sn.id "
        // + "    inner join node rn on en.rnode_id=rn.id "
        // + "    inner join link l on op.link_id=l.id "
        // + "    inner join interface si on l.sif_id=si.id "
        // + "    inner join interface ri on l.rif_id=ri.id "
        // + "    inner join node psn on si.node_id=psn.id "
        // + "    inner join node prn on ri.node_id=prn.id "
        // + "    order by en.id, op.pathser "
        // + ") opath, (select @seq::=0) seq "
        // + "order by id";

        String sqlString = "select ort.id, sn.nodename source_node,rn.nodename destination_node, rp.pathser path_order, "
	        + "case "
            + "when l.vsif_id is null then "
            + "    concat(psn.nodename, '[', si.ifname, '] -> ', prn.nodename, '[', ri.ifname, ']') "
            + "else "
            + "    concat(psn.nodename, '[', vsi.ifname, '(', si.ifname, ')', '] -> ', prn.nodename, '[', ri.ifname, ']') "
            + "end path "
            + "from ospfroute ort "
            + "inner join e2enode en on ort.E2eNode_Id=en.id "
            + "inner join node sn on en.snode_id=sn.id "
            + "inner join node rn on en.rnode_id=rn.id "
            + "inner join routepathset rps on ort.routepathset_id=rps.id "
            + "inner join routepath rp on rp.routepathset_id=rps.id "
            + "inner join link l on rp.link_id=l.id "
            + "inner join interface si on l.sif_id=si.id "
            + "inner join interface ri on l.rif_id=ri.id "
            + "inner join node psn on si.node_id=psn.id "
            + "inner join node prn on ri.node_id=prn.id "
            + "left join interface vsi on l.vsif_id=vsi.id ";
        if(sNode!=null && rNode==null)
            sqlString = String.format("%s where lower(sn.nodename)='%s' ", sqlString, sNode.toLowerCase());
        else if(sNode==null && rNode!=null)
            sqlString = String.format("%s where lower(rn.nodename)='%s' ", sqlString, rNode.toLowerCase());
        else if(sNode!=null && rNode!=null)
            sqlString = String.format("%s where lower(sn.nodename)='%s' and lower(rn.nodename)='%s' ", sqlString, sNode.toLowerCase(), rNode.toLowerCase());
        sqlString += "order by en.id, rp.pathser ";

        Query query = em.createNativeQuery(sqlString);
        List<Object[]> results = query.getResultList();
        List<ViewOspfPath> ospfPaths = new ArrayList<>();
        HashMap<Long, ViewOspfPath> ospfpathMap = new HashMap<>();
        for(Object[] row : results) {
            long id = ((BigInteger)row[0]).longValue();
            ViewOspfPath op = ospfpathMap.get(id);
            if(op==null) {
                op = new ViewOspfPath((String)row[1], (String)row[2]);
                ospfpathMap.put(id, op);
                ospfPaths.add(op);
            }
            op.getPaths().add(new ViewOspfPathSet((short)row[3], (String)row[4]));
        }

        // List<Node> nodes = nodeRepository.findAll();
        // List<Interface> interfaces = interfaceRepository.findAll();
        // List<Link> links = linkRepository.findAll();
        // List<E2eNode> e2enodes = e2eNodeRepository.findAllUsaged();
        // HashMap<Long, Node> nodeMap = new HashMap<>();
        // HashMap<Long, Interface> interfaceMap = new HashMap<>();
        // HashMap<Long, Link> linkMap = new HashMap<>();
        // HashMap<Long, E2eNode> e2eNodeMap = new HashMap<>();
        // nodes.forEach(node -> {
        //     nodeMap.put(node.getId(), node);
        // });
        // interfaces.forEach(intf -> {
        //     interfaceMap.put(intf.getId(), intf);
        // });
        // links.forEach(link -> {
        //     linkMap.put(link.getId(), link);
        // });
        // e2enodes.forEach(e2e -> {
        //     e2eNodeMap.put(e2e.getId(), e2e);
        // });

        return new Response(true, "현재 OSPF경로 조회완료", ospfPaths);
    }
    public RoutePathSet findRoutePathSet(List<RoutePath> opt_rplist, HashMap<Long, RoutePathSet> mapRoutePathSets) {
        RoutePathSet routePathSet = null;
        for(Long rps_id : mapRoutePathSets.keySet()) {
            RoutePathSet rps = mapRoutePathSets.get(rps_id);
            if(rps.getRoutePaths().size()==0) continue;
            if(rps.getRoutePaths().size()!=opt_rplist.size()) continue;

            boolean isExact = true;
            for(RoutePath opt_rp : opt_rplist) {
                boolean isExist = false;
                for(RoutePath rp : rps.getRoutePaths()) {
                    if(rp.getPathser()==opt_rp.getPathser() && 
                       rp.getLink_id()==opt_rp.getLink_id()) {
                        isExist = true;
                        break;
                    }
                }
                if(!isExist) isExact = false;
            }
            // for(int i=0; i<opt_rplist.size(); i++) {
            //     RoutePath rp = rps.getRoutePaths().get(i);
            //     RoutePath opt_rp = opt_rplist.get(i);
            //     if(rp.getPathser()==opt_rp.getPathser() && rp.getLink_id()==opt_rp.getLink_id()) {
            //         isExact = true;
            //     }
            // }
            if(isExact) {
                routePathSet = rps;
                break;
            }
        }
        return routePathSet;
    }

}
