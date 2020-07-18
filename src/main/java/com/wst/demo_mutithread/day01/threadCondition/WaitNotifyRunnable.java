package com.wst.demo_mutithread.day01.threadCondition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaitNotifyRunnable{
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Integer i=0;
    public void odd() {
        while(i<10){
            lock.lock();
            try{
                if(i%2 == 1){
                    System.out.println("奇数："+i);
                    i++;
                    condition.signal();
                } else {
                    condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }

    public void even(){
        while(i<10){
            lock.lock();
            try{
                if(i%2 == 0){
                    System.out.println("偶数："+i);
                    i++;
                    condition.signal();
                } else {
                    condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }
    public static void main(String[] args){
        final WaitNotifyRunnable runnable = new WaitNotifyRunnable();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                runnable.odd();
            }
        }, "偶数线程");
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                runnable.even();
            }
        }, "奇数线程");

        t1.start();
        t2.start();
    }
}
