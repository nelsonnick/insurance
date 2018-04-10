package com.wts.util;

import com.alibaba.fastjson.JSON;
import com.wts.entity.model.Family;

public class d {
    public static void main(String[] args) {
        Family family = new Family();
        family.set("name", "22")
                .set("number", "33")
                .set("state", 1)
                .set("pid", 1);
        System.out.println(JSON.toJSONString(family));
    }
}
