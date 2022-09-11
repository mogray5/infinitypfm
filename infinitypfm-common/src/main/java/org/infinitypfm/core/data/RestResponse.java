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
package org.infinitypfm.core.data;

import org.infinitypfm.core.exception.RemoteRequestFailedException;

/**
 * 
 * Class to hold responses from rest requests
 * RestReponse will contain the HTTP return code and 
 * the response body as a string.
 *
 */
public class RestResponse {

	private String body = null;
	private int code = 200;
	private RemoteRequestFailedException exception = null;
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public RemoteRequestFailedException getException() {
		return exception;
	}
	public void setException(RemoteRequestFailedException exception) {
		this.exception = exception;
	}
	
}
