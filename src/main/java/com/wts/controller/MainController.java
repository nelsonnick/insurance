package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.*;
import com.wts.interceptor.LoginInterceptor;
import com.wts.util.IDNumber;
import com.wts.util.Jnjgfw;
import com.wts.weixin.WxSend;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainController extends Controller {

    public void index() {
        render("/login/index.html");
    }

    public void login() {
        User u = User.dao.findFirst("SELECT * FROM user WHERE login=? AND pass=? AND state = 1", getPara("login"), getPara("pass"));
        if (u != null) {
            setSessionAttr("user", u);
            redirect("/person");
        } else {
            redirect("/");
        }
    }

    public void logout() {
        setSessionAttr("user", null);
        redirect("/");
    }

    @Before(LoginInterceptor.class)
    public void getType() {
        List<Type> type1 = Type.dao.find("SELECT * FROM type WHERE category = 1");
        List<Type> type2 = Type.dao.find("SELECT * FROM type WHERE category = 2");
        List<Type> type3 = Type.dao.find("SELECT * FROM type WHERE category = 3");
        String str1 = "";
        String str2 = "";
        String str3 = "";
        for (Type type : type1) {
            str1 = str1 + "{value:'" + type.getId() + "',label:'" + type.getName() + "'},";
        }
        str1 = str1.substring(0, str1.length() - 1);
        for (Type type : type2) {
            str2 = str2 + "{value:'" + type.getId() + "',label:'" + type.getName() + "'},";
        }
        str2 = str2.substring(0, str2.length() - 1);
        for (Type type : type3) {
            str3 = str3 + "{value:'" + type.getId() + "',label:'" + type.getName() + "'},";
        }
        str3 = str3.substring(0, str3.length() - 1);
        renderJson("[{value:'1',label:'灵活就业',children:[" + str1 + "]}," +
                "{value:'2',label:'公益岗位',children:[" + str2 + "]}," +
                "{value:'3',label:'企业吸纳',children:[" + str3 + "]}]");
    }

    @Before(LoginInterceptor.class)
    public void getJob() {
        List<Job> jobs = Job.dao.find("SELECT * FROM job");
        String str = "";
        for (Job job : jobs) {
            str = str + "{value: '" + job.getId() + "',label:'" + job.getName() + "'},";
        }
        str = str.substring(0, str.length() - 1);
        renderJson("[" + str + "]");
    }

    @Before(LoginInterceptor.class)
    public void getCommunity() {
        String str = "";
        if (((User) getSessionAttr("user")).getLid() != 1) {
            List<Community> communities = Community.dao.find("SELECT * FROM community WHERE lid=1 OR lid=?", ((User) getSessionAttr("user")).getLid());
            for (Community community : communities) {
                str = str + "{value: '" + community.getId() + "',label:'" + community.getName() + "'},";
            }
        } else {
            List<Community> communities = Community.dao.find("SELECT * FROM community ");
            for (Community community : communities) {
                str = str + "{value: '" + community.getId() + "',label:'" + community.getName() + "'},";
            }
        }
        str = str.substring(0, str.length() - 1);
        renderJson("[" + str + "]");
    }

    @Before(LoginInterceptor.class)
    public void getLocation() {
        List<Location> locations = Location.dao.find("SELECT * FROM location");
        String str = "";
        for (Location location : locations) {
            str = str + "{value: '" + location.getId() + "',label:'" + location.getName() + "'},";
        }
        str = str.substring(0, str.length() - 1);
        renderJson("[" + str + "]");
    }

    @Before(LoginInterceptor.class)
    public void getUser() {
        if (getSessionAttr("user").equals("") || getSessionAttr("user") == null) {
            renderText("无法识别");
        } else {
            renderJson(((User) getSessionAttr("user")));
        }
    }

}
