package com.jdk.concurrent.atomic;

/**
 * 查看AtomicInteger 内部实现
 */
public class MyAtomicInteger {
    /**
     * 悲观锁是一种独占锁，它假设的前提是“冲突一定会发生”，所以处理某段可能出现数据冲突的代码时，这个代码段就要被某个线程独占。<br>
     * 而独占意味着“其它即将执行这段代码的其他线程”都将进入“阻塞”/“挂起”状态。是的，synchronized关键字就是java对于悲观锁的实现。
     * 由于悲观锁的影响下，其他线程都将进入 阻塞/挂起 状态。<br>
     * 而我们在之前的文章中都已经讲过，CPU执行线程状态切换是要耗费相当资源的，这主要涉及到CPU寄存器的操作。<br>
     * 所以悲观锁在性能上不会有太多惊艳的表现（但是也不至于成为性能瓶颈）
     * 有悲观锁的存在当然就有乐观锁的存在。乐观锁假定“冲突不一定会出现”，如果出现冲突则进行重试，直到冲突消失。
     * 由于乐观锁的假定条件，所以乐观锁不会独占资源，自然性能就会好于悲观锁。AtomicInteger是一个标准的乐观锁实现，sun.misc.Unsafe是JDK提供的乐观锁的支持。
     */

    //  class AtomicInteger extends Number implements java.io.Serializable {

    //  private static final Unsafe unsafe = Unsafe.getUnsafe();

    //  private volatile int        value;

    //    public final int get() {
    //        return value;
    //    }

    //    public final int incrementAndGet() {
    //      一直循环的目的是为了“预期值”与“真实值”不一致的情况下，
    //      能够重新进行+1计算
    //      for (;;) {
    //          取得/重新取得 当前的value值
    //          int current = get();
    //          将当前值+1
    //          int next = current + 1;
    //          这是最关键的，使用JDK中实现的CAS机制
    //          对当前值和预期值进行比较
    //          如果当前值和预期的不一样，说明有某一个其他线程完成了值的更改
    //          那么进行下一次循环，进行重新操作（因为之前的操作结果就不对了）
    //          if (compareAndSet(current, next))
    //              return next;
    //          }
    //    }这就是整个利用乐观锁进行原子操作的过程。当然您在理解了这个过程后，就可以将乐观锁的支持直接运用到您的业务代码中，帮助改善性能了。

    /**
     * Atomically sets the value to the given updated value if the current value
     * {@code ==} the expected value.
     * 
     * @param expect the expected value
     * @param update the new value
     * @return true if successful. False return indicates that the actual value
     *         was not equal to the expected value.
     */
    //    public final boolean compareAndSet(int expect, int update) {
    //        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);

}
