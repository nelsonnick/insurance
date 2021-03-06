package com.wts.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setLid(java.lang.Integer lid) {
		set("lid", lid);
	}
	
	public java.lang.Integer getLid() {
		return getInt("lid");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}

	public void setWeixin(java.lang.String weixin) {
		set("weixin", weixin);
	}
	
	public java.lang.String getWeixin() {
		return getStr("weixin");
	}

	public void setLogin(java.lang.String login) {
		set("login", login);
	}
	
	public java.lang.String getLogin() {
		return getStr("login");
	}

	public void setPass(java.lang.String pass) {
		set("pass", pass);
	}
	
	public java.lang.String getPass() {
		return getStr("pass");
	}

	public void setState(java.lang.Integer state) {
		set("state", state);
	}
	
	public java.lang.Integer getState() {
		return getInt("state");
	}

}
