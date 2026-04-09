package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ViewFactor {
    private int totalLink;
    private int measuredLink;
    private LocalDateTime measuredDateTime;
    private List<ViewLinkFactor> linkfactors = new ArrayList<>();
}
