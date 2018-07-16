package com.jdk.thread.factorydemo;

public interface ThreadFactory {

    Thread newThread(Runnable r);
}
