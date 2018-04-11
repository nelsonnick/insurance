package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.*;
import com.wts.interceptor.LoginInterceptor;
import com.wts.util.IDNumber;
import com.wts.util.Util;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class UserController extends Controller {
    private static Logger logger = Logger.getLogger(User.class);

    public void index() {
        render("/static/html/user.html");
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
    public void getUser() {
        if (getSessionAttr("user").equals("") || getSessionAttr("user") == null) {
            renderText("无法识别");
        } else {
            renderJson(((User) getSessionAttr("user")));
        }
    }

    @Before(LoginInterceptor.class)
    public void Query() {
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT user.id, user.name, user.login,location.name AS location,user.state AS sid, user.lid," +
                        "CASE user.state WHEN '0' THEN '停用' WHEN '1' THEN '启用' ELSE '状态错误' END AS state",
                "FROM user LEFT JOIN location ON user.lid = location.id " +
                        "WHERE user.login LIKE '%" + getPara("keyword") + "%' " +
                        "OR user.name LIKE '%" + getPara("keyword") + "%' ").getList());
    }

    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM user " +
                "WHERE login LIKE '%" + getPara("keyword") + "%' " +
                "OR name LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }

    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(User.dao.findById(getPara("id")));
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Del() {
        User u = User.dao.findById(getPara("id"));
        u.set("state", 0).update();
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Active() {
        User u = User.dao.findById(getPara("id"));
        u.set("state", 1).update();
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Reset() {
        User u = User.dao.findById(getPara("id"));
        u.set("pass", "00000000").update();
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Add() {
        List<User> users = User.dao.find("select * from user where login=?", getPara("login"));
        if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
            renderText("人员姓名必须为汉字!");
        } else if (getPara("name").length() < 2) {
            renderText("人员姓名必须在2个汉字以上!");
        } else if (users.size() != 0) {
            renderText("该用户名数据库中已存在，请核实!");
        } else if (getPara("lid").length() < 1) {
            renderText("所属中心未选择！");
        } else {
            User user = new User();
            user.set("name", getPara("name"))
                    .set("login", getPara("login"))
                    .set("lid", getPara("lid"))
                    .set("pass", getPara("00000000"))
                    .set("state", 1)
                    .save();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Add;" + "login:" + getPara("login") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Edit() {
        User user = User.dao.findById(getPara("id"));
        if (user == null) {
            renderText("要修改的用户不存在，请刷新页面后再试！");
        } else if (Util.CheckNull(user.getStr("name")).equals(getPara("name").trim())
                && Util.CheckNull(user.getStr("login")).equals(getPara("login").trim())
                && Util.CheckNull(user.getStr("lid")).equals(getPara("lid").trim())
                ) {
            renderText("未找到修改内容，请核实后再修改！");
        } else if (!Util.CheckNull(user.getStr("login")).equals(getPara("login"))
                && User.dao.find("select * from user where login=?", getPara("login")).size() > 0) {
            renderText("该用户名数据库中已存在，请核实！");
        } else if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
            renderText("人员姓名必须为汉字!");
        } else if (getPara("name").length() < 2) {
            renderText("人员姓名必须在2个汉字以上!");
        } else if (getPara("lid").length() < 1) {
            renderText("所属中心未填写！");
        } else {
            user.set("name", getPara("name"))
                    .set("login", getPara("login"))
                    .set("lid", IDNumber.getSex(getPara("lid")))
                    .update();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Edit;" + "login:" + getPara("login") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }

}



