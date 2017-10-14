/*
 * Copyright (c) 2005-2012 Wayne Gray All rights reserved
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

package org.infinitypfm.core.conf;

import java.io.File;


/**
 * @author wayne
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PfmSettings {

	/*
	 * Program Constants
	 */
	public static final String APPTITLE = "Infinity PFM";
	public static final String APPVERSION = "0.7.0";
	public static final String APPLINK = "https://www.infinitypfm.org";
	public static final String APPLICENCE = "GNU General Public License v3";
	public static final String APPCOPYRIGHT = "(c) 2005-2013 by Wayne Gray";
	//public static final String APPPATH = System.getProperty("INFINITYPFM_HOME") + File.separator;
	public static final String APPPATH = "blah";
	
	public static final String ACT_TYPE_EXPENSE = "Expense";
	public static final String ACT_TYPE_LIABILITY = "Liability";
	public static final String ACT_TYPE_INCOME = "Income";
	
	//public static String DATPATH = System.getProperty("INFINITYPFM_DATA") + File.separator;
	public static String DATPATH = "/var/lib/infinitypfm";
	public static String HELPPATH = "file://///" + PfmSettings.APPPATH + 
	 	"docs" + File.separator + "index.html";
	public static String REPORTFOLDERURL = "file://///" + PfmSettings.APPPATH + 
 	"reports" + File.separator;
	public static String REPORTFOLDER = PfmSettings.APPPATH + 
 	"reports" + File.separator;
	
	public static String MOCK_FOLDER = "/tmp/";
	
	public static final String RECUR_WEEKLY = "147";
	public static final String RECUR_BIWEEKLY = "149";
	public static final String RECUR_MONTHLY = "146";
	public static final String RECUR_YEARLY = "148";
	
	public static final int THIS_MONTH = 4;
    public static final int LAST_MONTH = 5;
    
    public static final int MENU_REPORTS_MONTHLY_BALANCE = 100;
	public static final int MENU_REPORTS_PRIOR_MONTHLY_BALANCE = 101;
	public static final int MENU_REPORTS_ACCOUNT_HISTORY = 102;
	public static final int MENU_REPORTS_BUDGET_PERFORMANCE = 103;
	public static final int MENU_REPORTS_BUDGET_PERFORMANCE_ACT = 104;
	public static final int MENU_REPORTS_INCOME_VS_EXPENSE = 105;
	
	public static final String REPORT_MONTHLY_BALANCES = "getReportMonthlyBalances";
	public static final String REPORT_ACCOUNT_HISTORY = "getReportAccountHistory";
	public static final String REPORT_BUDGET_VS_EXPENSE = "getBudgetVsExpenseByMonth";
	public static final String REPORT_BUDGET_VS_EXPENSE_MONTHLY = "getBudgetVsExpenseByMonthAndAccount";
	public static final String REPORT_INCOME_VS_EXPENSE = "getIncomeVsExpense";
	
}
