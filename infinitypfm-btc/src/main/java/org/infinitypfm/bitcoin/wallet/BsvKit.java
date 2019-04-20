package org.infinitypfm.bitcoin.wallet;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;

/**
 * Wrapper class to initialize a wallet kit.  Kit
 * will be injected into BsvWallet class to make
 * it easier to test.
 */
public class BsvKit implements Runnable {

	private final static Logger LOG = Logger.getLogger(BsvKit.class);
	private WalletAppKit _kit;
	private final String _walletDir;
	private final String _nodeIp;
	private NetworkParameters _params;
	private boolean _running;
	
	public static final String WALLET_PREFIX = "infinitypfm_bsv";
	
	public BsvKit(String walletDir, String nodeIp) throws UnknownHostException {
		
		LOG.info("Initializing wallet kit");
		
		_nodeIp = nodeIp;
		_walletDir = walletDir;
		_params= MainNetParams.get();
		_kit = new WalletAppKit(_params, new File(_walletDir), BsvKit.WALLET_PREFIX);
		_kit.setAutoSave(false);
		_running = false;
	}

	public WalletAppKit get() {
		return _kit;
	}
	
	public void restoreFromSeed(String seedCode, String passphrase) throws UnreadableWalletException {
		
		_kit.stopAsync();
        _kit.awaitTerminated();
        
        this.removeChainFile();
        this.backupWalletFile();
        
		Long creationtime = new Date().getTime();
		DeterministicSeed seed = new DeterministicSeed(seedCode, null, passphrase, creationtime);
		_kit.restoreWalletFromSeed(seed);
        		
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
			try {
				address = new PeerAddress(InetAddress.getByName(_nodeIp), _params.getPort());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			_kit.setPeerNodes(address);
			_kit.startAsync();
			_kit.awaitRunning();
			_kit.peerGroup().setMaxConnections(1);
			
		} else {
			_kit.startAsync();
			_kit.awaitRunning();
		_kit.peerGroup().setMaxConnections(5);
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
