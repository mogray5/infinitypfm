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

package org.infinitypfm.core.data;

import java.io.Serializable;

public class Options implements Serializable {

	private static final long serialVersionUID = 1L;
	private String appVersion = null;
	private int defaultCurrencyID = 0;
	private int currencyPrecision = 2;
	private boolean reportsInBrowswer = false;
	private boolean consoleDefaultOpen = false;
	private boolean enableWallet = false;
	private String spendPassword = null;
	private String defaultBsvCurrencyMethod = null;
	private long defaultBsvCurrencyID = 0;
	private String emailAddress = null;
	private String walletUser = null;
	private String walletToken = null;
	private String bookmarksUrl = null;
	private String walletId = null;
	
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public int getDefaultCurrencyID() {
		return defaultCurrencyID;
	}
	public void setDefaultCurrencyID(int defaultCurrencyID) {
		this.defaultCurrencyID = defaultCurrencyID;
	}
	public int getCurrencyPrecision() {
		return currencyPrecision;
	}
	public void setCurrencyPrecision(int currencyPrecision) {
		this.currencyPrecision = currencyPrecision;
	}
	public boolean isReportsInBrowswer() {
		return reportsInBrowswer;
	}
	public void setReportsInBrowswer(boolean reportsInBrowswer) {
		this.reportsInBrowswer = reportsInBrowswer;
	}
	public boolean isConsoleDefaultOpen() {
		return consoleDefaultOpen;
	}
	public void setConsoleDefaultOpen(boolean consoleDefaultOpen) {
		this.consoleDefaultOpen = consoleDefaultOpen;
	}
	public boolean isEnableWallet() {
		return enableWallet;
	}
	public void setEnableWallet(boolean enableWallet) {
		this.enableWallet = enableWallet;
	}
	public String getSpendPassword() {
		return spendPassword;
	}
	public void setSpendPassword(String spendPassword) {
		this.spendPassword = spendPassword;
	}
	public String getDefaultBsvCurrencyMethod() {
		return defaultBsvCurrencyMethod;
	}
	public void setDefaultBsvCurrencyMethod(String defaultBsvCurrencyMethod) {
		this.defaultBsvCurrencyMethod = defaultBsvCurrencyMethod;
	}
	public long getDefaultBsvCurrencyID() {
		return defaultBsvCurrencyID;
	}
	public void setDefaultBsvCurrencyID(long defaultBsvCurrencyID) {
		this.defaultBsvCurrencyID = defaultBsvCurrencyID;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getWalletUser() {
		return walletUser;
	}
	public void setWalletUser(String walletUser) {
		this.walletUser = walletUser;
	}
	public String getWalletToken() {
		return walletToken;
	}
	public void setWalletToken(String walletToken) {
		this.walletToken = walletToken;
	}
	public String getBookmarksUrl() {
		return bookmarksUrl;
	}
	public void setBookmarksUrl(String bookmarksUrl) {
		this.bookmarksUrl = bookmarksUrl;
	}
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

}
