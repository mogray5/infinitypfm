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
import java.util.Date;

/**
 * POJO defining a Currency.
 *
 */
public class Currency  implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private long currencyID = 0;
	private String currencyName = "";
	private String isoName = "";
	private String exchangeRate = "1"; 
	private Date lastUpdate;
	private String isDefault = "";
	private int currencyPrecision = 2;
	
	public long getCurrencyID() {
		return currencyID;
	}
	public void setCurrencyID(long currencyID) {
		this.currencyID = currencyID;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getIsoName() {
		return isoName;
	}
	public void setIsoName(String isoName) {
		this.isoName = isoName;
	}
	public String getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public int getCurrencyPrecision() {
		return currencyPrecision;
	}
	public void setCurrencyPrecision(int currencyPrecision) {
		this.currencyPrecision = currencyPrecision;
	}
	
}
