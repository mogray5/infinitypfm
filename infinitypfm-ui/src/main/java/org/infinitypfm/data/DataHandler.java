/*
 * Copyright (c) 2005-2017 Wayne Gray All rights reserved
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Budget;
import org.infinitypfm.core.data.BudgetDetail;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.MonthlyBalance;
import org.infinitypfm.core.data.RecurDetail;
import org.infinitypfm.core.data.RecurHeader;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.exception.TransactionException;
import org.infinitypfm.core.processor.SimpleProcessor;
import org.infinitypfm.core.processor.ProcessorCallback;
import org.infinitypfm.core.processor.TransactionProcessor;
import org.infinitypfm.exception.AccountException;

/**
 * 
 * Collection of common data manipulation functions.
 * 
 */
public class DataHandler implements ProcessorCallback {

	DataFormatUtil formatter = null;
	TransactionProcessor _processor = null;
	
	/**
	 * 
	 */
	public DataHandler() {
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		// Configure a processor
		_processor = new SimpleProcessor(MM.options.getCurrencyPrecision());
		_processor.setLanguage(MM.PHRASES);
		_processor.setMessageCallback(this);
		_processor.setSession(MM.sqlMap);
	
	}

	public void AddAccount(Account act) throws AccountException, SQLException {

		Integer id = GetAccountType(act);

		act.setActTypeId(id.intValue());
		MM.sqlMap.insert("insertAccount", act);
	}

	public void EditAccount(Account act) throws AccountException, SQLException {

		try {
			
			Account newName = (Account) MM.sqlMap.selectOne("getAccountForName", act.getActName());
			
			if (newName != null){
				throw new AccountException(MM.PHRASES.getPhrase("247"));
			}
			
		} catch (Exception e) {
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
		Integer id = (Integer) (MM.sqlMap.selectOne(
				"getAccountTypeIdForName", act.getActTypeName()));

		if (id == null) {
			throw new AccountException(MM.PHRASES.getPhrase("40"));
		}
	
		return id;
		
	}
	
	public void DeleteAccount(Account act) throws AccountException,
			SQLException {

		Account account = null;

		account = (Account) MM.sqlMap.selectOne("getAccountForName",
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

	public void AddTransaction(Transaction tran, boolean saveMemo)
			throws SQLException, TransactionException {

		_processor.AddTransaction(tran, saveMemo);
		
		
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
		budget = (Budget) MM.sqlMap.selectOne("getBudget", sBudget);

		if (budget != null) {

			actList = MM.sqlMap.selectList("getAccountsForType",
					MM.ACT_TYPE_LIABILITY);
			AddBudgetDetailBatch(budget.getBudgetId(), actList);
			actList = MM.sqlMap.selectList("getAccountsForType",
					MM.ACT_TYPE_EXPENSE);
			AddBudgetDetailBatch(budget.getBudgetId(), actList);

		}
		
	}

	public void RemoveBudget(Budget budget) throws SQLException {

		if (budget != null) {
			// delete the detail
			MM.sqlMap.delete("deleteBudgetDetail", budget);
			// delete the budget
			MM.sqlMap.delete("deleteBudget", budget);
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

		MonthlyBalance bal = (MonthlyBalance) MM.sqlMap.selectOne(
				"getMonthlyBalance", tran);

		if (bal != null) {
			MM.sqlTransactionMap.update("updateMonthlyBalance", tran);
		} else {
			// create new month
			MM.sqlTransactionMap.insert("insertMonthlyBalance", tran);
		}

	}

	@SuppressWarnings("rawtypes")
	public void SyncBudgetMonth(Budget budget, int mth) throws SQLException {

		BudgetDetail budgetDetail = new BudgetDetail();
		budgetDetail.setBudgetId(budget.getBudgetId());
		budgetDetail.setMth(mth);
		List actList = null;

		actList = MM.sqlMap.selectList("getBudgetDetailForMonth",
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

		List trans = MM.sqlMap.selectList("getPastDueRecurringTransactions",
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
				
				currency = (Currency) MM.sqlMap.selectOne("getCurrencyByIsoCode", offsetAccount.getIsoCode());
				BigDecimal rated = formatter.strictDivide("1", currency.getExchangeRate(), 4);
				result = rated.toString();
				
			} else if (offsetAccount.getCurrencyID()==0){
				
				currency = (Currency) MM.sqlMap.selectOne("getCurrencyByIsoCode", toAccount.getIsoCode());
				result = currency.getExchangeRate();
				
			}
		}

		return result;
		
	}

	@Override
	public void LogMessage(String msg) {
		InfinityPfm.LogMessage(msg);
	}
}
