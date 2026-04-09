package com.ipsdn_opt.app.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsageRate {
    private LocalDate sourceDate;
    private Float usageRate;

    public UsageRate(LocalDate sourceDate, Float usageRate) {
        this.sourceDate = sourceDate;
        this.usageRate = usageRate;
    }
}
