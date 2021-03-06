package com.demo.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("department", "id", Department.class);
		arp.addMapping("info", "id", Info.class);
		arp.addMapping("patienthistory", "id", Patienthistory.class);
		arp.addMapping("patientinfo", "id", Patientinfo.class);
		arp.addMapping("patientrecord", "id", Patientrecord.class);
		arp.addMapping("reimbursement", "id", Reimbursement.class);
	}
}

