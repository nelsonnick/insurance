package com.wts.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.wts.entity.model.*;

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
