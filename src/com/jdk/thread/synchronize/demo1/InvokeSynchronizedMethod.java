package com.jdk.thread.synchronize.demo1;

/**
 * @author wangzhifeng
 * @date 2018年6月23日 下午7:03:48
 */
public class InvokeSynchronizedMethod {

    public synchronized void synMethod() {
        System.out.println(Thread.currentThread().getName() + " ,start synMethod ...");
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " ,end synMethod");
    }

    public void noSyncMethod() {
        System.out.println(Thread.currentThread().getName() + "  ,start noSyncMethod ");
    }
}
