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

import java.io.File;
import java.io.IOException;

import javax.naming.AuthenticationException;

import org.apache.log4j.Logger;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.utils.MonetaryFormat;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.BalanceType;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsSentEventListener;
import org.infinitypfm.core.data.Password;
import org.infinitypfm.core.util.EncryptUtil;

import net.glxn.qrgen.javase.QRCode;

public class BsvWallet {

	private final static Logger LOG = Logger.getLogger(BsvWallet.class);
	private BsvKit _bsvKit;
	private WalletAppKit _kit;
	private final Password _spendingPassword;
	private WalletEvents _walletEvents;
	private final EncryptUtil _encryptUtil;
	private boolean _firstUse = true;

	
	public BsvWallet(BsvKit kit, Password spendPwd) {
		_spendingPassword = spendPwd;
		_bsvKit = kit;
		_encryptUtil = new EncryptUtil();
	}
	
	/**************/
	/* Public API */
	/**************/
	
	public boolean isRunning() {
		return _bsvKit.isRunning();
	}
	
	/**
	 * Shut down the wallet
	 */
	public void stop() {
		_kit.stopAsync();
        _kit.awaitTerminated();
        _bsvKit.setRunning(false);
	}
	
	/**
	 * Return balance denominated in FIAT 
	 * 
	 * TODO: (USD only ?)
	 * 
	 * @return
	 */
	public String getFiatBalance() {
		if (_firstUse) init();
		return MonetaryFormat.FIAT.noCode().format(_kit.wallet().getBalance(BalanceType.ESTIMATED)).toString();
	}

	/**
	 * Return balance in BSV
	 * 
	 * TODO:  Support different denominations (Satoshi's, etc)
	 * 
	 * @return
	 */
	public String getBsvBalance() {
		if (_firstUse) init();
		return MonetaryFormat.BTC.noCode().format(_kit.wallet().getBalance(BalanceType.ESTIMATED)).toString();
	}
	
	/**
	 * Start listening for wallet events.
	 * 
	 * @param events Callback object to UI
	 */
	public void registerForEvents(WalletEvents events) {
		if (_firstUse) init();
		_walletEvents = events;
	}
	
	/**
	 * Stop listening for wallet events like coin receipt.
	 */
	public void unregisterForEvents() {
		if (_firstUse) init();
		_walletEvents = null;
	}
	
	/**
	 * Returns current public address in base58
	 * for receiving coins
	 * 
	 * @return base58 receiving address
	 */
	public String getCurrentReceivingAddress() {
		if (_firstUse) init();
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
		if (_firstUse) init();
		
		if (authorized(password)) {
			DeterministicSeed seed = _kit.wallet().getKeyChainSeed();
			return Utils.join(seed.getMnemonicCode());
		}
		return null;
		
	}
	
	/**
	 * Restore a wallet from a mnuemonic seed.
	 * The previous wallet file is first backed up to <prefix>.wallet.bak
	 * @param seedCode BIP 39 passphrase: https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki
	 * @param password InfinityPfm passphrase
	 * @param passphrase seed passphrase
	 * @throws AuthenticationException thrown for password mismatch
	 * @throws UnreadableWalletException thrown if wallet seed can not be used
	 */
	public void restoreFromSeed(String seedCode, String password, String passphrase) throws AuthenticationException, UnreadableWalletException{
		if (_firstUse) init();
		if (authorized(password)) {
			_bsvKit.restoreFromSeed(seedCode, passphrase);
			_kit = _bsvKit.get();
		}
	}
	
	public File getQrCode(String address) throws IOException {
		if (_firstUse) init();
		File newFile = QRCode.from(address)
		.withSize(250, 250).file();
		
		//URL url = new URL("http://chart.googleapis.com/chart?chs=250x250&cht=qr&chl=" + address);
		//File newFile = File.createTempFile("ipfmqr", "png", new File(System.getProperty("java.io.tmpdir")));
		//if (newFile.exists()) newFile.delete();
		//FileUtils.copyURLToFile(url, newFile);
		
		return newFile;
		
	}
	
	/**
	 * Send BSV to passed address
	 * 
	 * @param toAddress  Address to send to
	 * @param amount amount, in Bitcoin, to send
	 * @throws AddressFormatException
	 * @throws InsufficientMoneyException
	 */
	public void sendCoins(String toAddress, Coin amount) throws AddressFormatException, InsufficientMoneyException {
		
		if (_firstUse) init();
		//Transaction tx = _kit.wallet().createSend(Address.fromBase58(_kit.params(), toAddress), amount);
		//SendRequest request = SendRequest.forTx(tx);
		//request.ensureMinRequiredFee = true;
		//_kit.wallet().completeTx(request);
		
		final Wallet.SendResult sendResult = _kit.wallet().sendCoins(_kit.peerGroup(), Address.fromBase58(_kit.params(), toAddress), amount);
		
		
		//TransactionBroadcast broadCast = _kit.peerGroup().broadcastTransaction(request.tx);
		//broadCast.broadcast();
		
	}
	
	/*******************/
	/* Private Methods */
	/*******************/

	private void init() {
		BriefLogFormatter.init();
		_kit = _bsvKit.get();
		
		while (!_kit.isRunning()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		_kit.wallet().addCoinsReceivedEventListener(onCoinsReceived);;
        _kit.wallet().addCoinsSentEventListener(onCoinsSent);
        _firstUse = false;
	}
	
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
	
	WalletCoinsSentEventListener onCoinsSent = new WalletCoinsSentEventListener() {

		@Override
		public void onCoinsSent(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
			
			LOG.info("Coins sent");
            Coin value = tx.getValueSentToMe(w);
            
            if (_walletEvents != null) 
            	_walletEvents.coinsSent(tx, value, prevBalance, newBalance);
		}
		
	};
	
	 DownloadProgressTracker onDownloadProgress = new DownloadProgressTracker() {
         @Override
         public void doneDownload() {
             System.out.println("blockchain downloaded");
         }
     };
}
