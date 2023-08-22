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

import java.io.Serializable;

public class PlanResult  implements Serializable, IReportable {

	private static final long serialVersionUID = -8375198175258359313L;
	private String planName; 
	private int age;
	private long remaining;
	private long draw;
	private long earnInvest;
	private long earnWage;
	private long contribution;
	private long tax;
	private long netEarnings;
	private DataFormatUtil _formatter = null;
	
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
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
	public String getRemainingFormatted() {
		return _formatter.getAmountFormatted(remaining, DataFormatUtil.NUM_FORMAT_NO_CENTS);
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
	public String getDrawFormatted() {
		return _formatter.getAmountFormatted(draw, DataFormatUtil.NUM_FORMAT_NO_CENTS);
	}
	public long getEarnInvest() {
		return earnInvest;
	}
	public void setEarnInvest(long earnInvest) {
		this.earnInvest = earnInvest;
	}
	public String getEarnInvestFormatted() {
		return _formatter.getAmountFormatted(earnInvest, DataFormatUtil.NUM_FORMAT_NO_CENTS);
	}
	public long getEarnWage() {
		return earnWage;
	}
	public void setEarnWage(long earnWage) {
		this.earnWage = earnWage;
	}
	public String getEarnWageFormatted() {
		return _formatter.getAmountFormatted(earnWage, DataFormatUtil.NUM_FORMAT_NO_CENTS);
	}
	public long getContribution() {
		return contribution;
	}
	public void setContribution(long contribution) {
		this.contribution = contribution;
	}
	public String getContributionFormatted() {
		return _formatter.getAmountFormatted(contribution, DataFormatUtil.NUM_FORMAT_NO_CENTS);
	}
	public long getTax() {
		return tax;
	}
	public void setTax(long tax) {
		this.tax = tax;
	}
	public String getTaxFormatted() {
		return _formatter.getAmountFormatted(tax, DataFormatUtil.NUM_FORMAT_NO_CENTS);
	}
	public long getNetEarnings() {
		return netEarnings;
	}
	public void setNetEarnings(long netEarnings) {
		this.netEarnings = netEarnings;
	} 
	public String getNetEarningsFormatted() {
		return _formatter.getAmountFormatted(netEarnings, DataFormatUtil.NUM_FORMAT_NO_CENTS);
	}
	@Override
	public String getHeaderRow() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toReportRow() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setFormatter(DataFormatUtil formatter) {
		_formatter = formatter;
		
	}
}
