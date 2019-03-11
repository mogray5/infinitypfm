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

import org.apache.log4j.Logger;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.utils.MonetaryFormat;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.infinitypfm.core.data.Password;

public class BsvWallet implements Runnable {

	private final static Logger LOG = Logger.getLogger(BsvWallet.class);
	private WalletAppKit _kit;
	private final Password _spendingPassword;
	
	public BsvWallet(WalletAppKit kit, Password spendPwd) {
		_kit = kit;
		_spendingPassword = spendPwd;
	}
	
	@Override
	public void run() {
	
		LOG.info("Starting BSV Wallet");
		BriefLogFormatter.init();
        
	}
	
	
	public String getFiatBalance() {
		return MonetaryFormat.FIAT.noCode().format(_kit.wallet().getBalance()).toString();
	}

	/*************/
	/* Listeners */
	/*************/
	WalletCoinsReceivedEventListener onCoinsReceived = new WalletCoinsReceivedEventListener() {

		public void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
			
			LOG.info("Coins received");
            Coin value = tx.getValueSentToMe(w);
            System.out.println("Received tx for " + value.toFriendlyString() + ": " + tx);
		}
	};
	
}
