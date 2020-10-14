package com.jeonghyeong.healthchecker.model;

import lombok.Data;


/*
 * 알림톡 수신대상 객체
 *
 * @since 2020. 07. 09.
 * @version 0.2
 * @author JeongHyeongKim
 * @class MonitoringSystemManager.java
 */

@Data
public class ManagerInfo {

	// 매니저 시퀀스 넘버
	private Long monMgrSeq;

	// 이름 마스킹
	private String mgrNmMask;

	// 암호화된 전화번호
	private String mgrHpTelNoEnc;


}
