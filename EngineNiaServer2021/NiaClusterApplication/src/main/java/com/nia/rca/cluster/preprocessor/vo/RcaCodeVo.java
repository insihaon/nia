package com.nia.rca.cluster.preprocessor.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class RcaCodeVo implements Serializable{
	private static final long serialVersionUID = 4286518697004186973L;

	private String category;
	private int lvl;
    private String pCode;
    private String code;
    private String name;
    private String useYn;
    private String displayOrder;
    private String description;
    private String etc;
}
