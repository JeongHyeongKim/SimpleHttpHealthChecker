
package com.jeonghyeong.healthchecker.repository;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeonghyeong.healthchecker.model.ManagerInfo;
import com.jeonghyeong.healthchecker.model.TargetSystem;

	/*
	 *  Monitoring에 필요한 query mapper
	 *
	 * @since 2020. 07. 09.
	 * @version 0.1
	 * @author JeongHyeongKim
	 * @interface MonitoringSystemRepository.java
	 *
	 */

@Repository
public interface TargetSystemRepository{



	/*
	 * 모든 시스템의 상태를 가져오는 method
	 *
	 * @parameter
	 * @return
	 * 			Type : MonitoringSystem
	 * 			reference : com.jeonghyeong.healthchecker.model.MonitoringSystem.java
	 */
	List<TargetSystem> getAllMonitoringSystemList();






	/*
	 * 시스템 상태를 업데이트하는 method
	 *
	 * @parameter
	 * 			system(MonitoringSystem) : 모니터링 시스템 대상
	 * @return
	 * 			void
	 */
	void updateMonitoringSystem(TargetSystem system);




	/*
	 * 스케줄링 시스템의 상태를 체크하기 위한 네이티브 쿼리 사용 method
	 *
	 * @parameter
	 * 			query(String) : DB에 저장되어 있는 네이티브 쿼리
	 * @return
	 * 			실행결과(boolean) : 성공시 true, 실패서 false
	 */
	boolean getScheduledSystemStatus(String query);





	/*
	 * 알림톡 발송 명단을 가져오기 위한 method
	 *
	 * @parameter
	 * 			No Parameter
	 * @return
	 * 			알림톡 수신 명단(List<MonitoringSystemManager>) : 알림톡을 수신 받을 명단
	 */
	List<ManagerInfo> getMonitoringManagerList();





	/*
	 * HealthCheck Table 전체 row 개수를 가져오는 method
	 *
	 * @parameter
	 * 			No Parameter
	 * @return
	 * 			int : table row count
	 */
	int getMonitoringSystemCount();



}
