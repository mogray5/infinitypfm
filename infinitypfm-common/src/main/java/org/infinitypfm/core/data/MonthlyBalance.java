/*
 * Copyright (c) 2005-2020 Wayne Gray All rights reserved
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

public class MonthlyBalance implements Serializable, IReportable {
	
	private static final long serialVersionUID = 1L;
	int actId=0;
	int yr = 0;
	int mth = 0;
	long actBalance = 0;
	String actTypeName = "";
	String actName = "";
	String isoCode = "";
	DataFormatUtil _formatter;
	
	public MonthlyBalance() {
		super();
	}

	public String getActTypeName() {
		return actTypeName;
	}

	public void setActTypeName(String actTypeName) {
		this.actTypeName = actTypeName;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	/**
	 * @return Returns the actBalance.
	 */
	public long getActBalance() {
		return actBalance;
	}
	
	/**
	 * @return Returns the actBalance.
	 */
	public String getActBalanceFmt() {
		
		if (_formatter != null) 
			return _formatter.getAmountFormatted(actBalance, "#,##0.00;(#,##0.00)");
		 else 
			return Long.toString(actBalance);
	}
	
	/**
	 * @param actBalance The actBalance to set.
	 */
	public void setActBalance(long actBalance) {
		this.actBalance = actBalance;
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
	 * @return Returns the mth.
	 */
	public int getMth() {
		return mth;
	}
	/**
	 * @param mth The mth to set.
	 */
	public void setMth(int mth) {
		this.mth = mth;
	}
	/**
	 * @return Returns the yr.
	 */
	public int getYr() {
		return yr;
	}
	
	public String getYrString() {
		return Integer.toString(yr);
	}
	
	/**
	 * @param yr The yr to set.
	 */
	public void setYr(int yr) {
		this.yr = yr;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}
	
	@Override
	public void setFormatter(DataFormatUtil _formatter) {
		this._formatter = _formatter;
	}

	@Override
	public String getHeaderRow() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Category").append("|");
		sb.append("Account Name").append("|");
		sb.append("Currency").append("|");
		sb.append("Account Balance").append("|");
		sb.append("Year-Month");
		return sb.toString();
	}

	@Override
	public String toReportRow() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(actTypeName).append("|");
		sb.append(actName).append("|");
		sb.append(isoCode).append("|");
		sb.append(-actBalance).append("|");
		sb.append(yr).append("-").append(mth);
		return sb.toString();
	}
	
}
