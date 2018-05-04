package com.wts.task;

import com.wts.entity.model.*;
import com.wts.util.IDNumber;
import com.wts.util.Jnjgfw;
import com.wts.weixin.WxSend;
import com.wts.weixin.WxService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class Commercial implements Runnable {
    private static Logger logger = Logger.getLogger(Commercial.class);

    public void run() {
        List<Person> persons = Person.dao.find("SELECT * FROM person WHERE state = 1 AND check = 1");
        for (Person person : persons) {
            Integer commercialNum = Jnjgfw.checkByIdz(person.getNumber());
            List<User> users = User.dao.find("SELECT * FROM user WHERE state = 1 AND lid=?", person.getLid());
            for (User user : users) {
                switch (commercialNum) {
                    case 1://有在营的工商营业执照
                        String content = "申请人：" + person.getNumber() + person.getName() + "-->存在正在营业的工商信息，请核查确认！";
                        WxSend.sendMessage(user, content);
                        break;
                    case 3://查询失败---->由于可能出现大量的查询错误，所以这类人员并不发送消息
                        Message message = new Message();
                        message.get("uid", user.getId());
                        message.get("time", new Date());
                        message.get("content", "申请人: " + person.getNumber() + person.getName() + "-->获取工商信息失败");
                        message.get("state", 0);
                        message.save();
                        break;
                    default:
                        break;
                }
            }

        }
        List<Family> families = Family.dao.find("SELECT * FROM family WHERE state = 1 AND check = 1");
        for (Family family : families) {
            if (IDNumber.isRetire(family.getNumber())) {
                continue;
            } else {
                Integer commercialNum = Jnjgfw.checkByIdz(family.getNumber());
                Person person = Person.dao.findById(family.getPid());
                List<User> users = User.dao.find("SELECT * FROM user WHERE state = 1 AND lid=?", person.getLid());
                for (User user : users) {
                    switch (commercialNum) {
                        case 1:
                            String content = "家属：" + family.getNumber() + family.getName() + "-->存在正在营业的工商信息，请核查确认！";
                            WxSend.sendMessage(user, content);
                            break;
                        case 3:
                            Message message = new Message();
                            message.get("uid", user.getId());
                            message.get("time", new Date());
                            message.get("content", "家属: " + person.getNumber() + person.getName() + "-->获取工商信息失败");
                            message.get("state", 0);
                            message.save();
                            break;
                        default:
                            break;
                    }
                }

            }
        }
        logger.info("工商核查结束--->time:" + new Date());
    }

}
