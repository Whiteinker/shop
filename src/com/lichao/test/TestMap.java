package com.lichao.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMap {
    @Test
    public void test1(){
        Map<String,Object> map = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        list.add("mmm");

        map.put("name","lichao");
        map.put("list",list);

        List<String> s = null;
        s =(List) map.get("list");
        System.out.println(s);
    }

}
