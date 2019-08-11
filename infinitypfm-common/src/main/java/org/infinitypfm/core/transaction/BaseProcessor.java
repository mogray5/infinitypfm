package org.infinitypfm.core.transaction;

import java.sql.SQLException;
import java.util.ArrayList;

import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.MonthlyBalance;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;

import com.ibatis.sqlmap.client.SqlMapClient;

public class BaseProcessor implements TransactionProcessor {

	protected Transaction _transaction = null;
	protected Account _account = null;
	protected Account _bsvWallet = null;
	protected ArrayList<TransactionOffset> _offsets = null;
	protected Currency _bsvCurrency = null;
	protected Currency _defaultCurrency = null;
	protected DataFormatUtil _formatter = null;
	
	protected SqlMapClient _map = null;
	
	public BaseProcessor(SqlMapClient map) {
		
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
		
		Account account = (Account)_map.queryForObject("getAccountByActId", actId);
		
		return account != null && 
				account.getCurrencyID() == _bsvCurrency.getCurrencyID();
	}
	
	@Override
	public boolean isDefault(long actId) throws SQLException {
		
		if (!isValid()) return false;
		
		Account account = (Account)_map.queryForObject("getAccountByActId", actId);
		
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
			_account = (Account)_map.queryForObject("getAccountById", t);
			
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
				_bsvCurrency = (Currency)_map.queryForObject("getCurrencyByName", "Bitcoin SV");
			
			if (_defaultCurrency == null)
				_defaultCurrency = (Currency)_map.queryForObject("getCurrencyDefault");
			
			if (_bsvWallet == null)
				_bsvWallet = (Account)_map.queryForObject("getAccountForName", "Bitcion SV Wallet");
			
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
		
		MonthlyBalance bal = (MonthlyBalance) _map.queryForObject(
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
	
}
