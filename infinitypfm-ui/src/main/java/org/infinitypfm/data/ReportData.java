/*
 * Copyright (c) 2005-2020 Wayne Gray All rights reserved
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.BudgetBalance;
import org.infinitypfm.core.data.BudgetDetail;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.IReportable;
import org.infinitypfm.core.data.MonthlyBalance;
import org.infinitypfm.core.data.ParamDateRange;
import org.infinitypfm.core.data.ParamDateRangeAccount;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.YearlyBalance;
import org.infinitypfm.reporting.BaseReport;
import org.infinitypfm.reporting.ScriptLoader;
import org.infinitypfm.ui.view.dialogs.AccountSelectorDialog;
import org.infinitypfm.ui.view.dialogs.BudgetSelector;
import org.infinitypfm.ui.view.dialogs.MonthYearDialog;
import org.infinitypfm.ui.view.dialogs.YearDialog;

/**
 * 
 * This class gathers up all the data needed for 
 * a report.  
 * 
 * User will be prompted for input if required.
 *
 */
public class ReportData {

	private DataFormatUtil dataUtil;
	@SuppressWarnings("rawtypes")
	private List reportData;
	@SuppressWarnings("rawtypes")
	private List reportDataIncome;
	@SuppressWarnings("rawtypes")
	private List reportDataExpense;
	private String incomeTotal;
	private String expenseTotal;
	private String liabilityTotal;
	private String accountTotal;
	private String title;
	private String budget;
	private String account;
	private String _template;
	private int year;
	
	/********************/
	/* Script Libraries */
	private ScriptLoader _scriptLoader = null;
	/********************/
	
	private Boolean userCancelled = false;

	public ReportData (int reportType) {
		this(reportType, null);
	}
	
