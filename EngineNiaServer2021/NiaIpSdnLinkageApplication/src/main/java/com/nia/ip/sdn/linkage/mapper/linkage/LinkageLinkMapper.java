package com.nia.ip.sdn.linkage.mapper.linkage;

import com.nia.ip.sdn.linkage.vo.LinkVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface LinkageLinkMapper {
    ArrayList<LinkVo> selectLinkList();
}
