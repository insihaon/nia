package com.nia.ip.sdn.linkage.mapper.linkage;

import com.nia.ip.sdn.linkage.vo.LinkTrafficVo;
import com.nia.ip.sdn.linkage.vo.NodeFactorVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface LinkageLinkTrafficeMapper {
    LinkTrafficVo selectMaxId();
    ArrayList<LinkTrafficVo> selectLinkTrafficVoList(int id);
}
