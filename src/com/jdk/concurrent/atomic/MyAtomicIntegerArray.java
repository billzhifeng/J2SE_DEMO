package com.jdk.concurrent.atomic;

public class MyAtomicIntegerArray {

    //class AtomicIntegerArray implements java.io.Serializable {
    //        
    //        private static final Unsafe unsafe = Unsafe.getUnsafe();
    //        private static final int base = unsafe.arrayBaseOffset(int[].class);
    //
    //        private final int[] array;
    //
    //        /**
    //         * Creates a new AtomicIntegerArray of the given length, with all
    //         * elements initially zero.
    //         *
    //         * @param length the length of the array
    //         */
    //        public AtomicIntegerArray(int length) {
    //            array = new int[length];
    //        }
    //
    //        /**
    //         * Atomically decrements by one the element at index {@code i}.
    //         *
    //         * @param i the index
    //         * @return the updated value
    //         */
    //        public final int decrementAndGet(int i) {
    //            return addAndGet(i, -1);
    //        }

    //        public final int addAndGet(int i, int delta) {
    //            long offset = checkedByteOffset(i);
    //            while (true) {
    //                int current = getRaw(offset);
    //                int next = current + delta;
    //                if (compareAndSetRaw(offset, current, next))
    //                    return next;
    //            }
    //        }
    //}
    /**
     * get(int i)：获取数组指定位置的值，并不会改变原来的值。<br>
     * set(int i, int newValue)：为数组指定索引位设置一个新值。数组的索引位都是从0开始计数。 <br>
     * getAndSet(int i, int newValue)：获取数组指定位置的原始值后，用newValue这个新值进行覆盖。 <br>
     * getAndAdd(int i, int delta)：获取数组指定索引位的原始值后，为数组指定索引位的值增加delta。<br>
     * 那么还有个类似的操作为： addAndGet。 incrementAndGet(int
     * i)：为数组指定索引位的值+1后，然后获取这个位置上的新值。<br>
     * 当然，还有个类似的操作：getAndIncrement。 decrementAndGet(int
     * i)：为数组指定索引位的值-1后，然后获取这个位置上的新值。当然，类似的操作为：getAndDecrement。
     */
}
