package com.jeonghyeong.healthchecker.request;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;




/*
 * HttpRequest의 Method를 결정해주는 클래스
 *
 * @since 2020. 09. 15.
 * @version 0.2
 * @author JeongHyeongKim
 * @class HttpObjectInitializer.java
 * @param
 * 			url(String)   : http요청을 할 url String
 * 			method(String) : http method
 * @return
 * 			각 메소드에 맞는 HttpRequest객체
 */



public class HttpMethodInitializer {

	private static final String HTTP_GET = "GET";

	private static final String HTTP_POST = "POST";

	private static final String HTTP_HEAD = "HEAD";

	public static HttpRequestBase getRequestMethod(String url, String method) {

		switch(method) {

			case HTTP_GET:
				return new HttpGet(url);


			case HTTP_POST:
				return new HttpPost(url);


			case HTTP_HEAD:
				return new HttpHead(url);

			default:
				return null;
		}
	}



}
