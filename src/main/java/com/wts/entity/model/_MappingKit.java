package com.wts.entity.model;

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
		arp.addMapping("changefamily", "id", Changefamily.class);
		arp.addMapping("changeperson", "id", Changeperson.class);
		arp.addMapping("family", "id", Family.class);
		arp.addMapping("location", "id", Location.class);
		arp.addMapping("person", "id", Person.class);
		arp.addMapping("sendmessage", "id", Sendmessage.class);
		arp.addMapping("type", "id", Type.class);
		arp.addMapping("user", "id", User.class);
	}
}

