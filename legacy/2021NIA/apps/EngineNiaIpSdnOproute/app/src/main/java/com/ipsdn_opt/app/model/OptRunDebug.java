package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "optrundebug")
public class OptRunDebug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long runhis_id;
    private String message;

    public OptRunDebug() {
    }
    public OptRunDebug(long runhis_id, String message) {
        this.runhis_id = runhis_id;
        this.message = message;
    }
}
