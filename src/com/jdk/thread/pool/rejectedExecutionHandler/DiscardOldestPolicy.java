package com.jdk.thread.pool.rejectedExecutionHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 丢弃老的任务
 * 
 * @author wangzhifeng
 * @date 2018年6月23日 上午12:14:41
 */
public class DiscardOldestPolicy {

    public static void main(String[] args) {
        int corePoolSize = 5;
        int maxPoolSize = 10;
        long keepAliveTime = 5;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10);

        //拒绝策略3：丢弃老任务
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                queue, handler);
        for (int i = 0; i < 100; i++) {
            executor.execute(new Worker());
        }
        executor.shutdown();
    }
}

//这样运行结果就不会有100个线程全部被执行。处理源码如下：
//public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//    if (!e.isShutdown()) {
//        e.getQueue().poll();
//        e.execute(r);
//    }
//}
