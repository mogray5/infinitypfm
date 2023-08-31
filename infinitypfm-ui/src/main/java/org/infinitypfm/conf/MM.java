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

package org.infinitypfm.conf;

import java.io.File;

import org.eclipse.swt.SWT;
import org.infinitypfm.bitcoin.wallet.BsvWallet;
import org.infinitypfm.core.conf.LangLoader;
import org.infinitypfm.core.data.Options;
import org.infinitypfm.core.data.Plan;
import org.apache.ibatis.session.SqlSession;

import freemarker.template.Configuration;

public class MM {

	/*
	 * Program Constants
	 */
	public static final String APPTITLE = "Infinity PFM";
	public static final String APPVERSION = "0.9.6"; // <-- Used during database creation only
	public static final String APPLINK = "https://www.infinitypfm.org";
	public static final String APPLICENCE = "GNU General Public License v3";
	public static final String APPCOPYRIGHT = "(c) 2005-2023 by Wayne Gray";
	public static final String APPPATH = System.getProperty("INFINITYPFM_HOME") + File.separator;
	public static final String ENVAPPHOME = "INFINITYPFM_HOME";
	public static final int ROW_BACKGROUND = SWT.COLOR_LIST_BACKGROUND;
	public static final String MAPFILE = "org/infinitypfm/core/data/SqlMapConfig.xml";
	public static final String PROPS_FILE = "infinitypfm.properties";
	
	public static final String IMG_ADD = "list-add.png";
	public static final String IMG_ARROW_DOWN = "go-down.png";
	public static final String IMG_ARROW_RIGHT = "arrowr_32x32.png";
	public static final String IMG_ARROW_UP = "go-up.png";
	public static final String IMG_BSV = "bsv.png";
	public static final String IMG_BSV_LOGO = "bai-icon-small-raised.png";
	public static final String IMG_CALCULATOR = "accessories-calculator.png";
	public static final String IMG_CALENDAR = "x-office-calendar.png";
	public static final String IMG_CANCEL = "edit-undo.png";
	public static final String IMG_CHART = "chart2_32x32.png";
	public static final String IMG_CLEAR = "edit-clear.png";
	public static final String IMG_CLIPBOARD = "clipboard_32x32.png";
	public static final String IMG_CLOCK = "appointment-soon.png";
	public static final String IMG_CLOSE_SMALL = "fileclose.png";
	public static final String IMG_CLOSE = "window-close.png";
	public static final String IMG_CONFIGTOPIC = "system-run.png";
	public static final String IMG_CONNECTED = "call-start.png";
	public static final String IMG_CREDIT = "credit_32x32.png";
	public static final String IMG_DOLLAR = "coins_32x32.png";
	public static final String IMG_EDITSERVER = "edit-paste.png";
	public static final String IMG_ENGINEERING = "applications-engineering.png";
	public static final String IMG_EMBLEM = "emblem-generic.png";
	public static final String IMG_EMBLEM_WEB = "emblem-web.png";
	public static final String IMG_EMBLEM_DOCUMENTS = "emblem-documents.png";
	public static final String IMG_EYE = "eye.gif";
	public static final String IMG_FILE = "file_obj.gif";
	public static final String IMG_FOLDER_DOWNLOAD = "folder-download.png";
	public static final String IMG_HELPBACK = "go-previous.png";
	public static final String IMG_HELPFORWARD = "go-previous-rtl.png";
	public static final String IMG_HELP = "help-faq.png";
	public static final String IMG_INTERNET = "applications-internet.png";
	public static final String IMG_KEY = "dialog-password.png";
	public static final String IMG_LOGASSOC = "log.gif";
	public static final String IMG_LOGO_ICON = "infinityLogo_128x128.png";
	public static final String IMG_LOGO_ICON_SMALL = "infinityLogo_16x16.png";
	public static final String IMG_LOGTREE = "document.png";
	public static final String IMG_MESSAGE_DEFAULT = "hwinfo.png";
	public static final String IMG_NEWOBJECT = "zoom-in.png";
	public static final String IMG_NEWSERVERSMALL = "blockdevice.png";
	public static final String IMG_NOTCONNECTED = "noconn.gif";
	public static final String IMG_OPTIONS = "tools.gif";
	public static final String IMG_PLUGINCLOSE = "tree_mode.gif";
	public static final String IMG_QUE = "circle.gif";
	public static final String IMG_QUEZEN_BANNER = "infinitylogo2.jpg";
	public static final String IMG_QUEZEN_BANNER_SMALL = "infinitylogo1_small.jpg";
	public static final String IMG_REFRESH2 = "refresh.png";
	public static final String IMG_REFRESH = "reload.png";
	public static final String IMG_REMOVEQUEUE = "remove.png";
	public static final String IMG_SAVE = "document-save.png";
	public static final String IMG_SELECTALL = "ok.png";
	public static final String IMG_SHARE = "emblem-shared.png";
	public static final String IMG_SPREADSHEET = "x-office-spreadsheet.png";
	public static final String IMG_STAR_EMPTY = "non-starred.png";
	public static final String IMG_STAR_FULL = "starred.png";
	public static final String IMG_TESTMSG = "mail_forward.png";
	public static final String IMG_TEST_QR = "qrTstImg.png";
	public static final String IMG_TOPIC = "circle.gif";
	public static final String IMG_TREELEAF = "tree_leaf_32x32.png";
	public static final String IMG_WALLET = "accessories-dictionary.png";

	
	public static final String ACT_TYPE_BANK = "Bank";
	public static final String ACT_TYPE_EXPENSE = "Expense";
	public static final String ACT_TYPE_LIABILITY = "Liability";
	public static final String ACT_TYPE_INCOME = "Income";
	
