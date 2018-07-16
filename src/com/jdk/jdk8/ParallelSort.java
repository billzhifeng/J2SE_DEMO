package com.jdk.jdk8;

import java.util.Arrays;

public class ParallelSort {
    /**
     * 在Java 7中已经有了Arrays.sort()方法可对对象进行排序，而在Java
     * 8中，引入了新的并行排序，它比前者的排序速度更快，且遵循了Java 7引入的Fork/Join框架，可以把排序任务分配给线程池中可用的多个线程。
     * Java 8在java.util.Arrays类中新增了并行排序功能，能够更充分地利用多线程机制。
     * 
     * @param args
     */
    public static void main(String[] args) {
        int arr[] = { 1, 4, 2, 8, 5 };
        Arrays.parallelSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
    // output: 1 2 4 5 8 
}
