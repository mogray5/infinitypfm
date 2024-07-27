/*
 * Copyright (c) 2005-2023 Wayne Gray All rights reserved
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
package org.infinitypfm.bitcoin.wallet;

import static org.junit.Assert.assertTrue;

import org.infinitypfm.bitcoin.wallet.exception.SendException;
import org.junit.Test;

public class TestLiteClientWallet {

	@Test
	public void LiteClientTest() {
		//LiteClientWallet wallet = new LiteClientWallet("http://192.168.0.21:8443");
		//String result = wallet.getBsvBalance();
		//assertTrue(result.equals("0"));
		
		
		try {
			//wallet.sendCoins("avc", "1000", "derp");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
