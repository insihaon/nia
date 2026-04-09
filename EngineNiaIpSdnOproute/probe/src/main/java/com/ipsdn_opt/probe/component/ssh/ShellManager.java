package com.ipsdn_opt.probe.component.ssh;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Component;

import com.ipsdn_opt.probe.model.CommandServer;

import lombok.extern.slf4j.Slf4j;

@Component("shellManager")
@Slf4j
public class ShellManager {

    ShellWorker sw;
    private Timer timer = null;
    private TimerTask timerTask = null;
    HashMap<String, ShellWorker> shell_workers = null;
    List<String> removeKeys = null;
    private static final int START_DELAY = 1000 * 10;
    private static final int TIMER = 1000 * 5;


    public ShellManager() {
        shell_workers = new HashMap<String, ShellWorker>();
        removeKeys = new ArrayList<>();

        if (timer != null) {
            timer.cancel();
        }

		timer = new Timer("closeSessionTimer");
		timerTask = new TimerTask() {
			public void run() {
                try {
                    if(shell_workers.size()>0) log.info("shellworker count: {}", Integer.toString(shell_workers.size()));
                    for(String sessionId : shell_workers.keySet()) {
                        ShellWorker worker = shell_workers.get(sessionId);

                        LocalDateTime workTime = worker.getLastWorkTime();
                        if (workTime != null) {
                            Duration duration = Duration.between(workTime, LocalDateTime.now());
                        //log.info(String.format("shellWorker server-%d alive check: worktime=%s, duration=%d", svrId, workTime.toString(), duration.toSeconds()));
                            if (duration.toSeconds() >= 60) {
                                worker.close();
                                removeKeys.add(sessionId);
                            }
                        }
                    }
                    for(String key : removeKeys) {
                        if (shell_workers.remove(key) != null) {
                            //log.info("shellWorker [{}] is removed", key);
                        }
                    }
                    removeKeys.clear();
                }
                catch(Exception e) {
                    log.info("Timer Task Error: " + e.getMessage());
                }
            }
		};

		timer.schedule( timerTask, START_DELAY, TIMER);
    }
    public synchronized ShellWorker getShellWorker(CommandServer cmdServer, String sessionId) throws Exception {
        ShellWorker ret = shell_workers.get(sessionId);
        try {
            if (ret == null && shell_workers.size()<=100) {
                ret = new ShellWorker(cmdServer);
                shell_workers.put(sessionId, ret);
                //log.info("shellWorker [" + sessionId + "] is created.");
            }
            else if(shell_workers.size() > 100) {
                log.info(String.format("shellWorker count(%d) > 100 : shell worker is not created no more.", shell_workers.size()));
            }
            else {
                log.info("shellWorker [" + sessionId + "] is reused.");
            }
            if(ret!=null) ret.setPrompt(cmdServer.getPrompt());
        }
        catch(Exception e) {
            throw e;
        }
        return ret;
    }

}
