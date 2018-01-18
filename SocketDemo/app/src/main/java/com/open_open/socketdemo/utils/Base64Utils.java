package com.open_open.socketdemo.utils;


import org.apache.commons.codec.binary.Base64;

/**
 * Created by boby on 2017/2/21.
 */

public class Base64Utils {


    public static byte[] decode(final byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

}
