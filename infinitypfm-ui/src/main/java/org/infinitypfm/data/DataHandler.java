/*
 * Copyright (c) 2005-2019 Wayne Gray All rights reserved
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

package org.infinitypfm.data;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Basis;
import org.infinitypfm.core.data.Budget;
import org.infinitypfm.core.data.BudgetDetail;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.MonthlyBalance;
import org.infinitypfm.core.data.RecurDetail;
import org.infinitypfm.core.data.RecurHeader;
import org.infinitypfm.core.data.Trade2;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;
import org.infinitypfm.exception.AccountException;
import org.infinitypfm.exception.TransactionException;

/**
 * 
 * Collection of common data manipulation functions.
 * 
 */
public class DataHandler {

	DataFormatUtil formatter = null;
	
	/**
	 * 
	 */
	public DataHandler() {
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
	}

	public void AddAccount(Account act) throws AccountException, SQLException {

		Integer id = GetAccountType(act);

		act.setActTypeId(id.intValue());
		MM.sqlMap.insert("insertAccount", act);
	}

	public void EditAccount(Account act) throws AccountException, SQLException {

		try {
			
			Account newName = (Account) MM.sqlMap.queryForObject("getAccountForName", act.getActName());
			
			if (newName != null){
				throw new AccountException(MM.PHRASES.getPhrase("247"));
			}
			
		} catch (SQLException e) {
			throw e;
		}

		Integer id = GetAccountType(act);
		
		act.setActTypeId(id.intValue());
		MM.sqlMap.update("updateAccountName", act);
	}
	
	private Integer GetAccountType(Account act) throws AccountException, SQLException {
		
		// check account name
		if (act.getActName() == null || act.getActName().length() == 0) {
			throw new AccountException(MM.PHRASES.getPhrase("39"));
		}

		// lookup type
		Integer id = (Integer) (MM.sqlMap.queryForObject(
				"getAccountTypeIdForName", act.getActTypeName()));

		if (id == null) {
			throw new AccountException(MM.PHRASES.getPhrase("40"));
		}
	
		return id;
		
	}
	
