package com.nia.ip.sdn.linkage.vo;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebServiceResultVo implements Serializable {
    private int res_code;
    private String res_msg;
}
