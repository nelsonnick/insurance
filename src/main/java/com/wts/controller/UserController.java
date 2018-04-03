package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.model.User;
import com.wts.interceptor.LoginInterceptor;

public class UserController extends Controller {

    @Before(LoginInterceptor.class)
    public void pass() {
        render("/static/html/pass.html");
    }

    @Before(LoginInterceptor.class)
    public void getLocationId() {
        if (getSessionAttr("user").equals("") || getSessionAttr("user") == null) {
            renderText("无法识别");
        } else {
            renderText(((User) getSessionAttr("user")).get("lid").toString().trim());
        }
    }

    @Before(LoginInterceptor.class)
    public void getUserName() {
        if (getSessionAttr("user").equals("") || getSessionAttr("user") == null) {
            renderText("无法识别");
        } else {
            renderText(((User) getSessionAttr("user")).get("name").toString().trim());
        }
    }
    @Before(LoginInterceptor.class)
    public void Change() {
        User user = User.dao.findById(((User) getSessionAttr("user")).get("id"));
        if (user.get("pass").toString().trim().equals(getPara("pass1"))) {
            user.set("pass", getPara("pass2")).update();
            renderText("OK");
        } else {
            renderText("原始密码错误，请重新输入！");
        }
    }
}



