package cn.bytecloud.smartCommunity.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class ThreadPool {
    //线程池工厂
    public static ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("demo-pool-id").build();
    //线程池
    public static ExecutorService threadPool = new ThreadPoolExecutor(20, 20, 1000L * 3600 * 24 * 365, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(1024), factory, new ThreadPoolExecutor.AbortPolicy());

}
