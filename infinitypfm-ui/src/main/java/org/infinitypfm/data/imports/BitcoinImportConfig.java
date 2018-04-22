/*
 * Copyright (c) 2005-2018 Wayne Gray All rights reserved
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
package org.infinitypfm.data.imports;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.exception.ConfigurationException;
import org.infinitypfm.ui.view.dialogs.PasswordDialog;
import org.infinitypfm.util.FileHandler;

public class BitcoinImportConfig implements ImportConfig {

	@Override
	public void config() throws ConfigurationException {

		if (MM.bitcoinUser == null){

			//Check if bitcoin.conf exists
			FileHandler fileHandler = new FileHandler();
			
			try {
				fileHandler.FileOpenRead(System.getProperty("user.home") + "/.bitcoin/bitcoin.conf");
				String temp = fileHandler.LineInput();
				while (temp!=null){
					
					if (MM.bitcoinUser!=null && MM.bitcoinPwd != null) break;
					
					if (temp.startsWith("rpcuser")){
						MM.bitcoinUser = temp.split("=")[1].trim();
					} else if (temp.startsWith("rpcpassword")){
						MM.bitcoinPwd = temp.split("=")[1].trim();
					}
					
					temp = fileHandler.LineInput();
				}

				fileHandler.FileClose();
				
			} catch (FileNotFoundException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			} catch (IOException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			}
			
			//Show a dialog if bitcoin.conf was not used
			if (MM.bitcoinUser==null || MM.bitcoinPwd==null){
			
				PasswordDialog pwdDlg = new PasswordDialog();
				
				String[] creds = pwdDlg.getCredentials();
				
				if (creds.length==2){
				
					MM.bitcoinUser = creds[0];
					MM.bitcoinPwd = creds[1];
				}
			}
		
			if (MM.bitcoinUser==null || MM.bitcoinUser.length()==0){
				throw new ConfigurationException(MM.PHRASES.getPhrase("251"));
			}	
		}
	}
}
