/*
 * Copyright (c) 2005-2011 Wayne Gray All rights reserved
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
 * @author wayne
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int actId;
	private int actTypeId;
	private String actTypeName=null;
	private String actName=null;
	private String actNumber=null;
	private long actBalance = 0;
	private long currencyID;
	private String currencyName = null;
	private String isoCode = null;
	

	/**
	 * 
	 */
	public Account() {
		super();
	}
	
	

	/**
	 * @return Returns the actId.
	 */
	public int getActId() {
		return actId;
	}
	/**
	 * @param actId The actId to set.
	 */
	public void setActId(int actId) {
		this.actId = actId;
	}
	/**
	 * @return Returns the actName.
	 */
	public String getActName() {
		return actName;
	}
	/**
	 * @param actName The actName to set.
	 */
	public void setActName(String actName) {
		this.actName = actName;
	}
	/**
	 * @return Returns the actNumber.
	 */
	public String getActNumber() {
		return actNumber;
	}
	/**
	 * @param actNumber The actNumber to set.
	 */
	public void setActNumber(String actNumber) {
		this.actNumber = actNumber;
	}
	/**
	 * @return Returns the actTypeId.
	 */
	public int getActTypeId() {
		return actTypeId;
	}
	/**
	 * @param actTypeId The actTypeId to set.
	 */
	public void setActTypeId(int actTypeId) {
		this.actTypeId = actTypeId;
	}
	
	
	/**
	 * @return Returns the actTypeName.
	 */
	public String getActTypeName() {
		return actTypeName;
	}
	/**
	 * @param actTypeName The actTypeName to set.
	 */
	public void setActTypeName(String actTypeName) {
		this.actTypeName = actTypeName;
	}
	
	/**
	 * @return Returns the actBalance.
	 */
	public long getActBalance() {
		return actBalance;
	}
	/**
	 * @param actBalance The actBalance to set.
	 */
	public void setActBalance(long actBalance) {
		this.actBalance = actBalance;
	}

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



	public String getIsoCode() {
		return isoCode;
	}



	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}
	
	
	
	
}
