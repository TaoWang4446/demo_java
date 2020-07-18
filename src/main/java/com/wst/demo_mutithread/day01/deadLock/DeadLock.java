package com.wst.demo_mutithread.day01.deadLock;

public class DeadLock implements Runnable {
    private static Object obj1 = new Object();//定义成静态变量，使线程可以共享实例
    private static Object obj2 = new Object();//定义成静态变量，使线程可以共享实例
    public int flag = 0;
    public void run() {
        if(flag == 0){
            System.out.println("flag："+flag);
            synchronized (obj1){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj2){
                    System.out.println("flag："+flag);
                }
            }
        }
        if(flag == 1){
            System.out.println("flag："+flag);
            synchronized (obj2){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj1){
                    System.out.println("flag："+flag);
                }
            }
        }
    }
}
