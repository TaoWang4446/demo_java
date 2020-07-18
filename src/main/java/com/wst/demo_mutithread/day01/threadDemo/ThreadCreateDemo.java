package com.wst.demo_mutithread.day01.threadDemo;


import java.util.Date;

public class ThreadCreateDemo {
    public static void main(String[] args){
        //1.创建自定义线程
        MyThread thread = new MyThread();
        thread.start();
        //2.主线程循环打印
        for (int i=0; i<10; i++){
            System.out.println("main主线程正在执行："+new Date().getTime());
        }
    }
}
