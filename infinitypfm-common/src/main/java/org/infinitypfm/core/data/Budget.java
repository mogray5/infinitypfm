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

import org.infinitypfm.core.data.BudgetDetail;


/**
 * POJO for Budgets.
 *
 */
public class Budget implements Serializable {

	private static final long serialVersionUID = 1L;
	int budgetId = -1;
	String budgetName = null;
	BudgetDetail[] budgetDetail = null;
	
	public Budget() {
		super();
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
	 * @return Returns the budgetName.
	 */
	public String getBudgetName() {
		return budgetName;
	}
	/**
	 * @param budgetName The budgetName to set.
	 */
	public void setBudgetName(String budgetName) {
		this.budgetName = budgetName;
	}
	
	/**
	 * @return Returns the budgetDetail.
	 */
	public BudgetDetail[] getBudgetDetail() {
		return budgetDetail;
	}
	/**
	 * @param budgetDetail The budgetDetail to set.
	 */
	public void setBudgetDetail(BudgetDetail[] budgetDetail) {
		this.budgetDetail = budgetDetail;
	}
}
