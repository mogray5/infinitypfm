/*
 * Copyright (c) 2005-2022 Wayne Gray All rights reserved
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
package org.infinitypfm.bitcoin.relysia.api.v1;

public class SendData {
	private String to = null;
	private float amount = 0F;
	private String type = null;
	private String notes = null;
	private String tokenId = null;
	private int sn = 0;
	
	public SendData(String to, float amount, String type, String notes, String tokenId, int sn) {
		super();
		this.to = to;
		this.amount = amount;
		this.type = type;
		this.notes = notes;
		this.tokenId = tokenId;
		this.sn = sn;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}

	
}
