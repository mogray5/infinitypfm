package org.infinitypfm.core.conf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestLangLoader {

	@Test
	public void LoadTest() {
		
		LangLoader loader = new LangLoader();
		assertEquals("Name", loader.getPhrase("1"));
		assertEquals("Balance", loader.getPhrase("2"));

	}
	
}
