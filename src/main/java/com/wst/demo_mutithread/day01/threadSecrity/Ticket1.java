package com.wst.demo_mutithread.day01.threadSecrity;

public class Ticket1 implements Runnable {
    private int ticktNum = 100;

    public void run() {
        while(true){
            sellTicket();
        }
    }

    private synchronized void sellTicket(){
        if(ticktNum > 0){
            //1.模拟出票时间
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //2.打印进程号和票号，票数减1
            String name = Thread.currentThread().getName();
            System.out.println("线程"+name+"售票："+ticktNum--);
        }
    }
}
