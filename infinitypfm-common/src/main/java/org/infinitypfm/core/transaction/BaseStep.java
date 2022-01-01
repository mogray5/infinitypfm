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
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.MonthlyBalance;
import org.infinitypfm.core.data.NumberFormat;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;


public class BaseStep implements TransactionStep {

	protected Transaction _transaction = null;
	protected Account _account = null;
	protected Account _bsvWallet = null;
	protected ArrayList<TransactionOffset> _offsets = null;
	protected Currency _bsvCurrency = null;
	protected Currency _defaultCurrency = null;
	protected DataFormatUtil _formatter = null;
	
	protected SqlSession _map = null;
	
	public BaseStep(SqlSession map) {
		
		// Map is required to lookup account and currency information
		if (map == null)
			throw new NullPointerException();
		
		_formatter = new DataFormatUtil();
		_formatter.setPrecision(8);
		
		_map = map;
	}
	
	@Override
	public boolean fromBsv() {

		if (!isValid()) return false;
		
		return _account != null && 
				_account.getCurrencyID() == _bsvCurrency.getCurrencyID();
	}

	@Override
	public boolean isBSV(long actId) throws SQLException {
		
		if (!isValid()) return false;
		
		Account account = (Account)_map.selectOne("getAccountByActId", actId);
		
		return account != null && 
				account.getCurrencyID() == _bsvCurrency.getCurrencyID();
	}
	
	@Override
	public boolean isDefault(long actId) throws SQLException {
		
		if (!isValid()) return false;
		
		Account account = (Account)_map.selectOne("getAccountByActId", actId);
		
		return account != null && 
				account.getCurrencyID() == _defaultCurrency.getCurrencyID();
	}

	@Override
	public void process() throws SQLException {
		
		// Once processed set transaction and accounts to null so it
		// can not be processed again without caller re-populating it.
		_transaction = null;
		_account = null;
		_offsets = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTransaction(Transaction t) throws SQLException {
		_transaction = t;

		// Look up accounts involved in transaction and also BSV currency if not already populated
		if (t != null) {
			_account = (Account)_map.selectOne("getAccountById", t);
			
			// If transaction is using single offset interface then convert to offset array
			if (t.getActOffset() > 0 && t.getOffsets() == null) {
				TransactionOffset tranOffset = new TransactionOffset();
				tranOffset.setOffsetId(t.getActOffset());
				tranOffset.setOffsetName(t.getActOffsetName());
				tranOffset.setOffsetAmount(t.getTranAmount() * -1L );
				_offsets = new ArrayList<TransactionOffset>();
				_offsets.add(tranOffset);
			} else {
				// Use the offsets array from the transaction
				_offsets = t.getOffsets();
			}
			
			if (_bsvCurrency == null)
				_bsvCurrency = (Currency)_map.selectOne("getCurrencyByName", "Bitcoin SV");
			
			if (_defaultCurrency == null)
				_defaultCurrency = (Currency)_map.selectOne("getCurrencyDefault");
			
			if (_bsvWallet == null)
				_bsvWallet = (Account)_map.selectOne("getAccountForName", "Bitcion SV Wallet");
			
		}
	}

	@Override
	public Transaction getTransaction() {
		return _transaction;
	}

	@Override
	public boolean isValid() {
		
		if (_account != null) 
			if (_transaction != null)
				if (_transaction.getOffsets() != null || _transaction.getActOffset() > 0)
					if (_bsvCurrency != null)
						return true;
		
		return false;
	}

	protected void UpdateMonthlyBalance(Transaction tran) throws SQLException {

		if (!isValid()) return;
		
		MonthlyBalance bal = (MonthlyBalance) _map.selectOne(
				"getMonthlyBalance", tran);

		if (bal != null) {
			_map.update("updateMonthlyBalance", tran);
		} else {
			// create new month
			_map.insert("insertMonthlyBalance", tran);
		}
	}

	@Override
	public boolean fromDefault() {
		
		if (!isValid()) return false;
		
		return _account.getCurrencyID() == _defaultCurrency.getCurrencyID();
	}

	@Override
	public BigDecimal convert(long amount, long actId) throws SQLException {
		
		BigDecimal newOffsetAmount = null;
		String numFormat = NumberFormat.getNoCommaNoParems(8);
		
		// Perform currency conversion if going to/from Default/BSV
		if (this.isBSV(actId)) {
			
			newOffsetAmount = this._formatter.strictDivide(this._transaction.getExchangeRate(), 
					_formatter.getAmountFormatted(amount, numFormat), 8);
			
		} else if (this.isDefault(actId)) {
			
			newOffsetAmount = this._formatter.strictMultiply(this._transaction.getExchangeRate(), 
					_formatter.getAmountFormatted(amount, numFormat));
			
		} 
	
		return newOffsetAmount;
	}

	@Override
	public void checkAndConvert(TransactionOffset offset) throws SQLException {
		
		if (offset.needsConversion()) {
			
			offset.setOffsetAmount(DataFormatUtil.moneyToLong(this.convert(offset.getOffsetAmount(), offset.getOffsetId())));
			offset.setNeedsConversion(false);
			
		}		
	}
	
}
