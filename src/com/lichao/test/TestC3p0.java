package com.lichao.test;

import com.lichao.domain.Product;
import com.lichao.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class TestC3p0 {

    @Test
    public void test1(){
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";

        try {
            int update = runner.update(sql,"sas","asdf","asdf","sdaf","sadf","asdf","1999-12-11","asdf",1,"cd");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("hello sql");

    }
    @Test
    public void test2(){
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where cid=? limit?,?";
        try {
            List<Product> list = runner.query(sql,new BeanListHandler<Product>(Product.class),1,0,9);
            for(Product p : list){
                System.out.println(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test public void test3(){
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select count(*) from category";
        try {
            int big =(int)runner.query(sql, new ScalarHandler());
            int num=big;

            System.out.println(num);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
