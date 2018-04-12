package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.Message;
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


public class MessageController extends Controller {
    private static Logger logger = Logger.getLogger(Message.class);

    public void index() {
        render("/static/html/message.html");
    }

    @Before(LoginInterceptor.class)
    public void Query() {
        String st = "";
        if (((User) getSessionAttr("user")).getInt("lid") == 1) {
            st = "";
        } else {
            st = "AND location.id = " + ((User) getSessionAttr("user")).get("lid");
        }
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT message.id,user.name, message.time,message.content, " +
                        "CASE message.state " +
                        "WHEN '0' THEN '发送失败' " +
                        "WHEN '1' THEN '发送成功' " +
                        "ELSE '无法识别' END",
                "FROM user " +
                        "LEFT JOIN message ON user.id = message.uid " +
                        "LEFT JOIN location ON location.id = user.lid " +
                        "WHERE (message.content LIKE '%" + getPara("keyword") + "%' " +
                        "OR user.name LIKE '%" + getPara("keyword") + "%') " + st + " ORDER BY message.id DESC").getList());
    }

    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM message LEFT JOIN user ON message.uid = user.id " +
                "WHERE message.content LIKE '%" + getPara("keyword") + "%' " +
                "OR user.name LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }

    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(Message.dao.findById(getPara("id")));
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void export() throws IOException {
        String[] title = {"序号", "所属中心", "发送人", "发送时间", "消息内容", "消息状态"};
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        String st = "";
        if (((User) getSessionAttr("user")).getInt("lid") == 1) {
            st = "";
        } else {
            st = "AND location.id = " + ((User) getSessionAttr("user")).get("lid");
        }
        String sql = "SELECT user.name, message.time,message.content, location.lname, " +
                "CASE message.state " +
                "WHEN '0' THEN '发送失败' " +
                "WHEN '1' THEN '发送成功' " +
                "ELSE '无法识别' END " +
                "FROM user " +
                "LEFT JOIN message ON user.id = message.uid " +
                "LEFT JOIN location ON location.id = user.lid " +
                "WHERE (message.content LIKE '%" + getPara("keyword") + "%' " +
                "OR user.name LIKE '%" + getPara("keyword") + "%') " + st;
        List<Record> r = Db.find(sql);
        for (int i = 0; i < r.size(); i++) {
            XSSFRow nextRow = sheet.createRow(i + 1);
            nextRow.createCell(0).setCellValue(Util.CheckNull(r.get(i).get("id").toString()));
            nextRow.createCell(1).setCellValue(Util.CheckNull(r.get(i).get("lname").toString()));
            nextRow.createCell(2).setCellValue(Util.CheckNull(r.get(i).get("name").toString()));
            nextRow.createCell(3).setCellValue(Util.CheckNull(r.get(i).get("time").toString()));
            nextRow.createCell(4).setCellValue(Util.CheckNull(r.get(i).get("state").toString()));
            nextRow.createCell(5).setCellValue(Util.CheckNull(r.get(i).get("content").toString()));
        }
        HttpServletResponse response = getResponse();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=MessageExport.xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
        renderNull();
    }
}



