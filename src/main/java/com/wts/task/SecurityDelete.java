package com.wts.task;

import com.jfinal.plugin.activerecord.Db;
import org.apache.log4j.Logger;

import java.util.Date;
public class SecurityDelete implements Runnable {
    private static Logger logger = Logger.getLogger(SecurityDelete.class);

    public void run() {
        Db.update("Delete FROM security WHERE 1=1");
        logger.info("社保信息删除完成--->time:" + new Date() + ";");
    }

}
