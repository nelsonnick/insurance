package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.*;
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

public class PersonChangeController extends Controller {
    private static Logger logger = Logger.getLogger(Personchange.class);

    public void index() {
        render("/static/html/personChange.html");
    }
    @Before(LoginInterceptor.class)
    public void Query() {
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT personchange.id,personchange.type AS tid,personchange.reason,personchange.time,user.name AS user,person.name,person.number,person.phone,location.name AS location,location.id AS lid," +
                        "CASE personchange.type " +
                        "WHEN '1' THEN '新增困难人员' " +
                        "WHEN '2' THEN '困难人员变更' " +
                        "WHEN '3' THEN '退出困难人员' " +
                        "WHEN '4' THEN '重新认定困难人员' " +
                        "WHEN '5' THEN '关闭自动核查' " +
                        "WHEN '6' THEN '开启自动核查' " +
                        "ELSE '无法识别' END AS type ",
                "FROM personchange " +
                        "LEFT JOIN person ON personchange.pid = person.id " +
                        "LEFT JOIN user ON personchange.uid = user.id " +
                        "LEFT JOIN location ON user.lid = location.id " +
                        "WHERE person.number LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR person.phone LIKE '%" + getPara("keyword") + "%' ORDER BY personchange.id DESC").getList());
    }
    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM personchange LEFT JOIN person ON personchange.pid = person.id " +
                "WHERE person.number LIKE '%" + getPara("keyword") + "%' " +
                "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                "OR person.phone LIKE '%" + getPara("keyword") + "%' ");
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
        String sql = "SELECT personchange.id,personchange.type AS tid,personchange.reason,personchange.time,personchange.before,personchange.after," +
                "user.name AS user,person.name,person.number,person.phone,location.name AS location,location.id AS lid," +
                "CASE personchange.type " +
                "WHEN '1' THEN '新增困难人员' " +
                "WHEN '2' THEN '困难人员变更' " +
                "WHEN '3' THEN '退出困难人员' " +
                "WHEN '4' THEN '重新认定困难人员' " +
                "WHEN '5' THEN '关闭自动核查' " +
                "WHEN '6' THEN '开启自动核查' " +
                "ELSE '无法识别' END AS type " +
                "FROM personchange " +
                "LEFT JOIN person ON personchange.pid = person.id " +
                "LEFT JOIN user ON personchange.uid = user.id " +
                "LEFT JOIN location ON user.lid = location.id " +
                "WHERE (person.number LIKE '%" + getPara("keyword") + "%' " +
                "OR person.name LIKE '%" + getPara("keyword") + "%' " +
                "OR person.phone LIKE '%" + getPara("keyword") + "%' ) "
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
        response.setHeader("Content-Disposition", "attachment;filename=PersonChangeExport.xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
        renderNull() ;
    }
}



