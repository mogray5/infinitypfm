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
package org.infinitypfm.core.data;

import org.infinitypfm.core.util.EncryptUtil;
import org.mindrot.jbcrypt.BCrypt;

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
    	
		if (_hashedPassword==null ? inPwd.getHashedPassword()==null : 
			BCrypt.checkpw(inPwd.getPlainPassword(), _hashedPassword))
			result = 0;
		
		return result;
	}
	
}
