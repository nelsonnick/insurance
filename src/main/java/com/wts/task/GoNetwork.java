package com.wts.task;

import org.apache.log4j.Logger;

//切外网
public class GoNetwork implements Runnable {
    private static Logger logger = Logger.getLogger(GoNetwork.class);

    public void run() {
        try {
            Runtime.getRuntime().exec("netsh interface ip set address name=\"本地连接\" source=dhcp");
            logger.info("切换外网成功");
        } catch (Exception e) {
            logger.info("切换外网失败");
        }
    }
}
