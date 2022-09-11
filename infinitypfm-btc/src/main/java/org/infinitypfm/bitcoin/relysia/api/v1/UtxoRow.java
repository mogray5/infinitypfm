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

public class UtxoRow {

	private long height;
	private long tx_pos;
	private String tx_hash = null;
	private long value;
	private String script = null;
	private String path = null;
	private String address  = null;
	
	public long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	
	public long getTx_pos() {
		return tx_pos;
	}
	public void setTx_pos(long tx_pos) {
		this.tx_pos = tx_pos;
	}
	public String getTx_hash() {
		return tx_hash;
	}
	public void setTx_hash(String tx_hash) {
		this.tx_hash = tx_hash;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}

