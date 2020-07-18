package com.wst.demo_mutithread.day01.threadSecrity;

public class TicketDemo {
    public static void main(String[] args){
        Ticket2 ticket = new Ticket2();
        Thread thread1 = new Thread(ticket, "窗口1");
        Thread thread2 = new Thread(ticket, "窗口2");
        Thread thread3 = new Thread(ticket, "窗口3");

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
