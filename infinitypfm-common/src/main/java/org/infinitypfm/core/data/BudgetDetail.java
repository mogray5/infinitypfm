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
 * @author Wayne Gray
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BudgetDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int budgetId = -1;
	int mth = 0;
	int yr = 0;
	int actId = 0;
	String actName = null;
	long actBalance = 0;
	long amount = 0;	
	
	/**
	 * 
	 */
	public BudgetDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Returns the amount.
	 */
	public long getAmount() {
		return amount;
	}
	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}
	/**
	 * @return Returns the budgetId.
	 */
	public int getBudgetId() {
		return budgetId;
	}
	/**
	 * @param budgetId The budgetId to set.
	 */
	public void setBudgetId(int budgetId) {
		this.budgetId = budgetId;
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
	 * @return Returns the actID.
	 */
	public int getActId() {
		return actId;
	}
	/**
	 * @param actID The actID to set.
	 */
	public void setActId(int actID) {
		this.actId = actID;
	}
	
	
	public String getActName() {
		return actName;
	}
	public void setActName(String actName) {
		this.actName = actName;
	}
	
	
	public long getActBalance() {
		return actBalance;
	}
	public void setActBalance(long actBalance) {
		this.actBalance = actBalance;
	}
	
	
	public int getYr() {
		return yr;
	}
	public void setYr(int yr) {
		this.yr = yr;
	}
}
