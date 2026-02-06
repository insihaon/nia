package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ShellCommand {
    String router_name;
    List<String> command = new ArrayList<>();
    long time_out;
    String prompt;

    public ShellCommand() {
    }
    public ShellCommand(String router_name, List<String> command, long time_out) {
        this.router_name = router_name;
        this.command = command;
        this.time_out = time_out;
    }
}
