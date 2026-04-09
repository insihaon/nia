package com.ipsdn_opt.app.model;

import lombok.Data;

@Data
public class ViewLinkSet {
    private String link;
    private ViewLinkFactor forward;
    private ViewLinkFactor backward; 
}
