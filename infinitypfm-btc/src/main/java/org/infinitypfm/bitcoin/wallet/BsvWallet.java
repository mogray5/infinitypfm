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
package org.infinitypfm.bitcoin.wallet;

import javax.naming.AuthenticationException;

import org.apache.log4j.Logger;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.utils.MonetaryFormat;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.infinitypfm.core.data.Password;
import org.infinitypfm.core.util.EncryptUtil;

public class BsvWallet implements Runnable {

	private final static Logger LOG = Logger.getLogger(BsvWallet.class);
	private WalletAppKit _kit;
	private final Password _spendingPassword;
	private boolean _running = true;
	private WalletEvents _walletEvents;
	private final EncryptUtil _encryptUtil;
	
	public BsvWallet(WalletAppKit kit, Password spendPwd) {
		_kit = kit;
		_spendingPassword = spendPwd;
		_encryptUtil = new EncryptUtil();
	}
	
	@Override
	public void run() {
	
		LOG.info("Starting BSV Wallet");
		BriefLogFormatter.init();
        
		while (_running) {
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	/**************/
	/* Public API */
	/**************/
	
	/**
	 * Shut down the wallet
	 */
	public void stop() {
		_running = false;
		//TODO:  More action needed to gracefully shutdown wallet?
	}
	
	/**
	 * Return balance denominated in FIAT 
	 * 
	 * TODO: (USD only ?)
	 * 
	 * @return
	 */
	public String getFiatBalance() {
		return MonetaryFormat.FIAT.noCode().format(_kit.wallet().getBalance()).toString();
	}

	/**
	 * Return balance in BSV
	 * 
	 * TODO:  Support different denominations (Satoshi's, etc)
	 * 
	 * @return
	 */
	public String getBsvBalance() {
		return MonetaryFormat.BTC.noCode().format(_kit.wallet().getBalance()).toString();
	}
	
	/**
	 * Start listening for wallet events.
	 * 
	 * @param events Callback object to UI
	 */
	public void registerForEvents(WalletEvents events) {
		_walletEvents = events;
	}
	
	/**
	 * Stop listening for wallet events like coin receipt.
	 */
	public void unregisterForEvents() {
		_walletEvents = null;
	}
	
	/**
	 * Returns current public address in base58
	 * for receiving coins
	 * 
	 * @return base58 receiving address
	 */
	public String getCurrentReceivingAddress() {
		Address a = _kit.wallet().currentReceiveAddress();
		return a.toBase58();
	}
	
	/**
	 * Return 12 word mnemonic seed.  Requires password.
	 * 
	 * @param password Password required
	 * @return String seed phrase
	 * @throws AuthenticationException 
	 */
	public String getMnemonicCode(String password) throws AuthenticationException {
		
		if (authorized(password)) {
			DeterministicSeed seed = _kit.wallet().getKeyChainSeed();
			return Utils.join(seed.getMnemonicCode());
		}
		return null;
		
	}
	
	/*******************/
	/* Private Methods */
	/*******************/

	private boolean authorized(String password) throws AuthenticationException {
		
		Password p = new Password(password, null, _encryptUtil);
		
		if (_spendingPassword.compareTo(p)==0)
			return true;
		else 
			throw new AuthenticationException();
	}
	
	/*************/
	/* Listeners */
	/*************/

	WalletCoinsReceivedEventListener onCoinsReceived = new WalletCoinsReceivedEventListener() {

		public void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
			
			LOG.info("Coins received");
            Coin value = tx.getValueSentToMe(w);
            
            if (_walletEvents != null) 
            	_walletEvents.coinsReceived(tx, value, prevBalance, newBalance);
		}
	};
}
