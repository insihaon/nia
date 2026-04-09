package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LinkCollectCheck {
    private long link_id;
    private String link_name;
    private int fatalErrors;
    private int errCount;
    private List<LinkFactor> linkFactors = new ArrayList<>();
}
