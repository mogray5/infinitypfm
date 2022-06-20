package org.infinitypfm.bitcoin.wallet;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.naming.AuthenticationException;

import org.infinitypfm.core.data.Password;
import org.junit.Test;

public class TestBsvWallet {

	//@Test
	public void TestMnemonicCodeBadPassword() {
		
		BsvKit kit = createNiceMock(BsvKit.class);
		Password pwdWallet = createNiceMock(Password.class);
		expect(pwdWallet.getHashedPassword()).andReturn("somethingdiffent");
		Exception error = null;
		
		BsvWallet wallet = new BitcoinJWallet(kit, pwdWallet);
		String result = null;
		
		try {
			result = wallet.getMnemonicCode();
		} catch (Exception e) {
			error = e;
		}
		
		assertNull(result);
		assertTrue(error instanceof AuthenticationException);
	}
}
