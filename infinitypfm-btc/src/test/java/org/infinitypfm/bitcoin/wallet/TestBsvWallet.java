package org.infinitypfm.bitcoin.wallet;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.naming.AuthenticationException;

import org.infinitypfm.core.data.Password;
import org.junit.Test;
import org.mockito.Mockito;

public class TestBsvWallet {

	@Test
	public void TestMnemonicCodeBadPassword() {
		
		BsvKit kit = Mockito.mock(BsvKit.class);
		Password pwdWallet = Mockito.mock(Password.class);
		Mockito.when(pwdWallet.getHashedPassword()).thenReturn("somethingdiffent");
		Exception error = null;
		
		BsvWallet wallet = new BitcoinJWallet(kit, pwdWallet);
		String result = null;
		
		try {
			result = wallet.getMnemonicCode("mypassword");
		} catch (Exception e) {
			error = e;
		}
		
		assertNull(result);
		assertTrue(error instanceof AuthenticationException);
	}
}
