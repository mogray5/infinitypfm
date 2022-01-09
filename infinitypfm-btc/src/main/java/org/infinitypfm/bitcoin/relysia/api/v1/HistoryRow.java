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

public class HistoryRow {

	private String protocol = null;
	private String from = null;
	private String timestamp = null;
	private String notes = null;
	private String txId = null;
	private String type = null;
	private String to = null;
	private long balance_change = 0;
	private String docId = null;

	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public long getBalance_change() {
		return balance_change;
	}
	public void setBalance_change(long balance_change) {
		this.balance_change = balance_change;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}

}
