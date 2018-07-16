package com.jdk.thread.threadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
 * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
 */
public class ThreadLocalTest {
    //    ThreadLocal类接口很简单，只有4个方法，我们先来了解一下：
    //    void set(Object value)设置当前线程的线程局部变量的值。
    //    public Object get()该方法返回当前线程所对应的线程局部变量。
    //    public void remove()将当前线程局部变量的值删除，目的是为了减少内存的占用，该方法是JDK 5.0新增的方法。需要指出的是，当线程结束后，对应该线程的局部变量将自动被垃圾回收，所以显式调用该方法清除线程的局部变量并不是必须的操作，但它可以加快内存回收的速度。
    //    protected Object initialValue()返回该线程局部变量的初始值，该方法是一个protected的方法，显然是为了让子类覆盖而设计的。这个方法是一个延迟调用方法，在线程第1次调用get()或set(Object)时才执行，并且仅执行1次。ThreadLocal中的缺省实现直接返回一个null。

    static final ThreadLocal<Integer> local = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] a) {
        System.out.println("Thread_" + Thread.currentThread().getName() + " start ");
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 1; i <= 3; i++) {
            executor.execute(new TaskLocal(i));
        }
        executor.shutdown();
        System.out.println("Thread_" + Thread.currentThread().getName() + " end ");
    }

    static class TaskLocal implements Runnable {

        private int num;

        public TaskLocal(int num) {
            this.num = num;
        }

        @Override
        public void run() {

            local.set((int) (100 * Math.random()));//设值
            System.out.println("Task " + num + " local num result is " + local.get());
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep((int) (1000 * Math.random()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i % 2 == 0) {
                    local.set((int) (100 * Math.random()));//设值
                }
                System.out.println("Task " + num + " and i= " + i + " ,local num result is " + local.get());//取值
            }

        }
    }

}
