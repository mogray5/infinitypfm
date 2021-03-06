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
import java.text.ParseException;
import java.util.List;

import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.exception.ConfigurationException;



/**
 * @author Wayne Gray
 */
public abstract class BaseImport {

	public static final String FLD_CHECKNUM = "CHECKNUM";
	public static final String FLD_DTPOSTED = "DTPOSTED";
	public static final String FLD_FITID = "FITID";
	public static final String FLD_NAME = "NAME";
	public static final String FLD_MEMO = "MEMO";
	public static final String FLD_STMTTRN = "STMTTRN";
	public static final String FLD_TRNAMT = "TRNAMT";
	
	public BaseImport() {
		super();
	}
	
	/**
	 * Perform the import from the passed file path.  What to do with the data is handled
	 * in sub classes.
	 * 
	 * @param sFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public abstract List<Transaction> ImportFile(ImportConfig config) throws FileNotFoundException, IOException, ParseException, ConfigurationException;
	
   }
    
    

	
