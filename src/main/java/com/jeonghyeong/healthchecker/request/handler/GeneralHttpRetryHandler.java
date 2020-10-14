package com.jeonghyeong.healthchecker.request.handler;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.client.HttpRequestRetryHandler;
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
 * @return
 * 			재시도 여부
 * 			- 재시도를 해야하는 상황이면, true
 * 			- 재시도가 필요없으면, false
 */



public class GeneralHttpRetryHandler implements HttpRequestRetryHandler{

	private static final Logger logger = LoggerFactory.getLogger(GeneralHttpRetryHandler.class);


	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {

			logger.info("",exception );
            logger.info("재시도 횟수 : " + executionCount);


            //5번이상 재시도 하지 말것
            if (executionCount > 5) {
            	logger.error("재시도 5회 초과로 에러처리 합니다.");
                return false;
            }



            //호스트를 찾을 수 없으면 재시도 하지 말것
            if (exception instanceof UnknownHostException) {
            	logger.error("알 수 없는 호스트입니다.");
                return false;
            }

            //SSL인증서에 문제가 있으면 재시도 하지 말것
            if (exception instanceof SSLException) {
            	logger.error("SSL 인증서에 문제가 있습니다.");
                return false;
            }


//            idempotent한 request에 대해서 재시도 할 것
//            HttpClientContext clientContext = HttpClientContext.adapt(context);
//            HttpRequest request = clientContext.getRequest();
//            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
//            if (idempotent) {
//                // Retry if the request is considered idempotent
//                return true;
//            }


            return true;
	}

}
