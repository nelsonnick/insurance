package com.wts.common;

import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.template.Engine;
import com.wts.controller.*;
import com.wts.entity.model._MappingKit;
import com.wts.task.*;

/**
 * API引导式配置
 */
public class Config extends JFinalConfig {

    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        PropKit.use("a_little_config.txt");
        me.setDevMode(false);
        me.setError404View("/dist/404.html");
        me.setError500View("/dist/500.html");
    }

    /**
     * 配置路由
     */
    public void configRoute(Routes me) {
        me.add("/", MainController.class);
        me.add("/person", PersonController.class);
        me.add("/family", FamilyController.class);
        me.add("/user", UserController.class);
        me.add("/pass", PassController.class);
        me.add("/message", MessageController.class);
    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        // 配置C3p0数据库连接池插件
        C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
        me.add(c3p0Plugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
        arp.setShowSql(false);
        me.add(arp);
        _MappingKit.mapping(arp);
        //配置任务调度插件
        Cron4jPlugin cp = new Cron4jPlugin();
        cp.addTask("0 6 * * *", new SecurityDown());
        cp.addTask("0 9 * * *", new SecuritySend());
        cp.addTask("30 9 * * *", new Commercial());
        cp.addTask("0 22 * * *", new SecurityDelete());
        me.add(cp);
    }

    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {
        me.add(new TxByMethods("save","update"));
    }

    /**
     * 配置处理器
     */
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("contextPath"));//设置上下文路径
    }

    /**
     * 配置模板引擎
     */
    public void configEngine(Engine me) {}
    /**
     * 建议使用 JFinal 手册推荐的方式启动项目
     * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
     */
    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 80, "/");
    }
}
