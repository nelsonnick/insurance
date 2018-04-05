package com.wts.util;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Jnjgfw {

    public static Enterprise getEnterprise(String regNo, String id) throws IOException {

        String qymc = "", xydm = "", yyzz = "", lx = "", jyz = "", zcrq = "", hzrq = "", djjg = "", djzt = "", zs = "", jyfw = "", jycs = "", zczb = "", yyqx1 = "", yyqx2 = "";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.jnjgfw.gov.cn/command/search/detail?entName=&uniscId=&orgCode=&regNo=" + regNo + "&taxpayId=&keyword=")
                .get()
                .addHeader("content-type", "text/html;charset=UTF-8")
                .addHeader("connection", "keep-alive")
                .addHeader("access-control-allow-headers", "X-Requested-With")
                .addHeader("cache-control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        Document doc = Jsoup.parse(response.body().string());
        Element masthead = doc.select("div.qyqx-title>span").first();
        qymc = masthead.text();
        List<Element> mastheads = doc.select("table.table-bordered").first().select("tr");
        xydm = blank(mastheads.get(0).select("td.fieldInput").get(0).text().trim());
        yyzz = blank(mastheads.get(0).select("td.fieldInput").get(1).text().trim());
        lx = blank(mastheads.get(1).select("td.fieldInput").get(0).text().trim());
        jyz = blank(mastheads.get(1).select("td.fieldInput").get(1).text().trim());
        zcrq = blank(mastheads.get(2).select("td.fieldInput").get(0).text().trim());
        hzrq = blank(mastheads.get(2).select("td.fieldInput").get(1).text().trim());
        yyqx1 = blank(mastheads.get(3).select("td.fieldInput").get(0).text().trim());
        yyqx2 = blank(mastheads.get(3).select("td.fieldInput").get(1).text().trim());
        djjg = blank(mastheads.get(4).select("td.fieldInput").get(0).text().trim());
        djzt = blank(mastheads.get(4).select("td.fieldInput").get(1).text().trim());
        zczb = blank(mastheads.get(5).select("td.fieldInput").get(0).text().trim());
        zs = blank(mastheads.get(6).select("td.fieldInput").get(0).text().trim());
        jycs = blank(mastheads.get(7).select("td.fieldInput").get(0).text().trim());
        jyfw = blank(mastheads.get(8).select("td.fieldInput").get(0).text().trim());
        return new Enterprise(qymc, xydm, yyzz, lx, jyz, zcrq, hzrq, yyqx1, yyqx2, djjg, djzt, zczb, zs, jycs, jyfw);

    }

    public static List<Enterprise> searchById(String id) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(0, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"keyword\"\r\n\r\n" + id + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url("http://www.jnjgfw.gov.cn/command/search?keyword=" + id)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("connection", "keep-alive")
                .addHeader("access-control-allow-headers", "X-Requested-With")
                .addHeader("cache-control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        Document doc = Jsoup.parse(response.body().string());
        List<Element> mastheads = doc.select("div.result");
        List<Enterprise> enterprises = null;
        if (mastheads.size() != 0) {
            for (int i = 0; i < mastheads.size(); i++) {
                String e = mastheads.get(i).select("div.result_title>a").first().attr("href");
                String f = e.substring(22, e.length() - 2).split(",")[2];
                String regNo = f.substring(1, f.length() - 1);
                enterprises.add(getEnterprise(regNo, id));
            }

        }
        return enterprises;
    }

    public static Boolean checkById(String id) {
        try {
            List<Enterprise> enterprises = searchById(id);
            if (enterprises.size() == 0) {
                return false;
            } else {
                for (Enterprise e : enterprises) {
                    if (e.getDjzt().equals("在营（开业）企业")) {
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 判断是否为空字符串
     *
     * @param str
     * @return 空字符串返回空格，否则返回原值
     */
    public static String blank(String str) {
        if (str.equals("")) {
            return " ";
        } else {
            return str;
        }
    }

}
