package com.jdk.thread.synchronize.demo2;

import java.util.Date;

/**
 * @author wangzhifeng
 * @date 2018年6月23日 下午7:03:48
 */
public class InvokeSynchronizedMethod {

    public synchronized void synMethod() {
        System.out.println(Thread.currentThread().getName() + " ,start synMethod time= " + new Date().toLocaleString());
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " ,end synMethod time= " + new Date().toLocaleString());
    }

    public synchronized void syncMethod2() {
        System.out
                .println(Thread.currentThread().getName() + " ,start syncMethod2 time= " + new Date().toLocaleString());
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " ,end syncMethod2 time= " + new Date().toLocaleString());
    }
}