	public ReportData(int reportType, Object params) {

		dataUtil = new DataFormatUtil(MM.options.getCurrencyPrecision());
		ParamDateRange reportParam = null;;

		switch (reportType) {

		case MM.THIS_MONTH:

			dataUtil.setDate(new Date());
			initTotals();
			title = MM.PHRASES.getPhrase("123") + " "
					+ dataUtil.getMonthName(0) + " " + dataUtil.getYear();
			if (params == null) {
				reportParam = new ParamDateRange();
				reportParam.setMth(dataUtil.getMonth());
				reportParam.setYr(dataUtil.getYear());
				setReportData("getReportMonthlyBalances", reportParam);
			} else 
				setReportData("getReportMonthlyBalances", params);

			populateIncomeAndExpenseLists();
			
			_template = MM.RPT_MONTHLY_BALANCES;
			
			break;
		case MM.LAST_MONTH:

			promptForMonthYr();
			initTotals();
			title = MM.PHRASES.getPhrase("123") + " "
					+ dataUtil.getMonthName(0) + " " + dataUtil.getYear();
			if (params == null) {
				reportParam = new ParamDateRange();
				reportParam.setMth(dataUtil.getMonth());
				reportParam.setYr(dataUtil.getYear());
				setReportData("getReportMonthlyBalances", reportParam);
			} else 
				setReportData("getReportMonthlyBalances", params);
			
			populateIncomeAndExpenseLists();
			
			_template = MM.RPT_MONTHLY_BALANCES;
			
			break;
		case MM.LAST_YEAR:

			promptForYear();
			initTotalsYear();
			title = MM.PHRASES.getPhrase("311") + " " + year;
			if (params == null) {
				reportParam = new ParamDateRange();
				reportParam.setYr(year);
				setReportData("getReportYearlyBalances", reportParam);
			} else 
				setReportData("getReportYearlyBalances", params);
			
			populateIncomeAndExpenseListYear();
			
			_template = MM.RPT_YEARLY_BALANCES;
			
			break;
		case MM.MENU_REPORTS_ACCOUNT_HISTORY:

			dataUtil.setDate(new Date());
			title = MM.PHRASES.getPhrase("136");
			MonthlyBalance monthlyBalance = null;

			account = promptForAccount(true); //Include income accounts

			if (account != null) {
				initTotalsAccount();
				if (params == null) {
					monthlyBalance = new MonthlyBalance();
					monthlyBalance.setActName(account);
					monthlyBalance.setMth(dataUtil.getMonth());
					monthlyBalance.setYr(dataUtil.getYear() - 1);
					setReportData("getReportAccountHistory", monthlyBalance);
				} else 
					setReportData("getReportAccountHistory", params);
				
				_template = MM.RPT_ACCOUNT_HISTORY;

			}

			break;
		case MM.MENU_REPORTS_ACCOUNT_HISTORY_ALL_TIME:
			dataUtil.setDate(new Date());
			title = MM.PHRASES.getPhrase("273");
			monthlyBalance = new MonthlyBalance();

			account = promptForAccount(true); //Include income accounts

			if (account != null) {
				initTotalsAccount();
				monthlyBalance.setActName(account);
				monthlyBalance.setMth(dataUtil.getMonth());
				monthlyBalance.setYr(dataUtil.getYear() - 50);
				setReportData("getReportAccountHistory", monthlyBalance);
				_template = MM.RPT_ACCOUNT_HISTORY;

			}
			break;
		case MM.MENU_REPORTS_BUDGET_PERFORMANCE:

			dataUtil.setDate(new Date());
			title = MM.PHRASES.getPhrase("164");

			budget = promptForBudget();

			if (budget != null) {

				if (params == null) {
					BudgetBalance args = new BudgetBalance();
					args.setBudgetName(budget);
					args.setMth(dataUtil.getMonth());
					args.setYr(dataUtil.getYear());
					args.setActTypeName(MM.ACT_TYPE_EXPENSE);
					setReportData("getBudgetVsExpenseByMonth", args);
				} else
					setReportData("getBudgetVsExpenseByMonth", params);
				
				_template = MM.RPT_BUDGET_PERFORMANCE;
			}

			break;
		case MM.MENU_REPORTS_BUDGET_PERFORMANCE_ACT:

			dataUtil.setDate(new Date());

			account = promptForAccount();
			budget = promptForBudget();

			if (budget != null && account != null) {

				title = MM.PHRASES.getPhrase("164") + ":  " + account;

				if (params == null) {
					BudgetBalance args = new BudgetBalance();
					args.setActName(account);
					args.setBudgetName(budget);
					args.setMth(dataUtil.getMonth());
					args.setYr(dataUtil.getYear());
					args.setActTypeName(MM.ACT_TYPE_EXPENSE);
					setReportData("getBudgetVsExpenseByMonthAndAccount", args);
				} else
					setReportData("getBudgetVsExpenseByMonthAndAccount", params);

				_template = MM.RPT_BUDGET_PERFORMANCE;
			}

			break;
		case MM.MENU_REPORTS_INCOME_VS_EXPENSE:

			dataUtil.setDate(new Date());
			title = MM.PHRASES.getPhrase("221");
			if (params == null) {
				reportParam = new ParamDateRange();
				reportParam.setMth(dataUtil.getMonth());
				reportParam.setYr(dataUtil.getYear());
				setReportData("getIncomeVsExpense", reportParam);
			} else
				setReportData("getIncomeVsExpense", params);
			_template = MM.RPT_INCOME_VS_EXPENSE;
			
			break;
		case MM.MENU_REPORTS_REGISTER:
			
			title = MM.PHRASES.getPhrase("314");
			account = ((ParamDateRangeAccount)params).getAccountName();
			Account act = (Account)MM.sqlMap.selectOne("getAccountForName", account);
			dataUtil.setPrecision(act.getCurrencyPrecision());
			setReportData("getRegister", params);
			_template = MM.RPT_ACCOUNT_REGISTER;
			break;
		}
	}

	@SuppressWarnings("rawtypes")
	public List getReportData() {
		return reportData;
	}

	public String getIncomeTotal() {		
		return formatLongString(incomeTotal);
	}

	public long getIncomeTotalRaw() {
	
		return Long.parseLong(incomeTotal);
	}
	
	public String getExpenseTotal() {
		return formatLongString(expenseTotal);
	}
	
	public long getExpenseTotalRaw() {
		return Long.parseLong(expenseTotal);
	}

	public String getLiabilityTotal() {
		return formatLongString(liabilityTotal);
	}

	public long getLiabilityTotalRaw() {
		return Long.parseLong(liabilityTotal);
	}
	
	public String getAccountTotal() {
		return formatLongString(accountTotal);
	}

	public String getTitle() {
		return title;
	}

	public String getAccount() {
		return account;
	}
	
	public Boolean getUserCanceled() {
		return userCancelled;
	}

	public void setUserCanceled(Boolean userCanceled) {
		this.userCancelled = userCanceled;
	}
	
	public String getTemplate() {
		return _template;
	}

	public void setTemplate(String template) {
		this._template = template;
	}

	@SuppressWarnings("rawtypes")
	public List getReportDataIncome() {
		return reportDataIncome;
	}

