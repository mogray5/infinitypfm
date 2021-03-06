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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.apache.commons.io.IOUtils;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.exception.ConfigurationException;

public class QifImport extends BaseImport {

	
	public QifImport() {
		super();
	}


	public List<Transaction> ImportFile(ImportConfig config) throws IOException, ParseException, ConfigurationException {
		
		boolean startElement = false;
		boolean inTag =  false;
		
		String element = "";
		String val = "";
		Transaction tran = null;
		ArrayList<Transaction> tranList = new ArrayList<Transaction>();
		
		config.config();
		
		if (MM.importFile==null){return null;}
		
		InputStream in = new BufferedInputStream(
	            new FileInputStream(MM.importFile));
		
		String body = IOUtils.toString(in);
		char letters[] = body.toCharArray();
		char l;
		for (int i=0; i<letters.length; i++){
			l = letters[i];
			if (l=='\n'){
				startElement = true;
				inTag=false;
				if (tran != null){
					SaveElement(element, val, tran);
				}
				
				val = "";
				element = "";
				
			} else if ((l=='D' || l=='T' || l=='N' || l=='P') && startElement){
			  startElement = false;
			  element = String.valueOf(l);				  
			  inTag = true;
			  
			  if (tran==null){
				  tran = new Transaction();
				  tranList.add(tran);
			  }
			  				  
			} else if (!startElement && inTag){
				if (l != '\n' && l != '\r'){
					val += String.valueOf(l);	
				}
				
			} else if (l=='^'){
			  	tran = null;					
			}
			
		}
		
		return tranList;
			
	}
	
	private void SaveElement(String element, String val, Transaction tran) throws ParseException{
	
		if (element.equalsIgnoreCase("T")){
			
			tran.setTranAmount(DataFormatUtil.moneyToLong(val));
			
		} else if (element.equalsIgnoreCase("P")){
			tran.setTranMemo(val);

		} else if (element.equalsIgnoreCase("D")){
			DateFormat dateFmt = new SimpleDateFormat("M/dd/yyyy");
			
		Date dt = dateFmt.parse(val);
		tran.setTranDate(dt);
			
		}
	}

}
