package com.wst.demo_mutithread.day01.threadSecrity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ticket2 implements Runnable {
    private int ticktNum = 100;

    //定义锁对象：构造函数参数为线程是否公平获取锁true-公平；false-不公平，即由某个线程独占，默认是false
    Lock lock = new ReentrantLock(true);

    public void run() {
        while(true){
            lock.lock();
            try{
                //加锁
                if(ticktNum > 0){
                    //1.模拟出票时间
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //2.打印进程号和票号，票数减1
                    String name = Thread.currentThread().getName();
                    System.out.println("线程"+name+"售票："+ticktNum--);
                }
            } finally {
                //放锁
                lock.unlock();
            }
        }
    }
}
