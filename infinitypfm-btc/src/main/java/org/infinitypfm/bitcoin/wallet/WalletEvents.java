package org.infinitypfm.bitcoin.wallet;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;

public interface WalletEvents {

	/**
	 * Event callback for received coins
	 * 
	 * @param tx
	 * @param value
	 * @param prevBalance
	 * @param newBalance
	 */
	public void coinsReceived(Transaction tx, Coin value, Coin prevBalance, Coin newBalance);
	
	/**
	 * Event callback for sent coins
	 * 
	 * @param tx
	 * @param value
	 * @param prevBalance
	 * @param newBalance
	 */
	public void coinsSent(Transaction tx, Coin value, Coin prevBalance, Coin newBalance);
}
