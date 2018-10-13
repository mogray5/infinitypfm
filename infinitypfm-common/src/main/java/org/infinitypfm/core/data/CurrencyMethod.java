/*
 * Copyright (c) 2005-2018 Wayne Gray All rights reserved
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

import java.io.Serializable;

/**
 * POJO for defining a CurrencyMethod.
 * 
 * Currency Method are a mechanism for looking
 * up the exchange rate for a currency.
 * 
 * The lookup requires a HTTP address and
 * the results need to be JSON.
 * 
 * They are user defined.
 *
 */
public class CurrencyMethod   implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long currencyID;
	private String methodName = null;
	private String methodUrl = null;
	private String methodPath = null;
	
	public long getCurrencyID() {
		return currencyID;
	}
	public void setCurrencyID(long currencyID) {
		this.currencyID = currencyID;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodUrl() {
		return methodUrl;
	}
	public void setMethodUrl(String methodUrl) {
		this.methodUrl = methodUrl;
	}
	public String getMethodPath() {
		return methodPath;
	}
	public void setMethodPath(String methodPath) {
		this.methodPath = methodPath;
	}
	
}
