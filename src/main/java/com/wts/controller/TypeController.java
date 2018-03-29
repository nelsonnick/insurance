package com.wts.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Type;

public class TypeController extends Controller {

    public void index() {
        render("/static/html/type.html");
    }

    public void Query() {
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT * ",
                "FROM type  WHERE name LIKE '%" + getPara("keyword") + "%'").getList());
    }

    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM type " +
                "WHERE name LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }

    public void Get() {
        renderJson(Type.dao.findById(getPara("id")));
    }
}



