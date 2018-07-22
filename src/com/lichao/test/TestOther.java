package com.lichao.test;

import com.lichao.domain.Category;
import com.lichao.domain.User;
import com.lichao.service.AdminService;
import com.lichao.utils.BeanFactory;
import com.lichao.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TestOther {

    @Test
    public void test1(){
        Long isExist = 0L;
        System.out.println(isExist.getClass());
    }

    @Test
    public void test2(){
        String a="1";
        int s = Integer.parseInt(a);
        System.out.println(1);

    }

    @Test
    public void test3(){
        Integer i = new Integer(10);
        Integer j=10;
        System.out.println(i==j);
    }

    @Test
    public void test4(){
        String pids = "1_2_3_4_5_2_2_3_4_5_";
        String[] split = pids.split("_");
        System.out.println(split);
        List<String > asList = Arrays.asList(split);
        System.out.println(asList.get(1).getClass());
        LinkedList list = new LinkedList<String >(asList);

        if(list.size()>6){
            LinkedList<String> list1 = new LinkedList<String >(list.subList(0,6));
            list = list1;
        }

        System.out.println(list);
    }
    @Test
    public void test5(){

        int buyNum = Integer.parseInt("1");
        System.out.println(buyNum+1);
    }
    @Test
    public void test6(){

        System.out.println((AdminService) BeanFactory.getBean("adminService"));
    }

    @Test
    public void test7(){
        Class clazz = null;
        try {
            clazz = Class.forName("com.lichao.domain.User");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            User object =(User) clazz.newInstance();
            object.setUid("Uid");
            System.out.println(object.getUid());
            System.out.println(object.getClass());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test8(){
        Object test="12";
        Double price = Double.parseDouble(test.toString());
        System.out.println(price);
        }

        @Test
        public void  testFindCnameByCid() throws SQLException {
            QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
            String sql = "select cname from category where cid=?";
            String cname = runner.query(sql,new BeanHandler<String>(String.class),1);
            System.out.println(cname);

        }

}
