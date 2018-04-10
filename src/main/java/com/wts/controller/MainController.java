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
}
