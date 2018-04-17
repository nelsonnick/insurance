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
                "SELECT person.id, person.name,person.number,person.phone,person.address,location.name AS location,location.id AS lid,person.state AS sid, " +
                        "CASE person.tid " +
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
                        "ELSE '无法识别' END AS type, " +
                        "CASE person.marriage WHEN '1' THEN '未婚' WHEN '2' THEN '已婚' WHEN '3' THEN '离异' WHEN '4' THEN '丧偶' ELSE '状态错误' END AS marriage, " +
                        "CASE person.state WHEN '0' THEN '未享受' WHEN '1' THEN '正在享受' ELSE '状态错误' END AS state",
                "FROM person LEFT JOIN location ON person.lid = location.id " +
                        "WHERE person.number LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.phone LIKE '%" + getPara("keyword") + "%' ORDER BY person.id DESC").getList());
    }
    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM person " +
                "WHERE number LIKE '%" + getPara("keyword") + "%' " +
                "OR name LIKE '%" + getPara("keyword") + "%' " +
                "OR phone LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }
    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(Person.dao.findById(getPara("id")));
    }
    @Before({Tx.class,LoginInterceptor.class,TimeoutInterceptor.class})
    public void Del() {
        Person person = Person.dao.findById(getPara("id"));
        String before = JSON.toJSONString(person);
        person.set("state",0);
        if (getPara("reason").trim().equals("")){
            renderText("请输入注销原因！");
        }else {
            if (person.update()) {
                Changeperson cp = new Changeperson();
                cp.set("pid", person.getId())
                        .set("uid", ((User) getSessionAttr("user")).get("id"))
                        .set("type", 3)
                        .set("reason", getPara("reason"))
                        .set("time", new Date())
                        .set("before", before)
                        .set("after", JSON.toJSONString(person))
                        .save();
                List<Family> families = Family.dao.find("select * from family where pid=?", person.getId());
                for (Family family : families) {
                    String before_family = JSON.toJSONString(family);
                    family.set("state", 0);
                    if (family.update()) {
                        Changefamily cf = new Changefamily();
                        cf.set("fid", family.getId())
                                .set("uid", ((User) getSessionAttr("user")).get("id"))
                                .set("type", 3)
                                .set("reason", "由于申请人被注销，其关联的家庭成员被全部注销")
                                .set("time", new Date())
                                .set("before", before_family)
                                .set("after", JSON.toJSONString(family))
                                .save();
                        logger.warn("function:FamilyController/Del;" + "id:" + family.getId() + ";time:" + new Date() + ";");
                    }
                }
            }
            logger.warn("function:" + this.getClass().getSimpleName() + "/Del;" + "id:" + getPara("id") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }
    @Before({Tx.class,LoginInterceptor.class,TimeoutInterceptor.class})
    public void Active() {
        Person person = Person.dao.findById(getPara("id"));
        String before = JSON.toJSONString(person);
        person.set("state",1);
        if (getPara("reason").trim().equals("")){
            renderText("请输入激活原因！");
        }else {
            if (person.update()) {
                Changeperson cp = new Changeperson();
                cp.set("pid", person.getId())
                        .set("uid", ((User) getSessionAttr("user")).get("id"))
                        .set("type", 4)
                        .set("reason", getPara("reason"))
                        .set("time", new Date())
                        .set("before", before)
                        .set("after", JSON.toJSONString(person))
                        .save();
                List<Family> families = Family.dao.find("select * from family where pid=?", person.getId());
                for (Family family : families) {
                    String before_family = JSON.toJSONString(family);
                    family.set("state", 1);
                    if (family.update()) {
                        Changefamily cf = new Changefamily();
                        cf.set("fid", family.getId())
                                .set("uid", ((User) getSessionAttr("user")).get("id"))
                                .set("type", 4)
                                .set("reason", "由于申请人被激活，其关联的家庭成员被全部激活")
                                .set("time", new Date())
                                .set("before", before_family)
                                .set("after", JSON.toJSONString(family))
                                .save();
                        logger.warn("function:FamilyController/Active;" + "id:" + family.getId() + ";time:" + new Date() + ";");
                    }
                }
            }
            logger.warn("function:" + this.getClass().getSimpleName() + "/Active;" + "id:" + getPara("id") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }
    @Before({Tx.class,LoginInterceptor.class,TimeoutInterceptor.class})
    public void Add() {
        System.out.println(getPara("timeOut"));
        List<Person> persons = Person.dao.find("select * from person where number=?", getPara("number"));
        if (!IDNumber.availableIDNumber(getPara("number"))){
            renderText("证件号码" + IDNumber.checkIDNumber(getPara("number")));
        } else if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
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
                    .set("bank", getPara("bank"))
                    .set("community", getPara("community"))
                    .set("remark", getPara("remark"))
                    .set("state", 1)
                    .set("lid", ((User) getSessionAttr("user")).get("lid"));
            if (person.save()){
                Changeperson cp = new Changeperson();
                cp.set("pid", person.getId())
                        .set("uid",((User) getSessionAttr("user")).get("id"))
                        .set("type",1)
                        .set("time",new Date())
                        .set("before","")
                        .set("after", JSON.toJSONString(person))
                        .save();
            }
            logger.warn("function:" + this.getClass().getSimpleName() + "/Add;" + "number:" + getPara("number") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }
    @Before({Tx.class,LoginInterceptor.class})
    public void Edit() {
        Person person = Person.dao.findById(getPara("id"));
        if (person == null) {
            renderText("要修改的人员不存在，请刷新页面后再试！");
        } else if (Util.CheckNull(person.getStr("name")).equals(getPara("name").trim())
                && Util.CheckNull(person.getStr("number")).equals(getPara("number").trim())
                && Util.CheckNull(person.getStr("phone")).equals(getPara("phone").trim())
                && Util.CheckNull(person.getStr("address")).equals(getPara("address").trim())
                && Util.CheckNull(person.getStr("marriage")).equals(getPara("marriage").trim())
                && Util.CheckNull(person.getStr("bank")).equals(getPara("bank").trim())
                && Util.CheckNull(person.getStr("community")).equals(getPara("community").trim())
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
            String before = JSON.toJSONString(person);
            person.set("name", getPara("name"))
                    .set("number", getPara("number"))
                    .set("sex", IDNumber.getSex(getPara("number")))
                    .set("birth", IDNumber.getBirthDate(getPara("number")))
                    .set("phone", getPara("phone"))
                    .set("address", getPara("address"))
                    .set("tid", getParaToInt("tid"))
                    .set("marriage", getParaToInt("marriage"))
                    .set("delay", getParaToInt("delay"))
                    .set("bank", getPara("bank"))
                    .set("community", getPara("community"))
                    .set("remark", getPara("remark"))
                    .update();
            if (person.update()){
                Changeperson cp = new Changeperson();
                cp.set("pid", person.getId())
                        .set("uid",((User) getSessionAttr("user")).get("id"))
                        .set("type",2)
                        .set("time",new Date())
                        .set("before",before)
                        .set("after", JSON.toJSONString(person))
                        .save();
            }
            logger.warn("function:" + this.getClass().getSimpleName() + "/Edit;" + "number:" + getPara("number") + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }
    @Before({Tx.class,LoginInterceptor.class})
    public  void export() throws IOException {
        String[] title={"序号","所属中心","人员类别","证件号码","人员姓名","性别","出生年月","联系电话","联系地址","银行卡号","所属社区","婚姻状况","人员状态","延期情况","备注"};
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
            st = "AND location.id = " + ((User) getSessionAttr("user")).get("lid");
        }
        String sql = "SELECT person.id, person.name,person.number,person.phone,person.address,person.bank,person.community,person.birth,person.remark,location.name AS location,location.id AS lid, " +
                "CASE person.tid " +
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
                "ELSE '无法识别' END AS type, " +
                "CASE person.marriage WHEN '1' THEN '未婚' WHEN '2' THEN '已婚' WHEN '3' THEN '离异' WHEN '4' THEN '丧偶' ELSE '状态错误' END AS marriage, " +
                "CASE person.state WHEN '0' THEN '未享受' WHEN '1' THEN '正在享受' ELSE '状态错误' END AS state, " +
                "CASE person.sex WHEN '1' THEN '男' WHEN '2' THEN '女' ELSE '状态错误' END AS sex, " +
                "CASE person.delay WHEN '0' THEN '不延期' WHEN '1' THEN '延期' ELSE '状态错误' END AS delay " +
                "FROM person LEFT JOIN location ON person.lid = location.id " +
                "WHERE (person.number LIKE '%" + getPara("keyword") + "%' " +
                "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                "OR person.phone LIKE '%" + getPara("keyword") + "%' ) "
                + st;
        List<Record> r = Db.find(sql);
        for (int i = 0; i < r.size(); i++) {
            XSSFRow nextRow = sheet.createRow(i+1);
            nextRow.createCell(0).setCellValue(Util.CheckNull(r.get(i).get("id").toString()));
            nextRow.createCell(1).setCellValue(Util.CheckNull(r.get(i).get("location").toString()));
            nextRow.createCell(2).setCellValue(Util.CheckNull(r.get(i).get("type").toString()));
            nextRow.createCell(3).setCellValue(Util.CheckNull(r.get(i).get("number").toString()));
            nextRow.createCell(4).setCellValue(Util.CheckNull(r.get(i).get("name").toString()));
            nextRow.createCell(5).setCellValue(Util.CheckNull(r.get(i).get("sex").toString()));
            nextRow.createCell(6).setCellValue(Util.CheckNull(r.get(i).get("birth").toString()));
            nextRow.createCell(7).setCellValue(Util.CheckNull(r.get(i).get("phone").toString()));
            nextRow.createCell(8).setCellValue(Util.CheckNull(r.get(i).get("address").toString()));
            nextRow.createCell(9).setCellValue(Util.CheckNull(r.get(i).get("bank").toString()));
            nextRow.createCell(10).setCellValue(Util.CheckNull(r.get(i).get("community").toString()));
            nextRow.createCell(11).setCellValue(Util.CheckNull(r.get(i).get("marriage").toString()));
            nextRow.createCell(12).setCellValue(Util.CheckNull(r.get(i).get("state").toString()));
            nextRow.createCell(13).setCellValue(Util.CheckNull(r.get(i).get("delay").toString()));
            nextRow.createCell(14).setCellValue(Util.CheckNull(r.get(i).get("remark").toString()));
        }
        HttpServletResponse response = getResponse();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=PersonExport.xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
        renderNull() ;
    }
}



