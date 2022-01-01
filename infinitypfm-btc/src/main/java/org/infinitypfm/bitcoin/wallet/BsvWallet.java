/*
 * Copyright (c) 2005-2021 Wayne Gray All rights reserved
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
package org.infinitypfm.bitcoin.wallet;

import java.io.File;
import java.io.IOException;

import javax.naming.AuthenticationException;

import org.infinitypfm.bitcoin.wallet.exception.SendException;
import org.infinitypfm.bitcoin.wallet.exception.WalletException;

public interface BsvWallet {

	/*******************************************************************/
	/* Public API with common methods for implementing Bitcoin Wallets */
	/*******************************************************************/

	boolean isRunning();

	/**
	 * Shut down the wallet
	 */
	void stop();

	/**
	 * Return balance denominated in FIAT 
	 * 
	 * TODO: (USD only ?)
	 * 
	 * @return
	 */
	String getFiatBalance();

	/**
	 * Return balance in BSV
	 * 
	 * TODO:  Support different denominations (Satoshi's, etc)
	 * 
	 * @return
	 */
	String getBsvBalance();

	/**
	 * Start listening for wallet events.
	 * 
	 * @param events Callback object to UI
	 */
	void registerForEvents(WalletEvents events);

	/**
	 * Stop listening for wallet events like coin receipt.
	 */
	void unregisterForEvents();

	/**
	 * Returns current public address in base58
	 * for receiving coins
	 * 
	 * @return base58 receiving address
	 */
	String getCurrentReceivingAddress();

	/**
	 * Return 12 word mnemonic seed.  Requires password.
	 * 
	 * @param password Password required
	 * @return String seed phrase
	 * @throws AuthenticationException 
	 */
	String getMnemonicCode(String password) throws AuthenticationException;

	/**
	 * Restore a wallet from a mnuemonic seed.
	 * The previous wallet file is first backed up to <prefix>.wallet.bak
	 * @param seedCode BIP 39 passphrase: https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki
	 * @param password InfinityPfm passphrase
	 * @param passphrase seed passphrase
	 * @throws WalletEception thrown if resore failes
	 */
	void restoreFromSeed(String seedCode, String password, String passphrase)
			throws WalletException;

	File getQrCode(String address) throws IOException;

	/**
	 * Send BSV to passed address
	 * 
	 * @param toAddress  Address to send to
	 * @param amount amount, in Bitcoin, to send
	 * @throws SendException thrown when sending error occurs
	 */
	void sendCoins(String toAddress, String amount) throws SendException;

	/**
	 * 
	 * Allow wallet implementation to specify which features are available.
	 *
	 */
	public enum WalletFunction {
		GETSETBALANCEFIAT, GETSETBALANCEBSV,
		REGISTERFOREVENTS, UNREGISTERFOREVENTS,
		CURRENTRECEIVINGADDRESS, GETMNEUMONIC,
		RESTOREFROMSEED, GETQRCODE,
		SENDCOINS
	}
	
	boolean isImplemented(WalletFunction function);
}