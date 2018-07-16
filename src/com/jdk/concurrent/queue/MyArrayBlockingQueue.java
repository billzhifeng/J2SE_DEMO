package com.jdk.concurrent.queue;

public class MyArrayBlockingQueue<E> {

    //    final Object[]          items;
    //
    //    int                     takeIndex;
    //
    //    int                     putIndex;
    //
    //    int                     count;
    //
    //    final ReentrantLock     lock;
    //
    //    private final Condition notEmpty;
    //
    //    private final Condition notFull;
    //
    //    public E take() throws InterruptedException {
    //        final ReentrantLock lock = this.lock;
    //        /*
    //         * 尝试获取锁，如果此时锁被其他线程锁占用，那么当前线程就处于Waiting的状态。
    //         * 注意:当方法是支持线程中断响应的如果其他线程此时中断当前线程， 那么当前线程就会抛出InterruptedException
    //         */
    //        lock.lockInterruptibly();
    //        try {
    //            /*
    //             * 如果此时队列中的元素个数为0,那么就让当前线程wait,并且释放锁。
    //             * 注意:这里使用了while进行重复检查，是为了防止当前线程可能由于 其他未知的原因被唤醒。
    //             * (通常这种情况被称为"spurious wakeup")
    //             */
    //            while (count == 0)
    //                notEmpty.await();
    //            //如果队列不为空，则从队列的头部取元素
    //            return extract();
    //        } finally {
    //            //完成锁的释放
    //            lock.unlock();
    //        }
    //    }
    //
    //    public void put(E e) throws InterruptedException {
    //        //首先检查元素是否为空，否则抛出NullPointerException
    //        checkNotNull(e);
    //        final ReentrantLock lock = this.lock;
    //        //进行锁的抢占
    //        lock.lockInterruptibly();
    //        try {
    //            /*
    //             * 当队列的长度等于数组的长度,此时说明队列已经满了,这里同样 使用了while来方式当前线程被"伪唤醒"。
    //             */
    //            while (count == items.length)
    //                //则让当前线程处于等待状态
    //                notFull.await();
    //            //一旦获取到锁并且队列还未满时，则执行insert操作。
    //            insert(e);
    //        } finally {
    //            //完成锁的释放
    //            lock.unlock();
    //        }
    //    }
    //
    //    //检查元素是否为空
    //    private static void checkNotNull(Object v) {
    //        if (v == null)
    //            throw new NullPointerException();
    //    }
    //
    //    //该方法的逻辑非常简单
    //    private void insert(E x) {
    //        //将当前元素设置到putIndex位置   
    //        items[putIndex] = x;
    //        //让putIndex++
    //        putIndex = inc(putIndex);
    //        //将队列的大小加1
    //        ++count;
    //        //唤醒其他正在处于等待状态的线程
    //        notEmpty.signal();
    //    }

}
