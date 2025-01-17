package cn.zjiali.robot.main;

import cn.zjiali.robot.util.Threads;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import javax.annotation.PreDestroy;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 *
 * @author zJiaLi
 * @since 2021-04-09 09:44
 */
public class AsyncManager {

    private static final ScheduledExecutorService scheduledExecutorService;

    static {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat("schedule-pool-%d").build()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }

    /**
     * 操作延迟10毫秒
     */
    private static final int OPERATE_DELAY_TIME = 10;

    /**
     * 单例模式
     */
    private AsyncManager() {
    }

    private static final AsyncManager me = new AsyncManager();

    public static AsyncManager me() {
        return me;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task) {
        scheduledExecutorService.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    @PreDestroy
    public void shutdown() {
        Threads.shutdownAndAwaitTermination(scheduledExecutorService);
    }
}
