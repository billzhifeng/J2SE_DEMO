package com.jdk.thread.deadLock;

public class TestMain {
    public static void main(String[] args)
    {
        DeadLock dl = new DeadLock();
        Thread0 t0 = new Thread0(dl);
        Thread1 t1 = new Thread1(dl);
        t0.start();
        t1.start();
    }
}
