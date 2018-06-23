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

public class Connector {

	private long connectorId;
	private String connectorName;
	private String mailServer;
	private String mailBox;
	private String mailPassword;
	private String mailSubject;
	private String mailBodyPrefix;
	private String mailBodySuffix;
	
	public long getConnectorId() {
		return connectorId;
	}
	public void setConnectorId(long connectorId) {
		this.connectorId = connectorId;
	}
	public String getConnectorName() {
		return connectorName;
	}
	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}
	public String getMailServer() {
		return mailServer;
	}
	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}
	public String getMailBox() {
		return mailBox;
	}
	public void setMailBox(String mailBox) {
		this.mailBox = mailBox;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getMailBodyPrefix() {
		return mailBodyPrefix;
	}
	public void setMailBodyPrefix(String mailBodyPrefix) {
		this.mailBodyPrefix = mailBodyPrefix;
	}
	public String getMailBodySuffix() {
		return mailBodySuffix;
	}
	public void setMailBodySuffix(String mailBodySuffix) {
		this.mailBodySuffix = mailBodySuffix;
	}
	
}
