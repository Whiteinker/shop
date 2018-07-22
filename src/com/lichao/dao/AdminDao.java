package com.lichao.dao;

import com.lichao.domain.Admin;
import com.lichao.domain.Category;
import com.lichao.domain.Order;
import com.lichao.domain.Product;
import com.lichao.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdminDao {
    public List<Category> findAllCategory() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from category";
        return runner.query(sql,new BeanListHandler<Category>(Category.class));
    }

    public void savePtoduct(Product product) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into Product values(?,?,?,?,?,?,?,?,?,?)";
        runner.update(sql,product.getPid(),product.getPname(),product.getMarket_price(),
        product.getShop_price(),product.getPimage(),product.getPdate(),product.getIs_hot(),
        product.getPdesc(),product.getPflag(),product.getCategory().getCid());
    }

    public List<Order> findAllOrders() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from orders order by ordertime desc";
        return runner.query(sql,new BeanListHandler<Order>(Order.class));

    }

    public List<Map<String,Object>> findOrderInfoByOid(String oid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select p.pimage,p.pname,p.shop_price,i.count,i.subtotal from orderitem i,product p where i.pid=p.pid and i.oid=?";
        List<Map<String,Object>> mapList = runner.query(sql,new MapListHandler(),oid);
        return mapList;
    }

    public List<Product> findAllProducts() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select pid,pimage,pname,shop_price,is_hot from product order by pdate desc";
        return runner.query(sql,new BeanListHandler<Product>(Product.class));
    }


    public void EditProduct(Product product) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update product set pimage=?,shop_price=?,pname=?,is_hot=?,pdesc=?,market_price=?,cid=? where pid=? ";
        runner.update(sql,product.getPimage(),product.getShop_price(),product.getPname(),product.getIs_hot(),
                product.getPdesc(),product.getMarket_price(),product.getCategory().getCid(),product.getPid());
    }

    public int delProduct(String pid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "delete from product where pid=?";
        int row = runner.update(sql,pid);
        System.out.println("删除商品"+row+"条");
        return 0;
    }

    public Admin adminAuth(String adminUsername, String adminPassword) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from admin where adminusername=? and adminpassword=?";

        Admin admin = runner.query(sql,new BeanHandler<Admin>(Admin.class),adminUsername,adminPassword);

        return admin;
    }
}
