package com.jeonghyeong.healthchecker.service;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeonghyeong.healthchecker.config.AsyncThreadConfig;
import com.jeonghyeong.healthchecker.model.TargetSystem;
import com.jeonghyeong.healthchecker.repository.TargetSystemRepository;

/*
 *
 *
 * @since 2020. 09. 15.
 * @version 0.2
 * @author JeongHyeongKim
 * @class MonitoringSystemService.java
 * @param
 * @return
 */



@Service
public class HealthCheckSchedulerService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@Autowired
	private TargetSystemRepository targetSystemRepository;

	@Autowired
    private ThreadExecutorService threadExecutorService;

	@Autowired
    private AsyncThreadConfig asyncConfig;


	/*
	 * 스케줄러 method
	 *
	 * @parameter
	 * @return
	 * 			Type : void
	 * @cycle period : 모니터링이 끝난 후, 15초
	 */

	@Scheduled(fixedDelay = 15000)
	@Transactional
	public void scheduledMonitoringService() throws Exception {


		try {
			List<TargetSystem> systemInfoList = targetSystemRepository.getAllMonitoringSystemList();

			//시스템별 상태 체크 및 톡 발송 실행
			for( TargetSystem systemInfo : systemInfoList) {

				 try {
			            // 등록 가능 여부 체크
			            if (asyncConfig.isThreadPoolAvailable()) {
			            	threadExecutorService.threadExecutor(systemInfo);
			            } else {
			            	logger.info("Thread 한도 초과");
			            }
			        } catch (TaskRejectedException e) {
			        	logger.info(e.getLocalizedMessage());
			        }

			}

		}catch (Exception e) {
			logger.error(e.getMessage());
		}


	}

}
