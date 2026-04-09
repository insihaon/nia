package com.codej.base.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Session {
    private String user_id;
    private String session_id;
    private LocalDateTime in_time;
    private LocalDateTime out_time;
    private long duration;
    private String host;
}