	@SuppressWarnings("rawtypes")
	public List getReportDataExpense() {
		return reportDataExpense;
	}

	private void initTotals() {

		BudgetDetail budgetDetail = new BudgetDetail();
		budgetDetail.setMth(dataUtil.getMonth());
		budgetDetail.setYr(dataUtil.getYear());

		try {

			incomeTotal = (String) MM.sqlMap.selectOne(
					"getIncomeTotalForMonth", budgetDetail);
			expenseTotal = (String) MM.sqlMap.selectOne(
					"getExpenseTotalForMonth", budgetDetail);
			liabilityTotal = (String) MM.sqlMap.selectOne(
					"getLiabilityTotalForMonth", budgetDetail);

		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}

	private void initTotalsYear() {

		BudgetDetail budgetDetail = new BudgetDetail();
		budgetDetail.setYr(year);

		try {

			incomeTotal = (String) MM.sqlMap.selectOne(
					"getIncomeTotalForYear", budgetDetail);
			expenseTotal = (String) MM.sqlMap.selectOne(
					"getExpenseTotalForYear", budgetDetail);
			liabilityTotal = (String) MM.sqlMap.selectOne(
					"getLiabilityTotalForYear", budgetDetail);

		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}
	
	private void initTotalsAccount() {

		Transaction tran = new Transaction();
		tran.setTranDate(dataUtil.getDate());
		tran.setActName(account);
		
		try {

			accountTotal = (String) MM.sqlMap.selectOne(
					"getMonthlyTotalByAccountName", tran);

		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}
	
	private void promptForMonthYr() {

		MonthYearDialog monthPicker = new MonthYearDialog();
		monthPicker.Open();

		dataUtil.setDate(monthPicker.getYear(), monthPicker.getMonth());
		userCancelled = monthPicker.userCancelled();

	}

	private void promptForYear() {

		YearDialog yearPicker = new YearDialog();
		yearPicker.Open();

		year = yearPicker.getYear();
		
		userCancelled = yearPicker.userCancelled();

	}
	
	private void setReportData(String reportName, Object args) {

		try {

			reportData = MM.sqlMap.selectList(reportName, args);

			for (Object row : reportData) {
				if (row != null)
					((IReportable) row).setFormatter(dataUtil);
			}
			
			
		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}

	private String promptForBudget() {

		BudgetSelector budgetDialog = new BudgetSelector();
		budgetDialog.Open();
		userCancelled = budgetDialog.userCancelled();

		return budgetDialog.getBudgetName();
	}

	private String promptForAccount() {
		return promptForAccount(false);
	}
	
	private String promptForAccount(boolean inludeIncome) {

		AccountSelectorDialog acctSelect = new AccountSelectorDialog();
		acctSelect.setIncludeIncomeAccounts(inludeIncome);
		acctSelect.Open();
		userCancelled = acctSelect.userCancelled();

		return acctSelect.getAccountName();

	}

	public String getRaphael() {
		try {
			return _scriptLoader.getGraphLib(true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getJslib() {
		try {
			return _scriptLoader.getJsLib(true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getBarChartBase() {
		try {
			return _scriptLoader.getScript(BaseReport.BAR_CHART_BASE, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getLineChartBase() {
		try {
			return _scriptLoader.getScript(BaseReport.LINE_CHART_BASE, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getLineChartBaseSingle() {
		try {
			return _scriptLoader.getScript(BaseReport.LINE_CHART_BASE_SINGLE, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}
	
	public String getPieChartBase() {
		try {
			return _scriptLoader.getScript(BaseReport.PIE_CHART_BASE, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getBarChartOne() {
		try {
			return _scriptLoader.getScript(BaseReport.BAR_CHART_1, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getLineChartOne() {
		try {
			return _scriptLoader.getScript(BaseReport.LINE_CHART_1, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getLineChartSingleOne() {
		try {
			return _scriptLoader.getScript(BaseReport.LINE_CHART_SINGLE_1, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}
	
	public String getPieChartOne() {
		try {
			return _scriptLoader.getScript(BaseReport.PIE_CHART_1, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getPieChartTwo() {
		try {
			return _scriptLoader.getScript(BaseReport.PIE_CHART_2, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public ScriptLoader get_scriptLoader() {
		return _scriptLoader;
	}

	public void set_scriptLoader(ScriptLoader _scriptLoader) {
		this._scriptLoader = _scriptLoader;
	}

	public String getStyles() {
		try {
			return _scriptLoader.getScript(BaseReport.REPORT_CSS, true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	

/**
 * Derived 
 * From: https://stackoverflow.com/questions/3548733/how-do-i-convert-from-list-to-listt-in-java-using-generics
 * 
 * @param <T>
 * @param <C>
 * @param from
 * @param to
 * @param listClass
 * @return
 */
  private <T, C extends Collection<T>> C typesafeAdd(Iterable<?> from, C to, Class<T> listClass) {
    if (from != null) {
    	for (Object item: from) {
    		to.add(listClass.cast(item));
    	}
    return to;
    } else 
    	return null;
  }

  /**
   * This method creates separate lists for income and account data and 
   * stores them in reportDataIncome and reportDataExpense
   */
  private void populateIncomeAndExpenseLists() {
	  
	  List<MonthlyBalance>listMB = new ArrayList<MonthlyBalance>();
		listMB = typesafeAdd(this.reportData, listMB, MonthlyBalance.class);
		
		if (listMB == null || listMB.size()==0) return;
		
		reportDataIncome = listMB.stream().filter(mb -> mb.getActTypeName().equalsIgnoreCase("income"))
				.collect(Collectors.toList());

		reportDataExpense = listMB.stream().filter(mb -> mb.getActTypeName().equalsIgnoreCase("expense"))
				.collect(Collectors.toList());
		
		String x = "a";
		
  }
  
  /**
   * This method creates separate lists for income and account data and 
   * stores them in reportDataIncome and reportDataExpense
   */
  private void populateIncomeAndExpenseListYear() {
	  
	  List<YearlyBalance>listMB = new ArrayList<YearlyBalance>();
		listMB = typesafeAdd(this.reportData, listMB, YearlyBalance.class);
		
		if (listMB == null || listMB.size()==0) return;
		
		reportDataIncome = listMB.stream().filter(mb -> mb.getActTypeName().equalsIgnoreCase("income"))
				.collect(Collectors.toList());

		reportDataExpense = listMB.stream().filter(mb -> mb.getActTypeName().equalsIgnoreCase("expense"))
				.collect(Collectors.toList());
		
  }
  
  /**
   * Takes a currency value represented as long and returns formatted number.
   * If formatter class is null then original value is returned.
   * @param value
   * @return
   */
  private String formatLongString(String value) {

	if (dataUtil != null && value != null) 
		return dataUtil.getAmountFormatted(Long.parseLong(value));
	 else 
		return value;
  }
  
  
  /* Header translations */
  
  public String getWordIncome() {
	  return MM.PHRASES.getPhrase("118");
  }

  public String getWordIncomeTotal() {
	  return MM.PHRASES.getPhrase("262");
  }
  
  public String getWordExpenses() {
	  return MM.PHRASES.getPhrase("94");
  }
  
  public String getWordExpenseTotal() {
	  return MM.PHRASES.getPhrase("263");
  }
  
  public String getWordLiabilityTotal() {
	  return MM.PHRASES.getPhrase("264");
  }
  
  public String getWordYear() {
	  return MM.PHRASES.getPhrase("166");
  }
  
  public String getWordYearMonth() {
	  return MM.PHRASES.getPhrase("265");
  }
  
  public String getWordAccountBalance() {
	  return MM.PHRASES.getPhrase("266");
  }
  public String getWordExpenseBalance() {
	  return MM.PHRASES.getPhrase("267");
  }
  public String getWordBudgetBalance() {
	  return MM.PHRASES.getPhrase("268");
  }
  public String getWordBudgetName() {
	  return MM.PHRASES.getPhrase("269");
  }
  public String getWordIncomeBalance() {
	  return MM.PHRASES.getPhrase("270");
  }
  public String getWordLiabilityBalance() {
	  return MM.PHRASES.getPhrase("271");
  }
  public String getWordAccountType() {
	  return MM.PHRASES.getPhrase("272");
  }
  public String getWordAccountName() {
	  return MM.PHRASES.getPhrase("93");
  }
  public String getWordAccountTotal() {
	  return MM.PHRASES.getPhrase("275");
  }
  
  public String getWordTranDate() {
	  return MM.PHRASES.getPhrase("24");
  }
  public String getWordMemo() {
	  return MM.PHRASES.getPhrase("41");
  }
  public String getWordDebit() {
	  return MM.PHRASES.getPhrase("48");
  }
  public String getWordCredit() {
	  return MM.PHRASES.getPhrase("46");
  }
}

