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

public class PersonController extends Controller {
    private static Logger logger = Logger.getLogger(Person.class);

    public void index() {
        render("/static/html/person.html");
    }

    @Before(LoginInterceptor.class)
    public void Query() {
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT person.id, person.name,person.number,person.phone,person.address,location.name AS location,location.id AS lid,person.state AS sid," +
                        " CASE person.tid " +
                        "WHEN '1' THEN '灵活就业/零就业' " +
                        "WHEN '2' THEN '灵活就业/单亲' " +
                        "WHEN '3' THEN '灵活就业/低保' " +
                        "WHEN '4' THEN '灵活就业/残疾' " +
                        "WHEN '5' THEN '灵活就业/特困高校' " +
                        "WHEN '6' THEN '灵活就业/成年孤儿' " +
                        "WHEN '7' THEN '公益岗位/零就业' " +
                        "WHEN '8' THEN '公益岗位/单亲' " +
                        "WHEN '8' THEN '公益岗位/低保' " +
                        "WHEN '10' THEN '公益岗位/残疾' " +
                        "WHEN '11' THEN '公益岗位/特困高校' " +
                        "WHEN '12' THEN '公益岗位/成年孤儿' " +
                        "WHEN '13' THEN '企业吸纳/失业半年' " +
                        "WHEN '14' THEN '企业吸纳/零就业' " +
                        "WHEN '15' THEN '企业吸纳/单亲' " +
                        "WHEN '16' THEN '企业吸纳/低保' " +
                        "WHEN '17' THEN '企业吸纳/残疾' " +
                        "WHEN '18' THEN '企业吸纳/特困高校' " +
                        "WHEN '19' THEN '企业吸纳/成年孤儿' " +
                        "ELSE '无法识别' END AS type," +
                        " CASE person.state WHEN '0' THEN '未享受' WHEN '1' THEN '正在享受' ELSE '状态错误' END AS state",
                "FROM person LEFT JOIN location ON person.lid = location.id " +
                        "WHERE person.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.number LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.phone LIKE '%" + getPara("keyword") + "%' ").getList());
    }
    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM person " +
                "WHERE name LIKE '%" + getPara("keyword") + "%' " +
                "OR number LIKE '%" + getPara("keyword") + "%' " +
                "OR phone LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }
    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(Person.dao.findById(getPara("id")));
    }
    @Before(LoginInterceptor.class)
    public void Del() {
        Person p = Person.dao.findById(getPara("id"));
        p.set("state",0).update();
        renderText("OK");
    }
    @Before(LoginInterceptor.class)
    public void Active() {
        Person p = Person.dao.findById(getPara("id"));
        p.set("state",1).update();
        renderText("OK");
    }

    @Before(LoginInterceptor.class)
    public void Add() {
        List<Person> persons = Person.dao.find("select * from person where number=?", getPara("number"));
        if (!IDNumber.availableIDNumber(getPara("number"))){
            renderText("证件号码" + IDNumber.checkIDNumber(getPara("number")));
        }else if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
            renderText("人员姓名必须为汉字!");
        } else if (getPara("name").length() < 2) {
            renderText("人员姓名必须在2个汉字以上!");
        } else if (persons.size() != 0) {
            renderText("该证件号码数据库中已存在，请核实!");
        } else if (!getPara("phone").matches("\\d{11}")) {
            renderText("联系电话必须为11位数字!");
        } else if (getPara("address").length()<1) {
            renderText("联系地址未填写！");
        } else if (getPara("marriage").length()<1) {
            renderText("婚姻状况未选择！");
        } else if (getPara("delay").length()<1) {
            renderText("延期政策未选择！");
        } else if (getPara("tid").length()<1) {
            renderText("申请类别未选择！");
        } else {
            Person person = new Person();
            person.set("name", getPara("name"))
                    .set("number", getPara("number"))
                    .set("sex", IDNumber.getSex(getPara("number")))
                    .set("birth", IDNumber.getBirthDate(getPara("number")))
                    .set("phone", getPara("phone"))
                    .set("address", getPara("address"))
                    .set("tid", getParaToInt("tid"))
                    .set("marriage", getParaToInt("marriage"))
                    .set("delay", getParaToInt("delay"))
                    .set("remark", getPara("remark"))
                    .set("state", 1)
                    .set("lid", ((User) getSessionAttr("user")).get("lid"))
                    .save();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Save;" + "number:" + getPara("number") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }

    @Before(LoginInterceptor.class)
    public void Edit() {
        Person person = Person.dao.findById(getPara("id"));
        if (person == null) {
            renderText("要修改的人员不存在，请刷新页面后再试！");
        } else if (Util.CheckNull(person.getStr("name")).equals(getPara("name").trim())
                && Util.CheckNull(person.getStr("number")).equals(getPara("number").trim())
                && Util.CheckNull(person.getStr("phone")).equals(getPara("phone").trim())
                && Util.CheckNull(person.getStr("address")).equals(getPara("address").trim())
                && Util.CheckNull(person.getStr("marriage")).equals(getPara("marriage").trim())
                && Util.CheckNull(person.getStr("remark")).equals(getPara("remark").trim())
                && Util.CheckNull(person.getStr("delay")).equals(getPara("delay").trim())
                && Util.CheckNull(person.getStr("tid")).equals(getPara("tid").trim())
                ) {
            renderText("未找到修改内容，请核实后再修改！");
        } else if (!Util.CheckNull(person.getStr("number")).equals(getPara("number"))
                && Person.dao.find("select * from person where number=?", getPara("number")).size() > 0) {
            renderText("该证件号码数据库中已存在，请核实！");
        } else if (!IDNumber.availableIDNumber(getPara("number"))){
            renderText("证件号码" + IDNumber.checkIDNumber(getPara("number")));
        }else if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
            renderText("人员姓名必须为汉字!");
        } else if (getPara("name").length() < 2) {
            renderText("人员姓名必须在2个汉字以上!");
        } else if (!getPara("phone").matches("\\d{11}")) {
            renderText("联系电话必须为11位数字!");
        } else if (getPara("address").length()<1) {
            renderText("联系地址未填写！");
        } else if (getPara("marriage").length()<1) {
            renderText("婚姻状况未选择！");
        } else if (getPara("delay").length()<1) {
            renderText("延期政策未选择！");
        } else if (getPara("tid").length()<1) {
            renderText("申请类别未选择！");
        } else {
            person.set("name", getPara("name"))
                    .set("number", getPara("number"))
                    .set("sex", IDNumber.getSex(getPara("number")))
                    .set("birth", IDNumber.getBirthDate(getPara("number")))
                    .set("phone", getPara("phone"))
                    .set("address", getPara("address"))
                    .set("tid", getParaToInt("tid"))
                    .set("marriage", getParaToInt("marriage"))
                    .set("delay", getParaToInt("delay"))
                    .set("remark", getPara("remark"))
                    .update();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Edit;" + "number:" + getPara("number") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }


}



