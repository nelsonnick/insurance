package com.wts.task;

import com.wts.entity.model.Family;
import com.wts.entity.model.Security;
import com.wts.entity.model.Person;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import static com.wts.util.Ggywzxt.getSecurity;
import static com.wts.util.IpKit.setIP;
import static com.wts.util.IpKit.setIP2;

public class SecurityDown implements Runnable {
    private static Logger logger = Logger.getLogger(SecurityDown.class);

    public void run() {
        setIP("10.153.73.163");
        try {
            Thread.sleep(10000); //10秒
        } catch (Exception e) {
            logger.error("延时错误：" + e);
        }
        List<Person> persons = Person.dao.find("SELECT * FROM person WHERE state = 1 AND check = 1");
        for (Person person : persons) {
            Security security = getSecurity(person.getNumber(), person.getName(), person.getLid(), 0);
            security.save();
        }
        List<Family> families = Family.dao.find("SELECT * FROM family WHERE state = 1 AND check = 1");
        for (Family family : families) {
            Person person = Person.dao.findById(family.getId());
            Security security = getSecurity(family.getNumber(), family.getName(), person.getLid(), 1);
            security.save();
        }
        logger.info("社保信息下载完成--->time:" + new Date() + ";");
        try {
            Thread.sleep(10000); //10秒
        } catch (Exception e) {
            logger.error("延时错误：" + e);
        }
//        setIP();
        setIP2("192.168.2.196");
    }
}
