package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseInfo<M extends BaseInfo<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setCompanyinfo(java.lang.String companyinfo) {
		set("companyinfo", companyinfo);
	}

	public java.lang.String getCompanyinfo() {
		return get("companyinfo");
	}

	public void setSoftinfo(java.lang.String softinfo) {
		set("softinfo", softinfo);
	}

	public java.lang.String getSoftinfo() {
		return get("softinfo");
	}

}
