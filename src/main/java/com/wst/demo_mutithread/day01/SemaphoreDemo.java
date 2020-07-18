package com.wst.demo_mutithread.day01;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    static class Machine implements Runnable{
        private int num;
        private Semaphore semaphore;

        public Machine(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        public void run() {
            try {
                semaphore.acquire();//请求机器
                System.out.println("工人"+this.num+"请求机器，正在使用机器");
                Thread.sleep(1000);
                System.out.println("工人"+this.num+"使用完毕，已经释放机器");
                semaphore.release();//释放机器
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        int worker = 8;//工人数
        Semaphore semaphore = new Semaphore(3);//机器数
        for (int i=0; i< worker; i++){
            new Thread(new Machine(i, semaphore)).start();
        }
    }
}
