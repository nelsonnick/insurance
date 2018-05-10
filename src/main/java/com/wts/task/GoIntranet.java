package com.wts.task;

import org.apache.log4j.Logger;

//切内网
public class GoIntranet implements Runnable {
    private static Logger logger = Logger.getLogger(GoIntranet.class);

    public void run() {
        try {
            Runtime.getRuntime().exec("netsh    interface    ip    set    addr    \"本地连接\"    static    "
                    + "10.153.73.163" + "    255.255.255.0     10.153.73.254     1");
            logger.info("切换内网成功");
        } catch (Exception e) {
            logger.info("切换内网失败");
        }
    }
}
