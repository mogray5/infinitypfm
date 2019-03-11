package org.infinitypfm.bitcoin.wallet;

import java.io.File;

import org.apache.log4j.Logger;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;

/**
 * Wrapper class to initialize a wallet kit.  Kit
 * will be injected into BsvWallet class to make
 * it easier to test.
 */
public class BsvKit {

	private final static Logger LOG = Logger.getLogger(BsvKit.class);
	private WalletAppKit _kit;
	
	public BsvKit(String walletDir) {
		
		LOG.info("Initializing wallet kit");
		
		NetworkParameters params= MainNetParams.get();
		
		_kit = new WalletAppKit(params, new File(walletDir), "infinitypfm_bsv");
		_kit.setAutoSave(false);
		_kit.startAsync();
		_kit.awaitRunning();
		_kit.peerGroup().setMaxConnections(5);
		
	}

	public WalletAppKit get() {
		return _kit;
	}
}
