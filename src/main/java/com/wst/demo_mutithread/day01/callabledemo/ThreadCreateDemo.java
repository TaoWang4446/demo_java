package com.wst.demo_mutithread.day01.callabledemo;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadCreateDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask task = new FutureTask(new MyCallable());
        Thread thread = new Thread(task);
        thread.start();

        for (int i=0; i<10; i++){
            System.out.println("main主线程正在执行："+new Date().getTime());
        }
        System.out.println(task.get());
    }
}
