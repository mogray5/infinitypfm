/*
 * Copyright (c) 2005-2022 Wayne Gray All rights reserved
 * 
 * This file is part of Infinity PFM.
 * 
 * Infinity PFM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Infinity PFM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Infinity PFM.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.infinitypfm.core.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.infinitypfm.core.data.RestResponse;
import org.infinitypfm.core.exception.RemoteRequestFailedException;

public class RestClient {

	private final String _baseUrl;
	private CloseableHttpClient _client = null;
	private StringBuilder _builder = null;
	
	
	public RestClient(String baseUrl) {
		_baseUrl = baseUrl;
		_client = HttpClients.createDefault();
		_builder = new StringBuilder();
	}
	
	
	/**
	 * Make a post request with passed headers and body
	 * @param method String fragment to add to baseUrl to complete URL request
	 * @param headers List of headers to add
	 * @param body String Post body to pass
	 * @return RestReponse containing result code, string result, and exceptions if any
	 */
	public RestResponse post(String method, List<Pair<String, String>> headers, String body) {
		
		_builder.setLength(0);
		_builder.append(_baseUrl).append("/").append(method);
		RestResponse result = new RestResponse();
		CloseableHttpResponse response = null;
		
		try {
		
			HttpPost httpPost = new HttpPost(_builder.toString()); 
			
			try {
			
				if (body != null && body.length() > 0) {
					StringEntity entity = new StringEntity(body);
					httpPost.setEntity(entity);
				}
				
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
		
				// Add passed headers
				if (headers != null) {
					for (Pair <String,String> pair : headers) {
						httpPost.setHeader(pair.getKey(), pair.getValue());
					}
				}
				
				response = _client.execute(httpPost);
				result.setCode(response.getStatusLine().getStatusCode());
			
				String responseBody = EntityUtils.toString(response.getEntity());
				
				result.setBody(responseBody);
				
			} catch (ParseException e) {
				result.setException(new RemoteRequestFailedException(e));
			} catch (IOException e) {
				result.setException(new RemoteRequestFailedException(e));
			} finally {
				try {EntityUtils.consume(response.getEntity());} catch (Exception e) {}
			}

		} finally {
			try {
				response.close();
			} catch (IOException e) {}
		}
		
		return result;
	}
	
	/**
	 * HTTP GET request with passed headers.  
	 * @param method String fragment to add to baseUrl to complete URL request
	 * @param headers List of headers to add
	 * @return RestReponse containing result code, string result, and exceptions if any
	 */
	public RestResponse get(String method, List<Pair<String, String>> headers) {
	
		_builder.setLength(0);
		_builder.append(_baseUrl).append("/").append(method);
		RestResponse result = new RestResponse();
		CloseableHttpResponse response = null;
		
		try {
		
			HttpGet httpGet = new HttpGet(_builder.toString()); 
			
			try {
		
				// Add passed headers
				if (headers != null) {
					for (Pair <String,String> pair : headers) {
						httpGet.setHeader(pair.getKey(), pair.getValue());
					}
				}
				
				response = _client.execute(httpGet);
				result.setCode(response.getStatusLine().getStatusCode());
			
				String responseBody = EntityUtils.toString(response.getEntity());
				
				result.setBody(responseBody);
				
			} catch (ParseException e) {
				result.setException(new RemoteRequestFailedException(e));
			} catch (IOException e) {
				result.setException(new RemoteRequestFailedException(e));
			} finally {
				try {EntityUtils.consume(response.getEntity());} catch (Exception e) {}
			}

		} finally {
			try {
				response.close();
			} catch (IOException e) {}
		}
		
		return result;
	}
	
	public void close() {
		try {_client.close();} catch (Exception e) {}
	}
}
