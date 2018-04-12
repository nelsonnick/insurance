package com.wts.weixin;

import com.wts.entity.model.Message;
import com.wts.entity.model.User;
import me.chanjar.weixin.cp.bean.WxCpMessage;

import java.util.Date;
import java.util.List;

public class WxSend {
    public static void sendMessage(List<User> users, String content) {
        for (User user : users) {
            WxCpMessage message = WxCpMessage
                    .TEXT()
                    .agentId(49) // 企业号应用ID
                    .toUser(user.getName())
                    .content(content)
                    .build();
            Message m = new Message();
            try {
                WxService.getMe().messageSend(message);
                m.set("uid", user.getId())
                        .set("content", content)
                        .set("state", 1)
                        .set("time", new Date())
                        .save();
            } catch (Exception e) {
                m.set("uid", user.getId())
                        .set("content", content)
                        .set("state", 0)
                        .set("time", new Date())
                        .save();
            }
        }
    }

    public static void sendMessage(User user, String content) {
        WxCpMessage message = WxCpMessage
                .TEXT()
                .agentId(49) // 企业号应用ID
                .toUser(user.getName())
                .content(content)
                .build();
        Message m = new Message();
        try {
            WxService.getMe().messageSend(message);
            m.set("uid", user.getId())
                    .set("content", content)
                    .set("state", 1)
                    .set("time", new Date())
                    .save();
        } catch (Exception e) {
            m.set("uid", user.getId())
                    .set("content", content)
                    .set("state", 0)
                    .set("time", new Date())
                    .save();
        }


    }
}
