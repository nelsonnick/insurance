package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.model.*;
import com.wts.interceptor.LoginInterceptor;
import com.wts.util.IDNumber;

public class MainController extends Controller {

    public void index() {
        render("/login/index.html");
    }

    public void login() {
        User u =User.dao.findFirst("select * from user where login=? and pass=? and state = 1", getPara("login"),getPara("pass"));
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
    public void NumberCheck() {
        renderText(IDNumber.checkIDNumber(getPara("number")));
    }
}
