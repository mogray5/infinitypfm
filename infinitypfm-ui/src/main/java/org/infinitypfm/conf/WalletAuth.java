package org.infinitypfm.conf;

import org.infinitypfm.core.data.AuthData;
import org.infinitypfm.core.data.Password;
import org.infinitypfm.core.exception.PasswordInvalidException;
import org.infinitypfm.core.util.EncryptUtil;
import org.infinitypfm.ui.view.dialogs.PasswordDialog;

public class WalletAuth {

	private static WalletAuth _walletAuth = null;
	
	private String _walletPassword = null;
	
	private WalletAuth() {}
	
	public static WalletAuth getInstance() {
		if (_walletAuth == null)
			_walletAuth = new WalletAuth();
		
		return _walletAuth;
	}
	
	/**
	 * Show Password prompt dialog if user has set
	 * a spending password in options
	 * 
	 * @return true/false user authorized
	 * @throws PasswordInvalidException 
	 */
	public String walletPassword() throws PasswordInvalidException {
		
		// _walletPassword will be populated if already logged in
		if (_walletPassword != null) return _walletPassword;
		
		if (MM.wallet.getAuthData() != null) {
			PasswordDialog password = new PasswordDialog(false);
			String[] answer = password.getCredentials();
			// Compare against stored password throw error if not a match
			Password incoming = new Password(answer[1], null, new EncryptUtil());
			if (MM.wallet.getAuthData().getPassword().compareTo(incoming)==0) {
				_walletPassword = answer[1];
				AuthData auth = MM.wallet.getAuthData();
				auth.setPlainPassword(_walletPassword);
			}
			else {
				_walletPassword = null;
				throw new PasswordInvalidException(MM.PHRASES.getPhrase("292"));
			}
			
		}
		
		return _walletPassword;
	}
	
	/**
	 * Allow clearing of password so client can re initiate password challenge as needed.
	 */
	public void clearPassword() {
		_walletPassword = null;
	}

}
