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

//@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyBalanceByType implements Serializable, IReportable{

	private static final long serialVersionUID = 1919973868244212053L;
	private int yr = 0;
	private int mth = 0;
	private long incomeBalance = 0;
	private long expenseBalance = 0;
	private long liabilityBalance = 0;
	private long bankBalance = 0;
	private DataFormatUtil _formatter;
	
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

	public String getYrString() {
		return Integer.toString(yr);
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

	public String getIncomeBalanceFmt() {
		return _formatter.getAmountFormatted(-incomeBalance, "#,##0.00;(#,##0.00)");
	}
	
	public void setIncomeBalance(long incomeBalance) {
		this.incomeBalance = incomeBalance;
	}

	public long getExpenseBalance() {
		return expenseBalance;
	}
	
	public String getExpenseBalanceFmt() {
		return _formatter.getAmountFormatted(expenseBalance, "#,##0.00;(#,##0.00)");
	}

	public void setExpenseBalance(long expenseBalance) {
		this.expenseBalance = expenseBalance;
	}

	public long getLiabilityBalance() {
		return liabilityBalance;
	}

	public String getLiabilityBalanceFmt() {
		return _formatter.getAmountFormatted(liabilityBalance, "#,##0.00;(#,##0.00)");
	}
	
	public String getLiabilityPlusExpenseBalanceFmt() {
		return _formatter.getAmountFormatted(liabilityBalance+expenseBalance, "#,##0.00;(#,##0.00)");
	}

	public long getLiabilityPlusExpenseBalance() {
		return liabilityBalance+expenseBalance;
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

	@Override
	public void setFormatter(DataFormatUtil formatter) {
		_formatter = formatter;
	}
}
