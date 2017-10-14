/*
 * Copyright (c) 2005-2013 Wayne Gray All rights reserved
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.TransactionOffset;
import org.infinitypfm.core.types.DefaultDateFormat;

public class Transaction implements Serializable {

	private static final long serialVersionUID = -3989677930922526215L;
	private long tranId;
	private int actId;
	private String actName = null;
	private String actTypeName;
	private Date tranDate;
	private String tranMemo = null;
	private long tranAmount = 0;
	private ArrayList<TransactionOffset> offsets = null;
	private int tranMonth = 0;
	private int tranYear = 0;
	private DataFormatUtil dateUtil = null;
	private long recurTranId;
	private String transactionKey = null;
	private String exchangeRate = "1";
	private long actBalance = 0;

	/**
	 * 
	 */
	public Transaction() {
		super();
	}

	/**
	 * @return Returns the actId.
	 */
	public int getActId() {
		return actId;
	}

	/**
	 * @param actId
	 *            The actId to set.
	 */
	public void setActId(int actId) {
		this.actId = actId;
	}

	/**
	 * @return Returns the tranCreditAmount.
	 */
	public long getTranAmount() {
		return tranAmount;
	}

	/**
	 * @param tranCreditAmount
	 *            The tranCreditAmount to set.
	 */
	public void setTranAmount(long amt) {
		this.tranAmount = amt;
	}

	public void setTranAmountBd(BigDecimal amt) {
		// assume cents
		BigDecimal multiplier = new BigDecimal("100000000");
		BigDecimal result = amt.multiply(multiplier);
		this.tranAmount = result.longValue();
	}

	/**
	 * @return Returns the tranDate.
	 */
	public Date getTranDate() {
		return tranDate;
	}

	/**
	 * @param tranDate
	 *            The tranDate to set.
	 */
	public void setTranDate(Date tranDate) {
		this.tranDate = tranDate;

		if (tranDate != null) {
			this.dateUtil = new DataFormatUtil();
			this.dateUtil.setDate(tranDate);
			tranMonth = dateUtil.getMonth();
			tranYear = dateUtil.getYear();
		}
	}

	/**
	 * @return Returns the tranId.
	 */
	public long getTranId() {
		return tranId;
	}

	/**
	 * @param tranId
	 *            The tranId to set.
	 */
	public void setTranId(long tranId) {
		this.tranId = tranId;
	}

	/**
	 * @return Returns the tranMemo.
	 */
	public String getTranMemo() {

		if (tranMemo != null) {
			return tranMemo;
		} else {
			/*
			 * This shouldn't happen. Return something other than null to avoid
			 * pointer errors.
			 */
			return "";
		}
	}

	/**
	 * @param tranMemo
	 *            The tranMemo to set.
	 */
	public void setTranMemo(String tranMemo) {

		if (tranMemo != null) {
			if (this.tranMemo != null) {
				this.tranMemo += " " + tranMemo;
			} else {
				this.tranMemo = tranMemo;
			}
		}
	}

	public String getTranDateFmt() {
		return dateUtil.getFormat(DefaultDateFormat.DAY);
	}
	
	public void setTranDateFmt(String fmt){}

	/**
	 * @return Returns the tranMonth.
	 */
	public int getTranMonth() {
		return tranMonth;
	}

	/**
	 * @return Returns the tranYear.
	 */
	public int getTranYear() {
		return tranYear;
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

	public long getRecurTranId() {
		return recurTranId;
	}

	public void setRecurTranId(long recurTranId) {
		this.recurTranId = recurTranId;
	}

	public String getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(String transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	/*
	 * ****************************************************
	 * Still support single offset interface for now.
	 * ****************************************************
	 */

	public int getActOffset() {
		initializeOffset();
		TransactionOffset offset = offsets.get(0);
		return offset.getOffsetId();
	}

	public void setActOffset(int actOffset) {
		initializeOffset();
		TransactionOffset offset = offsets.get(0);
		offset.setOffsetId(actOffset);
		offset.setOffsetAmount(-this.getTranAmount());
	}

	public String getActOffsetName() {
		initializeOffset();
		TransactionOffset offset = offsets.get(0);
		return offset.getOffsetName();
	}

	public void setActOffsetName(String actOffsetName) {
		initializeOffset();
		TransactionOffset offset = offsets.get(0);
		offset.setOffsetName(actOffsetName);
		offset.setOffsetAmount(-this.getTranAmount());
	}

	public long getActBalance() {
		return actBalance;
	}

	public void setActBalance(long actBalance) {
		this.actBalance = actBalance;
	}

	/*
	 * ****************************************************
	 */

	@SuppressWarnings("rawtypes")
	public ArrayList getOffsets() {
		return offsets;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setOffsets(ArrayList offsets) {
		this.offsets = offsets;
	}

	private void initializeOffset() {

		if (offsets == null) {
			offsets = new ArrayList<TransactionOffset>();
			TransactionOffset newOffset = new TransactionOffset();
			offsets.add(newOffset);
		}

	}

}
