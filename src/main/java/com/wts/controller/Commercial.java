package com.wts.controller;

import com.wts.entity.model.*;
import com.wts.util.IDNumber;
import com.wts.util.Jnjgfw;
import com.wts.weixin.WxService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class Commercial implements Runnable {
    private static Logger logger = Logger.getLogger(Commercial.class);

    public void run() {
        List<Person> persons = Person.dao.find("SELECT * FROM person WHERE state = 1");
        for (Person person : persons) {
            Integer commercialNum = Jnjgfw.checkByIdz(person.getNumber());
            switch (commercialNum) {
                case 1:
                    List<User> users = User.dao.find("SELECT * FROM user WHERE state = 1 AND lid=?", person.getLid());
                    String content = "申请人：" + person.getNumber() + person.getName() + "-->存在正在营业的工商信息，请核查确认！";
                    sendMessage(users,content,person.getNumber(),person.getName());
                    break;
                case 4:
                    logger.error("获取工商信息失败---> number: " + person.getNumber() + ";name:" + person.getName() + ";time:" + new Date() + ";");
                    break;
                default:
                    break;
            }
        }
        List<Family> families = Family.dao.find("SELECT * FROM family WHERE state = 1");
        for (Family family : families) {
            if (IDNumber.isRetire(family.getNumber())) {
                continue;
            } else {
                Integer commercialNum = Jnjgfw.checkByIdz(family.getNumber());
                switch (commercialNum) {
                    case 1:
                        Person person = Person.dao.findById(family.getPid());
                        List<User> users = User.dao.find("SELECT * FROM user WHERE state = 1", person.getLid());
                        String content = "家属：" + family.getNumber() + family.getName() + "-->存在正在营业的工商信息，请核查确认！";
                        sendMessage(users, content, family.getNumber(), family.getName());
                        break;
                    case 4:
                        logger.error("获取工商信息失败---> number: " + family.getNumber() + ";name:" + family.getName() + ";time:" + new Date() + ";");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void sendMessage(List<User> users, String content, String number, String name) {
        for (User user : users) {
            WxCpMessage message = WxCpMessage
                    .TEXT()
                    .agentId(49) // 企业号应用ID
                    .toUser(user.getName())
                    .content(content)
                    .build();
            try {
                WxService.getMe().messageSend(message);
                Sendmessage sm = new Sendmessage();
                sm.set("uid", user.getId())
                        .set("content", content)
                        .set("time", new Date())
                        .save();
            } catch (Exception e) {
                logger.error("发送消息失败---> number: " + number + ";name:" + name + ";userName:" + user.getName() + ";time:" + new Date() + ";");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        WxCpMessage message = WxCpMessage
                .TEXT()
                .agentId(49) // 企业号应用ID
                .toUser("WangTianShuo")
                .content("测试一下")
                .build();
        try {
            WxService.getMe().messageSend(message);
        } catch (Exception e) {
            System.out.println("出错啦！");
        }
    }
}
