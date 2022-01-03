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
package org.infinitypfm.core.exception;

public class RemoteRequestFailedException extends Exception {

	private static final long serialVersionUID = 6103415998449828397L;

	public RemoteRequestFailedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RemoteRequestFailedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RemoteRequestFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemoteRequestFailedException(String message) {
		super(message);
	}

	public RemoteRequestFailedException(Throwable cause) {
		super(cause);
	}

}
