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
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.ImportDef;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.exception.ConfigurationException;
import org.infinitypfm.ui.view.dialogs.ImportDefSelector;

public class CsvImport extends BaseImport {

	@Override
	public List<Transaction> ImportFile(ImportConfig config) throws FileNotFoundException, IOException, ParseException {
		
		List<Transaction> resultList = new ArrayList<Transaction>();
		
		try {
			
			// Get the file path
			config.config();
			// Get the import def
			ImportDefSelector defSelect = new ImportDefSelector();
			ImportDef param = new ImportDef();
			param.setImportID(defSelect.Open());
			
			ImportDef def = (ImportDef) MM.sqlMap.queryForObject("getImportDef", param);
			
			if (def == null) return null;
			
			DataFormatUtil dataUtils = new DataFormatUtil();
			
			Reader in = new FileReader(MM.importFile);
		
			final CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader());
			
			//Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
			
			try {
			    for (final CSVRecord record : parser) {
			        
				    String memo = record.get(def.getMemoField());
				    long amount = DataFormatUtil.moneyToLong(record.get(def.getAmountField()));
				    
				    dataUtils.setDate(record.get(def.getDateField()), def.getDateFormat());
				    Date dt = dataUtils.getDate();
				    
				    Transaction t = new Transaction();
				    t.setTranAmount(amount);
				    t.setTranMemo(memo);
				    t.setTranDate(dt);
				    
				    resultList.add(t);
			    }
			} finally {
			    parser.close();
			    in.close();
			}
			
					
		} catch (ConfigurationException | SQLException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
		} 
		
		return resultList;
	}

	
	
}
