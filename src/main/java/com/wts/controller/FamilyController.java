package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.*;
import com.wts.interceptor.LoginInterceptor;

public class FamilyController extends Controller {

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


}



