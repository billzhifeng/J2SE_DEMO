package com.jdk.thread.threadLocal;

/**
 * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
 * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
 */
public class ThreadLocalTest2 {

    public static void main(String[] a) {
        //模拟单例
        DoTask task = new DoTask();
        for (int i = 0; i < 4; i++) {
            Counter c = new Counter(i, task);
            c.start();
        }
    }
}

/**
 * 计数器
 * 
 * @author wangzhifeng
 * @date 2018年6月24日 上午11:28:43
 */
class Counter extends Thread {
    private int    count;
    private DoTask task;

    public Counter(Integer count, DoTask task) {
        this.count = count;
        this.task = task;
    }

    @Override
    public void run() {
        int random = (int) (100 * Math.random());
        count = random;
        System.out.println(Thread.currentThread().getName() + " count=random is " + random);

        ThreadContext.local.set(count);

        task.changeMainCount((int) (1000 * Math.random()));
        task.print("before");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.print("after");
        task.printThreadCount();

    }
}

class DoTask {
    //单例的成员变量,多线程访问会线程不安全
    Integer mainCount = 0;

    public void printThreadCount() {
        //局部变量线程安全，无论单例还是多例
        int a = 100;
        int currentTheadCount = ThreadContext.local.get();
        System.out.println(
                "DoTask-ThreadLocal " + Thread.currentThread().getName() + " count is" + currentTheadCount + ",a=" + a);

        //一个线程改变了a，但是对其他线程没影响
        a = 20;

    }

    public void changeMainCount(int i) {
        mainCount = i;
    }

    public void print(String env) {
        System.out.println(env + " DoTask-" + Thread.currentThread().getName() + " mainCount=" + mainCount);
    }
}

/**
 * 上线文
 * 
 * @author wangzhifeng
 * @date 2018年6月24日 上午11:27:04
 */
class ThreadContext {
    static final ThreadLocal<Integer> local = new ThreadLocal<Integer>();

}