	public void DeleteAccount(Account act) throws AccountException,
			SQLException {

		Account account = null;

		account = (Account) MM.sqlMap.queryForObject("getAccountForName",
				act.getActName());
		;

		if (account != null) {

			// delete if only if balance is zero
			if (account.getActBalance() == 0) {

				MM.sqlMap.delete("deleteAccount", account);

			} else {

				throw new AccountException(MM.PHRASES.getPhrase("50"));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void AddTransaction(Transaction tran, boolean saveMemo)
			throws SQLException, TransactionException {

		Transaction tranOffset = null;
		Trade2 trade = null;
		BigDecimal newAmount = null;
		TransactionOffset offset = null;
		Account offsetAccount = null;
		Account account = null;
		Currency actCurrency = null;
		Currency offsetCurrency = null;

		try {
			
			InfinityPfm.LogMessage(MM.PHRASES.getPhrase("79") + " " + tran.getTranMemo());
			
			boolean isExchange = false;
			boolean isDefaultToOther = false;
			boolean isOtherToDefault = false;
			
			if (!offsetsValid(tran.getOffsets(), tran.getTranAmount())) {
				InfinityPfm.LogMessage(MM.PHRASES.getPhrase("234"));
				return;
			}
			
			MM.sqlMap.startTransaction();
			
			for (int i=0; i<tran.getOffsets().size(); i++){
				offset = (TransactionOffset)tran.getOffsets().get(i);
				tranOffset = new Transaction();
				tranOffset.setActId(offset.getOffsetId());

				offsetAccount = (Account)MM.sqlMap.queryForObject("getAccountForOffset", tran);
				account = (Account)MM.sqlMap.queryForObject("getAccountById", tran);
				
				actCurrency = (Currency) MM.sqlMap.queryForObject("getCurrencyById", account.getCurrencyID());
				offsetCurrency = (Currency) MM.sqlMap.queryForObject("getCurrencyById", offsetAccount.getCurrencyID());
				
				isExchange =  account.getCurrencyID() != offsetAccount.getCurrencyID();
				
				isDefaultToOther = account.getCurrencyID() == MM.options.getDefaultCurrencyID() && 
						offsetAccount.getCurrencyID() != MM.options.getDefaultCurrencyID();
				
				isOtherToDefault = account.getCurrencyID() != MM.options.getDefaultCurrencyID() && 
						offsetAccount.getCurrencyID() == MM.options.getDefaultCurrencyID(); 
				
				if (isExchange){
					
					if (isDefaultToOther) {
						newAmount = formatter.strictMultiply(tran.getExchangeRate(), Long.toString(offset.getOffsetAmount()));
						tranOffset.setTranAmount(newAmount.longValue());
					} else if (isOtherToDefault) {
						newAmount = formatter.strictDivide(tran.getExchangeRate(), Long.toString(offset.getOffsetAmount()), MM.MAX_PRECISION);
						tranOffset.setTranAmount(newAmount.longValue());
					}
				
				} else {
					tranOffset.setTranAmount(offset.getOffsetAmount());
				}
				
				tranOffset.setTranMemo(tran.getTranMemo());
				tranOffset.setTranDate(tran.getTranDate());
				tranOffset.setTransactionKey(tran.getTransactionKey());
				
				MM.sqlMap.insert("insertTransaction", tranOffset);
				MM.sqlMap.insert("updateAccountBalance", tranOffset);
				UpdateMonthlyBalance(tranOffset);
			}
			
			MM.sqlMap.insert("insertTransaction", tran);
			MM.sqlMap.insert("updateAccountBalance", tran);
			UpdateMonthlyBalance(tran);

			Transaction memoResult = null;

			if (saveMemo) {

				memoResult = (Transaction) MM.sqlMap.queryForObject(
						"getSavedMemo", tranOffset);
				if (memoResult == null) {
					MM.sqlMap.insert("insertMemo", tranOffset);
				}
			}
			
			MM.sqlMap.commitTransaction();
			MM.sqlMap.endTransaction();
			
			// Add to basis if receiving cc funds
			if (isOtherToDefault && tran.getTranAmount() > 0) 
				addBasis(actCurrency, offsetCurrency, tran, tran.getTranAmount(), tranOffset.getTranAmount());
			else if (isDefaultToOther && tranOffset.getTranAmount() > 0)
				addBasis(offsetCurrency, actCurrency, tran,  tranOffset.getTranAmount(), tran.getTranAmount());

			long basisAmountLifo = 0, basisAmountFifo = 0, tradeAmount=0;
			Currency basisCurrency = null;
			
			// Subtract from basis if spending cc funds
			if (isOtherToDefault && tran.getTranAmount() < 0 || isDefaultToOther && tranOffset.getTranAmount() < 0) {
				
				long spendQtyLifo=0, spendQtyFifo=0;
				
				Basis basis = null;
				
				if (isOtherToDefault) {
					spendQtyLifo = tran.getTranAmount();
					spendQtyFifo = tran.getTranAmount();
					tradeAmount = tranOffset.getTranAmount();
					basisCurrency = actCurrency;
				}
				else {
					spendQtyLifo = tranOffset.getTranAmount();
					spendQtyFifo  = tranOffset.getTranAmount();
					tradeAmount = tran.getTranAmount();
					basisCurrency = offsetCurrency;
				}
				
				// Lifo
				while (spendQtyLifo > 0) {
				
					if (basis == null || basis.getQtyLifo() ==0) 
						basis = (Basis) MM.sqlMap.queryForObject("getLifoBasis", basisCurrency);
					
					if (basis == null || basis.getQtyLifo() == 0) break;
					
					if (basis.getQtyLifo() < spendQtyLifo) {
						
						BigDecimal amt = formatter.strictMultiply(formatter.getAmountFormatted(basis.getQtyLifo()), 
								formatter.getAmountFormatted(basis.getCost()));

						basisAmountLifo += DataFormatUtil.moneyToLong(amt);
						
						spendQtyLifo = spendQtyLifo - basis.getQtyLifo();
						basis.setQtyLifo(0);
						
					} else {
						
						BigDecimal amt = formatter.strictMultiply(formatter.getAmountFormatted(spendQtyLifo), 
								formatter.getAmountFormatted(basis.getCost()));

						basisAmountLifo += DataFormatUtil.moneyToLong(amt);
						
						basis.setQtyLifo(basis.getQtyLifo() - spendQtyLifo);
						spendQtyLifo = 0;
					}
					
					MM.sqlMap.update("updateLifo", basis);
					
				}

				// Fifo
				while (spendQtyFifo > 0) {
				
					if (basis == null || basis.getQtyFifo() ==0) 
						basis = (Basis) MM.sqlMap.queryForObject("getFifoBasis", basisCurrency);
					
					if (basis == null || basis.getQtyFifo() == 0) break;
					
					if (basis.getQtyFifo() < spendQtyFifo) {
						
						BigDecimal amt = formatter.strictMultiply(formatter.getAmountFormatted(basis.getQtyFifo()), 
								formatter.getAmountFormatted(basis.getCost()));

						basisAmountFifo += DataFormatUtil.moneyToLong(amt);
						
						spendQtyFifo = spendQtyFifo - basis.getQtyFifo();
						basis.setQtyFifo(0);
					} else {
						
						BigDecimal amt = formatter.strictMultiply(formatter.getAmountFormatted(spendQtyFifo), 
								formatter.getAmountFormatted(basis.getCost()));

						basisAmountFifo += DataFormatUtil.moneyToLong(amt);
						
						basis.setQtyFifo(basis.getQtyFifo() - spendQtyFifo);
						spendQtyLifo = 0;
					}
					
					MM.sqlMap.update("updateFifo", basis);
					
				}
			}
			
			// Post a trade if an exchange being made.  Use calculated basis
			
			if (isExchange){
				
				MM.sqlMap.startTransaction();
				
				tran = (Transaction)MM.sqlMap.queryForObject("getLastTransactionByAccount", account.getActId());
				
				trade = new Trade2();
				trade.setTranId(tran.getTranId());
				trade.setTranDate(tran.getTranDate());
				trade.setBasisFifo(basisAmountFifo);
				trade.setBasisLifo(basisAmountLifo);
				trade.setToAmount(tradeAmount);
				trade.setFromCurrencyID(basisCurrency == null ? MM.options.getDefaultBsvCurrencyID() : basisCurrency.getCurrencyID());
				trade.setToCurrencyID(MM.options.getDefaultCurrencyID());
				
				MM.sqlMap.insert("insertTrade2", trade);
				
				MM.sqlMap.commitTransaction();
				
			}
			
		} finally {
			try {
				MM.sqlMap.endTransaction();
			} catch (SQLException se) {
			}
		}
	}
	
	/**
	 * Add an entry to table bases to keep track of cost basis needed for capital gains/loss estimation when
	 * spending a digital currency that increased/decreased in value.
	 * 
	 * @param aquireCurrency
	 * @param costCurrency
	 * @param tran
	 * @param qty
	 * @param cost
	 */
	public void addBasis(Currency aquireCurrency, Currency costCurrency, Transaction tran, long qty, long cost) {
		
		try {
			MM.sqlMap.startTransaction();
			Basis basis = new Basis();
			basis.setAquireCurrencyID(aquireCurrency.getCurrencyID());
			basis.setAquireDate(tran.getTranDate());
			basis.setCost(cost);
			basis.setCostCurrencyID(costCurrency.getCurrencyID());
			basis.setTranId(tran.getTranId());
			basis.setQtyFifo(qty);
			basis.setQtyLifo(qty);
			MM.sqlMap.commitTransaction();
			MM.sqlMap.endTransaction();
			MM.sqlMap.insert("insertBasis", basis);
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
	}

	public void addTrade(Transaction transaction) {
		
		
		
	}
	
	public void AddTransactionBatch(Object tran[]) throws SQLException, TransactionException {

		Transaction transaction = null;

		for (int i = 0; i < tran.length; i++) {
			
			transaction = (Transaction) tran[i];
			this.AddTransaction(transaction, true);

		}
	}

	@SuppressWarnings("rawtypes")
	public void AddBudget(String sBudget) throws SQLException {
		Budget budget = new Budget();
		budget.setBudgetName(sBudget);
		List actList = null;

		// add the budget
		MM.sqlMap.insert("insertBudget", budget);
		// get the budget id
		budget = (Budget) MM.sqlMap.queryForObject("getBudget", sBudget);

		if (budget != null) {

			actList = MM.sqlMap.queryForList("getAccountsForType",
					MM.ACT_TYPE_LIABILITY);
			AddBudgetDetailBatch(budget.getBudgetId(), actList);
			actList = MM.sqlMap.queryForList("getAccountsForType",
					MM.ACT_TYPE_EXPENSE);
			AddBudgetDetailBatch(budget.getBudgetId(), actList);

		}

	}

	@SuppressWarnings("rawtypes")
	private void AddBudgetDetailBatch(int budgetId, List actList)
			throws SQLException {

		BudgetDetail detail = new BudgetDetail();
		Account act = null;

		if (actList != null) {
			for (int i = 0; i < actList.size(); i++) {
				act = (Account) actList.get(i);
				for (int j = 1; j < 13; j++) {
					// create and insert budget detail
					detail.setActId(act.getActId());
					detail.setMth(j);
					detail.setAmount(0);
					detail.setBudgetId(budgetId);
					MM.sqlMap.insert("insertBudgetDetail", detail);
				}
			}
		}

	}
	
	public void AddAccountToBudget(Budget budget, Account account)
			throws SQLException {

		// create and insert budget detail
		BudgetDetail budgetDetail = new BudgetDetail();
		budgetDetail.setBudgetId(budget.getBudgetId());
		budgetDetail.setActId(account.getActId());
		budgetDetail.setAmount(0);

		for (int j = 1; j < 13; j++) {
			;
			budgetDetail.setMth(j);

			MM.sqlMap.insert("insertBudgetDetail", budgetDetail);
		}

	}

	public void AddBudgetDetail(BudgetDetail detail) throws SQLException {

		MM.sqlMap.insert("insertBudgetDetail", detail);

	}

	public void DeleteBudgetDetail(BudgetDetail detail) throws SQLException {

		MM.sqlMap.delete("deleteAccountFromBudget", detail);

	}

	public void UpdateMonthlyBalance(Transaction tran) throws SQLException {

		MonthlyBalance bal = (MonthlyBalance) MM.sqlMap.queryForObject(
				"getMonthlyBalance", tran);

		if (bal != null) {
			MM.sqlMap.update("updateMonthlyBalance", tran);
		} else {
			// create new month
			MM.sqlMap.insert("insertMonthlyBalance", tran);
		}

	}

	@SuppressWarnings("rawtypes")
	public void SyncBudgetMonth(Budget budget, int mth) throws SQLException {

		BudgetDetail budgetDetail = new BudgetDetail();
		budgetDetail.setBudgetId(budget.getBudgetId());
		budgetDetail.setMth(mth);
		List actList = null;

		actList = MM.sqlMap.queryForList("getBudgetDetailForMonth",
				budgetDetail);

		if (actList != null) {

			for (int i = 0; i < actList.size(); i++) {

				budgetDetail = (BudgetDetail) actList.get(i);
				MM.sqlMap.update("updateAccountForBudget", budgetDetail);

			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void ProcessRecurringTransaction() throws SQLException {

		Calendar calendar = new GregorianCalendar();

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);

		List trans = MM.sqlMap.queryForList("getPastDueRecurringTransactions",
				calendar.getTime());

		if (trans != null) {
			for (int i = 0; i < trans.size(); i++) {

				RecurHeader recurHeader = (RecurHeader) trans.get(i);
				formatter.setDate(recurHeader.getNextRunDate());

				while (formatter.getDate().before(formatter.getToday())) {
					RecurDetail detail = new RecurDetail();
					detail.setTranDate(formatter.getDate());
					detail.setRecurId(recurHeader.getRecurId());
					MM.sqlMap.insert("insertPendingTransaction", detail);
					formatter.setNext(recurHeader.getFrequency());
				}

				recurHeader.setNextRunDate(formatter.getDate());
				MM.sqlMap.update("setNextRunDate", recurHeader);
			}
		}

	}
	
	/**
	 * Returns the exchange required between two accounts
	 * 
	 * @param toAccount  Destination account
	 * @param offsetAccount offsetting account for double entry
	 * @return exchange rate as a string
	 * @throws SQLException
	 */
	public String getExchangeRate(Account toAccount, Account offsetAccount) throws SQLException{
		
		String result = null;
		
		if (!offsetAccount.getIsoCode().equals(toAccount.getIsoCode())){
			
			Currency currency = null;
			
			if (toAccount.getCurrencyID()==0){
				
				currency = (Currency) MM.sqlMap.queryForObject("getCurrencyByIsoCode", offsetAccount.getIsoCode());
				BigDecimal rated = formatter.strictDivide("1", currency.getExchangeRate(), 4);
				result = rated.toString();
				
			} else if (offsetAccount.getCurrencyID()==0){
				
				currency = (Currency) MM.sqlMap.queryForObject("getCurrencyByIsoCode", toAccount.getIsoCode());
				result = currency.getExchangeRate();
				
			}
		}

		return result;
		
	}
	
	
	private boolean offsetsValid(ArrayList<TransactionOffset> offsets, long transactionAmount){
		
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
