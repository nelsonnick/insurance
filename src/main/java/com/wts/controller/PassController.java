package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.User;
import com.wts.interceptor.LoginInterceptor;
import org.apache.log4j.Logger;


public class PassController extends Controller {
    private static Logger logger = Logger.getLogger(User.class);

    public void index() {
        render("/static/html/pass.html");
    }

    @Before(LoginInterceptor.class)
    public void getUser() {
        if (getSessionAttr("user").equals("") || getSessionAttr("user") == null) {
            renderText("无法识别");
        } else {
            renderJson(((User) getSessionAttr("user")));
        }
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Change() {
        User user = ((User) getSessionAttr("user"));
        if (user.get("pass").toString().trim().equals(getPara("pass1").trim())) {
            if (!getPara("pass2").matches("[\u4e00-\u9fa5]+")) {
                if (getPara("pass2").trim().length() > 5) {
                    user.set("pass", getPara("pass2").trim()).update();
                    renderText("OK");
                } else {
                    renderText("新密码长度应在六位以上!");
                }
            } else {
                renderText("新密码不能含有汉字!");
            }
        } else {
            renderText("原始密码错误，请重新输入！");
        }
    }

}



