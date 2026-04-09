package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class MappedNmsInterface {
    @Id
    private long if_id;
    private long nms_if_id;
    private String nms_interface_name;
}
