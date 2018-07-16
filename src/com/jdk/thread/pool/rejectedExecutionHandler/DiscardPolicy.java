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
public class DiscardPolicy {

    public static void main(String[] args) {
        int corePoolSize = 5;
        int maxPoolSize = 10;
        long keepAliveTime = 5;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10);

        //拒绝策略4：DiscardPolicy 对拒绝任务直接无声抛弃，没有异常信息。
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                queue, handler);
        for (int i = 0; i < 100; i++) {
            executor.execute(new Worker());
        }
        executor.shutdown();
    }
}

//这样运行结果就不会有100个线程全部被执行。处理源码如下：
//public static class DiscardPolicy implements RejectedExecutionHandler {
//    /**
//     * Creates a {@code DiscardPolicy}.
//     */
//    public DiscardPolicy() { }
//
//    /**
//     * Does nothing, which has the effect of discarding task r.
//     *
//     * @param r the runnable task requested to be executed
//     * @param e the executor attempting to execute this task
//     */
//    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//    }
//}
