package com.wts.task;

import com.wts.entity.model.Family;
import com.wts.entity.model.Security;
import com.wts.entity.model.Person;
import org.apache.log4j.Logger;

import java.util.List;

import static com.wts.util.Ggywzxt.getSecurity;
import static com.wts.util.IpKit.getLocalHostIP;

public class SecurityDown implements Runnable {
    private static Logger logger = Logger.getLogger(SecurityDown.class);

    public void run() {
        if (getLocalHostIP().equals("10.153.73.163")){
            List<Person> persons = Person.dao.find("SELECT * FROM person WHERE person.state = 1 AND person.check = 1");
            for (Person person : persons) {
                Security security = getSecurity(person.getNumber(), person.getName(), person.getLid(), 0);
                security.save();
            }
            List<Family> families = Family.dao.find("SELECT * FROM family WHERE family.state = 1 AND family.check = 1");
            for (Family family : families) {
                Person person = Person.dao.findById(family.getId());
                Security security = getSecurity(family.getNumber(), family.getName(), person.getLid(), 1);
                security.save();
            }
        }else{
            logger.error("不在内网环境，无法下载数据！");
        }

    }
}
