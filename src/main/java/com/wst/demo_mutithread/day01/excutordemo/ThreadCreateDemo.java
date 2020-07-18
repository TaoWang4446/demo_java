package com.wst.demo_mutithread.day01.excutordemo;

import java.util.Date;
import java.util.concurrent.*;

public class ThreadCreateDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.使用Executors创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //2.通过线程池执行线程
        executorService.execute(new MyRunable());
        //3.主线程循环打印
        for (int i=0; i<10; i++){
            System.out.println("main主线程正在执行："+new Date().getTime());
        }
    }
}
