package com.wst.demo_mutithread.day01;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args){
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        new Thread(new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName()+"：准备...");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"启动完毕："+new Date().getTime());
            }
        },"线程1").start();
        new Thread(new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName()+"：准备...");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"启动完毕："+new Date().getTime());
            }
        },"线程2").start();
        new Thread(new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName()+"：准备...");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"启动完毕："+new Date().getTime());
            }
        },"线程3").start();
    }
}
