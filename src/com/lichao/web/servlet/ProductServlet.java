package com.lichao.web.servlet;

import com.google.gson.Gson;
import com.lichao.dao.ProductDao;
import com.lichao.domain.*;
import com.lichao.service.ProductService;
import com.lichao.utils.CommonsUtils;
import com.lichao.utils.JedisPoolUtils;
import com.lichao.utils.PaymentUtil;
import com.lichao.utils.SerializeUtils;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

import javax.print.ServiceUI;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@WebServlet(name = "ProductServlet")
public class ProductServlet extends BaseServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doGet(request,response);
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//
//
//        //做一个Url路由
//        //获得那个方法的method
//        String methodName = request.getParameter("method");
//        if("productList".equals(methodName)){
//            productList(request,response);
//        }else if("categoryList".equals(methodName)){
//            categoryList(request,response);
//        }else if("index".equals(methodName)){
//            index(request,response);
//        }else if("productInfo".equals(methodName)){
//            productInfo(request,response);
//        } else {
//            index(request,response);
//        }
//    }

    //获得我的订单
    public void myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return; //不让后面代码执行
        }

        ProductService service = new ProductService();
        //查询该用户所有订单
        //此时每个OrderItem 缺少List(OrderItem)
        List<Order> orderList = service.findAllOrders(user.getUid());
        //循环所有Orders为其添加OrderItem项(OrderItem和Product一对一连表查询)
        for(Order order : orderList){
            //获得Oid
            String oid = order.getOid();
            // order:List<Order<List<Map<String,Object>>>>
            List<Map<String,Object>> mapList = service.findAllOrderItemByOid(oid);
            //将MapList转换List<OrderItem> orderItems
            for(Map<String,Object> map : mapList){
                //从map中取出count,subtotal,封装到OrderItem中
                OrderItem item = new OrderItem();
                //item.setCount(Integer.parseInt(map.get("count").toString()));
                try {
                    BeanUtils.populate(item,map);  //从map中找item字段
                    //从map中取出pimage,pname,shop_price 封装到Product
                    Product product = new Product();
                    BeanUtils.populate(product,map);
                    //将product封装daoOrderItem
                    item.setProduct(product);
                    //将OrderItem 封装到Order中
                    order.getOrderItems().add(item);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    //orderList封装完成
    request.setAttribute("orderList",orderList);

    request.getRequestDispatcher("/order_list.jsp").forward(request,response);
}

    //确认订单---更新收货人信息+在线支付
    public void confirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //更新收货人信息
        //第三方支付接口待完善
        if(true) {
            String oid = request.getParameter("oid");
            ProductService service = new ProductService();
            service.updateOrderState(oid);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("<h1>第三方支付接口待完善</h1><br><h1>为测试使用</h1><br><h1>假装已经支付成功</h1><br><a href=" + request.getContextPath() + ">返回首页</a>");


            Map<String, String[]> properties = request.getParameterMap();
            Order order = new Order();
            try {
                BeanUtils.populate(order, properties);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
             service = new ProductService();
            service.updateOrderAddr(order);
            return;
        }
        //在线支付
//        if(pd_FrpId.equals("ABC-NET-B2C")){
//            //农业银行接口
//        }else if(pd_FrpId.equals("ICBC-NET-B2C")){
//            //工商银行接口
//        }
        //只接入一个接口，这个接口已经集成所有银行接口，这个接口是第三方支付平台提供的
        //接入的是易宝支付
        // 获得 支付必须基本数据
        String orderid = request.getParameter("oid");
        //String money = order.getTotal()+"";//支付金额
        String money = "0.01";//支付金额
        // 银行
        String pd_FrpId = request.getParameter("pd_FrpId");

        // 发给支付公司需要哪些数据
        String p0_Cmd = "Buy";
        String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
        String p2_Order = orderid;
        String p3_Amt = money;
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        // 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
        // 第三方支付可以访问网址
        String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
        String p9_SAF = "";
        String pa_MP = "";
        String pr_NeedResponse = "1";
        // 加密hmac 需要密钥
        String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
                "keyValue");
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
                p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
                pd_FrpId, pr_NeedResponse, keyValue);


        String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
                "&p0_Cmd="+p0_Cmd+
                "&p1_MerId="+p1_MerId+
                "&p2_Order="+p2_Order+
                "&p3_Amt="+p3_Amt+
                "&p4_Cur="+p4_Cur+
                "&p5_Pid="+p5_Pid+
                "&p6_Pcat="+p6_Pcat+
                "&p7_Pdesc="+p7_Pdesc+
                "&p8_Url="+p8_Url+
                "&p9_SAF="+p9_SAF+
                "&pa_MP="+pa_MP+
                "&pr_NeedResponse="+pr_NeedResponse+
                "&hmac="+hmac;

        //重定向到第三方支付平台
        response.sendRedirect(url);

    }

        //显示商品类别的功能
    public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductService service = new ProductService();

        //先从缓存中查询categoryList，如果没有从数据库查询并存入缓存。
        //获得redis对象，连接redis
        Jedis jedis = JedisPoolUtils.getJedis();
        String categoryListJson = jedis.get("categoryListJson");
        if (categoryListJson == null) {
            System.out.println("缓存没有数据");
            //准备分类数据(category)
            List<Category> categoryList = service.findAllCategory();
            Gson gson = new Gson();
            categoryListJson = gson.toJson(categoryList);
            jedis.set("categoryListJson", categoryListJson);

        }
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(categoryListJson);
    }

    //显示首页
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductService service = new ProductService();
        Jedis jedis = JedisPoolUtils.getJedis();
        //创建序列化对象
        SerializeUtils serializeUtils = new SerializeUtils();

        //准备热门商品---List<Produce>
        byte[] hotProductListByte = jedis.get("hotProductList".getBytes());
        List<Product> hotProductList =null ;
        if(null == hotProductListByte){
            hotProductList = service.findHotProductList();
            //把该对象加入redis
            jedis.setex("hotProductList".getBytes(),20,serializeUtils.serialize(hotProductList));
        }else {
            hotProductList = (List<Product>) serializeUtils.unserizlize(hotProductListByte);
        }


        //准备最新商品---List<Produce>
        byte[] newProductListByte = jedis.get("newProductList".getBytes());
        List<Product> newProductList = null;
        if(null == newProductListByte){
            newProductList = service.findNewProductList();
            //把该对象加入redis
            jedis.setex("newProductList".getBytes(),1800,serializeUtils.serialize(newProductList));
        }else {
            newProductList = (List<Product>) serializeUtils.unserizlize(newProductListByte);
        }

        //准备分类数据(category)
