package com.argo.common.domain.common.util;

public class AES256UtilTest {

    public static void main(String[] args) throws Exception {
        String encode = AES256Util.getInstance().encrypt("test");
        System.out.print(encode);
    }
}
