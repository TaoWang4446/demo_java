package com.wst.demo_mutithread.day01.excutordemo;

import java.util.Date;

public class MyRunable implements Runnable {
    public void run() {
        for (int i=0; i<10; i++){
            System.out.println("MyRunnable线程正在执行："+new Date().getTime());
        }
    }
}
