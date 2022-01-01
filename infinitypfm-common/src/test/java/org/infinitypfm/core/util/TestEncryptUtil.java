package org.infinitypfm.core.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class TestEncryptUtil {

	@Test
	public void TestPasswordHashing() {
		
		EncryptUtil util = new EncryptUtil();
		
		String plain = "test123";
		String hash = util.getHashWithSalt(plain);
		boolean verified = util.verifyPassword(plain, hash);
		
		assertTrue(verified);	
		
	}
	
	@Test
	public void TestPasswordHashingWithWrongPwd() {
		
		EncryptUtil util = new EncryptUtil();
		
		String plain = "test123";
		String hash = util.getHashWithSalt(plain);
		boolean verified = util.verifyPassword(plain+"456", hash);
		
		assertFalse(verified);	
		
	}
	
}
