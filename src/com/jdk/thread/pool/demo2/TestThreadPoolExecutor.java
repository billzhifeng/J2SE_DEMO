package com.jdk.thread.pool.demo2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangzhifeng
 * @date 2018年1月18日 上午11:15:32
 */
public class TestThreadPoolExecutor {

    public DoBizService       doBizService;
    public ThreadPoolExecutor threadPoolExecutor;

    private final int         corePoolSize  = 5;
    private final int         maxPoolSize   = 5;
    private final int         keepAliveTime = 1000;

    public TestThreadPoolExecutor(DoBizService oBizService) {
        this.doBizService = oBizService;
        initExecutor();
    }

    public void put(Object obj) {
        threadPoolExecutor.execute(() -> doBizService.doSomeThing(obj.toString()));
    }

    public void waitUntilDone() {
        try {
            threadPoolExecutor.shutdown();
            if (!threadPoolExecutor.awaitTermination(2, TimeUnit.HOURS)) {
                throw new RuntimeException("THREAD_POOL_SHUTDOWN_FAIL");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("THREAD_POOL_SHUTDOWN_FAIL", e);
        }
    }

    /**
     * 初始化线程池
     */
    private void initExecutor() {
        /**
         * 使用SynchronousQueue, CallerRunsPolicy的原因： 对账使用线程池的主要原因是利用多线程来提升对账处理速度，
         * 所以内存队列为SynchronousQueue，相当于queue设置为0，CallerRunsPolicy含义为当线程池繁忙时让主线程执行
         */
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1),

                new ThreadFactory() {
                    private AtomicInteger count = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "checkThread-" + count.incrementAndGet());
                    }
                });
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
