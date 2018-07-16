package com.jdk.thread.pool.demo;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TheadPoolDemo {

    public ThreadPoolExecutor                        threadPoolExecutor;

    private final int                                corePoolSize  = 3;
    private final int                                maxPoolSize   = 3;
    private final int                                keepAliveTime = 1000;

    private IBizExecute                              bizExecute;

    //之前批次
    public ConcurrentHashMap<Integer, AtomicInteger> map           = new ConcurrentHashMap<Integer, AtomicInteger>();

    public static void main(String[] args) {

        for (int st = 0; st < 10000; st++) {

            IBizExecute bizExe = new BizExeImpl();
            TheadPoolDemo demo = new TheadPoolDemo(bizExe);
            int total = 0;
            for (int i = 1; i <= 6400; i++) {
                demo.put(i);
                total += i;
            }

            demo.waitUntilDone();
            int check = 0;
            for (Map.Entry<Integer, AtomicInteger> m : demo.map.entrySet()) {
                check += m.getValue().get();
            }

            if (0 != (total - check)) {
                System.out.println("任务执行完成" + total + "|" + check + "|统计" + (0 == (total - check)));
            }
        }
        System.out.println("任务执行完成");
    }

    public TheadPoolDemo(IBizExecute bizExe) {
        this.bizExecute = bizExe;
        initExecutor();
    }

    /***
     * 线程池接受执行任务
     * 
     * @param o
     */
    public void put(Integer i) {
        threadPoolExecutor.execute(() -> bizExecute.exe(i, map));
    }

    public void waitUntilDone() {
        try {
            threadPoolExecutor.shutdown();
            if (!threadPoolExecutor.awaitTermination(2, TimeUnit.HOURS)) {
                throw new RuntimeException("任务执行超时");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("任务执行超时", e);
        }
    }

    /**
     * 初始化线程池
     */
    private void initExecutor() {
        /**
         * 使用SynchronousQueue, CallerRunsPolicy的原因： 对账使用线程池的主要原因是利用多线程来提升业务处理速度，
         * 所以内存队列为SynchronousQueue，相当于queue设置为0，CallerRunsPolicy含义为当线程池繁忙时让主线程执行
         */
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1), new ThreadFactory() {
                    private AtomicInteger count = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "poolThread-" + count.incrementAndGet());
                    }
                });
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

}

interface IBizExecute {

    public void exe(Integer i, ConcurrentHashMap<Integer, AtomicInteger> map);

}

class BizExeImpl implements IBizExecute {

    @Override
    public void exe(Integer i, ConcurrentHashMap<Integer, AtomicInteger> map) {
        //synchronized (map) {
        int key = i % 4;
        if (!map.containsKey(key)) {
            synchronized (map) {
                if (!map.containsKey(key)) {
                    AtomicInteger at = new AtomicInteger();
                    at.addAndGet(i);
                    map.put(key, at);
                } else {
                    map.get(key).addAndGet(i);
                }
            }
            //            System.out.println("thread=" + Thread.currentThread().getName() + ",put  i%4=" + (i % 4) + ",value="
            //                    + map.get(i % 4).get() + " add " + i + ",map=" + map.toString());
        } else {
            map.get(key).addAndGet(i);
            //            System.out.println("thread=" + Thread.currentThread().getName() + ",update i%4=" + (i % 4) + ",value="
            //                    + map.get(i % 4).get() + " add " + i + ",map=" + map.toString());
        }
        // }
    }

}
