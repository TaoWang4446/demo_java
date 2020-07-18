package com.wst.demo_mutithread.day01;

import java.util.concurrent.CountDownLatch;

public class CountDown {
    private Integer i = 0;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void odd(){
        while(i < 10){
            if(i%2 == 1){
                System.out.println("奇数："+i);
                i++;
                countDownLatch.countDown();
            } else {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void even(){
        while(i < 10){
            if(i%2 == 0){
                System.out.println("偶数："+i);
                i++;
                countDownLatch.countDown();
            } else {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args){
        final CountDown countDown = new CountDown();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                countDown.odd();
            }
        },"奇数");
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                countDown.even();
            }
        },"偶数");

        t1.start();
        t2.start();
    }
}
