package com.lichao.test;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestDate {

    @Test
    public void test1() {
        String value = "1888-12-11";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {

            parse = format.parse(value.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(parse);
    }
    @Test
    public void test2(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println(date.toString());
    }

}
