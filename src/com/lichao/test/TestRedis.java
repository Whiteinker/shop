package com.lichao.test;

import com.lichao.domain.Product;
import com.lichao.service.ProductService;
import com.lichao.utils.JedisPoolUtils;
import com.lichao.utils.SerializeUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

public class TestRedis {

    @Test
    public void test1(){
        Jedis jedis = JedisPoolUtils.getJedis();
        System.out.println(jedis.get("categoryListJson")==null);
    }

    @Test
    public void setRedis(){
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.set("categoryListJson","f");
    }

    @Test
    public void setRedisWithTtl(){
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.set("testttl","testttl");
        jedis.expire("testttl",5);
    }

    @Test
    public void test2(){
        ProductService service = new ProductService();
        //准备热门商品---List<Produce>
        List<Product> hotProductList = service.findHotProductList();
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.set("hotProduct","hhh");

    }

    @Test
    public void hotProduct(){
        SerializeUtils serializeUtils = new SerializeUtils();
        ProductService service = new ProductService();
        Jedis jedis = new Jedis();

        //准备热门商品---List<Produce>
        byte[] hotProductListByte = jedis.get("hotProductList".getBytes());
        List<Product> hotProductList =null ;
        if(null == hotProductListByte){
            hotProductList = service.findHotProductList();
            //把该对象加入redis
            try {
                jedis.setex("hotProductList".getBytes(),1800,serializeUtils.serialize(hotProductList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            hotProductList = (List<Product>) serializeUtils.unserizlize(hotProductListByte);
        }
    }
    @Test
    public void test3() {
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.setex("name",10,"lichao");
        SerializeUtils serializeUtils = new SerializeUtils();
        byte[] hotProductListByte = jedis.get("hotProductList".getBytes());

        List<Product> hotProductList =null ;
        hotProductList = (List<Product>) serializeUtils.unserizlize(hotProductListByte);
        System.out.println(hotProductList.get(0).getShop_price());
    }


}
