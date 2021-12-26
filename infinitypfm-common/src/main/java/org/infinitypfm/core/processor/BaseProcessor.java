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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.infinitypfm.core.conf.LangLoader;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.MonthlyBalance;
import org.infinitypfm.core.data.Trade;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;
import org.infinitypfm.core.exception.TransactionException;
/**
 * 
 * Original double entry processor.  Keeping around
 * to fall back to or extend if needed.
 *
 */
public class BaseProcessor implements TransactionProcessor {

	private ProcessorCallback _callback;
	private LangLoader _language;
	private SqlSession _session;
	private DataFormatUtil _formatter = null;
	
	public BaseProcessor(int precision)
	{
		_formatter = new DataFormatUtil(precision);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void AddTransaction(Transaction tran, boolean saveMemo) throws SQLException, TransactionException {
		Transaction tranOffset = null;
		Trade trade = null;
		BigDecimal newAmount = null;
		TransactionOffset offset = null;

		try {
			
			_callback.LogMessage(_language.getPhrase("79") + " " + tran.getTranMemo());
			
			boolean isExchange = tran.getExchangeRate() != null && !tran.getExchangeRate().equals("1");
			
			if (!offsetsValid(tran.getOffsets(), tran.getTranAmount())) {
				_callback.LogMessage(_language.getPhrase("234"));
				return;
			}
			
			// use MM.sqlTransactionMap
			
			try {
			
				for (int i=0; i<tran.getOffsets().size(); i++){
					offset = (TransactionOffset)tran.getOffsets().get(i);
					tranOffset = new Transaction();
					tranOffset.setActId(offset.getOffsetId());
					
					if (isExchange){
						
						newAmount = _formatter.strictMultiply(tran.getExchangeRate(), Long.toString(offset.getOffsetAmount()));
						
						tranOffset.setTranAmount(newAmount.longValue());
					
					} else {
						tranOffset.setTranAmount(offset.getOffsetAmount());
					}
					
					tranOffset.setTranMemo(tran.getTranMemo());
					tranOffset.setTranDate(tran.getTranDate());
					tranOffset.setTransactionKey(tran.getTransactionKey());
					
					_session.insert("insertTransaction", tranOffset);
					_session.insert("updateAccountBalance", tranOffset);
					
					UpdateMonthlyBalance(tranOffset);
					
				}
				
				_session.insert("insertTransaction", tran);
				_session.insert("updateAccountBalance", tran);
				UpdateMonthlyBalance(tran);
				
				Transaction memoResult = null;
	
				if (saveMemo) {
	
					memoResult = (Transaction) _session.selectOne(
							"getSavedMemo", tranOffset);
					if (memoResult == null) {
						_session.insert("insertMemo", tranOffset);
					}
				}
				
				_session.commit();
		
			} catch (Exception e) {
				try {_session.rollback();} catch (Exception er) {}
			}
			
			if (isExchange){
				try {
					Account account = (Account)_session.selectOne("getAccountById", tran);
					Account offsetAccount = (Account)_session.selectOne("getAccountForOffset", tran);
					
					tran = (Transaction)_session.selectOne("getLastTransactionByAccount", account.getActId());
					
					trade = new Trade();
					trade.setTranId(tran.getTranId());
					trade.setTranDate(tran.getTranDate());
					trade.setAmount(tran.getTranAmount());
					trade.setCurrencyID(account.getCurrencyID());
					
					_session.insert("insertTrade", trade);
					
					trade.setCurrencyID(offsetAccount.getCurrencyID());
					trade.setAmount(newAmount.longValue());
					
					_session.insert("insertTrade", trade);
					
					_session.commit();
				
				} catch (Exception e) {
					try {_session.rollback();} catch (Exception er) {}
				}				
			}

		} finally {
			try {_session.commit();} catch (Exception se) {}
		}
	}

	@Override
	public void setSession(SqlSession session) {
		_session = session;
	}

	@Override
	public void setLanguage(LangLoader language) {
		_language = language;
	}

	@Override
	public void setMessageCallback(ProcessorCallback callback) {
		_callback = callback;
	}

	@Override
	public void UpdateMonthlyBalance(Transaction tran) throws SQLException {

		MonthlyBalance bal = (MonthlyBalance) _session.selectOne(
				"getMonthlyBalance", tran);

		if (bal != null) {
			_session.update("updateMonthlyBalance", tran);
		} else {
			// create new month
			_session.insert("insertMonthlyBalance", tran);
		}
	}

	@Override
	public boolean offsetsValid(ArrayList<TransactionOffset> offsets, long transactionAmount) {
	TransactionOffset offset = null;
		
		if (offsets == null) {
			return false;
		} else {
			
			long compareAmount = 0;
			
			for (int i=0; i<offsets.size(); i++){
				
				offset = (TransactionOffset)offsets.get(i);
				compareAmount += offset.getOffsetAmount();
				
			}
			
			if ((compareAmount + transactionAmount) == 0) {
				return true;
			}
			
			// If a offset id is specified but offset amount is zero then use 
			// -Transaction amount as the offset, otherwise return false (invalid)
			
			if (offsets.size()==1){
				
				offset = (TransactionOffset)offsets.get(0);
				if (offset.getOffsetId()>0 && offset.getOffsetAmount()==0){
					offset.setOffsetAmount(-transactionAmount);
					return true;
				}
			}
		}
		
		return false;
	}
}
