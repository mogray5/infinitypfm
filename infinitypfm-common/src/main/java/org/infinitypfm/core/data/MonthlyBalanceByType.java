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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.infinitypfm.core.data.IReportable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyBalanceByType implements Serializable, IReportable{

	private static final long serialVersionUID = 1919973868244212053L;
	int yr = 0;
	int mth = 0;
	long incomeBalance = 0;
	long expenseBalance = 0;
	long liabilityBalance = 0;
	long bankBalance = 0;
	
	@Override
	public String getHeaderRow() {
StringBuilder sb = new StringBuilder();
		
		sb.append("Year-Month").append("|");
		sb.append("Income Balance").append("|");
		sb.append("Liability Balance").append("|");
		sb.append("Expense Balance").append("|");
		return sb.toString();
	}

	@Override
	public String toReportRow() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(yr).append("-").append(mth).append("|");
		sb.append(-incomeBalance).append("|");
		sb.append(liabilityBalance).append("|");
		sb.append(expenseBalance).append("|");
		
		return sb.toString();
	}

	public int getYr() {
		return yr;
	}

	public void setYr(int yr) {
		this.yr = yr;
	}

	public int getMth() {
		return mth;
	}

	public void setMth(int mth) {
		this.mth = mth;
	}

	public long getIncomeBalance() {
		return incomeBalance;
	}

	public void setIncomeBalance(long incomeBalance) {
		this.incomeBalance = incomeBalance;
	}

	public long getExpenseBalance() {
		return expenseBalance;
	}

	public void setExpenseBalance(long expenseBalance) {
		this.expenseBalance = expenseBalance;
	}

	public long getLiabilityBalance() {
		return liabilityBalance;
	}

	public void setLiabilityBalance(long liabilityBalance) {
		this.liabilityBalance = liabilityBalance;
	}

	public long getBankBalance() {
		return bankBalance;
	}

	public void setBankBalance(long bankBalance) {
		this.bankBalance = bankBalance;
	}
	
	

}
