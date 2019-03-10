package org.infinitypfm.core.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 
 * Utility class for hashing and encryption related
 * functions. 
 *
 */
public class EncryptUtil {

	/**
	 * Hash and salt a string.
	 * 
	 * @param inString raw string to hash
	 * @return Hashed and salted string
	 */
	public String getHashWithSalt(String inString) {
		
		String salt = BCrypt.gensalt(12);
		String hashed_password = BCrypt.hashpw(inString, salt);

		return(hashed_password);
	}
	
	/**
	 * Verify a plain text password against a hashed password.
	 * 
	 * Based on example from:
	 * 
	 * https://gist.github.com/craSH/5217757
	 * 
	 * @param plainPassword Password in plain text
	 * @param hashPassword Stored hashed password to compare with
	 * @return boolean true = password match, false = non match
	 */
	public boolean verifyPassword(String plainPassword, String hashPassword) {
		
		boolean result = false;

		if(hashPassword == null || !hashPassword.startsWith("$2a$"))
			throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

		result = BCrypt.checkpw(plainPassword, hashPassword);

		return result;
		
	}
	
}
