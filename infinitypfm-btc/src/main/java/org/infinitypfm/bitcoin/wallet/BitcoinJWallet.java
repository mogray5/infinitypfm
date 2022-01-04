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

import javax.naming.AuthenticationException;

import org.infinitypfm.bitcoin.wallet.exception.SendException;
import org.infinitypfm.bitcoin.wallet.exception.WalletException;
import org.infinitypfm.core.data.AuthData;
import org.infinitypfm.core.data.Password;
import org.infinitypfm.core.data.ReceivingAddress;
import org.infinitypfm.core.util.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.glxn.qrgen.QRCode;

public class BitcoinJWallet implements BsvWallet {

	private final static Logger LOG = LoggerFactory.getLogger(BitcoinJWallet.class);
	private BsvKit _bsvKit;
	//private Wallet _kit;
	private final Password _spendingPassword;
	private WalletEvents _walletEvents;
	private final EncryptUtil _encryptUtil;
	private boolean _firstUse = true;

	
	public BitcoinJWallet(BsvKit kit, Password spendPwd) {
		_spendingPassword = spendPwd;
		_bsvKit = kit;
		_encryptUtil = new EncryptUtil();
	}
	
	/**************/
	/* Public API */
	/**************/
	
	@Override
	public boolean isRunning() {
		return isRunning(false);
	}
	
	@Override
	public boolean isRunning(boolean TriggerEventOnSignInSuccess) {
		return _bsvKit.isRunning();
	}
	
	/**
	 * Shut down the wallet
	 */
	@Override
	public void stop() {
		//_kit.stopAsync();
        //.awaitTerminated();
        _bsvKit.setRunning(false);
	}
	
	/**
	 * Return balance denominated in FIAT 
	 * 
	 * TODO: (USD only ?)
	 * 
	 * @return
	 */
	@Override
	public String getFiatBalance() {
		if (_firstUse) init();
		//return MonetaryFormat.FIAT.noCode().format(_kit.wallet().getBalance(BalanceType.ESTIMATED)).toString();
		return null;
	}

	/**
	 * Return balance in BSV
	 * 
	 * TODO:  Support different denominations (Satoshi's, etc)
	 * 
	 * @return
	 */
	@Override
	public String getBsvBalance() {
		if (_firstUse) init();
		
		return null;
		
		/*
		 
		List<Address> adrList = _kit.wallet().getIssuedReceiveAddresses();
		
		if (adrList != null) {
			for (Address a : adrList) {
				System.out.println(a.toBase58());
			}
		}
		
		return MonetaryFormat.BTC.noCode().format(_kit.wallet().getBalance(BalanceType.ESTIMATED)).toString();
	
		*/
	}
	
	/**
	 * Start listening for wallet events.
	 * 
	 * @param events Callback object to UI
	 */
	@Override
	public void registerForEvents(WalletEvents events) {
		if (_firstUse) init();
		_walletEvents = events;
	}
	
	/**
	 * Stop listening for wallet events like coin receipt.
	 */
	@Override
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
	@Override
	public ReceivingAddress getCurrentReceivingAddress() {
		if (_firstUse) init();
		//Address a = _kit.wallet().currentReceiveAddress();
		//return a.toBase58();
		return null;
	}
	
	/**
	 * Return 12 word mnemonic seed.  Requires password.
	 * 
	 * @param password Password required
	 * @return String seed phrase
	 * @throws AuthenticationException 
	 */
	@Override
	public String getMnemonicCode() {
		if (_firstUse) init();
		
		/*
		if (authorized(password)) {
			DeterministicSeed seed = _kit.wallet().getKeyChainSeed();
			return Utils.join(seed.getMnemonicCode());
		}
		return null;
		*/
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
	@Override
	public void restoreFromSeed(String seedCode, String passphrase) throws WalletException{
		if (_firstUse) init();
		
		/*
		try {
			if (authorized(password)) {
				_bsvKit.restoreFromSeed(seedCode, passphrase);
				_kit = _bsvKit.get();
			}
		} catch (AuthenticationException | UnreadableWalletException e) {
			throw new WalletException(e.getMessage(), e);
		}
		*/
	}
	
	@Override
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
	@Override
	public void sendCoins(String toAddress, String amount) throws SendException {
		
		if (_firstUse) init();
		
		//final Wallet.SendResult sendResult = _kit.wallet().sendCoins(_kit.peerGroup(), Address.fromBase58(_kit.params(), toAddress), amount);

		
	}
	
	/*******************/
	/* Private Methods */
	/*******************/

	private void init() {
		//BriefLogFormatter.init();
		//_kit = _bsvKit.get();
		
		/*
		
		while (!_kit.isRunning()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		_kit.wallet().addCoinsReceivedEventListener(onCoinsReceived);;
        _kit.wallet().addCoinsSentEventListener(onCoinsSent);
        
        */
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

	/*
	WalletCoinsReceivedEventListener onCoinsReceived = new WalletCoinsReceivedEventListener() {


		
		@Override
		public void onCoinsReceived(TransactionBag wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
			LOG.info("Coins received");
            //Coin value = tx.getValueSentToMe(wallet);
            
            //if (_walletEvents != null) 
            //	_walletEvents.coinsReceived(tx, value, prevBalance, newBalance);
			
		}
		
	};
	
	
	WalletCoinsSentEventListener onCoinsSent = new WalletCoinsSentEventListener() {

		@Override
		public void onCoinsSent(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
			
			LOG.info("Coins sent");
            //Coin value = tx.getValueSentToMe(w);
            
            //if (_walletEvents != null) 
            //	_walletEvents.coinsSent(tx, value, prevBalance, newBalance);
		}
		
	};
	
	 DownloadProgressTracker onDownloadProgress = new DownloadProgressTracker() {
         @Override
         public void doneDownload() {
             System.out.println("blockchain downloaded");
         }
     };

*/
	
	@Override
	public boolean isImplemented(WalletFunction function) {

		switch (function) {
		
		case GETSETBALANCEFIAT:
		case GETSETBALANCEBSV:
		case REGISTERFOREVENTS:
		case UNREGISTERFOREVENTS:
		case CURRENTRECEIVINGADDRESS:
		case GETMNEUMONIC:
		case RESTOREFROMSEED:
		case GETQRCODE:
		case SENDCOINS:
		case SIGNIN:
			return false;
		}
		
		return false;
	}

	@Override
	public AuthData getAuthData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAuthData(AuthData authData) {
		// TODO Auto-generated method stub
		
	}
}
