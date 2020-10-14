package com.jeonghyeong.healthchecker.config;

import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;



@Configuration
@EnableAsync
public class AsyncThreadConfig implements AsyncConfigurer{



	// 기본 thread 개수
	private static int THREAD_CORE_POOL_SIZE = 5;

	private int THREAD_MAX_POOL_SIZE=10;

    // Thread Queue 사이즈
    private static int THREAD_QUEUE_CAPACITY = 10;

    private static String THREAD_NAME = "healthCheckExecutor";

    @Resource(name = "healthCheckExecutor")
    private ThreadPoolTaskExecutor healthCheckExecutor;









	@Override
	@Bean(name = "healthCheckExecutor")
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(THREAD_CORE_POOL_SIZE);
        executor.setMaxPoolSize(THREAD_MAX_POOL_SIZE);
        executor.setQueueCapacity(THREAD_QUEUE_CAPACITY);
        executor.setBeanName(THREAD_NAME);
        executor.initialize();
        return executor;
	}


	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		// TODO Auto-generated method stub
		return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
	}






	public boolean isThreadPoolAvailable(int createCnt) {

		boolean threadStatus = true;

        if ((healthCheckExecutor.getActiveCount() + createCnt) > (THREAD_MAX_POOL_SIZE + THREAD_QUEUE_CAPACITY)) {
        	threadStatus = false;
        }

        return threadStatus;
    }


	public boolean isThreadPoolAvailable() {

		boolean threadStatus = true;

        if ((healthCheckExecutor.getActiveCount()) > (THREAD_MAX_POOL_SIZE + THREAD_QUEUE_CAPACITY)) {
        	threadStatus = false;
        }

        return threadStatus;
    }

	public int getActiveCount() {
		return healthCheckExecutor.getActiveCount();
	}










}
