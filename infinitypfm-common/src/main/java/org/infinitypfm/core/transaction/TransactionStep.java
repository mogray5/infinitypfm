/*
 * Copyright (c) 2005-2021 Wayne Gray All rights reserved
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
package org.infinitypfm.core.transaction;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;

public interface TransactionStep {

	public void process() throws SQLException ;
	
	public boolean fromBsv();
	
	public boolean isBSV(long actId) throws SQLException;
	
	public boolean isDefault(long actId) throws SQLException;
	
	public boolean fromDefault();
		
	public boolean isValid();
	
	public void setTransaction (Transaction t)  throws SQLException ;
	
	public Transaction getTransaction();
	
	public BigDecimal convert(long amount, long actId) throws SQLException;
	
	public void checkAndConvert(TransactionOffset offset) throws SQLException;

}
