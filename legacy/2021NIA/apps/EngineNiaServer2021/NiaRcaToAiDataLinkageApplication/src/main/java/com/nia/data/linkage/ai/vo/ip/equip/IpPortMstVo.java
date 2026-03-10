package com.nia.data.linkage.ai.vo.ip.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope(value = "prototype")
public class IpPortMstVo implements Serializable {
    private String node_num;
    private String node_id;
    private String if_num;
    private String if_id;
    private String if_nm;
    private int if_index;
    private String if_desc;
    private String if_type;
    private String if_speed;
    private String ip_addr;
    private String mac_addr;
    private String if_oper_status;
    private String ip_prefix;
    private String if_divistion;
    private String chng_datetime;
    private String reg_datetime;
    private String data_source;
}
