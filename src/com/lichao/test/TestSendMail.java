package com.lichao.test;

import com.lichao.utils.CommonsUtils;
import com.lichao.utils.MailUtils;
import org.junit.Test;

import javax.mail.MessagingException;

public class TestSendMail {

    @Test
    public void test1() {
        String activeCode = "yigejiadeUUID";
        String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户"
                + "<a href='http://localhost:8080/HeimaShop/active?activeCode=" + activeCode + "'>"
                + "http://localhost:8080/HeimaShop/active?activeCode=" + activeCode + "</a>";
        try {
            MailUtils.sendMail("2709132779@qq.com", emailMsg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
