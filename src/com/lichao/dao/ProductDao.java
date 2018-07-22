package com.lichao.dao;

import com.lichao.domain.Category;
import com.lichao.domain.Order;
import com.lichao.domain.OrderItem;
import com.lichao.domain.Product;
import com.lichao.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductDao {
    //获得热门商品
    public List<Product> findHotProductList() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where is_hot=? order by pdate desc limit ?,?";
        return runner.query(sql,new BeanListHandler<Product>(Product.class),1,0,9);
    }

    //获得最新商品
    public List<Product> findNewProductList() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product order by pdate desc limit ?,?";
        return runner.query(sql,new BeanListHandler<Product>(Product.class),0,9);
    }

    public List<Category> findAllCategory() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from category";
        return runner.query(sql,new BeanListHandler<Category>(Category.class));
    }

    public int getCount(String cid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select count(*) from product where cid=?";
        Long query = (Long) runner.query(sql, new ScalarHandler(),cid);
        return query.intValue();
    }

    public List<Product> findProductByPage(String cid, int index, int currentCount) throws SQLException {

        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where cid=? limit?,?";
        List<Product> list = runner.query(sql,new BeanListHandler<Product>(Product.class),cid,index,currentCount);
        return list;
    }

    public Product findProductByPid(String pid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where pid=?";
        Product product = runner.query(sql,new BeanHandler<Product>(Product.class),pid);
        return product;
    }

    //存储Order
    public void addOrders(Order order) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
        Connection conn = null;
        conn = DataSourceUtils.getConnection();
        System.out.println("ProductDao:"+order.getOrdertime());
        runner.update(conn,sql,order.getOid(),order.getSqlOrderTime(),order.getTotal(),
                order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());
    }

    //存储OrderItem
    public void addOrderItem(Order order) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into orderitem values(?,?,?,?,?)";
        Connection conn = null;
        conn = DataSourceUtils.getConnection();
        List<OrderItem> orderItem = order.getOrderItems();

        for(OrderItem item : orderItem){
            runner.update(conn,sql,item.getItemid(),item.getCount(),item.getSubtotal(),
                    item.getProduct().getPid(),item.getOrder().getOid());
        }
    }

    public void updateOrderAddr(Order order) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update orders set address=?,name=?,telephone=? where oid=?";
        runner.update(sql,order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
    }

    public void updateOrderState(String r6_order) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update orders set state   =1 where oid=?";
        runner.update(sql,r6_order);
    }

    public List<Order> findAllOrders(String uid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from orders where uid=? order by ordertime desc";
        return runner.query(sql,new BeanListHandler<Order>(Order.class),uid);
    }

    public List<Map<String,Object>> findAllOrderItemByOid(String oid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from orderitem i,product p where i.pid=p.pid and i.oid=?";
        List<Map<String,Object>> mapList= runner.query(sql,new MapListHandler(),oid);
        return mapList;
        //MapList:List中存放着Map
    }

    public Category findCidByPid(String pid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select cid from product where pid=?";
        Category cid = runner.query(sql,new BeanHandler<Category>(Category.class),pid);
        return cid;
    }

    public Category findCnameByCid(String cid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select cname from category where cid=?";
        Category category = runner.query(sql,new BeanHandler<Category>(Category.class),cid);
        return category;
    }
}
