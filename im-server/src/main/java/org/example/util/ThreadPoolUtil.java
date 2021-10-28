package org.example.util;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPoolUtil {

    private void ThreadPoolUtil() {

    }

    static volatile int theadCount = 0;

    /**
     * 通用的线程池。
     */
    public static ThreadPoolExecutor commonPool = new ThreadPoolExecutor(5,
            500, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5000)
            , r -> new Thread(r, "common thread " + theadCount++)
    );

    /**
     * 定时任务线程池， 注意定时任务要设置合理的定时时间， 要根据任务的耗时来合理设置。 建议定时任务还是使用spring的定时任务功能
     */
    public static ScheduledThreadPoolExecutor schedulePool = new ScheduledThreadPoolExecutor(1,
            r -> new Thread(r, "schedulePool thread " + theadCount++)
    );

    /**
     * 短时间批量任务的执行
     *
     * @param poolSize
     * @param runnables
     */
    public static void createPool(int poolSize, List<Runnable> runnables) {
        if (poolSize < 1) {
            throw new RuntimeException("请设置合理的线程池数量");
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(poolSize,
                poolSize, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(runnables.size())
                , r -> new Thread(r, "create new Pool thread " + theadCount++));

        runnables.forEach(runnable -> {
            threadPoolExecutor.execute(runnable);
        });
        //这里只是通知线程池不在接收新任务了，并且所有任务结束后，线程池要关闭
        threadPoolExecutor.shutdown();
    }

}

