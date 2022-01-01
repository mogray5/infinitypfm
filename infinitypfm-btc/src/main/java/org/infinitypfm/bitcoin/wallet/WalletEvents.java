package org.infinitypfm.bitcoin.wallet;

public interface WalletEvents {

	/**
	 * Event callback for received coins
	 * 
	 * @param tx
	 * @param value
	 * @param prevBalance
	 * @param newBalance
	 */
	public void coinsReceived(String transactionHash, String memo, String value, String prevBalance, String newBalance);
	
	/**
	 * Event callback for sent coins
	 * 
	 * @param tx
	 * @param value
	 * @param prevBalance
	 * @param newBalance
	 */
	public void coinsSent(String transactionHash, String memo, String value, String prevBalance, String newBalance);
}
