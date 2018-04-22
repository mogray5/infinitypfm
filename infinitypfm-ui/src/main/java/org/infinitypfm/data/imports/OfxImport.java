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

public class OfxImport extends BaseImport {
	
	public OfxImport() {
		super();
	}

	public List<Transaction> ImportFile(ImportConfig config) throws IOException, ParseException, ConfigurationException
	{
			
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
				if (l=='<'){
					startElement = true;
					inTag=false;
					if (tran != null){
						SaveElement(element, val, tran);
					}
					
					val = "";
					element = "";
					
				} else if (l!='>' && startElement){
					element += String.valueOf(l);
				} else if (l=='>' && startElement){
				  startElement = false;
				  inTag = true;
				  if (element.equalsIgnoreCase(FLD_STMTTRN)){
				  	tran = new Transaction();
				  	tranList.add(tran);
				  }
				  
				} else if (!startElement && inTag){
					if (l != '\n' && l != '\r'){
						val += String.valueOf(l);	
					}
					
				}
			}
			
			return tranList;

		}
	
	private void SaveElement(String element, String val, Transaction tran) throws ParseException{
	
		if (element.equalsIgnoreCase(FLD_TRNAMT)){
			
			tran.setTranAmount(DataFormatUtil.moneyToLong(val));
			
		} else if (element.equalsIgnoreCase(FLD_MEMO) || 
				element.equalsIgnoreCase(FLD_NAME) ||
				element.equalsIgnoreCase(FLD_FITID)){
			
			if (!tran.getTranMemo().trim().contains(val))
				tran.setTranMemo(val);
			
		} else if (element.equalsIgnoreCase(FLD_DTPOSTED)){
			
			DateFormat dateFmt = new SimpleDateFormat("yyyyMMddhhmmss");
			
				Date dt = dateFmt.parse(val);
				tran.setTranDate(dt);
			
			
		} else if (element.equalsIgnoreCase(FLD_CHECKNUM)){
			tran.setTranMemo(MM.PHRASES.getPhrase("237") + " " + val);
		}
	}

}
