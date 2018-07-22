package com.lichao.utils;

import java.io.*;

    /*序列化*/
    public class SerializeUtils {

        public byte[] serialize(Object obj) throws IOException {
        ObjectOutputStream oout=null;
        ByteArrayOutputStream bai=null;
            bai=new ByteArrayOutputStream();
            oout=new ObjectOutputStream(bai);
            oout.writeObject(obj);
            byte[] byt=bai.toByteArray();
            return byt;
        }

    /*解序列化*/
    public Object unserizlize(byte[] byt){
        ObjectInputStream oii=null;
        ByteArrayInputStream bis=null;
        bis=new ByteArrayInputStream(byt);
        try {
            oii=new ObjectInputStream(bis);
            Object obj=oii.readObject();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
