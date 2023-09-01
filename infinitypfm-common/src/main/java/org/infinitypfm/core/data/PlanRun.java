/*
 * Copyright (c) 2005-2023 Wayne Gray All rights reserved
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

import java.util.Date;

/**
 * POJO for retirement plans
 */
public class PlanRun {

	private int planID;
	private Date RunDate;
	private int age;
	private long remaining;
	private long draw;
	private long drawPercent;
	private long earnInvest;
	private long earnWage;
	private long contribution;
	private long tax;
	private long fee;
	
	public int getPlanID() {
		return planID;
	}
	public void setPlanID(int planID) {
		this.planID = planID;
	}
	public Date getRunDate() {
		return RunDate;
	}
	public void setRunDate(Date runDate) {
		RunDate = runDate;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getRemaining() {
		return remaining;
	}
	public void setRemaining(long remaining) {
		this.remaining = remaining;
	}
	public long getDraw() {
		return draw;
	}
	public void setDraw(long draw) {
		this.draw = draw;
	}
	public long getDrawPercent() {
		return drawPercent;
	}
	public void setDrawPercent(long drawPercent) {
		this.drawPercent = drawPercent;
	}
	public long getEarnInvest() {
		return earnInvest;
	}
	public void setEarnInvest(long earnInvest) {
		this.earnInvest = earnInvest;
	}
	public long getEarnWage() {
		return earnWage;
	}
	public void setEarnWage(long earnWage) {
		this.earnWage = earnWage;
	}
	public long getContribution() {
		return contribution;
	}
	public void setContribution(long contribution) {
		this.contribution = contribution;
	}
	public long getTax() {
		return tax;
	}
	public void setTax(long tax) {
		this.tax = tax;
	}
	public long getFee() {
		return fee;
	}
	public void setFee(long fee) {
		this.fee = fee;
	}
	
}
