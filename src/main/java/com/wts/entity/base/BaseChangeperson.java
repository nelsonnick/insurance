package com.wts.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseChangeperson<M extends BaseChangeperson<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setPid(java.lang.Integer pid) {
		set("pid", pid);
	}
	
	public java.lang.Integer getPid() {
		return getInt("pid");
	}

	public void setUid(java.lang.Integer uid) {
		set("uid", uid);
	}
	
	public java.lang.Integer getUid() {
		return getInt("uid");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}
	
	public java.lang.Integer getType() {
		return getInt("type");
	}

	public void setTime(java.util.Date time) {
		set("time", time);
	}
	
	public java.util.Date getTime() {
		return get("time");
	}

	public void setBefore(java.lang.String before) {
		set("before", before);
	}
	
	public java.lang.String getBefore() {
		return getStr("before");
	}

	public void setAfter(java.lang.String after) {
		set("after", after);
	}
	
	public java.lang.String getAfter() {
		return getStr("after");
	}

	public void setReason(java.lang.String reason) {
		set("reason", reason);
	}
	
	public java.lang.String getReason() {
		return getStr("reason");
	}

}
