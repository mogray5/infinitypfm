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
package org.infinitypfm.core.processor;

import java.sql.SQLException;
import java.util.ArrayList;

import org.infinitypfm.core.conf.LangLoader;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;
import org.infinitypfm.core.exception.TransactionException;
import org.apache.ibatis.session.SqlSession;

/**
 * Accounting functions must adhere to this interface.
 * Useful for phasing in new accounting functions and allow
 * roll-back to previous implementations.
 *
 */
public interface TransactionProcessor {
	/**
	 * Add a double entry transaction to the database 
	 * @param tran  Transaction object containing data to add
	 * @param saveMemo  Flag to save the memo field with the transaction.
	 * @throws SQLException
	 * @throws TransactionException
	 */
	public void AddTransaction(Transaction tran, boolean saveMemo)
			throws SQLException, TransactionException;
	
	/**
	 * Update maintained monthly balance value with passed transaction
	 * @param tran
	 * @throws SQLException
	 */
	public void UpdateMonthlyBalance(Transaction tran) throws SQLException;
	
	/**
	 * Mybatis session to use when adding data to DB
	 * @param session
	 */
	public void setSession(SqlSession session);
	
	/**
	 * Set language strings to use for return results and messages
	 * @param language
	 */
	public void setLanguage(LangLoader language);
	
	/**
	 * Callback for sending console messages back to UI
	 * @param callback
	 */
	public void setMessageCallback(ProcessorCallback callback);
	
	/**
	 * Checking function to make sure offsets in a transactions are defined correctly. 
	 * For split transactions, the total offset must match the transaction amount.
	 * @param offsets
	 * @param transactionAmount
	 * @return
	 */
	public boolean offsetsValid(ArrayList<TransactionOffset> offsets, long transactionAmount);
	
}
