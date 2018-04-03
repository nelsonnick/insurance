package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.*;
import com.wts.interceptor.LoginInterceptor;
import com.wts.util.IDNumber;
import com.wts.util.Util;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class FamilyController extends Controller {
    private static Logger logger = Logger.getLogger(Family.class);
    public void index() {
        render("/static/html/family.html");
    }

    @Before(LoginInterceptor.class)
    public void Query() {
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT person.name,person.number,person.phone,person.address,location.name AS location," +
                        " CASE person.tid WHEN '1' THEN 'a' WHEN '2' THEN 'b' ELSE 'c' END AS type," +
                        " CASE person.state WHEN '0' THEN '未享受' WHEN '1' THEN '正在享受' ELSE '状态错误' END AS state",
                "FROM person LEFT JOIN location ON person.lid = location.id WHERE person.name LIKE '%" + getPara("keyword") + "%'").getList());
    }
    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM family " +
                "WHERE name LIKE '%" + getPara("keyword") + "%'");
        renderText(count.toString());
    }
    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(Family.dao.findById(getPara("id")));
    }
    @Before(LoginInterceptor.class)
    public void Del() {
        Family f = Family.dao.findById(getPara("id"));
        f.set("state",0).update();
        renderText("OK");
    }
    @Before(LoginInterceptor.class)
    public void Active() {
        Family f = Family.dao.findById(getPara("id"));
        f.set("state",1).update();
        renderText("OK");
    }
    @Before(LoginInterceptor.class)
    public void Add() {
        List<Family> families = Family.dao.find("select * from family where number=?", getPara("number"));
        if (!IDNumber.availableIDNumber(getPara("number"))){
            renderText("证件号码" + IDNumber.checkIDNumber(getPara("number")));
        }else if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
            renderText("人员姓名必须为汉字!");
        } else if (getPara("name").length() < 2) {
            renderText("人员姓名必须在2个汉字以上!");
        } else if (families.size() != 0) {
            renderText("该证件号码数据库中已存在，请核实!");
        } else if (!getPara("phone").matches("\\d{11}")) {
            renderText("联系电话必须为11位数字!");
        } else if (getPara("marriage").length()<1) {
            renderText("婚姻状况未选择！");
        } else if (getPara("identity").length()<1) {
            renderText("人员身份未选择！");
        }  else {
            Family family = new Family();
            family.set("name", getPara("name"))
                    .set("number", getPara("number"))
                    .set("sex", IDNumber.getSex(getPara("number")))
                    .set("birth", IDNumber.getBirthDate(getPara("number")))
                    .set("phone", getPara("phone"))
                    .set("identity", getParaToInt("identity"))
                    .set("marriage", getParaToInt("marriage"))
                    .set("remark", getPara("remark"))
                    .set("state", 1)
                    .set("pid", getPara("id"))
                    .save();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Save;" + "number:" + getPara("number") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }

    @Before(LoginInterceptor.class)
    public void Edit() {
        Family family = Family.dao.findById(getPara("id"));
        if (family == null) {
            renderText("要修改的人员不存在，请刷新页面后再试！");
        } else if (Util.CheckNull(family.getStr("name")).equals(getPara("name").trim())
                && Util.CheckNull(family.getStr("number")).equals(getPara("number").trim())
                && Util.CheckNull(family.getStr("phone")).equals(getPara("phone").trim())
                && Util.CheckNull(family.getStr("identity")).equals(getPara("identity").trim())
                && Util.CheckNull(family.getStr("marriage")).equals(getPara("marriage").trim())
                && Util.CheckNull(family.getStr("remark")).equals(getPara("remark").trim())
                ) {
            renderText("未找到修改内容，请核实后再修改！");
        } else if (!Util.CheckNull(family.getStr("number")).equals(getPara("number"))
                && Family.dao.find("select * from family where number=?", getPara("number")).size() > 0) {
            renderText("该证件号码数据库中已存在，请核实！");
        } else if (!IDNumber.availableIDNumber(getPara("number"))){
            renderText("证件号码" + IDNumber.checkIDNumber(getPara("number")));
        }else if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
            renderText("人员姓名必须为汉字!");
        } else if (getPara("name").length() < 2) {
            renderText("人员姓名必须在2个汉字以上!");
        } else if (!getPara("phone").matches("\\d{11}")) {
            renderText("联系电话必须为11位数字!");
        } else if (getPara("marriage").length()<1) {
            renderText("婚姻状况未选择！");
        } else if (getPara("identity").length()<1) {
            renderText("人员身份未选择！");
        }  else {
            family.set("name", getPara("name"))
                    .set("number", getPara("number"))
                    .set("sex", IDNumber.getSex(getPara("number")))
                    .set("birth", IDNumber.getBirthDate(getPara("number")))
                    .set("phone", getPara("phone"))
                    .set("marriage", getParaToInt("marriage"))
                    .set("identity", getParaToInt("identity"))
                    .set("remark", getPara("remark"))
                    .update();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Edit;" + "number:" + getPara("number") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }

}



