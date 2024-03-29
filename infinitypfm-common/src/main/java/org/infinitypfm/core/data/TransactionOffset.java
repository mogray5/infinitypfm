/*
 * Copyright (c) 2005-2012 Wayne Gray All rights reserved
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

public class TransactionOffset implements Serializable {

	private static final long serialVersionUID = -7965742019018936346L;

	private int offsetId = -1;
	private String offsetName = null; 
	private long offsetAmount = 0;
	private boolean needsConversion = true;
	
	public int getOffsetId() {
		return offsetId;
	}
	public void setOffsetId(int offsetId) {
		this.offsetId = offsetId;
	}
	public String getOffsetName() {
		return offsetName;
	}
	public void setOffsetName(String offsetName) {
		this.offsetName = offsetName;
	}
	public long getOffsetAmount() {
		return offsetAmount;
	}
	public void setOffsetAmount(long offsetAmount) {
		this.offsetAmount = offsetAmount;
	}
	public boolean needsConversion() {
		return needsConversion;
	}
	public void setNeedsConversion(boolean needsConversion) {
		this.needsConversion = needsConversion;
	}
	
}
