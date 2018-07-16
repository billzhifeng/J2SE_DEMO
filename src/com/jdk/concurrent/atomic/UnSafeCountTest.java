package com.jdk.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程不安全的计数器
 * 
 * @author wangzhifeng
 * @date 2018年6月24日 下午3:15:20
 */
public class UnSafeCountTest {

    public static void main(String[] args) {
        Counter counter = new Counter();

        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            //多线程执行
            pool.execute(() -> counter.addOne());

            //单线程执行
            //counter.addOne();
        }

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        counter.print();
    }
}

class Counter {
    private int count;

    public void addOne() {
        System.out.println(Thread.currentThread().getName() + " before sleep，count=" + count);
        try {
            //模拟做业务耗时
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " end sleep, count=" + count);
        count = count + 1;
    }

    public void print() {
        System.out.println(count);
    }
}
