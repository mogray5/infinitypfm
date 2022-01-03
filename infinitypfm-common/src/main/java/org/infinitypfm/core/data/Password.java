package org.infinitypfm.core.data;

import org.infinitypfm.core.util.EncryptUtil;

/**
 * 
 * Class for making password comparisons easier.
 *
 */
public class Password implements Comparable<Password> {

	private String _plainPassword = null;
	private String _origHashedPassword = null;
	private String _hashedPassword = null;
	private boolean _passwordChanged = false;
	private final EncryptUtil _encryptUtil;
	
	public Password(String origPlainPwd, String origHashPwd, EncryptUtil util) {
		
		_origHashedPassword = origHashPwd;
		_hashedPassword = origHashPwd;
		_encryptUtil = util;
		
		if (util == null)
			throw new IllegalArgumentException("EncryptUtil cannot be null");
	
		if (_origHashedPassword == null)
			/************************************************/
			/* Want to generate a hash if one not passed in */
			/* This will set passwordChanged = true         */
			this.setPlainPassword(origPlainPwd);
			/************************************************/
		else 
			_plainPassword = origPlainPwd;
		
	}
	
	public String getPlainPassword() {
		return _plainPassword;
	}
	
	public void setPlainPassword(String _plain) {
		
		_plainPassword = _plain;
		
		// Convert empty password to null password
		if (_plainPassword != null && _plainPassword.length()==0)
			_plainPassword = null;
		
		if (_plainPassword != null) {
			_hashedPassword = _encryptUtil.getHashWithSalt(_plain);
		
			restPasswordChanged();
		}
		
		this._plainPassword = _plain;
	}
	
	public String getHashedPassword() {
		return _hashedPassword;
	}
	
	public void setHashedPassword(String _hashed) {
		this._hashedPassword = _hashed;
		restPasswordChanged();
		if (_passwordChanged)
			_plainPassword = null;
	}
	
	private void restPasswordChanged() {
				
		_passwordChanged = _origHashedPassword==null ?  _hashedPassword != null : !_origHashedPassword.equals(_hashedPassword);
		
	}
	
	public boolean passwordChanged() {
		return _passwordChanged;
	}

	@Override
	public int compareTo(Password inPwd) {
		
		int result = 1; // not equal 
    	
		if (_hashedPassword==null ? inPwd.getHashedPassword()==null : _hashedPassword.equals(inPwd.getHashedPassword()))
			result = 0;
		
		return result;
	}
	
}
