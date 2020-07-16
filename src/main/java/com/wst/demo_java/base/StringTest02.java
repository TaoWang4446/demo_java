package com.wst.demo_java.base;

public class StringTest02 {

    public static void main(String[] args) {
//在字符串常量池
        String s1 = "hello";
        String s2 = "hello";

        String s = "aaa"+"bbb"+"ccc";

//因为使用了字符串常量池
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));
        System.out.println(s);//常量池
//不建议使用这种方式，浪费内存,在堆里
        String s3 = new String("world");
        String s4 = new String("world");
//因为使用了new关键字，会在堆里面分别创建两个对象
        System.out.println(s3 == s4);//false
        System.out.println(s3.equals(s4));//true


        String s5 = "aaa";
        String s6 = "bbb";
//不会放入字符串常量池
        String s7 = s5 + s6;
        String s8 = "aaabbb";
        String s9 = "aaa" + "bbb";
        System.out.println(s7 == s8);//false
        System.out.println(s8 == s9);//true

        String s10 = "he" + new String("llo");
        String s11 = "hello";
        System.out.println(s10 == s11);//false
    }

}