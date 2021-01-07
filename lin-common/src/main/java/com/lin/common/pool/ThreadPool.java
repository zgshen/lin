package com.lin.common.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {

    /**
     * jdk 无外部依赖
     */
    public static ExecutorService create(int corePoolSize, int maximumPoolSize, long keepAlveTime) {
        ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAlveTime,
                TimeUnit.SECONDS, new ArrayBlockingQueue(1024));
        return executor;
    }

    /**
     * 自定义线程工厂和拒绝策略
     */
    public static ExecutorService customCreate(int corePoolSize, int maximumPoolSize, long keepAlveTime, String poolPreName) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAlveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(1024),
                new NameTreadFactory(poolPreName),
                new MyIgnorePolicy());
        return executor;
    }

    /**
     * 使用guava提供的ThreadFactoryBuilder来创建线程池
     */
    public static ExecutorService guavaCreate(int corePoolSize, int maximumPoolSize, long keepAlveTime, String poolPreName) {
        ExecutorService pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAlveTime,
                TimeUnit.MILLISECONDS,
                //队列长度1024
                new LinkedBlockingQueue<Runnable>(1024),
                new ThreadFactoryBuilder().setNameFormat(poolPreName.concat("-pool-%d")).build(),
                //拒绝策略，抛出异常
                new ThreadPoolExecutor.AbortPolicy());
        return pool;
    }

}

class NameTreadFactory implements ThreadFactory {
    private String poolPreName;
    public NameTreadFactory(String poolPreName) {
        this.poolPreName = poolPreName;
    }

    private final AtomicInteger mThreadNum = new AtomicInteger(1);
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, poolPreName.concat("-thread-").concat(mThreadNum.getAndIncrement()+""));
        System.out.println(t.getName() + " has been created");
        return t;
    }
}

class MyIgnorePolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        doLog(r, e);
    }
    private void doLog(Runnable r, ThreadPoolExecutor e) {
        // 日志记录
        System.err.println( r.toString() + " rejected");
    }
}