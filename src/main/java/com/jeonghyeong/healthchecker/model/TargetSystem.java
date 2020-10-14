package com.jeonghyeong.healthchecker.model;


import lombok.Data;

/*
 * 모니터링 시스템 대상 객체
 *
 * @since 2020. 07. 09.
 * @version 0.2
 * @author JeongHyeongKim
 * @class MonitoringSystem.java
 */


@Data
public class TargetSystem {


	// 모니터링 시스템 객체
	private Long monSysSeq;

	// 모니터링 시스템 유형
	private String monFlagCd;

	// 모니터링 시스템 ip address
	private String monSysIp;

	// 모니터링 시스템 포트
	private String monSysPort;

	// 모니터링 시스템 별칭
	private String monSysNm;

	// 모니터링 시스템 url
	private String monSysUrl;

	// 모니터링 할 네이티브 쿼리
	private String monSysQury;

	// 모니터링 시스템 상태
	private String monSysStsCd;

	// 마지막 알림톡 발송일자
	private String msgSendLastDt;

	// Http Request Method
	private String reqMtd;


	private String regDt;


	private String regID;


	private String regIp;


	private String modDt;


	private String modId;


	private String modIp;

}