//        List<Category> categoryList = service.findAllCategory();

        request.setAttribute("hotProductList", hotProductList);
        request.setAttribute("newProductList", newProductList);
//        request.setAttribute("categoryList",categoryList);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    //显示商品详细信息
    public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得当前页
        String currentPage = request.getParameter("currentPage");
//        System.out.println(currentPage);
        //获得商品类别
        String cid = request.getParameter("cid");

        //获得商品pid
        String pid = request.getParameter("pid");
        ProductService service = new ProductService();
        Product product = service.findProductByPid(pid);
//        System.out.println("CID"+cid);
        //根据category中cid获取cname
        if(null != cid) {
            String cname = service.findCnameByCid(cid).getCname();
            request.setAttribute("cname", cname);
        }

        request.setAttribute("product", product);
        request.setAttribute("cid", cid);
        request.setAttribute("currentPage", currentPage);

        //获得客户端携带的Cookie--获得名字pids的cookie
        Cookie[] cookies = request.getCookies();
        String pids = pid;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("pids".equals(cookie.getName())) {
                    pids = cookie.getValue(); //1-3-2
                    //将pids拆分
                    String[] split = pids.split("_");
                    List<String> asList = Arrays.asList(split);
                    LinkedList list = new LinkedList<String>(asList);

                    if (list.contains(pid)) {     //如果包含这个pid
                        list.remove(pid);
                    }
                    //直接放前面
                    list.addFirst(pid);
                    //保留7个历史记录
                    if (list.size() > 7) {
                        LinkedList list1 = new LinkedList<String>(list.subList(0, 7));
                        list = list1;
                    }
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < list.size(); i++) {
                        sb.append(list.get(i));
                        sb.append("_");
                    }
                    pids = sb.substring(0, sb.length() - 1);
                    //添加Cookie
                }
            }
        }

        //转发之前 创建/重建 Cookie 存储Pid
        Cookie cookie_pids = new Cookie("pids", pids);
        response.addCookie(cookie_pids);
        request.getRequestDispatcher("/product_info.jsp").forward(request, response);
    }

    //根据商品的类别获得列表
    public void productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得cid
        String cid = request.getParameter("cid");

        String currentPageStr = request.getParameter("currentPage");
        if (currentPageStr == null) {
            currentPageStr = "1";
        }
        int currentPage = Integer.parseInt(currentPageStr);
        int currentCount = 12;

        ProductService service = new ProductService();
        PageBean pagebean = service.findProductListByCid(cid, currentPage, currentCount);

        request.setAttribute("pageBean", pagebean);
        request.setAttribute("cid", cid);

        //定义历史记录商品的集合
        List<Product> historyProductList = new ArrayList<Product>();
        //获得Cookie中的pids
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("pids".equals(cookie.getName())) {
                    String pids = cookie.getValue(); //1-3-2
                    String[] split = pids.split("_");
                    for (String pid : split) {
                        Product pro = service.findProductByPid(pid);
                        historyProductList.add(pro);
                    }
                }
            }
        }
        //将历史记录集合放到域中
        request.setAttribute("historyProductList", historyProductList);
        request.getRequestDispatcher("/product_list.jsp").forward(request, response);

    }

    //把商品添加到购物车
    public void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("addtocart");
        ProductService service = new ProductService();
        HttpSession session = request.getSession();
        //获得需要加入购物车的商品
        String pid = request.getParameter("pid");
        //获得该商品购买数量
        int buyNum = Integer.parseInt(request.getParameter("buyNum"));
        //获得Product对象
        Product product = service.findProductByPid(pid);
        //计算小计
        double subtotal = product.getShop_price() * buyNum;
        //封装CartItem
        CartItem item = new CartItem();
        item.setBuyNum(buyNum);
        item.setProduct(product);
        item.setSubtotal(subtotal);
        //获得购物车---判断是否存在购物车
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        //将购物项放到购物车
        //先判断 购物车是否含有购物项
        Map<String, CartItem> cartItems = cart.getCartItems();
        double newSubTotal = 0.0;
        if (cartItems.containsKey(pid)) {
            //取出原有商品数量
            CartItem cartItem = cartItems.get(pid);
            int oldBuyNumber = cartItem.getBuyNum();
            oldBuyNumber += buyNum;
            cartItem.setBuyNum(oldBuyNumber);
            cart.setCartItems(cartItems);
            //修改小计
            //原来该商品小计
            double oldSubTotal = cartItem.getSubtotal();
            //新的商品小计
            newSubTotal = buyNum * product.getShop_price();
            //该商品小计
            cartItem.setSubtotal(oldSubTotal + newSubTotal);

        } else {
            cart.getCartItems().put(product.getPid(), item);
            //商品小计
            newSubTotal = buyNum * product.getShop_price();
        }
        //计算购物车商品总价格
        double total = cart.getTotal() + newSubTotal;
        cart.setTotal(total);
        //将车放回session
        session.setAttribute("cart", cart);
        System.out.println("addtocart_");
        //跳转到购物车页面
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
//        request.getRequestDispatcher("/cart.jsp").forward(request,response);
    }

    //删除单一商品
    public void delProFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid = request.getParameter("pid");
        //session中购物车购物项集合中的item
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            Map<String, CartItem> cartitem = cart.getCartItems();
            //修改总价
            cart.setTotal(cart.getTotal() - cartitem.get(pid).getSubtotal());
            //移除当前商品
            cartitem.remove(pid);
            cart.setCartItems(cartitem);

            session.setAttribute("cart", cart);
            //跳转回购物车页面
            response.sendRedirect(request.getContextPath() + "/cart.jsp");

        }
    }
    //清空购物车
    public void clearCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("cart");
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
    }
    //提交订单
    public void submitOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //判断用户是否登陆
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
//            response.sendRedirect(request.getContextPath()+"/login.jsp");
            String nextUrl =  request.getContextPath()+ request.getServletPath()+"?"+request.getQueryString();
            request.setAttribute("nextUrl",nextUrl);
            request.getRequestDispatcher("/login.jsp").forward(request,response);
            return; //不让后面代码执行
        }
        //封装好一个Order对象传递给service层
        Order order = new Order();
        //private String oid;//该订单的订单号
        String oid = CommonsUtils.getUUID();
        order.setOid(oid);
        //private Date ordertime;//下单时间
        order.setOrdertime(new Date());
        //private double total;//该订单的总金额
        //获得购物车
        Cart cart = (Cart) session.getAttribute("cart");
        double total = cart.getTotal();
        order.setTotal(total);
        //private int state;//订单支付状态 1代表已付款 0代表未付款
        order.setState(0);
        //private String address;//收货地址
        order.setAddress(null);
        //private String name;//收货人
        order.setName(null);
        //private String telephone;//收货人电话
        order.setTelephone(null);
        // private User user;//该订单属于哪个用户
        order.setUser(user);

    //封装订单项orderItem
        List<OrderItem> orderItems = new ArrayList<OrderItem>();

        Map<String,CartItem> cartItems = cart.getCartItems();
        for(Map.Entry<String,CartItem> entry : cartItems.entrySet()){
            CartItem cartItem = entry.getValue();
            OrderItem item = new OrderItem();
            //private String itemid;//订单项的id
            item.setItemid(CommonsUtils.getUUID());
            //private int count;//订单项内商品的购买数量
            item.setCount(cartItem.getBuyNum());
            //private double subtotal;//订单项小计
            item.setSubtotal(cartItem.getSubtotal());
            //private Product product;//订单项内部的商品
            item.setProduct(cartItem.getProduct());
            //private Order order;//该订单项属于哪个订单
            item.setOrder(order);
            //封装该条订单项
            order.getOrderItems().add(item);
        }
        //将数据传到Service层
        ProductService service= new ProductService();
        service.submitOrder(order);

        session.setAttribute("order",order);

        //清空购物车
        session.removeAttribute("cart");
        //页面重定向
        response.sendRedirect(request.getContextPath()+"/order_info.jsp");

    }

}