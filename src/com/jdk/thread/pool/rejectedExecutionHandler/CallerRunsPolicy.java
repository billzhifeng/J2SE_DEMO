package com.jdk.thread.pool.rejectedExecutionHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于被拒绝任务的处理程序，它直接在 execute 方法的调用线程中(就是主线程)运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
 * 
 * @author wangzhifeng
 * @date 2018年6月23日 上午12:09:55
 */
public class CallerRunsPolicy {

    public static void main(String[] args) {
        int corePoolSize = 5;
        int maxPoolSize = 10;
        long keepAliveTime = 5;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10);

        //拒绝策略2：在 execute 方法的调用线程中(就是主线程)运行被拒绝的任务
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                queue, handler);
        for (int i = 0; i < 100; i++) {
            executor.execute(new Worker());
        }
        executor.shutdown();
    }
}
//运行结果
//pool-1-thread-1 is running
//pool-1-thread-2 is running
//main is running
//main is running

//处理源码如下：
//public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//    if (!e.isShutdown()) {
//        r.run();
//    }
//}
