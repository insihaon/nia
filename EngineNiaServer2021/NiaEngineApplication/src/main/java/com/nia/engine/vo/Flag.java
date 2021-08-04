package com.nia.engine.vo;//package com.kt.rca.ttci.engine.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class Flag implements Serializable {
    private static final long serialVersionUID = -8313567057440586506L;

    private boolean flag = false;
}