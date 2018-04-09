package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.*;
import com.wts.interceptor.LoginInterceptor;

public class MainController extends Controller {

    public void index() {
        render("/login/index.html");
    }
    public void login() {
        User u = User.dao.findFirst("SELECT * FROM user WHERE login=? AND pass=? AND state = 1", getPara("login"),getPara("pass"));
        if (u!=null){
            setSessionAttr("user",u);
            redirect("/person");
        }else {
            redirect("/");
        }
    }
    public void logout() {
        setSessionAttr("user",null);
        redirect("/");
    }
    @Before(LoginInterceptor.class)
    public void getUser() {
        if (getSessionAttr("user").equals("") || getSessionAttr("user") == null) {
            renderText("无法识别");
        } else {
            renderJson(((User) getSessionAttr("user")));
        }
    }
    @Before({Tx.class,LoginInterceptor.class})
    public void Change() {
        User user = ((User) getSessionAttr("user"));
        if (user.get("pass").toString().trim().equals(getPara("pass1").trim())) {
            user.set("pass", getPara("pass2").trim()).update();
            renderText("OK");
        } else {
            renderText("原始密码错误，请重新输入！");
        }
    }
}
