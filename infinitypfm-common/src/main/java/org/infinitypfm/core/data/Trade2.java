/*
 * Copyright (c) 2005-2019 Wayne Gray All rights reserved
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

public class Trade2 {

	private long tranId; 
	private long fromCurrencyID = 0;
	private long toCurrencyID = 0;
	private Date tranDate;
	private long fromAmount=0;
	private long toAmount=0;
	private long basisFifo=0;
	private long basisLifo=0;
	
	public long getTranId() {
		return tranId;
	}
	public void setTranId(long tranId) {
		this.tranId = tranId;
	}
	public long getFromCurrencyID() {
		return fromCurrencyID;
	}
	public void setFromCurrencyID(long fromCurrencyID) {
		this.fromCurrencyID = fromCurrencyID;
	}
	public long getToCurrencyID() {
		return toCurrencyID;
	}
	public void setToCurrencyID(long toCurrencyID) {
		this.toCurrencyID = toCurrencyID;
	}
	public Date getTranDate() {
		return tranDate;
	}
	public void setTranDate(Date tranDate) {
		this.tranDate = tranDate;
	}
	public long getFromAmount() {
		return fromAmount;
	}
	public void setFromAmount(long fromAmount) {
		this.fromAmount = fromAmount;
	}
	public long getToAmount() {
		return toAmount;
	}
	public void setToAmount(long toAmount) {
		this.toAmount = toAmount;
	}
	public long getBasisFifo() {
		return basisFifo;
	}
	public void setBasisFifo(long basisFifo) {
		this.basisFifo = basisFifo;
	}
	public long getBasisLifo() {
		return basisLifo;
	}
	public void setBasisLifo(long basisLifo) {
		this.basisLifo = basisLifo;
	}
}
