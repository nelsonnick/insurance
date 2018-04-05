package com.wts.util;

public class Enterprise {
    private String qymc;//企业名称
    private String xydm;//信用代码
    private String yyzz;//营业执照
    private String lx;//类型
    private String jyz;//经营者
    private String zcrq;//注册日期
    private String hzrq;//核准日期
    private String yyqx1;//营业期限自
    private String yyqx2;//营业期限至
    private String djjg;//登记机关
    private String djzt;//登记状态
    private String zczb;//注册资本
    private String zs;//住所
    private String jycs;//经营场所
    private String jyfw;//经营范围

    public Enterprise(String qymc, String xydm, String yyzz, String lx, String jyz, String zcrq, String hzrq, String yyqx1, String yyqx2, String djjg, String djzt, String zczb, String zs, String jycs, String jyfw) {
        this.qymc = qymc;
        this.xydm = xydm;
        this.yyzz = yyzz;
        this.lx = lx;
        this.jyz = jyz;
        this.zcrq = zcrq;
        this.hzrq = hzrq;
        this.yyqx1 = yyqx1;
        this.yyqx2 = yyqx2;
        this.djjg = djjg;
        this.djzt = djzt;
        this.zczb = zczb;
        this.zs = zs;
        this.jycs = jycs;
        this.jyfw = jyfw;
    }

    public String getQymc() {
        return qymc;
    }

    public void setQymc(String qymc) {
        this.qymc = qymc;
    }

    public String getXydm() {
        return xydm;
    }

    public void setXydm(String xydm) {
        this.xydm = xydm;
    }

    public String getYyzz() {
        return yyzz;
    }

    public void setYyzz(String yyzz) {
        this.yyzz = yyzz;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getJyz() {
        return jyz;
    }

    public void setJyz(String jyz) {
        this.jyz = jyz;
    }

    public String getZcrq() {
        return zcrq;
    }

    public void setZcrq(String zcrq) {
        this.zcrq = zcrq;
    }

    public String getHzrq() {
        return hzrq;
    }

    public void setHzrq(String hzrq) {
        this.hzrq = hzrq;
    }

    public String getYyqx1() {
        return yyqx1;
    }

    public void setYyqx1(String yyqx1) {
        this.yyqx1 = yyqx1;
    }

    public String getYyqx2() {
        return yyqx2;
    }

    public void setYyqx2(String yyqx2) {
        this.yyqx2 = yyqx2;
    }

    public String getDjjg() {
        return djjg;
    }

    public void setDjjg(String djjg) {
        this.djjg = djjg;
    }

    public String getDjzt() {
        return djzt;
    }

    public void setDjzt(String djzt) {
        this.djzt = djzt;
    }

    public String getZczb() {
        return zczb;
    }

    public void setZczb(String zczb) {
        this.zczb = zczb;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }

    public String getJycs() {
        return jycs;
    }

    public void setJycs(String jycs) {
        this.jycs = jycs;
    }

    public String getJyfw() {
        return jyfw;
    }

    public void setJyfw(String jyfw) {
        this.jyfw = jyfw;
    }
}
