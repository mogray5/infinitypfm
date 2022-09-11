package org.infinitypfm.bitcoin.wallet;

import org.infinitypfm.bitcoin.wallet.exception.WalletException;

public interface WalletEvents {

	/**
	 * Event callback for received coins
	 * 
	 * @param tx
	 * @param value
	 * @param prevBalance
	 * @param newBalance
	 */
	public void coinsReceived(String transactionHash, String memo, String value, String prevBalance, String newBalance, String transactionTime);
	
	/**
	 * Event callback for sent coins
	 * 
	 * @param tx
	 * @param value
	 * @param prevBalance
	 * @param newBalance
	 */
	public void coinsSent(String transactionHash, String memo, String value, String prevBalance, String newBalance, String transactionTime);
	
	/**
	 * Sign in event.  Triggered on sign in for wallets that require sign-in
	 * 
	 * @param success flag to indicate success/failure of sign-in
	 * @param message String message containing error if sign-in failed
	 * @param e Exception for failed sign-in
	 */
	public void signIn(boolean success, String message, WalletException e);
	
	/**
	 * Pass status messages to UI
	 * 
	 * @param message 
	 */
	public void walletMessage(String message, WalletException e);
}
