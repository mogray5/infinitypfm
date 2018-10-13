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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.infinitypfm.core.data.IReportable;

/**
 * POJO for storing budget totals.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetBalance implements Serializable, IReportable {

	
	private static final long serialVersionUID = 1L;
	
	int yr = 0;
	int mth = 0;
	long expenseBalance = 0;
	long budgetBalance = 0;
	String actTypeName = "";
	int budgetId = -1;
	String budgetName = null;
	String actName = null;
	
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

	public long getBudgetBalance() {
		return budgetBalance;
	}

	public void setBudgetBalance(long budgetBalance) {
		this.budgetBalance = budgetBalance;
	}

	public long getExpenseBalance() {
		return expenseBalance;
	}

	public void setExpenseBalance(long expenseBalance) {
		this.expenseBalance = expenseBalance;
	}

	public String getActTypeName() {
		return actTypeName;
	}

	public void setActTypeName(String actTypeName) {
		this.actTypeName = actTypeName;
	}

	public int getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(int budgetId) {
		this.budgetId = budgetId;
	}

	public String getBudgetName() {
		return budgetName;
	}

	public void setBudgetName(String budgetName) {
		this.budgetName = budgetName;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	@Override
	public String getHeaderRow() {
	StringBuilder sb = new StringBuilder();
		
		sb.append("Budget Name").append("|");
		sb.append("Budget Balance").append("|");
		sb.append("Expense Balance").append("|");
		sb.append("Year-Month");
		return sb.toString();
	}

	@Override
	public String toReportRow() {

		StringBuilder sb = new StringBuilder();
		sb.append(budgetName).append("|");
		sb.append(budgetBalance).append("|");
		sb.append(expenseBalance).append("|");
		sb.append(yr).append("-").append(mth);
		return sb.toString();
	}

}
