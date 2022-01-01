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
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.bitcoinj.moved.wallet.UnreadableWalletException;
import org.bitcoinj.moved.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import io.bitcoinsv.bitcoinjsv.params.MainNetParams;
import io.bitcoinsv.bitcoinjsv.params.NetworkParameters;
import io.bitcoinsv.bitcoinjsv.params.TestNet2Params;
import io.bitcoinsv.bitcoinjsv.protos.Protos.PeerAddress;


/**
 * Wrapper class to initialize a wallet kit.  Kit
 * will be injected into BsvWallet class to make
 * it easier to test.
 */
public class BsvKit implements Runnable {

	private final static Logger LOG = LoggerFactory.getLogger(BsvKit.class);
	private Wallet _kit;
	private final String _walletDir;
	private final String _nodeIp;
	private NetworkParameters _params;
	private boolean _running;
	
	public static final String WALLET_PREFIX = "infinitypfm_bsv";
	
	public BsvKit(String walletDir, String nodeIp) throws UnknownHostException {
		
		LOG.info("Initializing wallet kit");
		
		_nodeIp = nodeIp;
		_walletDir = walletDir;
		_params= TestNet2Params.get();
		//_kit = new Wallet(_params);
		//_kit.setAutoSave(false);
		_running = false;
	}

	public Wallet get() {
		return _kit;
	}
	
	public void restoreFromSeed(String seedCode, String passphrase) throws UnreadableWalletException {
		
		//_kit.stopAsync();
        //_kit.awaitTerminated();
        
        this.removeChainFile();
        this.backupWalletFile();
        
		Long creationtime = new Date().getTime();
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

		if (_nodeIp != null && _nodeIp.length()>0) {
			PeerAddress address =  null;
			//try {
			//	address = new PeerAddress(InetAddress.getByName(_nodeIp), _params.getPort());
//			} catch (UnknownHostException e) {
				//e.printStackTrace();
			//}
			//_kit.setPeerNodes(address);
			//_kit.startAsync();
			//_kit.awaitRunning();
			//_kit.peerGroup().setMaxConnections(1);
			
		//} else {
			//_kit.startAsync();
			//_kit.awaitRunning();
		//_kit.peerGroup().setMaxConnections(5);
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
