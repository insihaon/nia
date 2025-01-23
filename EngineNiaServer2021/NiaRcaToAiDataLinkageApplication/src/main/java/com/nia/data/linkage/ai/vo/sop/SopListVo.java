package com.nia.data.linkage.ai.vo.sop;


import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Component()
@Scope(value = "prototype")
public class SopListVo implements Serializable {
    private ArrayList<SopVo> data;
}
