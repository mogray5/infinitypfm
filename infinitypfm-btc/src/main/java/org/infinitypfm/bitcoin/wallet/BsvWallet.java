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
package org.infinitypfm.bitcoin.wallet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.infinitypfm.bitcoin.wallet.exception.SendException;
import org.infinitypfm.bitcoin.wallet.exception.WalletException;
import org.infinitypfm.core.data.AuthData;
import org.infinitypfm.core.data.DigitalAssetTransaction;
import org.infinitypfm.core.data.DigitalAssetUtxo;
import org.infinitypfm.core.data.ReceivingAddress;

public interface BsvWallet {

	/*******************************************************************/
	/* Public API with common methods for implementing Bitcoin Wallets */
	/*******************************************************************/

	/**
	 * 
	 * Get and set AuthData containing information to access 3rd
	 * party services if required by the wallet.
	 */
	AuthData getAuthData();
	void setAuthData(AuthData authData);
	
	/**
	 * Return running status of wallet.  Will return true if operational and can handle
	 * wallet requests.
	 * @return
	 */
	boolean isRunning();
	boolean isRunning(boolean TriggerEventOnSignInSuccess);

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
	 * @return ReceivingAddress object containing
	 * base58 receiving address and paymail (if available)
	 */
	ReceivingAddress getCurrentReceivingAddress();

	/**
	 * Return 12 word mnemonic seed.  Requires password.
	 * 
	 * @return String seed phrase 
	 */
	String getMnemonicCode();

	/**
	 * Restore a wallet from a mnuemonic seed.
	 * The previous wallet file is first backed up to <prefix>.wallet.bak
	 * @param seedCode BIP 39 passphrase: https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki
	 * @param password InfinityPfm passphrase
	 * @param passphrase seed passphrase
	 * @throws WalletEception thrown if restore fails
	 */
	void restoreFromSeed(String seedCode, String passphrase)
			throws WalletException;

	File getQrCode(String address) throws IOException;

	/**
	 * Return a history of transactions for the wallet that happened since passed
	 * transaction.
	 * @param sinceTransaction Transaction watermark
	 * @return List of DigitalAssetTransaction
	 * @throws WalletException thrown if fetch fails
	 */
	List<DigitalAssetTransaction> getHistory(String sinceTransaction)
			throws WalletException;
	
	/**
	 * Send BSV to passed address
	 * 
	 * @param toAddress  Address to send to
	 * @param amount amount, in Bitcoin, to send
	 * @throws SendException thrown when sending error occurs
	 */
	void sendCoins(String toAddress, String amount, String memo) throws SendException;

	
	/**
	 * Returns list of unspent transaction outputs (UTXO) for wallet 
	 * @return
	 * @throws WalletException
	 */
	List<DigitalAssetUtxo> getUtxo() throws WalletException; 
	
	
	/**
	 * 
	 * Allow wallet implementation to specify which features are available.
	 *
	 */
	public enum WalletFunction {
		BACKUP,
		CURRENTRECEIVINGADDRESS,
		GETHISTORY,
		GETMNEUMONIC,
		GETQRCODE,
		GETSETBALANCEBSV,
		GETSETBALANCEFIAT,
		GETUTXO,
		RECIEVEREALTIME,
		REGISTERFOREVENTS,
		RESTOREFROMSEED,
		SENDCOINS,
		SIGNIN,
		UNREGISTERFOREVENTS
	}
	
	boolean isImplemented(WalletFunction function);
}