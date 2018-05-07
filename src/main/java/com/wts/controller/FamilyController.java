package com.wts.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.*;
import com.wts.interceptor.*;
import com.wts.util.IDNumber;
import com.wts.util.Util;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
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
                "SELECT person.name AS pname,person.number AS pnumber,person.phone AS pphone,person.id AS pid,person.lid AS lid,person.state AS psid,location.name AS location, " +
                        "family.id,family.name,family.number,family.phone,family.state AS sid,family.check, " +
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
                        "CASE family.state WHEN '0' THEN '停止' WHEN '1' THEN '开展' ELSE '状态错误' END AS state",
                "FROM family LEFT JOIN person ON family.pid = person.id " +
                        "LEFT JOIN location ON person.lid = location.id " +
                        "WHERE person.number LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.phone LIKE '%" + getPara("keyword") + "%' " +
                        "OR family.number LIKE '%" + getPara("keyword") + "%' " +
                        "OR family.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR family.phone LIKE '%" + getPara("keyword") + "%' ORDER BY family.id DESC").getList());
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
    public void GetPerson() {
        renderJson(Person.dao.findById(Family.dao.findById(getPara("id")).getPid()));
    }
    @Before({Tx.class,LoginInterceptor.class,TimeoutInterceptor.class})
    public void Del() {
        Family family = Family.dao.findById(getPara("id"));
        String before = JSON.toJSONString(family);
        if (getPara("reason").trim().equals("")){
            renderText("请输入停止核查的原因！");
        }else {
            family.set("state",0).set("check",0);
            if (family.update()) {
                Familychange fc = new Familychange();
                fc.set("fid", family.getId())
                        .set("uid", ((User) getSessionAttr("user")).get("id"))
                        .set("type", 3)
                        .set("reason", getPara("reason"))
                        .set("time", new Date())
                        .set("before", before)
                        .set("after", JSON.toJSONString(family))
                        .save();
            }
            logger.warn("function:" + this.getClass().getSimpleName() + "/Del;" + "id:" + getPara("id") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }
    @Before({Tx.class,LoginInterceptor.class,TimeoutInterceptor.class})
    public void Active() {
        Family family = Family.dao.findById(getPara("id"));
        String before = JSON.toJSONString(family);
        if (getPara("reason").trim().equals("")){
            renderText("请输入开展核查的原因！");
        }else {
            family.set("state",1).set("check",1);
            if (family.update()) {
                Familychange fc = new Familychange();
                fc.set("fid", family.getId())
                        .set("uid", ((User) getSessionAttr("user")).get("id"))
                        .set("type", 4)
                        .set("reason", getPara("reason"))
                        .set("time", new Date())
                        .set("before", before)
                        .set("after", JSON.toJSONString(family))
                        .save();
            }
            logger.warn("function:" + this.getClass().getSimpleName() + "/Active;" + "id:" + getPara("id") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }
    @Before({Tx.class,LoginInterceptor.class,TimeoutInterceptor.class})
    public void Open() {
        Family family = Family.dao.findById(getPara("id"));
        String before = JSON.toJSONString(family);

        family.set("check",1);
        if (family.update()) {
            Familychange fc = new Familychange();
            fc.set("fid", family.getId())
                    .set("uid", ((User) getSessionAttr("user")).get("id"))
                    .set("type", 6)
                    .set("reason", "开启自动核查")
                    .set("time", new Date())
                    .set("before", before)
                    .set("after", JSON.toJSONString(family))
                    .save();
        }
        logger.warn("function:" + this.getClass().getSimpleName() + "/Open;" + "id:" + getPara("id") + ";time:" + new Date() + ";");
        renderText("OK");

    }
    @Before({Tx.class,LoginInterceptor.class,TimeoutInterceptor.class})
    public void Close() {
        Family family = Family.dao.findById(getPara("id"));
        String before = JSON.toJSONString(family);
        family.set("check",0);
        if (family.update()) {
            Familychange fc = new Familychange();
            fc.set("fid", family.getId())
                    .set("uid", ((User) getSessionAttr("user")).get("id"))
                    .set("type", 5)
                    .set("reason", "关闭自动核查")
                    .set("time", new Date())
                    .set("before", before)
                    .set("after", JSON.toJSONString(family))
                    .save();
        }
        logger.warn("function:" + this.getClass().getSimpleName() + "/Close;" + "id:" + getPara("id") + ";time:" + new Date() + ";");
        renderText("OK");

    }
    @Before({Tx.class,LoginInterceptor.class,TimeoutInterceptor.class})
    public void Add() {
        List<Family> families = Family.dao.find("select * from family where number=?", getPara("number"));
        Person person = Person.dao.findById(getPara("id"));
        if (!IDNumber.availableIDNumber(getPara("number"))){
            renderText("证件号码" + IDNumber.checkIDNumber(getPara("number")));
        } else if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
            renderText("人员姓名必须为汉字!");
        } else if (getPara("name").length() < 2) {
            renderText("人员姓名必须在2个汉字以上!");
        } else if (person.getNumber().equals(getPara("number"))) {
            renderText("家属证件号码与申请人相同，请核实!");
        } else if (families.size() != 0 && !getPara("number").equals("000000000000000000")) {
            renderText("该证件号码数据库中已存在，请核实!");
        } else if (!getPara("phone").matches("\\d{11}")) {
            renderText("联系电话必须为11位数字!");
        } else if (getPara("marriage").length()<1) {
            renderText("婚姻状况未选择！");
        } else if (getPara("identity").length()<1) {
            renderText("人员身份未选择！");
        } else if(!IDNumber.checkIdentity(getPara("number"),getPara("identity")) && !getPara("number").equals("000000000000000000")) {
            renderText("人员性别与身份不相符！");
        } else if(person.get("marriage").equals("1") && (getPara("identity").equals("1") || getPara("identity").equals("2"))) {
            renderText("未婚人员不应有配偶信息！");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(getParaToLong("timeRegist"));
            Date timeRegist =c.getTime();
            Family family = new Family();
            family.set("name", getPara("name"))
                    .set("number", getPara("number"))
                    .set("sex", IDNumber.getSex(getPara("number")))
                    .set("birth", IDNumber.getBirthDate(getPara("number")))
                    .set("phone", getPara("phone"))
                    .set("identity", getParaToInt("identity"))
                    .set("company", getPara("company"))
                    .set("timeRegist", timeRegist)
                    .set("marriage", getParaToInt("marriage"))
                    .set("remark", getPara("remark"))
                    .set("state", 1)
                    .set("check", 1)
                    .set("pid", getParaToInt("id"));
            if (family.save()){
                Familychange fc = new Familychange();
                fc.set("fid", family.getId())
                        .set("uid",((User) getSessionAttr("user")).get("id"))
                        .set("type",1)
                        .set("time",new Date())
                        .set("before","")
                        .set("after", JSON.toJSONString(family))
                        .set("reason", "新增")
                        .save();
            }
            logger.warn("function:" + this.getClass().getSimpleName() + "/Add;" + "number:" + getPara("number") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }
    @Before({Tx.class,LoginInterceptor.class})
    public void Edit() {
        Family family = Family.dao.findById(getPara("id"));
        if (family == null) {
            renderText("要修改的家庭成员不存在，请刷新页面后再试！");
        } else if (Util.CheckNull(family.getStr("name")).equals(getPara("name").trim())
                && Util.CheckNull(family.getStr("number")).equals(getPara("number").trim())
                && Util.CheckNull(family.getStr("phone")).equals(getPara("phone").trim())
                && Util.CheckNull(family.getStr("identity")).equals(getPara("identity").trim())
                && Util.CheckNull(family.getStr("marriage")).equals(getPara("marriage").trim())
                && Util.CheckNull(family.getStr("company")).equals(getPara("company").trim())
                && Util.CheckNull(family.getTimeRegist().getTime()+"").equals(getPara("timeRegist").trim())
                && Util.CheckNull(family.getStr("remark")).equals(getPara("remark").trim())
                ) {
            renderText("未找到修改内容，请核实后再修改！");
        } else if (!Util.CheckNull(family.getStr("number")).equals(getPara("number"))
                && Family.dao.find("select * from family where state=1 and number=? ", getPara("number")).size() > 0
                && !getPara("number").equals("000000000000000000")
                ) {
            renderText("该证件号码数据库中存在正在享受的记录，请核实！");
        } else if (!IDNumber.availableIDNumber(getPara("number"))){
            renderText("证件号码" + IDNumber.checkIDNumber(getPara("number")));
        } else if (Person.dao.findById(family.getPid()).getNumber().equals(getPara("number"))) {
            renderText("家属证件号码与申请人相同，请核实!");
        } else if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
            renderText("人员姓名必须为汉字!");
        } else if (getPara("name").length() < 2) {
            renderText("人员姓名必须在2个汉字以上!");
        } else if (!getPara("phone").matches("\\d{11}")) {
            renderText("联系电话必须为11位数字!");
        } else if (getPara("marriage").length()<1) {
            renderText("婚姻状况未选择！");
        } else if (getPara("identity").length()<1) {
            renderText("人员身份未选择！");
        } else if(!IDNumber.checkIdentity(getPara("number"),getPara("identity")) && !getPara("number").equals("000000000000000000")) {
            renderText("人员性别与身份不相符！");
        } else if(Person.dao.findById(family.getPid()).get("marriage").equals("1") && (getPara("identity").equals("1") || getPara("identity").equals("2"))) {
            renderText("未婚人员不应有配偶信息！");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(getParaToLong("timeRegist"));
            Date timeRegist =c.getTime();
            String before = JSON.toJSONString(family);
            family.set("name", getPara("name"))
                    .set("number", getPara("number"))
                    .set("sex", IDNumber.getSex(getPara("number")))
                    .set("birth", IDNumber.getBirthDate(getPara("number")))
                    .set("phone", getPara("phone"))
                    .set("marriage", getParaToInt("marriage"))
                    .set("identity", getParaToInt("identity"))
                    .set("company", getPara("company"))
                    .set("timeRegist", timeRegist)
                    .set("remark", getPara("remark"));
            if (family.update()){
                Familychange fc = new Familychange();
                fc.set("fid", family.getId())
                        .set("uid",((User) getSessionAttr("user")).get("id"))
                        .set("type",2)
                        .set("time",new Date())
                        .set("before",before)
                        .set("after", JSON.toJSONString(family))
                        .set("reason", "修改")
                        .save();
            }
            logger.warn("function:" + this.getClass().getSimpleName() + "/Edit;" + "number:" + getPara("number") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }
    @Before({Tx.class,LoginInterceptor.class})
    public  void export() throws IOException{
        String[] title={"序号","申请人证件号码","申请人姓名","家属身份","家属证件号码","家属姓名","家属性别","家属出生年月","家属联系电话","婚姻状况","核查状态","备注"};
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow row =sheet.createRow(0);
        XSSFCell cell = null;
        for(int i=0;i<title.length;i++){
            cell=row.createCell(i);
            cell.setCellValue(title[i]);
        }
        String st = "";
        if (((User) getSessionAttr("user")).getInt("lid") == 1){
            st = "";
        }else{
            st = "AND person.lid = " + ((User) getSessionAttr("user")).get("lid");
        }
        String sql = "SELECT family.id, family.name,family.number,family.phone,family.birth,family.remark,person.name AS pname,person.number AS pnumber, " +
                "CASE family.identity " +
                "WHEN '1' THEN '夫' " +
                "WHEN '2' THEN '妻' " +
                "WHEN '3' THEN '子' " +
                "WHEN '4' THEN '女' " +
                "WHEN '5' THEN '父' " +
                "WHEN '6' THEN '母' " +
                "WHEN '7' THEN '兄弟' " +
                "WHEN '8' THEN '姐妹' " +
                "ELSE '无法识别' END AS identity, " +
                "CASE family.marriage WHEN '1' THEN '未婚' WHEN '2' THEN '已婚' WHEN '3' THEN '离异' WHEN '4' THEN '丧偶' ELSE '状态错误' END AS marriage, " +
                "CASE family.state WHEN '0' THEN '停止' WHEN '1' THEN '开展' ELSE '状态错误' END AS state, " +
                "CASE family.sex WHEN '1' THEN '男' WHEN '2' THEN '女' ELSE '状态错误' END AS sex " +
                "FROM family " +
                "LEFT JOIN person ON family.pid = person.id " +
                "LEFT JOIN location ON person.lid = location.id " +
                "WHERE (person.name LIKE '%" + getPara("keyword") + "%' " +
                "OR person.number LIKE '%" + getPara("keyword") + "%' " +
                "OR person.phone LIKE '%" + getPara("keyword") + "%' " +
                "OR family.number LIKE '%" + getPara("keyword") + "%' " +
                "OR family.name LIKE '%" + getPara("keyword") + "%' " +
                "OR family.phone LIKE '%" + getPara("keyword") + "%') "
                + st;
        List<Record> r = Db.find(sql);
        for (int i = 0; i < r.size(); i++) {
            XSSFRow nextRow = sheet.createRow(i+1);
            nextRow.createCell(0).setCellValue(Util.CheckNull(r.get(i).get("id").toString()));
            nextRow.createCell(1).setCellValue(Util.CheckNull(r.get(i).get("pnumber").toString()));
            nextRow.createCell(2).setCellValue(Util.CheckNull(r.get(i).get("pname").toString()));
            nextRow.createCell(3).setCellValue(Util.CheckNull(r.get(i).get("identity").toString()));
            nextRow.createCell(4).setCellValue(Util.CheckNull(r.get(i).get("number").toString()));
            nextRow.createCell(5).setCellValue(Util.CheckNull(r.get(i).get("name").toString()));
            nextRow.createCell(6).setCellValue(Util.CheckNull(r.get(i).get("sex").toString()));
            nextRow.createCell(7).setCellValue(Util.CheckNull(r.get(i).get("birth").toString()));
            nextRow.createCell(8).setCellValue(Util.CheckNull(r.get(i).get("phone").toString()));
            nextRow.createCell(9).setCellValue(Util.CheckNull(r.get(i).get("marriage").toString()));
            nextRow.createCell(10).setCellValue(Util.CheckNull(r.get(i).get("state").toString()));
            nextRow.createCell(11).setCellValue(Util.CheckNull(r.get(i).get("remark").toString()));
        }
        HttpServletResponse response = getResponse();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=FamilyExport.xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
        renderNull() ;
    }
}



