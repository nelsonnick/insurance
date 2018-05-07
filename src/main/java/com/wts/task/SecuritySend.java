package com.wts.task;

import com.wts.entity.model.Message;
import com.wts.entity.model.Security;
import com.wts.entity.model.User;
import com.wts.weixin.WxSend;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import static com.wts.util.Ggywzxt.getSecurityType;


public class SecuritySend implements Runnable {
    private static Logger logger = Logger.getLogger(SecuritySend.class);

    public void run() {
        List<Security> securities = Security.dao.find("SELECT * FROM security");
        for (Security security : securities) {
            Integer state = security.getState();
            List<User> users = User.dao.find("SELECT * FROM user WHERE state = 1 AND lid=?", security.getLid());
            String content;
            for (User user : users) {
                switch (state) {
                    case 1: //无权查询
                        if (security.getType() == 0) {
                            content = "申请人：" + security.getSfzhm() + security.getXm() + "-->无权查询其社保信息，请核查确认！";
                        } else {
                            content = "家属：" + security.getSfzhm() + security.getXm() + "-->无权查询其社保信息，请核查确认！";
                        }
                        WxSend.sendMessage(user, content);
                        break;
                    case 2: //正常
                        if (security.getYlrylb().equals("0")) {//在职员工
                            if (getSecurityType(security.getDwbh()).equals("单位五险")) {
                                if (security.getType() == 0) {
                                    content = "申请人：" + security.getSfzhm() + security.getXm() + "-->该人员正在缴纳五险，请核查确认！";
                                } else {
                                    content = "家属：" + security.getSfzhm() + security.getXm() + "-->该人员正在缴纳五险，请核查确认！";
                                }
                                WxSend.sendMessage(user, content);
                            }
                        }
                        break;
                    case 4: //查询出错---->由于可能出现大量的查询错误，所以这类人员并不发送消息
                        if (security.getType() == 0) {
                            content = "申请人：" + security.getSfzhm() + security.getXm() + "-->该人员查询出错，请核查确认！";
                        } else {
                            content = "家属：" + security.getSfzhm() + security.getXm() + "-->该人员查询出错，请核查确认！";
                        }
                        Message message = new Message();
                        message.get("uid", user.getId());
                        message.get("time", new Date());
                        message.get("content", content);
                        message.get("state", 0);
                        message.save();
                    default:
                        break;
                }
            }
        }
        logger.info("社保信息发送结束--->time:" + new Date());
    }
}
