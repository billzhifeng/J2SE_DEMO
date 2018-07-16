package com.jdk.thread.pool.rejectedExecutionHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 直接拒绝的策略，一般不是用
 * 
 * @author wangzhifeng
 * @date 2018年6月23日 上午12:02:10
 */
public class AbortPolicy {
    public static void main(String[] args) {
        int corePoolSize = 5;
        int maxPoolSize = 10;
        long keepAliveTime = 5;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10);
        //拒绝策略1：将抛出 RejectedExecutionException.
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                queue, handler);
        for (int i = 0; i < 100; i++) {
            try {
                executor.execute(new Worker());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }
}

//执行结果如下
//pool-1-thread-1 is running
//pool-1-thread-3 is running
//pool-1-thread-4 is running
//Exception in thread "main" pool-1-thread-5 is running
//pool-1-thread-6 is running
//java.util.concurrent.RejectedExecutionException: Task com.jdk.thread.pool.rejectedExecutionHandler.Worker@232204a1 rejected from java.util.concurrent.ThreadPoolExecutor@4aa298b7[Running, pool size = 10, active threads = 8, queued tasks = 0, completed tasks = 21]
//    at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2047)
//    at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:823)
//    at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1369)
//    at com.jdk.thread.pool.rejectedExecutionHandler.AbortPolicy.main(AbortPolicy.java:24)
//pool-1-thread-7 is running
//pool-1-thread-8 is running
//pool-1-thread-9 is running
//pool-1-thread-10 is running

//查看源码
///**
// * A handler for rejected tasks that throws a
// * {@code RejectedExecutionException}.
// */
//public static class AbortPolicy implements RejectedExecutionHandler {
//    /**
//     * Creates an {@code AbortPolicy}.
//     */
//    public AbortPolicy() { }
//
//    /**
//     * Always throws RejectedExecutionException.
//     *
//     * @param r the runnable task requested to be executed
//     * @param e the executor attempting to execute this task
//     * @throws RejectedExecutionException always
//     */
//    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//        throw new RejectedExecutionException("Task " + r.toString() +
//                                             " rejected from " +
//                                             e.toString());
//    }
//}
