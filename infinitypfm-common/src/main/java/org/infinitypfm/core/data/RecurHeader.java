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

import org.infinitypfm.core.conf.LangInstance;
import org.infinitypfm.core.types.DefaultDateFormat;


public class RecurHeader implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int recurId;
	private int frequencyId = 0;
	private String memo = null;
	private int actId;
	private String actName = null;
	private int offsetId;
	private String offsetName = null;
	private Date nextRunDate;
	private long amount=0;
	private DataFormatUtil dateUtil = new DataFormatUtil();
	
	public int getRecurId() {
		return recurId;
	}
	public void setRecurId(int recurId) {
		this.recurId = recurId;
	}
	public String getFrequency() {
		return LangInstance.getInstance().getPhrase(new Integer(frequencyId).toString());
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getActId() {
		return actId;
	}
	public void setActId(int actId) {
		this.actId = actId;
	}
	public int getOffsetId() {
		return offsetId;
	}
	public void setOffsetId(int offsetId) {
		this.offsetId = offsetId;
	}
	public Date getNextRunDate() {
		return nextRunDate;
	}
	public String getNextRunDateFmt(){
		return dateUtil.getFormat(DefaultDateFormat.DAY);
	}
	public void setNextRunDate(Date nextRunDate) {
		this.nextRunDate = nextRunDate;
		dateUtil.setDate(nextRunDate);
	}
	public long getAmount() {
		return amount;
	}
	public String getAmountFmt(int precision){
		dateUtil.setPrecision(precision);
		return dateUtil.getAmountFormatted(amount);
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getActName() {
		return actName;
	}
	public void setActName(String actName) {
		this.actName = actName;
	}
	public String getOffsetName() {
		return offsetName;
	}
	public void setOffsetName(String offsetName) {
		this.offsetName = offsetName;
	}
	public int getFrequencyId() {
		return frequencyId;
	}
	public void setFrequencyId(int frequencyId) {
		this.frequencyId = frequencyId;
	}
	
	
	
}
