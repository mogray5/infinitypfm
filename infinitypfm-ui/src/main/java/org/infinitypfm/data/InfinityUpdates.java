/*
 * Copyright (c) 2005-2020 Wayne Gray All rights reserved
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

package org.infinitypfm.data;

import java.sql.SQLException;

import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Currency;


public class InfinityUpdates {

	public InfinityUpdates() {
		super();
	}

	public void ProcessUpdates() throws SQLException {

		String sVersion = null;

		try {
			sVersion = (String) MM.sqlMap.queryForObject("getAppVersion", null);

		} catch (SQLException se) {

			int code = se.getErrorCode();

			if (code == -22) {
				sVersion = "0.0.1";
			} else {
				return;
			}
		}

		if (sVersion.equals("0.0.1") || sVersion == null) {
			ApplyVersion002();
			ApplyVersion010();
			ApplyVersion030();
			ApplyVersion035();
			ApplyVersion040();
			ApplyVersion050();
			ApplyVersion060();
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equals("0.0.2")) {
			ApplyVersion010();
			ApplyVersion030();
			ApplyVersion035();
			ApplyVersion040();
			ApplyVersion050();
			ApplyVersion060();
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equals("0.1.0")) {
			ApplyVersion030();
			ApplyVersion035();
			ApplyVersion040();
			ApplyVersion050();
			ApplyVersion060();
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equals("0.2.0")) {
			ApplyVersion030();
			ApplyVersion035();
			ApplyVersion040();
			ApplyVersion050();
			ApplyVersion060();
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equals("0.2.1")) {
			ApplyVersion030();
			ApplyVersion035();
			ApplyVersion040();
			ApplyVersion050();
			ApplyVersion060();
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equals("0.3.0")) {
			ApplyVersion035();
			ApplyVersion040();
			ApplyVersion050();
			ApplyVersion060();
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equals("0.3.5")) {
			ApplyVersion040();
			ApplyVersion050();
			ApplyVersion060();
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equals("0.4.0")) {
			ApplyVersion050();
			ApplyVersion060();
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equals("0.5.0")) {
			ApplyVersion060();
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equalsIgnoreCase("0.6.0")){
			ApplyVersion070();
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equalsIgnoreCase("0.7.0")){
			ApplyVersion075();
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equalsIgnoreCase("0.7.5")) {
			ApplyVersion076();
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equalsIgnoreCase("0.7.6")) {
			ApplyVersion077();
			ApplyVersion078();
		} else if (sVersion.equalsIgnoreCase("0.7.7")) {
			ApplyVersion078();
		 } else if (sVersion.equalsIgnoreCase("0.7.8")) {
			ApplyVersion085();
		}

	}

	private void ApplyVersion002() throws SQLException {
		MM.sqlMap.insert("v0_0_2_a", null);
		MM.sqlMap.insert("v0_0_2_b", null);
		MM.sqlMap.insert("v0_0_2_c", null);
	}

	private void ApplyVersion010() throws SQLException {
		MM.sqlMap.update("bumpVersion", "0.1.0");
	}

	private void ApplyVersion030() throws SQLException {
		MM.sqlMap.update("v0_3_0_a", null);
		MM.sqlMap.update("v0_3_0_b", null);
		MM.sqlMap.update("v0_3_0_c", null);
		MM.sqlMap.update("v0_3_0_d", null);
		MM.sqlMap.update("v0_3_0_e", null);
		MM.sqlMap.update("bumpVersion", "0.3.0");
	}
	
	private void ApplyVersion035() throws SQLException {
		MM.sqlMap.update("bumpVersion", "0.3.5");
	}
	
	private void ApplyVersion040() throws SQLException {
		MM.sqlMap.update("bumpVersion", "0.4.0");
	}
	private void ApplyVersion050() throws SQLException {
		MM.sqlMap.update("bumpVersion", "0.5.0");
		MM.sqlMap.insert("v0_5_0_e", null);
		MM.sqlMap.insert("v0_5_0_f", null);
		MM.sqlMap.insert("v0_5_0_g", null);
		MM.sqlMap.insert("v0_5_0_h", null);
		MM.sqlMap.insert("v0_5_0_i", null);
		MM.sqlMap.insert("v0_5_0_j", null);
		MM.sqlMap.insert("v0_5_0_k", null);
		MM.sqlMap.insert("v0_5_0_l", null);
		MM.sqlMap.insert("v0_5_0_m", null);
		MM.sqlMap.insert("v0_5_0_n", null);
		MM.sqlMap.insert("v0_5_0_o", null);
		MM.sqlMap.insert("v0_5_0_p", null);
		MM.sqlMap.insert("v0_5_0_q", null);
		MM.sqlMap.insert("v0_5_0_r", null);
		MM.sqlMap.insert("v0_5_0_s", null);
		MM.sqlMap.insert("v0_5_0_t", null);
		MM.sqlMap.insert("v0_5_0_u", null);
		MM.sqlMap.insert("v0_5_0_v", null);
		MM.sqlMap.insert("v0_5_0_w", null);
		MM.sqlMap.insert("v0_5_0_x", null);
		MM.sqlMap.insert("createTableCurrencies", null);
		MM.sqlMap.insert("createPKCurrencies", null);
		MM.sqlMap.insert("v0_5_0_b", null);
		
		MM.sqlMap.insert("v0_5_0_y", null);
		
		Currency currency = new Currency();
		currency.setCurrencyName(MM.PHRASES.getPhrase("212"));
		currency.setExchangeRate("1");
		currency.setIsoName(MM.PHRASES.getPhrase("213"));
		MM.sqlMap.insert("addCurrency", currency);
		
		currency.setCurrencyName(MM.PHRASES.getPhrase("217"));
		currency.setExchangeRate(".31");
		currency.setIsoName(MM.PHRASES.getPhrase("218"));
		MM.sqlMap.insert("addCurrency", currency);
		
		MM.sqlMap.insert("v0_5_0_z", null);
		
		MM.sqlMap.insert("createFKCurrencies", null);
		MM.sqlMap.insert("createFKCurrencies2", null);
		MM.sqlMap.insert("createTableCurrencyMethods", null);
		MM.sqlMap.insert("createPKCurrencyMethods", null);
		MM.sqlMap.insert("createFKCurrencyMethods", null);
		
		
		MM.sqlMap.insert("v0_5_0_za", null);
		
		MM.sqlMap.insert("createTableTrades", null);
		MM.sqlMap.insert("createPKTrades", null);
		MM.sqlMap.insert("createFKTrades1", null);
		MM.sqlMap.insert("createFKTrades2", null);
		
		
	}
	
	private void ApplyVersion060() throws SQLException {
		MM.sqlMap.update("v0_6_0_a", null);
		MM.sqlMap.update("v0_6_0_b", null);
		MM.sqlMap.update("v0_6_0_c", null);
		MM.sqlMap.update("v0_6_0_d", null);
		MM.sqlMap.update("v0_6_0_e", null);
		MM.sqlMap.update("v0_6_0_f", null);
		MM.sqlMap.update("v0_6_0_g", null);
		MM.sqlMap.update("v0_6_0_h", null);
		MM.sqlMap.update("v0_6_0_i", null);
		MM.sqlMap.update("v0_6_0_j", null);
		MM.sqlMap.update("v0_6_0_k", null);
		
		MM.sqlMap.update("bumpVersion", "0.6.0");
	}
	
	private void ApplyVersion070() throws SQLException {
		MM.sqlMap.insert("createTableImportRules");
		MM.sqlMap.insert("createPKImportRules");
		MM.sqlMap.insert("createFKImportRules");
		MM.sqlMap.update("bumpVersion", "0.7.0");
	}
	
	private void ApplyVersion075() throws SQLException {
		
		MM.sqlMap.update("v0_7_0_a");
		MM.sqlMap.update("v0_7_0_b");
		MM.sqlMap.update("v0_7_0_c");
		MM.sqlMap.update("v0_7_0_d");
		MM.sqlMap.update("v0_7_0_e");
		MM.sqlMap.update("v0_7_0_f");
		MM.sqlMap.update("v0_7_0_g");
		MM.sqlMap.update("bumpVersion", "0.7.5");
	}
	
	private void ApplyVersion076() throws SQLException {
		
		MM.sqlMap.update("akTransactions");
		MM.sqlMap.update("bumpVersion", "0.7.6");
		
	}
	
	private void ApplyVersion077() throws SQLException {
		
		MM.sqlMap.update("v0_7_7_a");
		MM.sqlMap.update("v0_7_7_b");
		MM.sqlMap.update("v0_7_7_c");
		MM.sqlMap.update("bumpVersion", "0.7.7");
	}
	
	private void ApplyVersion078() throws SQLException {
		
		MM.sqlMap.update("createTableConnectors");
		MM.sqlMap.update("createTableImportDef");
		MM.sqlMap.update("bumpVersion", "0.7.8");
	}
	
	private void ApplyVersion085() throws SQLException {
		
		MM.sqlMap.update("bumpVersion", "0.8.5");
	}
	
}
