package com.wst.demo_mutithread.day02.threaLocalDemo;

public class Bank {
    ThreadLocal<Integer> t = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue(){
            return 0;
        }
    };
    public Integer get(){
        return t.get();
    }
    public void set(){
        t.set(t.get()+10);
    }
    
    public static void main(String[] args){
        Bank bank = new Bank();
        Transfer transfer = new Transfer(bank);
        Thread t1 = new Thread(transfer);
        Thread t2 = new Thread(transfer);
        t1.start();
        t2.start();
    }
}

class Transfer implements Runnable{
    Bank bank;
    public Transfer(Bank bank){
        this.bank = bank;
    }
    public void run() {
        for (int i=0;i<10;i++){
            bank.set();
            System.out.println(Thread.currentThread()+""+bank.get());
        }
    }
}
