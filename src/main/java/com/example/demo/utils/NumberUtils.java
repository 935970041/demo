package com.example.demo.utils;

public class NumberUtils {
    /**
     *
     * @Description:判断该数是奇数还是偶数
     * @param num
     * @return
     * @Author:ZhangZhiyuan
     * @Since:2018年1月19日 上午9:39:12
     */
    public static boolean oddOrNot(int num) {
        // 例如3&1 实际为11&01=01 4&1=100&001=000
        return (num & 1) != 0; // 奇数返回true
    }

}
