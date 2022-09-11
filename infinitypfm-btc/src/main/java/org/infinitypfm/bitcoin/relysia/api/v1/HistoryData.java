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

public class HistoryData {

	private String status = null;
	private String msg = null;
	private HistoryRow[] histories = null;
	private String nextPageTokenId = null;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public HistoryRow[] getHistories() {
		return histories;
	}
	public void setHistories(HistoryRow[] histories) {
		this.histories = histories;
	}
	public String getNextPageTokenId() {
		return nextPageTokenId;
	}
	public void setNextPageTokenId(String nextPageTokenId) {
		this.nextPageTokenId = nextPageTokenId;
	}

}
