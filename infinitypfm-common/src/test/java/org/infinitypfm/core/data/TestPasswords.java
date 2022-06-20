package org.infinitypfm.core.data;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.infinitypfm.core.util.EncryptUtil;
import org.junit.Test;

public class TestPasswords {

	@Test
	public void TestNullEncryptUtil() {
		
		IllegalArgumentException error = null;
		
		try {
		@SuppressWarnings("unused")
		Password p = new Password("abc", "#$#$#$", null);
		} catch (IllegalArgumentException e) {
			error = e;
		}
		
		assertTrue(error != null);
	}
	
	//@Test
	public void TestCompareTwoPasswords() {
		
		EncryptUtil util = createNiceMock(EncryptUtil.class);
		
		Password p1 = new Password("abc", "YYUU", util);
		Password p2 = new Password("abc", "YYUU", util);
		
		assertFalse(p1.passwordChanged());
		assertFalse(p2.passwordChanged());
		//TODO:  Test fails here
		int result = p1.compareTo(p2);
		
		assertEquals(0, result);
		
		p2.setHashedPassword("AABB");
		result = p1.compareTo(p2);
		assertEquals(1, result);
		assertNull(p2.getPlainPassword());
		assertFalse(p1.passwordChanged());
		assertTrue(p2.passwordChanged());
		
	}
	
	//@Test
	public void TestChangePlainPassword() {
		
		String abcHash = "YYUU";
		String defHash = "AABB";
		
		EncryptUtil util = createNiceMock(EncryptUtil.class);
		
		Password p1 = new Password("abc", abcHash, util);
		expect(util.getHashWithSalt("def")).andReturn(defHash);
		expect(util.getHashWithSalt("abc")).andReturn(abcHash);
		
		assertEquals("abc", p1.getPlainPassword());
		assertFalse(p1.passwordChanged());
		
		p1.setPlainPassword("abc");
		
		assertFalse(p1.passwordChanged());
		assertEquals(abcHash, p1.getHashedPassword());
		assertEquals("abc", p1.getPlainPassword());
		
		p1.setPlainPassword("def");
		
		assertTrue(p1.passwordChanged());
		assertEquals(defHash, p1.getHashedPassword());
		assertEquals("def", p1.getPlainPassword());
		
	}
	
	@Test
	public void TestChangeHashedPassword() {
		
		String abcHash = "YYUU";
		String defHash = "AABB";
		
		EncryptUtil util = createNiceMock(EncryptUtil.class);
		
		Password p1 = new Password("abc", abcHash, util);
		
		assertEquals(abcHash, p1.getHashedPassword());
		assertFalse(p1.passwordChanged());
		
		p1.setHashedPassword(defHash);
		
		assertTrue(p1.passwordChanged());
		assertNull(p1.getPlainPassword());
		assertEquals(defHash, p1.getHashedPassword());
		
	}
	
	//@Test
	public void TestNullStartingHash() {
		
		String abcHash = "YYUU";
		
		EncryptUtil util = createNiceMock(EncryptUtil.class);
		
		expect(util.getHashWithSalt("abc")).andReturn(abcHash);
		
		Password p1 = new Password("abc", null, util);
		
		assertTrue(p1.passwordChanged());
		assertEquals(abcHash, p1.getHashedPassword());
		assertEquals("abc", p1.getPlainPassword());
		
	}
	
	@Test
	public void TestNullStartPlainPassword() {
		String abcHash = "YYUU";
		
		EncryptUtil util = createNiceMock(EncryptUtil.class);
		
		//Mockito.when(util.getHashWithSalt("abc")).thenReturn(abcHash);
		
		Password p1 = new Password(null, abcHash, util);
		
		assertNull(p1.getPlainPassword());
		assertFalse(p1.passwordChanged());
		assertEquals(abcHash, p1.getHashedPassword());
		
	}
	
	@Test
	public void TestBothStartNull() {
		
		EncryptUtil util = createNiceMock(EncryptUtil.class);
		Password p1 = new Password(null, null, util);
		
		assertNull(p1.getPlainPassword());
		assertNull(p1.getHashedPassword());
		assertFalse(p1.passwordChanged());
		
	}
}
