package com.nia.alarm.ip.preprocessor.service.impl.topology;

import com.nia.alarm.ip.preprocessor.mapper.TopologyMapper;
import com.nia.alarm.ip.preprocessor.service.topology.TopologyService;
import com.nia.alarm.ip.preprocessor.vo.topology.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**

 * @author
 *
 */
@Service("TopologyService")
@Scope(value = "prototype")
public class TopologyServiceImpl implements TopologyService {
    private final Logger LOGGER = Logger.getLogger(TopologyServiceImpl.class);

    @Autowired
    private TopologyMapper topologyMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TopologyDataVo> topologyDataVoFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TopologyTmpVo> topologyTmpObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TopologyNeVo> topologyNeObjectFactoryA;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TopologyNeVo> topologyNeObjectFactoryZ;

    @Autowired
    private TopologyTmpVo topologyTmpObject;

    @Autowired
    private TopologyDataVo topologyDataVo;

    @Autowired
    private TopologyNeVo topologyNeObjecta;

    @Autowired
    private TopologyNeVo topologyNeObjectz;

    /**
     * NNI Topology 리스트 조회
     * @param map
     * @return
     */
    @Override
    public TopologyTmpVo selectTopologyList(HashMap<String, String> map){
        String ptpName = null;
        String sysName = null;

        try{
            topologyTmpObject = topologyTmpObjectFactory.getObject();
            topologyDataVo = topologyDataVoFactory.getObject();
            topologyNeObjecta = topologyNeObjectFactoryA.getObject();
            topologyNeObjectz = topologyNeObjectFactoryZ.getObject();

            ptpName = map.get("ptpName");
            sysName = map.get("sysname");

            if(ptpName == null){
                ptpName = "";
            }

            if(sysName == null){
                sysName = "";
            }

             topologyDataVo = topologyMapper.selectTopologyList(map);

            if(topologyDataVo != null){
                topologyTmpObject.setLinkId(topologyDataVo.getLinkId());
                topologyNeObjecta.setPtpNameBau(topologyDataVo.getPtpnameaBau());
                topologyNeObjecta.setPtpNamePau(topologyDataVo.getPtpnameaPau());
                topologyNeObjecta.setSysname(topologyDataVo.getSysnamea());

                topologyNeObjectz.setPtpNameBau(topologyDataVo.getPtpnamezBau());
                topologyNeObjectz.setPtpNamePau(topologyDataVo.getPtpnamezPau());
                topologyNeObjectz.setSysname(topologyDataVo.getSysnamez());

               if((!StringUtils.isEmpty(topologyDataVo.getPtpnameaBau()) || !StringUtils.isEmpty(topologyDataVo.getPtpnameaPau())) && !StringUtils.isEmpty(topologyDataVo.getSysnamea())){
                    if((ptpName.equals(topologyDataVo.getPtpnameaBau()) || ptpName.equals(topologyDataVo.getPtpnameaPau()))
                            && (sysName.equals(topologyDataVo.getSysnamea()))){
                        topologyTmpObject.setNeA(topologyNeObjecta);
                        topologyTmpObject.setNeZ(topologyNeObjectz);
                    }else {
                        topologyTmpObject.setNeZ(topologyNeObjecta);
                        topologyTmpObject.setNeA(topologyNeObjectz);
                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>> TopologyServiceImpl selectTopologyList error("+map.toString()+") : " + ExceptionUtils.getStackTrace(e)+" <<<<<<<<<");
        }
        return topologyTmpObject;
    }
}
