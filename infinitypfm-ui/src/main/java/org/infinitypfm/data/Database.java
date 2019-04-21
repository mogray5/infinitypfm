/*
 * Copyright (c) 2005-2017 Wayne Gray All rights reserved
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.compress.archivers.ArchiveException;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Currency;

public class Database {

	public void build() throws SQLException {

		String[] schemaObjects = new String[] { "createTableAccountTypes",
				"createTableAccounts", "createTableTransactions",
				"createTableMonthlyBalance", "createTableBudget",
				"createTableBudgetDetail", "createTableAppSettings",
				"createTableAccountTemplates", "createTableImportHistory",
				"createTableRecurHeader", "createTableRecurDetail",
				"createFK2RecurDetail",
				"createTableImportRules",
				"createFKTransactions",
				"createFKAccounts",
				"createFKBudgetDetail",
				"createFK2BudgetDetail", "createFKMonthlyBalance",
				"createTableCurrencies",
				"createTableCurrencyMethods",
				"createFKCurrencyMethods", "createTableTrades",
				"createTableBasis", "createFKBasis1", "createFKBasis2",
				"createTableTrades2",
				"createFKTrades1", "createFKTrades2",
				"createFKTrades21", "createFKTrades22","createFKTrades23",
				"createFKImportRules",
				"akTransactions",
				"createTableConnectors",
				"createTableImportDef"
				};

		
			//add schema
			for (int i = 0; i < schemaObjects.length; i++) {

				MM.sqlMap.insert(schemaObjects[i]);

			}
			
			//add version
			MM.sqlMap.insert("addAppSetting", MM.APPVERSION); 
			
			//add account types
			MM.sqlMap.insert("addAccountType", MM.PHRASES.getPhrase("194"));
			MM.sqlMap.insert("addAccountType", MM.PHRASES.getPhrase("206"));
			MM.sqlMap.insert("addAccountType", MM.PHRASES.getPhrase("118"));
			MM.sqlMap.insert("addAccountType", MM.PHRASES.getPhrase("197"));
			
			Account acct = new Account();
			acct.setActTypeName("Expense");
			
			//add template accounts
			acct.setActName(MM.PHRASES.getPhrase("167"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("168"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("169"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("170"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("171"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("172"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("173"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("174"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("175"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("176"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("177"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("178"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("179"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("180"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("181"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("182"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("183"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("184"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("185"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("186"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("187"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("188"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("189"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			
			acct.setActTypeName(MM.PHRASES.getPhrase("190"));
			
			acct.setActName(MM.PHRASES.getPhrase("190"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("191"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("192"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("193"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			
			acct.setActTypeName(MM.PHRASES.getPhrase("194"));
			
			
			acct.setActName(MM.PHRASES.getPhrase("195"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("196"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			

			acct.setActTypeName(MM.PHRASES.getPhrase("197"));
			
			acct.setActName(MM.PHRASES.getPhrase("198"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("199"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("200"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("201"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("202"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("203"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("204"));
			MM.sqlMap.insert("addAccountTemplate", acct);
			acct.setActName(MM.PHRASES.getPhrase("205"));
			MM.sqlMap.insert("addAccountTemplate", acct);

			Currency currency = new Currency();
			currency.setCurrencyName(MM.PHRASES.getPhrase("212"));
			currency.setExchangeRate("1");
			currency.setIsoName(MM.PHRASES.getPhrase("213"));
			MM.sqlMap.insert("addCurrency", currency);
			
			currency.setCurrencyName(MM.PHRASES.getPhrase("217"));
			currency.setExchangeRate(".31");
			currency.setIsoName(MM.PHRASES.getPhrase("218"));
			MM.sqlMap.insert("addCurrency", currency);
			
			currency.setCurrencyID(1);
			
			MM.sqlMap.insert("v0_5_0_z", null);
			
			MM.sqlMap.insert("createFKCurrencies");
			MM.sqlMap.insert("createFKCurrencies2");
			
			// Add default currency methods
			MM.sqlMap.insert("v0_7_0_d");
			MM.sqlMap.insert("v0_7_0_e");
			
	}
	
	public String backup(String dir) throws IOException, ArchiveException{
		
		if (dir != null) {
		
			
			Connection conn = null;
			Statement st = null;
			
			try {
				
				Calendar c1 = Calendar.getInstance(); 
				String DATE_FORMAT = "yyyyMMdd";
			    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			    String backupFile = "/infinitypfm_backup_" + 
						sdf.format(c1.getTime()) + ".tar.gz";
			    
				
			    conn = MM.sqlMap.getDataSource().getConnection();
				st = conn.createStatement();
				st.executeUpdate("BACKUP DATABASE TO '" + dir + backupFile + "' BLOCKING");
				
			} catch (SQLException e) {
				InfinityPfm.LogMessage(e.getMessage());
			} finally {
				try {
					st.close();
					st = null;
				} catch (SQLException e) {
					InfinityPfm.LogMessage(e.getMessage(), true);
				}
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					InfinityPfm.LogMessage(e.getMessage(), true);
				}
				
			}
			
		}
					
		return dir;
		
	}
	
}
