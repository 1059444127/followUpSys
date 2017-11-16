package com.demo.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.activerecord.Record;

public class DepartmentInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {

		System.out.println("Before Department invoking " + inv.getActionKey());

		System.out.println();
		Record m = inv.getController().getSessionAttr("admin_user");

		if (m == null) {
			inv.getController().redirect("/admin/login");
		} else {
			inv.invoke();
		}

		System.out.println("After Admin invoking " + inv.getActionKey());

	}

}
