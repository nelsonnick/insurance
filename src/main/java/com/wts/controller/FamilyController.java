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
                "SELECT person.name AS pname,person.number AS pnumber,person.phone AS pphone,person.id AS pid,person.lid AS lid,person.state AS psid, " +
                        "family.name,family.number,family.phone,family.state AS sid, " +
                        "CASE family.identity " +
                        "WHEN '1' THEN '丈夫' " +
                        "WHEN '2' THEN '妻子' " +
                        "WHEN '3' THEN '儿子' " +
                        "WHEN '4' THEN '女儿' " +
                        "WHEN '5' THEN '父亲' " +
                        "WHEN '6' THEN '母亲' " +
                        "WHEN '7' THEN '兄弟' " +
                        "WHEN '8' THEN '姐妹' " +
                        "ELSE '无法识别' END AS identity, " +
                        "CASE family.marriage WHEN '1' THEN '未婚' WHEN '2' THEN '已婚' WHEN '3' THEN '离异' WHEN '4' THEN '丧偶' ELSE '状态错误' END AS marriage, " +
                        "CASE family.state WHEN '0' THEN '未享受' WHEN '1' THEN '正在享受' ELSE '状态错误' END AS state",
                "FROM family LEFT JOIN person ON family.pid = person.id " +
                        "WHERE person.number LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.phone LIKE '%" + getPara("keyword") + "%' " +
                        "OR family.number LIKE '%" + getPara("keyword") + "%' " +
                        "OR family.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR family.phone LIKE '%" + getPara("keyword") + "%' ").getList());
    }
    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM family LEFT JOIN person ON family.pid = person.id " +
                "WHERE person.name LIKE '%" + getPara("keyword") + "%' " +
                "OR person.number LIKE '%" + getPara("keyword") + "%' " +
                "OR person.phone LIKE '%" + getPara("keyword") + "%' " +
                "OR family.number LIKE '%" + getPara("keyword") + "%' " +
                "OR family.name LIKE '%" + getPara("keyword") + "%' " +
                "OR family.phone LIKE '%" + getPara("keyword") + "%' ");
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
            logger.warn("function:" + this.getClass().getSimpleName() + "/Add;" + "number:" + getPara("number") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }

    @Before(LoginInterceptor.class)
    public void Edit() {
        Family family = Family.dao.findById(getPara("id"));
        if (family == null) {
            renderText("要修改的家庭成员不存在，请刷新页面后再试！");
        } else if (Util.CheckNull(family.getStr("name")).equals(getPara("name").trim())
                && Util.CheckNull(family.getStr("number")).equals(getPara("number").trim())
                && Util.CheckNull(family.getStr("phone")).equals(getPara("phone").trim())
                && Util.CheckNull(family.getStr("identity")).equals(getPara("identity").trim())
                && Util.CheckNull(family.getStr("marriage")).equals(getPara("marriage").trim())
                && Util.CheckNull(family.getStr("remark")).equals(getPara("remark").trim())
                ) {
            renderText("未找到修改内容，请核实后再修改！");
        } else if (!Util.CheckNull(family.getStr("number")).equals(getPara("number"))
                && Family.dao.find("select * from family where state=1 and number=? ", getPara("number")).size() > 0) {
            renderText("该证件号码数据库中存在正在享受的记录，请核实！");
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



