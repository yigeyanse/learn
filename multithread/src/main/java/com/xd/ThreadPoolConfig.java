package com.xd;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ThreadPoolConfig {
    ThreadPoolProperties properties = new ThreadPoolProperties();

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        // 设置线程保持活跃的时间（默认：60）
        executor.setKeepAliveSeconds(properties.getKeepAliveTime());
        // 当任务完成后，长时间无待处理任务时，销毁线程池
        executor.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
        // 设置任务拒绝策略
        /**
         * 4种
         * ThreadPoolExecutor类有几个内部实现类来处理这类情况：
         - AbortPolicy 丢弃任务，抛RejectedExecutionException
         - CallerRunsPolicy 由该线程调用线程运行。直接调用Runnable的run方法运行。
         - DiscardPolicy  抛弃策略，直接丢弃这个新提交的任务
         - DiscardOldestPolicy 抛弃旧任务策略，从队列中踢出最先进入队列（最后一个执行）的任务
         * 实现RejectedExecutionHandler接口，可自定义处理器
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


    @Data
    class ThreadPoolProperties{
        /**
         * 核心线程数（默认是1）：若是IO密集型，cpu核心数*2，若是cpu密集型，cpu核心数
         * 核心线程会一直存活，及时没有任务需要执行
         * 设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭
         * 注意：当线程数小于核心线程数时，即使有线程空闲，线程池也会优先创建新线程处理
         */
        private int corePoolSize = 8;
        /**
         * 最大线程数，系统默认Integer.MAX_VALUE
         */
        private int maxPoolSize = 20;
        /**
         * 允许线程空闲时间（单位：默认为秒，默认60S）
         * 当线程空闲时间达到keepAliveTime时，线程会退出，直到线程数量=corePoolSize
         * 如果allowCoreThreadTimeout=true，则会直到线程数量=0
         */
        private int keepAliveTime;
        /**
         * 缓冲队列大小，系统默认Integer.MAX_VALUE
         * 注意：这个值肯定要改小，不然任务陡增时，都堆在队列中（队列值大），
         * 核心线程数就那几个，无法很快执行队列中的任务，
         * 就会延长响应时间，阻塞任务
         */
        private int queueCapacity = 6;
        /**
         * 线程池名前缀，用于监控识别
         */
        private String threadNamePrefix = "test";

        /**
         * 允许核心线程超时（默认false）
         */
        private boolean allowCoreThreadTimeout = false;

        /**
         * 当任务完成后，长时间无待处理任务时，销毁线程池
         */
        private boolean waitForTasksToCompleteOnShutdown = false;

        private int awaitTerminationSeconds;
    }
}
