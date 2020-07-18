package com.wst.demo_mutithread.day01.threadDemo;

import java.util.Date;

public class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i<10; i++){
            System.out.println("mythread线程正在执行："+new Date().getTime());
        }
    }

    public static void main(String[] args) {
        new MyThread().start();
    }
}
