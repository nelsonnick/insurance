package com.wts.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.*;
import com.wts.util.IDNumber;
import com.wts.util.Jnjgfw;
import com.wts.weixin.WxSend;

import java.util.Date;
import java.util.List;

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
