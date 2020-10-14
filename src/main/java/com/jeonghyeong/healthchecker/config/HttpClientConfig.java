package com.jeonghyeong.healthchecker.config;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jeonghyeong.healthchecker.request.handler.GeneralHttpRetryHandler;
import com.jeonghyeong.healthchecker.request.handler.Http5xxErrorRetryHandler;



/*
 * HttpClient를 재사용할 수 있도록 Bean으로 등록하는 Configuration Class
 *
 * @since 2020. 09. 15.
 * @version 0.2
 * @author JeongHyeongKim
 * @class HttpClientConfig.java
 * @param
 *
 * @return
 * 			아래 코드 주석 참조
 */



@Configuration
public class HttpClientConfig {

	private static final int CONNECTION_TIMEOUT = 2500;


	@Bean
	public CloseableHttpClient defalutHttpClient() {



		/*
		 * ConnectionTimeout        : 2.5초
		 * SocketTimeout            : 2.5초
		 * ConnectionRequestTimeout : 2.55초
		 * 쿠키 정책                                 : 쿠키 무시 (AWS ALB에서 오는 cookie때문에 지속적으로 warning 로그 방지)
		 */
		RequestConfig requestConfig = RequestConfig.custom()
				   .setConnectTimeout(CONNECTION_TIMEOUT)
				   .setSocketTimeout(CONNECTION_TIMEOUT)
				   .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
				   .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
				   .build();


		// HttpClient 설정
		CloseableHttpClient httpClient = HttpClients.custom()
		                 .setDefaultRequestConfig(requestConfig)
		                 .setRetryHandler(new GeneralHttpRetryHandler())
		                 .setServiceUnavailableRetryStrategy(new Http5xxErrorRetryHandler())
		                 .build();


		return httpClient;
	}


}
