package com.jeonghyeong.healthchecker.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jeonghyeong.healthchecker.model.ManagerInfo;
import com.jeonghyeong.healthchecker.model.TargetSystem;
import com.jeonghyeong.healthchecker.repository.TargetSystemRepository;
import com.jeonghyeong.healthchecker.request.HealthChecker;


@Service("asyncHealthChecker")
public class ThreadExecutorService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String PROCESS_SUCCESS = "S";

	private static final String PROCESS_FAIL = "F";





	@Autowired
	private TargetSystemRepository targetSystemRepository;


	@Async("healthCheckExecutor")
	public void threadExecutor(TargetSystem systemInfo) {
		//시스템별 상태 체크 및 톡 발송 실행
		this.setPid();

		try {
			switch(systemInfo.getMonFlagCd()) {

				//RealTime 호출 (엔드포인트 호출형)
				case "R":
						logger.info("모니터링 유형 : 엔드포인트 호출");
						logger.info("타겟 엔드포인트 : " + systemInfo.getMonSysUrl());
						logger.info("타겟 엔드포인트 포트 : " + systemInfo.getMonSysPort());
						logger.info("HttpRequest Method : " + systemInfo.getReqMtd());
						boolean httpRequestResult = HealthChecker.run(systemInfo.getMonSysUrl(), systemInfo.getReqMtd());


						systemInfo = this.sendMessage(systemInfo, httpRequestResult);
						this.modifySystemInfo(systemInfo, httpRequestResult);

					break;

				//Static 호출 (이메일 시스템 및 직접적인 쿼리 실행)
				case "S":
					logger.info("모니터링 유형 : 이메일 시스템 및 직접적인 쿼리");
					logger.info("실행 쿼리 : " + systemInfo.getMonSysQury());

					boolean scheduledSystemStatus = targetSystemRepository.getScheduledSystemStatus(systemInfo.getMonSysQury());
					logger.info("쿼리 정상 여부 : " + String.valueOf(scheduledSystemStatus));

					systemInfo = this.sendMessage(systemInfo, scheduledSystemStatus);
					this.modifySystemInfo(systemInfo, scheduledSystemStatus);
					break;
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}

			logger.info("");

	}




	/*
	 * 시스템 모니터링 결과 처리 메소드
	 *
	 * @parameter
	 * 			Type : (MonitringSystem, boolean) (모니터링 대상 객체, 모니터링 성공여부)
	 * 															  true -> 모니터링 성공
	 * 															  false -> 모니터링 실패
	 *
	 * @return
	 * 			Type : 상태가 업데이트 된 MonitoringSystem Object
	 */
	private TargetSystem modifySystemInfo(TargetSystem info, boolean isSuccess) {
		if(isSuccess) {
			logger.info(info.getMonSysNm() + "상태 : 정상");
			info.setMonSysStsCd(PROCESS_SUCCESS);
			targetSystemRepository.updateMonitoringSystem(info);
		}else {
			logger.info(info.getMonSysNm() + "상태 : 비정상");
			info.setMonSysStsCd(PROCESS_FAIL);
			targetSystemRepository.updateMonitoringSystem(info);
		}
		return info;
	}





	/*
	 * 메시지 전송 메소드
	 *
	 * @parameter
	 * 			Type : (MonitringSystem, boolean) (모니터링 대상 객체, 모니터링 성공여부)
	 * 															  true -> 모니터링 성공
	 * 														      false -> 모니터링 실패
	 *
	 * @return
	 * 			Type : 상태가 업데이트 된 MonitoringSystem Object
	 */

	private TargetSystem sendMessage(TargetSystem info, boolean isSuccess) {


		if(isSuccess==true && info.getMonSysStsCd().equals(PROCESS_FAIL)) {
			List<ManagerInfo> managerList = targetSystemRepository.getMonitoringManagerList();


			logger.info("시스템이 비정상 상태에서 정상으로 전환되었습니다.");
			logger.info("시스템 정상화 대상 : " + info.getMonSysNm());







		}else if(isSuccess==false && info.getMonSysStsCd().equals(PROCESS_SUCCESS)) {
			List<ManagerInfo> managerList = targetSystemRepository.getMonitoringManagerList();

			logger.info("시스템이 정상 상태에서 비정상으로 전환되었습니다.");
			logger.info("시스템 장애 알림톡 발송");
			logger.info("에러가 난 시스템 : " + info.getMonSysNm());


		}

		return info;

	}




	/*
	 * 현재시간 발급 메소드
	 *
	 * @parameter
	 * 			Type : No Parameter
	 *
	 * @return
	 * 			Type : String
	 * 			Content : "yyyy-MM-dd HH:mm:ss" 형태의 문자열
	 */
	private String getNowTime() {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date date = new Date(timestamp.getTime());
		SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


		return dateFormat.format(date);

	}



	/*
	 * 스케줄러 프로세스 식별자 MDC 적용 메소드
	 *
	 * @parameter
	 * 			Type : No Parameter
	 *
	 * @return
	 * 			Type : void
	 */


	private void setPid() {

		//랜덤한 10자리 식별자 발급
		int randomSchedulerId = (int) (Math.random()*1000000000);

		//MDC 적용
		MDC.put("PRCS_ID", randomSchedulerId);

	}



}
