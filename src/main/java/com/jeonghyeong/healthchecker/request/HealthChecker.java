package com.jeonghyeong.healthchecker.request;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


	/*
	 * HttpRequest를 처리하는 클래스
	 *
	 * @since 2020. 09. 15.
	 * @version 0.2
	 * @author JeongHyeongKim
	 * @class HealthChecker.java
     * @param
     * 			url(String)   : http요청을 할 url String
     * 			method(String) : http method
     * @return
     * 			httpRequest 성공여부(boolean)
     * 			- response code가 2XX이며, 모니터링 타겟 시스템에서 "S [조회성공시간]"를 수신 하였을 때 true
     *          - 성공 format 예시 : "S 20200915093336325"
     * 			- 그 이외의 상황이거나 exception 발생시 false
     */


@Component
public class HealthChecker {

	private static final Logger logger = LoggerFactory.getLogger(HealthChecker.class);

	private static CloseableHttpClient httpClient;

	@Autowired
	public void setHttpClient(CloseableHttpClient httpClient) {
		HealthChecker.httpClient = httpClient;
	}




	public static boolean run(String url, String method) throws Exception {

		 try {

			HttpRequestBase request = HttpMethodInitializer.getRequestMethod(url, method);




			// 실제 Http Request 실행 시점
			CloseableHttpResponse response = httpClient.execute(request);


			logger.info("------------------------------------------Response Code : " + String.valueOf(response.getStatusLine().getStatusCode()));


		    // 2XX 응답이 아니면 실패로 간주한다.
		    if(!String.valueOf(response.getStatusLine().getStatusCode()).startsWith("2"))
		    	return false;


		    // Response Body를 읽는다.
		    InputStreamReader isReader = new InputStreamReader(response.getEntity().getContent());
		    BufferedReader reader = new BufferedReader(isReader);
		    StringBuffer responseBody = new StringBuffer();
		    String buffer;
		    while((buffer = reader.readLine())!= null){
		       responseBody.append(buffer);
		    }


		    logger.info("------------------------------------------Response Body : " + responseBody.toString());

		    String responseString = responseBody.toString().replace("\"", "");

		    // Response Body에 따른 return value 핸들링
		    if(responseString.startsWith("S")) {
		    	boolean dateParsingResult = responseFormatChecker(responseString);

		    	if(dateParsingResult==false)
		    		return false;


		    	logger.info("Http Request 성공");
		    	return true;
		    }else {
		    	logger.info("올바르지 않은 Response 형식");
				return false;
			}


		 } catch (Exception e){
			logger.info("Http Request 실패");
			logger.error(e.getLocalizedMessage());
			return false;
		 }


	}


	private static boolean responseFormatChecker(String response) {

		try {
			String strDate = response.replace("S ", "");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	    	sdf.parse(strDate);
		}catch (Exception e) {
			logger.info("올바르지 않은 Response 날짜 형식");
			return false;
		}

		return true;

	}







}
