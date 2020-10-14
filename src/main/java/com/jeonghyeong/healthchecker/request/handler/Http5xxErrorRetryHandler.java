package com.jeonghyeong.healthchecker.request.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






/*
 * HttpRequest의 5XX 이외의 오류에 대해 재시도를 제어하는 클래스
 *
 * @since 2020. 09. 15.
 * @version 0.2
 * @author JeongHyeongKim
 * @class HttpRetryHandler.java
 * @param
 * 			url(String)   : http요청을 할 url String
 * 			method(String) : http method
 * @return
 * 			재시도 여부
 * 			- 재시도를 해야하는 상황이면, true
 * 			- 재시도가 필요없으면, false
 */


public class Http5xxErrorRetryHandler implements ServiceUnavailableRetryStrategy {

	private final Set<Integer> retryableErrorCodes = new HashSet<>(Arrays.asList(500, 503, 502));

	private static final Logger logger = LoggerFactory.getLogger(Http5xxErrorRetryHandler.class);



	@Override
	public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {

		int statusCode = response.getStatusLine().getStatusCode();




        if (executionCount > 5) {
            logger.error("재시도 5회 초과로 에러처리 합니다.");
            return false;
        }

        if (retryableErrorCodes.contains(statusCode)) {
            logger.warn(statusCode + "오류 발생");
            logger.info("재시도 횟수 : " + executionCount);
            return true;
        }

        return false;
	}

	@Override
	public long getRetryInterval() {
		return 5000;
	}

}
