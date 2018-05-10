package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.*;
import com.wts.entity.model.User;
import com.wts.interceptor.LoginInterceptor;
import com.wts.util.Util;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FamilyChangeController extends Controller {
    private static Logger logger = Logger.getLogger(Familychange.class);

    public void index() {
        render("/static/html/familyChange.html");
    }
    @Before(LoginInterceptor.class)
    public void Query() {
        String st = "";
        if (((User) getSessionAttr("user")).getInt("lid") == 1){
            st = "";
        }else{
            st = "AND person.lid = " + ((User) getSessionAttr("user")).get("lid");
        }
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT familychange.id,familychange.type AS tid,familychange.reason,familychange.time,person.name AS pname,person.number AS pnumber,user.name AS user,family.name,family.number,family.phone,location.name AS location,location.id AS lid," +
                        "CASE familychange.type " +
                        "WHEN '1' THEN '新增家庭成员' " +
                        "WHEN '2' THEN '家庭成员变更' " +
                        "WHEN '3' THEN '停止核查家庭成员' " +
                        "WHEN '4' THEN '开展核查家庭成员' " +
                        "WHEN '5' THEN '关闭自动核查' " +
                        "WHEN '6' THEN '开启自动核查' " +
                        "ELSE '无法识别' END AS type, "+
                        "CASE family.identity " +
                        "WHEN '1' THEN '丈夫' " +
                        "WHEN '2' THEN '妻子' " +
                        "WHEN '3' THEN '儿子' " +
                        "WHEN '4' THEN '女儿' " +
                        "WHEN '5' THEN '父亲' " +
                        "WHEN '6' THEN '母亲' " +
                        "WHEN '7' THEN '兄弟' " +
                        "WHEN '8' THEN '姐妹' " +
                        "ELSE '无法识别' END AS identity " ,
                "FROM familychange " +
                        "LEFT JOIN family ON familychange.fid = family.id " +
                        "LEFT JOIN user ON familychange.uid = user.id " +
                        "LEFT JOIN location ON user.lid = location.id " +
                        "LEFT JOIN person ON family.pid = person.id " +
                        "WHERE (person.number LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR family.number LIKE '%" + getPara("keyword") + "%' " +
                        "OR family.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR family.phone LIKE '%" + getPara("keyword") + "%') " + st + " ORDER BY familychange.id DESC").getList());
    }
    @Before(LoginInterceptor.class)
    public void Total() {
        String st = "";
        if (((User) getSessionAttr("user")).getInt("lid") == 1){
            st = "";
        }else{
            st = "AND person.lid = " + ((User) getSessionAttr("user")).get("lid");
        }
        Long count = Db.queryLong("SELECT COUNT(*) FROM familychange LEFT JOIN family ON familychange.fid = family.id LEFT JOIN person ON family.pid = person.id " +
                "WHERE (person.number LIKE '%" + getPara("keyword") + "%' " +
                "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                "OR family.number LIKE '%" + getPara("keyword") + "%' " +
                "OR family.name LIKE '%" + getPara("keyword") + "%' " +
                "OR family.phone LIKE '%" + getPara("keyword") + "%') " + st);
        renderText(count.toString());
    }

    @Before({Tx.class,LoginInterceptor.class})
    public  void export() throws IOException {
        String[] title={"序号","所属中心","经办人员","证件号码","人员姓名","变更类型","变更时间","变更原因","变更前","变更后"};
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
        String sql = "SELECT familychange.id,familychange.type AS tid,familychange.reason,familychange.time,familychange.before,familychange.after," +
                "person.name AS pname,person.number AS pnumber,user.name AS user," +
                "family.name,family.number,family.phone,location.name AS location,location.id AS lid," +
                "CASE familychange.type " +
                "WHEN '1' THEN '新增家庭成员' " +
                "WHEN '2' THEN '家庭成员变更' " +
                "WHEN '3' THEN '停止核查家庭成员' " +
                "WHEN '4' THEN '开展核查家庭成员' " +
                "WHEN '5' THEN '关闭自动核查' " +
                "WHEN '6' THEN '开启自动核查' " +
                "ELSE '无法识别' END AS type, " +
                "CASE family.identity " +
                "WHEN '1' THEN '丈夫' " +
                "WHEN '2' THEN '妻子' " +
                "WHEN '3' THEN '儿子' " +
                "WHEN '4' THEN '女儿' " +
                "WHEN '5' THEN '父亲' " +
                "WHEN '6' THEN '母亲' " +
                "WHEN '7' THEN '兄弟' " +
                "WHEN '8' THEN '姐妹' " +
                "ELSE '无法识别' END AS identity " +
                "FROM familychange " +
                "LEFT JOIN family ON familychange.fid = family.id " +
                "LEFT JOIN user ON familychange.uid = user.id " +
                "LEFT JOIN location ON user.lid = location.id " +
                "LEFT JOIN person ON family.pid = person.id " +
                "WHERE (person.number LIKE '%" + getPara("keyword") + "%' " +
                "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                "OR family.number LIKE '%" + getPara("keyword") + "%' " +
                "OR family.name LIKE '%" + getPara("keyword") + "%' " +
                "OR family.phone LIKE '%" + getPara("keyword") + "%' ) "
                + st;
        List<Record> r = Db.find(sql);
        for (int i = 0; i < r.size(); i++) {
            XSSFRow nextRow = sheet.createRow(i+1);
            nextRow.createCell(0).setCellValue(Util.CheckNull(r.get(i).get("id").toString()));
            nextRow.createCell(1).setCellValue(Util.CheckNull(r.get(i).get("location").toString()));
            nextRow.createCell(2).setCellValue(Util.CheckNull(r.get(i).get("user").toString()));
            nextRow.createCell(3).setCellValue(Util.CheckNull(r.get(i).get("number").toString()));
            nextRow.createCell(4).setCellValue(Util.CheckNull(r.get(i).get("name").toString()));
            nextRow.createCell(5).setCellValue(Util.CheckNull(r.get(i).get("type").toString()));
            nextRow.createCell(6).setCellValue(Util.CheckNull(r.get(i).get("time").toString()));
            nextRow.createCell(7).setCellValue(Util.CheckNull(r.get(i).get("reason").toString()));
            nextRow.createCell(8).setCellValue(Util.CheckNull(r.get(i).get("before").toString()));
            nextRow.createCell(9).setCellValue(Util.CheckNull(r.get(i).get("after").toString()));
        }
        HttpServletResponse response = getResponse();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=FamilyChangeExport.xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
        renderNull() ;
    }
}



