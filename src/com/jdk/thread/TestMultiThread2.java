package com.jdk.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMultiThread2 implements Runnable {

    private static Object        o    = new Object();

    private static Integer       si   = 0;

    private static AtomicInteger flag = new AtomicInteger();

    @Override
    public void run() {
        //run1();
        //run2();
        //run3();
        run4();
    }

    //当si做++操作后(可以直接看字节码，这里不贴了)，
    //在putstatic之前有几步操作，就是我们常说的非原子操作，而这时候si已经不是原来的对象了，这样锁对另外一个线程来说就失效了，
    public void run1() {
        for (int k = 0; k < 20000; k++) {
            synchronized (si) {
                si++;
            }
        }
        flag.incrementAndGet();
    }

    //执行结束 结果为40000
    public void run2() {
        for (int k = 0; k < 20000; k++) {
            synchronized (o) {
                si++;
            }
        }
        flag.incrementAndGet();
    }

    //执行结束结果<40000
    public void run3() {
        for (int k = 0; k < 20000; k++) {
            synchronized (o) {
                si++;
                o = new Object();
            }
        }
        flag.incrementAndGet();
    }

    //执行结果40000
    public void run4() {
        for (int k = 0; k < 2000000; k++) {
            synchronized (o) {
                si++;
                Object temp = o;
                o = new Object();
                o = temp;
            }
        }
        flag.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        TestMultiThread2 t1 = new TestMultiThread2();
        TestMultiThread2 t2 = new TestMultiThread2();
        ExecutorService exec1 = Executors.newCachedThreadPool();
        ExecutorService exec2 = Executors.newCachedThreadPool();
        exec1.execute(t1);
        exec2.execute(t2);
        while (true) {
            if (flag.intValue() == 2) {
                System.out.println("si>>>>>" + si);
                break;
            }
            Thread.sleep(50);
        }

    }

}