	public static final int YES = 0;
	public static final int NO = 1;
	public static final int OK = 2;
	public static final int CANCEL = 3;
    public static final int THIS_MONTH = 4;
    public static final int LAST_MONTH = 5;
    public static final int LAST_YEAR = 6;
	
	public static final int RETURNTYPE_SUCCESS = 0;
	public static final int RETURNTYPE_FAIL = -1;

	public static final int DIALOG_QUESTION = 0;
	public static final int DIALOG_INFO = 1;
	
	public static final int NODETYPE_LOG = 0;
	public static final int NODETYPE_LOG_ROOT = 1;
	public static final int NODETYPE_BANK_ACCOUNT = 2;
	public static final int NODETYPE_EXPENSE_ACCOUNT = 3;
	public static final int NODETYPE_INCOME_ACCOUNT = 4;
	public static final int NODETYPE_ROOT = 5;
	
	public static final int MAX_PRECISION = 8;
	
	public static final int MENU_DEFAULT = -1;
	
	public static final int MENU_VIEW_CONSOLE = 10;
	public static final int MENU_VIEW_TRANS_ENTRY = 11;
	public static final int MENU_VIEW_BOOKMARKS = 12;
	
	public static final int MENU_TREE_REFRESH = 20;
	public static final int MENU_TREE_ACT_REMOVE = 21;
	public static final int MENU_TREE_CLOSEVIEW = 22;
	public static final int MENU_TREE_ACT_ADD = 23;
	public static final int MENU_TREE_LOAD_REGISTER = 24;
	public static final int MENU_TREE_ADD_ACT_BUDGET = 25;
	public static final int MENU_TREE_ADD_ACT_FROM_TEMP = 26;
	public static final int MENU_TREE_REM_ACT_BUDGET = 27;
	public static final int MENU_TREE_REM_BUDGET = 28;
	public static final int MENU_TREE_EDIT_ACT = 29;
	public static final int MENU_TREE_ADD_PLAN = 30;
	public static final int MENU_TREE_CLONE_PLAN = 31;
	public static final int MENU_TREE_RENAME_PLAN = 32;
	public static final int MENU_TREE_DELETE_PLAN = 33;
	public static final int MENU_TREE_RUN_PLAN = 34;
	public static final int MENU_TREE_ADD_PLAN_EVENT = 35;
	public static final int MENU_TREE_DELETE_PLAN_EVENT = 36;
	public static final int MENU_TREE_EDIT_PLAN_EVENT = 37;
	
	
	public static final int MENU_FILE_EXIT = 40;
	public static final int MENU_FILE_SAVE = 41;
	public static final int MENU_FILE_IMPORT_OFX = 42;
	public static final int MENU_FILE_IMPORT_QFX = 43;
	public static final int MENU_FILE_IMPORT_QIF = 44;
	public static final int MENU_FILE_IMPORT_RULES = 45;
	public static final int MENU_FILE_BACKUP = 46;
	public static final int MENU_FILE_RESTORE = 47;
	public static final int MENU_FILE_IMPORT_CSV = 48;
	public static final int MENU_FILE_IMPORT_MAIL = 49;
	
	public static final int MENU_REPORT_EXECUTE = 60;
	public static final int MENU_REPORT_SAVE = 61;
	
	public static final int MENU_EDIT_ADD_ACCOUNT = 80;
	public static final int MENU_EDIT_ADD_BUDGET = 81;
	public static final int MENU_EDIT_ADD_CURRENCY = 82;
	
	public static final int MENU_CONSOLE_CLEAR = 100;
	public static final int MENU_CONSOLE_CLOSE = 101;
	
	public static final int MENU_BOOKMARKS_CLOSE = 102;
	
	public static final int MENU_TOPIC_ADD = 110;
	public static final int MENU_TOPIC_SELECTALL = 111;
	public static final int MENU_TOPIC_CONFIG = 112;
	
	public static final int MENU_SERVER_NEW = 122;
	public static final int MENU_SERVER_EDIT = 123;
	public static final int MENU_SERVER_REMOVE = 124;
	
	public static final int MENU_OPTIONS_CONFIG = 134;
	
	public static final int MENU_HELP_CONTENTS = 144;
	public static final int MENU_HELP_ABOUT = 145;
	
