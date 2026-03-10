package com.nia.data.linkage.ipsdn.vo.ipsdn.traffic;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Component
@Scope(value = "prototype")
public class LinkTrafficListVo implements Serializable {
    private ArrayList<LinkTrafficVo> data;

}
