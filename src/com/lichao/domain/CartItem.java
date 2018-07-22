package com.lichao.domain;

import java.security.PrivateKey;


public class CartItem {

    private Product product;
    private int buyNum;
    private double subtotal;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }
}
