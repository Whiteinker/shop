package com.lichao.test;

import com.lichao.utils.DBCPUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;

import java.sql.SQLException;

public class TestDbcp {
    @Test
    public void test1(){
        QueryRunner runner = new QueryRunner(DBCPUtils.getDataSource());
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
        try {
            int update = runner.update(sql,"2assfsv372","asdf","asdf","sdaf","sadf","asdf","1999-12-11","asdf",1,"cd");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("hello sql");

    }

}
