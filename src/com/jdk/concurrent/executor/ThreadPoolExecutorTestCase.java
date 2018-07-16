package com.jdk.concurrent.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Described：线程池测试
 * @author YHJ create at 2012-4-13 下午01:34:03
 * @FileNmae com.yhj.container.concurrent.ThreadPoolExecutorTestCase.java
 */
public class ThreadPoolExecutorTestCase {

    private AtomicInteger      successTask = new AtomicInteger(0);//成功的任务数目

    private AtomicInteger      failedTask  = new AtomicInteger(0);//失败的任务数目

    private Integer            thredCount;                        //启动的线程数

    private ThreadPoolExecutor executor;

    private CountDownLatch     latch;                             //计数器

    private CyclicBarrier      cyclicBarrier;                     //集合点

    //构造函数
    public ThreadPoolExecutorTestCase(BlockingQueue<Runnable> queue, Integer thredCount) {
        super();
        System.out.println("queue name:" + queue.getClass());
        this.thredCount = thredCount;
        executor = new ThreadPoolExecutor(10, 500, 30, TimeUnit.SECONDS, queue, Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    //要处理的任务列表
    class Task implements Runnable {

        private CountDownLatch latch;        //计数器

        private CyclicBarrier  cyclicBarrier;//集合点

        public Task(CountDownLatch latch, CyclicBarrier cyclicBarrier) {
            super();
            this.latch = latch;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();//到达预期集合点再执行
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                executor.execute(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);//休眠3秒
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        latch.countDown();
                        successTask.incrementAndGet();
                    }
                });
            } catch (RejectedExecutionException e) {
                latch.countDown();
                failedTask.incrementAndGet();
            }

        }

    }

    //初始化
    public void init() {
        latch = new CountDownLatch(thredCount);
        cyclicBarrier = new CyclicBarrier(thredCount);
    }

    //启动方法
    public void start() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < thredCount; ++i)
            new Thread(new Task(latch, cyclicBarrier)).start();
        try {
            latch.await();
            executor.shutdownNow();
            System.out.println("total time:" + (System.currentTimeMillis() - startTime));
            System.out.println("success count:" + successTask.intValue());
            System.out.println("failed count:" + failedTask.intValue());
            System.out.println("===end===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //强制关闭方法
    public void shutDonw() {
        executor.shutdownNow();
    }

    //主函数
    public static void main(String[] args) {
        //性能优先 速度优先
        ThreadPoolExecutorTestCase testCase = new ThreadPoolExecutorTestCase(new SynchronousQueue<Runnable>(), 1000);
        testCase.init();
        testCase.start();
        //稳定优先  使用数组缓存队列
        testCase = new ThreadPoolExecutorTestCase(new ArrayBlockingQueue<Runnable>(10), 1000);
        testCase.init();
        testCase.start();
        //稳定优先  使用链表缓存队列
        testCase = new ThreadPoolExecutorTestCase(new LinkedBlockingDeque<Runnable>(10), 1000);
        testCase.init();
        testCase.start();
        //关掉处理器
        //testCase.shutDonw();
    }

}
