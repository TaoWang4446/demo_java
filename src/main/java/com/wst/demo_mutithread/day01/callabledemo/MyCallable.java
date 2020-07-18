package com.wst.demo_mutithread.day01.callabledemo;

import java.util.Date;
import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {
    public String call() throws Exception {
        for (int i=0; i<10; i++){
            System.out.println("MyCallable正在执行："+new Date().getTime());
        }
        return "MyCallable执行完毕！";
    }
}
