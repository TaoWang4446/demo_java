package com.wst.demo_mutithread.day02.atomicDemo;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicClass1 {
    static AtomicInteger n;
    public static void main(String[] args) throws InterruptedException {
        int j = 0;
        while(j<100){
            n = new AtomicInteger(0);
            Thread t1 = new Thread(){
                public void run(){
                    for(int i=0; i<1000; i++){
                        n.getAndIncrement();
                    }
                }
            };
            Thread t2 = new Thread(){
                public void run(){
                    for(int i=0; i<1000; i++){
                        n.getAndIncrement();
                    }
                }
            };
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println("n的最终值是："+n);
            j++;
        }

    }
}
