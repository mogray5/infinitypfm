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

public class Basis {

	private long tranId; 
	private long aquireCurrencyID = 0;
	private long costCurrencyID = 0;
	private Date aquireDate;
	private long qtyFifo;
	private long qtyLifo;
	private long cost=0;
	
	public long getTranId() {
		return tranId;
	}
	public void setTranId(long tranId) {
		this.tranId = tranId;
	}
	public long getAquireCurrencyID() {
		return aquireCurrencyID;
	}
	public void setAquireCurrencyID(long aquireCurrencyID) {
		this.aquireCurrencyID = aquireCurrencyID;
	}
	public long getCostCurrencyID() {
		return costCurrencyID;
	}
	public void setCostCurrencyID(long costCurrencyID) {
		this.costCurrencyID = costCurrencyID;
	}
	public Date getAquireDate() {
		return aquireDate;
	}
	public void setAquireDate(Date aquireDate) {
		this.aquireDate = aquireDate;
	}
	public long getQtyFifo() {
		return qtyFifo;
	}
	public void setQtyFifo(long qtyFifo) {
		this.qtyFifo = qtyFifo;
	}
	public long getQtyLifo() {
		return qtyLifo;
	}
	public void setQtyLifo(long qtyLifo) {
		this.qtyLifo = qtyLifo;
	}
	public long getCost() {
		return cost;
	}
	public void setCost(long cost) {
		this.cost = cost;
	}
}