	public static final int MENU_REPORTS_MONTHLY_BALANCE = 155;
	public static final int MENU_REPORTS_YEARLY_BALANCE = 156;
	public static final int MENU_REPORTS_PRIOR_MONTHLY_BALANCE = 157;
	public static final int MENU_REPORTS_ACCOUNT_HISTORY = 158;
	public static final int MENU_REPORTS_ACCOUNT_HISTORY_ALL_TIME = 159;
	public static final int MENU_REPORTS_BUDGET_PERFORMANCE = 160;
	public static final int MENU_REPORTS_BUDGET_PERFORMANCE_ACT = 161;
	public static final int MENU_REPORTS_INCOME_VS_EXPENSE = 162;
	public static final int MENU_REPORTS_PLANNER = 163;
	public static final int MENU_REPORTS_REGISTER = 164;
	public static final int MENU_REPORTS_UTXO = 165;
	
	public static final int MENU_BUDGET_SAVE = 172;
	
	public static final int MENU_WALLET_SHOW_MNEMONIC = 182;
	public static final int MENU_WALLET_BACKUP = 183;
	public static final int MENU_WALLET_RESTORE = 184;
	public static final int MENU_WALLET_REFRESH = 185;
	public static final int MENU_WALLET_UTXO = 186;
	
	public static final int TAB_SERVER_AUTH = 16;
	public static final int TAB_SERVER_LOGS = 17;
	public static final int TAB_SERVER_TOPICS = 18;
	
	
	public static final int VIEW_SCRATCH = 401;
	public static final int VIEW_DEFAULT = 402;
	public static final int VIEW_REGISTER = 403;
	public static final int VIEW_LOG = 404;
	public static final int VIEW_BUDGET=405;
	public static final int VIEW_REPORT=406;
	public static final int VIEW_RECURRENCE=407;
	public static final int VIEW_CURRENCY=408;
	public static final int VIEW_WALLET=409;
	public static final int VIEW_PLANNER=410;
	
	public static final int QS_REFRESH_QUEUE = 0;
	public static final int QS_REFRESH_TOPIC = 1;
	public static final int QS_REFRESH_ALL = 2;
	
	public static final String RECUR_WEEKLY = "147";
	public static final String RECUR_BIWEEKLY = "149";
	public static final String RECUR_MONTHLY = "146";
	public static final String RECUR_YEARLY = "148";
	
	public static final int TRANSACTION_MODE_AUTOCOMMIT = 500;
	public static final int TRANSACTION_MODE_NOCOMMIT = 501;
	
	public static String DATPATH = System.getProperty("user.home") + "/.infinitypfm";
	public static String HELPPATH = "file://///" + MM.APPPATH + 
	 	"docs" + File.separator + "index.html";
	public static String REPORTFOLDERURL = "file://///" + MM.APPPATH + 
 	"reports" + File.separator;
	public static String REPORTFOLDER = MM.APPPATH + 
 	"reports" + File.separator;
	//iBatis SQL Map reference
	public static SqlSession sqlMap = null;
	public static SqlSession sqlTransactionMap = null;
	
	//Load language - use default for now
	public static LangLoader PHRASES = null;
	
	public static String importFile = null;
	public static String csvConfig = null;
	
	// Report Settings
	public static Object reportParams; 
	public static Configuration templateConfig = null;
	public static final String PIE_CHART_BASE = "piechartBase.js";
	public static final String PIE_CHART_1 ="piechart1.js";
	public static final String PIE_CHART_2 ="piechart2.js";
	public static final String BAR_CHART_BASE = "barChartBase.js";
	public static final String BAR_CHART_1 = "barchart1.js";
	public static final String LINE_CHART_1 = "linechart1.js";
	public static final String LINE_CHART_BASE = "linechartBase.js";
	public static final String JS_LIB = "jquery.js";
	public static final String GRAPH_LIB = "raphael-min.js";
	public static final String RPT_ACCOUNT_HISTORY = "AccountHistory.ftl";
	public static final String RPT_ACCOUNT_REGISTER = "AccountRegister.ftl";
	public static final String RPT_MONTHLY_BALANCES = "MonthlyBalances.ftl";
	public static final String RPT_YEARLY_BALANCES = "YearlyBalances.ftl";
	public static final String RPT_BUDGET_PERFORMANCE = "BudgetPerformance.ftl";
	public static final String RPT_INCOME_VS_EXPENSE = "IncomveVsExpense.ftl";
	public static final String RPT_PLANNER = "Planner.ftl";
	public static final String RPT_UTXO = "Utxo.ftl";
	
	public static Options options = null;
	public static BsvWallet wallet = null;
	public static final String BSV = "BSV";
	public static final String BSV_WALLET_ACCOUNT = "Bitcoin SV Wallet";
	public static final String BSV_WALLET_RECEIVING_ACCOUNT = "Coins Received";
	public static final String NUM_FORMAT_USE_PARENS = "#,##0.00;(#,##0.00)";
	public static boolean walletNeedsSync = true;
	public static Plan currentPlan;
	
}
