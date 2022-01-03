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
import java.net.UnknownHostException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Wrapper class to initialize a wallet kit.  Kit
 * will be injected into BsvWallet class to make
 * it easier to test.
 * 
 * 2022-01-02:  WGG - Not able to get this working with:  https://github.com/bitcoin-sv/bitcoinj-sv
 */
public class BsvKit implements Runnable {

	private final static Logger LOG = LoggerFactory.getLogger(BsvKit.class);
	//private Wallet _kit;
	private final String _walletDir;
	private final String _nodeIp;
	//private NetworkParameters _params;
	private boolean _running;
	
	public static final String WALLET_PREFIX = "infinitypfm_bsv";
	
	public BsvKit(String walletDir, String nodeIp) throws UnknownHostException {
		
		LOG.info("Initializing wallet kit");
		
		_nodeIp = nodeIp;
		_walletDir = walletDir;
		//_params= TestNet2Params.get();
		//_kit = new Wallet(_params);
		//_kit.setAutoSave(false);new File(_walletDir + File.pathSeparator + BsvKit.WALLET_PREFIX + ".wallet");
		_running = false;
	}

	public Object get() {
		return null;
		//return _kit;
	}
	
	public void restoreFromSeed(String seedCode, String passphrase) { //throws UnreadableWalletException {
		
		//_kit.stopAsync();
        //_kit.awaitTerminated();
        
        //this.removeChainFile();
        //this.backupWalletFile();
        
		//Long creationtime = new Date().getTime();
		//DeterministicSeed seed = new DeterministicSeed(seedCode, null, passphrase, creationtime);
		//_kit.restoreWalletFromSeed(seed);
        		
	}
	
	private void removeChainFile() {
		 File chainFile = new File(_walletDir + File.pathSeparator + BsvKit.WALLET_PREFIX + ".spvchain");
			if (chainFile.exists())
				chainFile.delete();
	}
	
	private void backupWalletFile() {
		File walletFile = new File(_walletDir + File.pathSeparator + BsvKit.WALLET_PREFIX + ".wallet");
		File destFile = new File(_walletDir + File.pathSeparator + BsvKit.WALLET_PREFIX + ".wallet.bak");
		
		if (walletFile.exists()) {
			try {
				FileUtils.copyFile(walletFile, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		
		File walletFile = new File(_walletDir + File.separator + BsvKit.WALLET_PREFIX + ".wallet");
		if (walletFile.exists()) {
			/*
			try {
				_kit = Wallet.loadFromFile(walletFile);
				System.out.println(_kit.toString());
			} catch (UnreadableWalletException e) {
				e.printStackTrace();
			}
		} else {
			NetworkParameters params = TestNet3Params.get();
			_kit = new Wallet(params);
			try {
				_kit.saveToFile(walletFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
     
		_running = true;
		
	}

	public boolean isRunning() {
		return _running;
	}	
	
	public void setRunning(boolean val) {
		_running = val;
	}
}
