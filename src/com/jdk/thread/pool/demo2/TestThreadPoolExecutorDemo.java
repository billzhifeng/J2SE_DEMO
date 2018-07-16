package com.jdk.thread.pool.demo2;

/**
 * @author wangzhifeng
 * @date 2018年1月18日 上午11:26:18
 */
public class TestThreadPoolExecutorDemo {

    public static void main(String[] args) {
        DoBizService service = new DoBizService();
        TestThreadPoolExecutor exe = new TestThreadPoolExecutor(service);
        for (int i = 0; i < 10; i++) {
            exe.put(i);
        }

        try {
            //等到线程池中所有任务执行完成
            exe.waitUntilDone();
        } catch (Exception e) {
            //可能任务没执行完成，但是超过计划执行时间，强制停止
        }

        //等所有任务执行完成，可以做统计等等任务
    }
}
