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

package org.infinitypfm.conf;

import java.io.File;

import org.eclipse.swt.SWT;
import org.infinitypfm.core.conf.LangLoader;
import org.infinitypfm.core.data.Options;

import com.ibatis.sqlmap.client.SqlMapClient;

public class MM {

	/*
	 * Program Constants
	 */
	public static final String APPTITLE = "Infinity PFM";
	public static final String APPVERSION = "0.7.7"; // <-- Used during database creation only
	public static final String APPLINK = "https://www.infinitypfm.org";
	public static final String APPLICENCE = "GNU General Public License v3";
	public static final String APPCOPYRIGHT = "(c) 2005-2017 by Wayne Gray";
	public static final String APPPATH = System.getProperty("INFINITYPFM_HOME") + File.separator;
	public static final String ENVAPPHOME = "INFINITYPFM_HOME";
	public static final int ROW_BACKGROUND = SWT.COLOR_LIST_BACKGROUND;
	public static final String MAPFILE = "org/infinitypfm/core/data/SqlMapConfig.xml";
	public static final String PROPS_FILE = "infinitypfm.properties";
	
	public static final String IMG_ADD = "list-add.png";
	public static final String IMG_ARROW_DOWN = "go-down.png";
	public static final String IMG_ARROW_UP = "go-up.png";
	public static final String IMG_CALCULATOR = "accessories-calculator.png";
	public static final String IMG_CALENDAR = "x-office-calendar.png";
	public static final String IMG_CANCEL = "edit-undo.png";
	public static final String IMG_CHART = "chart2_32x32.png";
	public static final String IMG_CLEAR = "edit-clear.png";
	public static final String IMG_CLOCK = "appointment-soon.png";
	public static final String IMG_CLOSE_SMALL = "fileclose.png";
	public static final String IMG_CLOSE = "window-close.png";
	public static final String IMG_CONFIGTOPIC = "system-run.png";
	public static final String IMG_CONNECTED = "call-start.png";
	public static final String IMG_CREDIT = "credit_32x32.png";
	public static final String IMG_EDITSERVER = "edit-paste.png";
	public static final String IMG_EMBLEM = "emblem-generic.png";
	public static final String IMG_EYE = "eye.gif";
	public static final String IMG_HELP = "help-faq.png";
	public static final String IMG_HELPBACK = "go-previous.png";
	public static final String IMG_HELPFORWARD = "go-previous-rtl.png";
	public static final String IMG_FILE = "file_obj.gif";
	public static final String IMG_LOGASSOC = "log.gif";
	public static final String IMG_LOGTREE = "document.png";
	public static final String IMG_NEWOBJECT = "x-office-spreadsheet.png";
	public static final String IMG_NEWSERVERSMALL = "blockdevice.png";
	public static final String IMG_NOTCONNECTED = "noconn.gif";
	public static final String IMG_OPTIONS = "tools.gif";
	public static final String IMG_PLUGINCLOSE = "tree_mode.gif";
	public static final String IMG_QUE = "circle.gif";
	public static final String IMG_QUEZEN_BANNER = "infinitylogo2.jpg";
	public static final String IMG_QUEZEN_BANNER_SMALL = "infinitylogo1_small.jpg";
	public static final String IMG_QUEZEN_ICON = "eye.gif";
	public static final String IMG_REFRESH = "reload.png";
	public static final String IMG_REMOVEQUEUE = "remove.png";
	public static final String IMG_SAVE = "document-save.png";
	public static final String IMG_SELECTALL = "ok.png";
	public static final String IMG_TOPIC = "circle.gif";
	public static final String IMG_TESTMSG = "mail_forward.png";
	public static final String IMG_TREELEAF = "tree_leaf_32x32.png";
	public static final String IMG_DOLLAR = "coins_32x32.png";
	
	public static final String ACT_TYPE_EXPENSE = "Expense";
	public static final String ACT_TYPE_LIABILITY = "Liability";
	public static final String ACT_TYPE_INCOME = "Income";
	
	public static final int YES = 0;
	public static final int NO = 1;
	public static final int OK = 2;
	public static final int CANCEL = 3;
    public static final int THIS_MONTH = 4;
    public static final int LAST_MONTH = 5;
	
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
	
	public static final int MENU_DEFAULT = -1;
	
	public static final int MENU_VIEW_CONSOLE = 10;
	public static final int MENU_VIEW_TRANS_ENTRY = 11;
	
	public static final int MENU_TREE_REFRESH = 20;
	public static final int MENU_TREE_ACT_REMOVE = 21;
	public static final int MENU_TREE_CLOSEVIEW = 22;
	public static final int MENU_TREE_ACT_ADD = 23;
	public static final int MENU_TREE_LOAD_REGISTER = 24;
	public static final int MENU_TREE_ADD_ACT_BUDGET = 25;
	public static final int MENU_TREE_ADD_ACT_FROM_TEMP = 26;
	public static final int MENU_TREE_REM_ACT_BUDGET = 27;
	public static final int MENU_TREE_EDIT_ACT = 28;

	public static final int MENU_FILE_EXIT = 30;
	public static final int MENU_FILE_SAVE = 31;
	public static final int MENU_FILE_IMPORT_OFX = 32;
	public static final int MENU_FILE_IMPORT_QFX = 33;
	public static final int MENU_FILE_IMPORT_QIF = 34;
	public static final int MENU_FILE_IMPORT_BTC = 35;
	public static final int MENU_FILE_IMPORT_RULES = 36;
	public static final int MENU_FILE_BACKUP = 37;
	public static final int MENU_FILE_RESTORE = 38;
	public static final int MENU_FILE_IMPORT_CSV = 39;
	
	public static final int MENU_REPORT_EXECUTE = 40;
	public static final int MENU_REPORT_SAVE = 41;
	
	public static final int MENU_EDIT_ADD_ACCOUNT = 42;
	public static final int MENU_EDIT_ADD_BUDGET = 43;
	public static final int MENU_EDIT_ADD_CURRENCY = 44;
	
	public static final int MENU_CONSOLE_CLEAR = 50;
	public static final int MENU_CONSOLE_CLOSE = 51;
	
	public static final int MENU_TOPIC_ADD = 60;
	public static final int MENU_TOPIC_SELECTALL = 61;
	public static final int MENU_TOPIC_CONFIG = 62;
	
	public static final int MENU_SERVER_NEW = 70;
	public static final int MENU_SERVER_EDIT = 71;
	public static final int MENU_SERVER_REMOVE = 72;
	
	public static final int MENU_OPTIONS_CONFIG = 80;
	
	public static final int MENU_HELP_CONTENTS = 90;
	public static final int MENU_HELP_ABOUT = 91;
	
	public static final int MENU_REPORTS_MONTHLY_BALANCE = 100;
	public static final int MENU_REPORTS_PRIOR_MONTHLY_BALANCE = 101;
	public static final int MENU_REPORTS_ACCOUNT_HISTORY = 102;
	public static final int MENU_REPORTS_BUDGET_PERFORMANCE = 103;
	public static final int MENU_REPORTS_BUDGET_PERFORMANCE_ACT = 104;
	public static final int MENU_REPORTS_INCOME_VS_EXPENSE = 105;
	
	public static final int MENU_BUDGET_SAVE = 120;
	
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
	public static SqlMapClient sqlMap = null;
	
	//Load language - use default for now
	public static LangLoader PHRASES = null;
	
	public static String bitcoinUser = null;
	public static String bitcoinPwd = null;
	public static String importFile = null;
	public static String csvConfig = null;
	
	public static Options options = null;
	
}
