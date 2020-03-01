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
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.BudgetBalance;
import org.infinitypfm.core.data.BudgetDetail;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.MonthlyBalance;
import org.infinitypfm.core.data.ParamDateRange;
import org.infinitypfm.reporting.BaseReport;
import org.infinitypfm.reporting.ScriptLoader;
import org.infinitypfm.ui.view.dialogs.AccountSelectorDialog;
import org.infinitypfm.ui.view.dialogs.BudgetSelector;
import org.infinitypfm.ui.view.dialogs.MonthYearDialog;

/**
 * 
 * This class gathers up all the data needed for 
 * a report.  
 * 
 * User will be prompted for input if required.
 *
 */
public class ReportData {

	private DataFormatUtil dateUtil;
	@SuppressWarnings("rawtypes")
	private List reportData;
	private String incomeTotal;
	private String expenseTotal;
	private String liabilityTotal;
	private String title;
	private String budget;
	private String account;
	private String _template;
	private String _styles = "";
	
	/********************/
	/* Script Libraries */
	private ScriptLoader _scriptLoader = null;
	/********************/
	
	private Boolean userCancelled = false;

	public ReportData(int reportType) {

		dateUtil = new DataFormatUtil(MM.options.getCurrencyPrecision());
		ParamDateRange reportParam = new ParamDateRange();

		switch (reportType) {

		case MM.THIS_MONTH:

			dateUtil.setDate(new Date());
			initTotals();
			title = MM.PHRASES.getPhrase("123") + " "
					+ dateUtil.getMonthName(0) + " " + dateUtil.getYear();
			reportParam.setMth(dateUtil.getMonth());
			reportParam.setYr(dateUtil.getYear());
			setReportData("getReportMonthlyBalances", reportParam);

			break;
		case MM.LAST_MONTH:

			promptForMonthYr();
			initTotals();
			title = MM.PHRASES.getPhrase("123") + " "
					+ dateUtil.getMonthName(0) + " " + dateUtil.getYear();
			reportParam.setMth(dateUtil.getMonth());
			reportParam.setYr(dateUtil.getYear());
			setReportData("getReportMonthlyBalances", reportParam);
			

			break;
		case MM.MENU_REPORTS_ACCOUNT_HISTORY:

			dateUtil.setDate(new Date());
			title = MM.PHRASES.getPhrase("136");
			MonthlyBalance monthlyBalance = new MonthlyBalance();

			account = promptForAccount(true); //Include income accounts

			if (account != null) {

				monthlyBalance.setActName(account);
				monthlyBalance.setMth(dateUtil.getMonth());
				monthlyBalance.setYr(dateUtil.getYear() - 1);
				setReportData("getReportAccountHistory", monthlyBalance);
				_template = MM.RPT_ACCOUNT_HISTORY;

			}

			break;
		case MM.MENU_REPORTS_BUDGET_PERFORMANCE:

			dateUtil.setDate(new Date());
			title = MM.PHRASES.getPhrase("164");

			budget = promptForBudget();

			if (budget != null) {

				BudgetBalance args = new BudgetBalance();
				args.setBudgetName(budget);
				args.setMth(dateUtil.getMonth());
				args.setYr(dateUtil.getYear());
				args.setActTypeName(MM.ACT_TYPE_EXPENSE);
				setReportData("getBudgetVsExpenseByMonth", args);

			}

			break;
		case MM.MENU_REPORTS_BUDGET_PERFORMANCE_ACT:

			dateUtil.setDate(new Date());

			account = promptForAccount();
			budget = promptForBudget();

			if (budget != null && account != null) {

				title = MM.PHRASES.getPhrase("164") + ":  " + account;

				BudgetBalance args = new BudgetBalance();
				args.setActName(account);
				args.setBudgetName(budget);
				args.setMth(dateUtil.getMonth());
				args.setYr(dateUtil.getYear());
				args.setActTypeName(MM.ACT_TYPE_EXPENSE);
				setReportData("getBudgetVsExpenseByMonthAndAccount", args);

			}

			break;
		case MM.MENU_REPORTS_INCOME_VS_EXPENSE:

			dateUtil.setDate(new Date());
			title = MM.PHRASES.getPhrase("221");
			reportParam.setMth(dateUtil.getMonth());
			reportParam.setYr(dateUtil.getYear());
			setReportData("getIncomeVsExpense", reportParam);

			break;

		}
	}

	@SuppressWarnings("rawtypes")
	public List getReportData() {
		return reportData;
	}

	public String getIncomeTotal() {
		return incomeTotal;
	}

	public String getExpenseTotal() {
		return expenseTotal;
	}

	public String getLiabilityTotal() {
		return liabilityTotal;
	}

	public String getTitle() {
		return title;
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

	private void initTotals() {

		BudgetDetail budgetDetail = new BudgetDetail();
		budgetDetail.setMth(dateUtil.getMonth());
		budgetDetail.setYr(dateUtil.getYear());

		try {

			incomeTotal = (String) MM.sqlMap.queryForObject(
					"getIncomeTotalForMonth", budgetDetail);
			expenseTotal = (String) MM.sqlMap.queryForObject(
					"getExpenseTotalForMonth", budgetDetail);
			liabilityTotal = (String) MM.sqlMap.queryForObject(
					"getLiabilityTotalForMonth", budgetDetail);

		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}

	private void promptForMonthYr() {

		MonthYearDialog monthPicker = new MonthYearDialog();
		monthPicker.Open();

		dateUtil.setDate(monthPicker.getYear(), monthPicker.getMonth());
		userCancelled = monthPicker.userCancelled();

	}

	private void setReportData(String reportName, Object args) {

		try {

			reportData = MM.sqlMap.queryForList(reportName, args);

		} catch (SQLException e) {
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
			return _scriptLoader.getGraphLib();
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getJslib() {
		try {
			return _scriptLoader.getJsLib();
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getBarChartBase() {
		try {
			return _scriptLoader.getScript(BaseReport.BAR_CHART_BASE);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getLineChartBase() {
		try {
			return _scriptLoader.getScript(BaseReport.LINE_CHART_BASE);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getPieChartBase() {
		try {
			return _scriptLoader.getScript(BaseReport.PIE_CHART_BASE);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getBarChartOne() {
		try {
			return _scriptLoader.getScript(BaseReport.BAR_CHART_1);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getLineChartOne() {
		try {
			return _scriptLoader.getScript(BaseReport.LINE_CHART_1);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getPieChartOne() {
		try {
			return _scriptLoader.getScript(BaseReport.PIE_CHART_1);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		return null;
	}

	public String getPieChartTwo() {
		try {
			return _scriptLoader.getScript(BaseReport.PIE_CHART_2);
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
		return _styles;
	}

	public void setStyles(String _styles) {
		this._styles = _styles;
	}

}
