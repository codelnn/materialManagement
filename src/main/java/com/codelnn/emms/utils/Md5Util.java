package com.codelnn.emms.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-16 20:54
 **/
public class Md5Util {

    public static String md5Encryption(String source,String salt){
        String algorithmName = "MD5";
        /**
         * 加密次数
         */
        int hashIterations = 1024;
        SimpleHash simpleHash = new SimpleHash(algorithmName,source,salt,hashIterations);
        return simpleHash.toString();
    }

    public static void main(String[] args) {
        System.out.println(md5Encryption("123456", "123456"));
    }

}
