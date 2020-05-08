package com.developervisits.oauth2.api;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

public class ApiBinding {

	protected RestTemplate restTemplate;

	public ApiBinding(String accessToken) {
		this.restTemplate = new RestTemplate();
		if(accessToken !=null && !accessToken.isEmpty()) {
			restTemplate.getInterceptors().add(getBearerInterceptor(accessToken));
		} else {
			restTemplate.getInterceptors().add(getNoTokenInterceptor());
		}
	}

	private ClientHttpRequestInterceptor getBearerInterceptor(String accessToken) {
		return new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken);
				return execution.execute(request, body);
			}
		};
	}

	private ClientHttpRequestInterceptor getNoTokenInterceptor() {
		return new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				throw new IllegalStateException("Access token is missing.");
			}
		};
	}
}
