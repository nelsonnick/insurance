package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import java.util.Calendar;

public class TimeoutInterceptor implements Interceptor {

    public void intercept(Invocation inv) {
        Integer day = Calendar.getInstance().get(Calendar.DATE);
        if (day > 24) {
            inv.getController().renderText("不在业务办理时限，无法进行操作！");
        } else {
            inv.invoke();
        }
    }

}