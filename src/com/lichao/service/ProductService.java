package com.lichao.service;

import com.lichao.dao.CategoryDao;
import com.lichao.dao.ProductDao;
import com.lichao.domain.*;
import com.lichao.utils.DataSourceUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductService {

    //获得热门商品
    public List<Product> findHotProductList(){
        ProductDao dao = new ProductDao();
        List<Product> hotProduct = null;
        try {
            hotProduct = dao.findHotProductList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println("hostproduct");
//        for (Product x :hotProduct){
//            System.out.println(x.getPname());
//        }

        return hotProduct;
    }

    //获得最新商品
    public List<Product> findNewProductList() {
        ProductDao dao = new ProductDao();
        List<Product> newProduct = null;
        try {
            newProduct = dao.findNewProductList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newProduct;
    }

    public List<Category> findAllCategory() {
        ProductDao dao = new ProductDao();
        List<Category> allCategory = null;
        try {
            allCategory = dao.findAllCategory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCategory;
    }

    public PageBean findProductListByCid(String cid,int currentPage,int currentCount) {

        ProductDao dao = new ProductDao();

        //封装一个PageBean 返回web层
        PageBean<Product> pageBean = new PageBean<Product>();


        //封装当前页
        pageBean.setCurrentPage(currentPage);
        //封装每页显示的条数
        pageBean.setCurrentCount(currentCount);
        //封装总条数
        int totalCount = 0;
        try {
            totalCount = dao.getCount(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageBean.setTotalCount(totalCount);
        //封装总页数
        int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
        pageBean.setTotalPage(totalPage);
        //当前页显示的数据
        //select * from product where cid=? limit?,?
        //当前页与起始索引index关系
        List<Product> list = null;
        int index = (currentPage-1)*currentCount;
        try {
            list = dao.findProductByPage(cid,index,currentCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageBean.setList(list);
        return pageBean;

    }

    public Product findProductByPid(String pid) {
        Product product = null;
        ProductDao dao = new ProductDao();
        try {
            product =  dao.findProductByPid(pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    //提交订单和订单项
    public void submitOrder(Order order){
        ProductDao dao = new ProductDao();
        try {
            //开启事务
            DataSourceUtils.startTransaction();
            //调用dao存储order表数据的方法
            dao.addOrders(order);
            //调用dao存储orderItem数据项的方法
            dao.addOrderItem(order);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                DataSourceUtils.commitAndRelease();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public void updateOrderAddr(Order order) {
        ProductDao dao = new ProductDao();
        try {
            dao.updateOrderAddr(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateOrderState(String r6_order) {

        ProductDao dao = new ProductDao();
        try {
            dao.updateOrderState(r6_order);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    //获得该用户的订单
    public List<Order> findAllOrders(String uid) {
        ProductDao dao = new ProductDao();
        List<Order> orderList = null;
        try {
            orderList = dao.findAllOrders(uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<Map<String,Object>> findAllOrderItemByOid(String oid) {
        ProductDao dao = new ProductDao();
        List<Map<String,Object>> mapList = null;
        try {
            mapList = dao.findAllOrderItemByOid(oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapList;
    }


    public Category findCidByPid(String pid) {
        ProductDao dao = new ProductDao();
        Category cid = null;
        try {
            cid = dao.findCidByPid(pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cid;
    }

    public Category findCnameByCid(String cid) {
        ProductDao dao = new ProductDao();
        Category category = null;
        try {
            category = dao.findCnameByCid(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;

    }
}
