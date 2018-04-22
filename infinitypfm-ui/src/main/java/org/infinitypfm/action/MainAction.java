/*
 * Copyright (c) 2005-2018 Wayne Gray All rights reserved
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

package org.infinitypfm.action;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Budget;
import org.infinitypfm.core.data.BudgetDetail;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.data.DataHandler;
import org.infinitypfm.data.Database;
import org.infinitypfm.data.ReportData;
import org.infinitypfm.data.imports.BaseImport;
import org.infinitypfm.data.imports.BitcoinImport;
import org.infinitypfm.data.imports.BitcoinImportConfig;
import org.infinitypfm.data.imports.FileImportConfig;
import org.infinitypfm.data.imports.ImportConfig;
import org.infinitypfm.data.imports.OfxImport;
import org.infinitypfm.data.imports.QfxImport;
import org.infinitypfm.data.imports.QifImport;
import org.infinitypfm.exception.AccountException;
import org.infinitypfm.exception.ConfigurationException;
import org.infinitypfm.reporting.BaseReport;
import org.infinitypfm.reporting.ReportFactory;
import org.infinitypfm.ui.view.dialogs.AboutDialog;
import org.infinitypfm.ui.view.dialogs.AccountSelectorDialog;
import org.infinitypfm.ui.view.dialogs.BaseDialog;
import org.infinitypfm.ui.view.dialogs.DefaultAccountSelectorDialog;
import org.infinitypfm.ui.view.dialogs.HelpDialog;
import org.infinitypfm.ui.view.dialogs.ImportDialog;
import org.infinitypfm.ui.view.dialogs.ImportRulesDialog;
import org.infinitypfm.ui.view.dialogs.InfoDialog;
import org.infinitypfm.ui.view.dialogs.MessageDialog;
import org.infinitypfm.ui.view.dialogs.NewAccountDialog;
import org.infinitypfm.ui.view.dialogs.NewCurrencyDialog;
import org.infinitypfm.ui.view.dialogs.OptionsDialog;
import org.infinitypfm.ui.view.dialogs.TransactionDialog;
import org.infinitypfm.ui.view.views.BaseView;
import org.infinitypfm.ui.view.views.ReportView;
import org.infinitypfm.util.FileHandler;

/**
 * This class contains handlers for the majority of menu item actions.
 * 
 */
public class MainAction {

	public MainAction() {
		super();
	}

	public void ProcessMenuItem(int item) {

		int selectedAccount = -1; 
				
		if (InfinityPfm.qzMain.getTrMain().getSelectedAccount() != null) {
			selectedAccount = InfinityPfm.qzMain.getTrMain().getSelectedAccount().getActId();
		}
		
		switch (item) {
		case MM.MENU_FILE_EXIT:
			InfinityPfm.shMain.dispose();
			break;
		case MM.MENU_FILE_SAVE:
			break;
		case MM.MENU_FILE_IMPORT_QFX:
		case MM.MENU_FILE_IMPORT_OFX:
		case MM.MENU_FILE_IMPORT_QIF:
		case MM.MENU_FILE_IMPORT_BTC:
			this.LoadImportDialog(item, selectedAccount);
			break;
		case MM.MENU_FILE_IMPORT_RULES:
			this.LoadImportRulesDialog();
			break;
		case MM.MENU_EDIT_ADD_ACCOUNT:
			this.LoadNewAccountDialog();
			break;
		case MM.MENU_EDIT_ADD_BUDGET:
			this.AddNewBudget();
			break;
		case MM.MENU_EDIT_ADD_CURRENCY:
			this.AddNewCurrency();
			break;
		case MM.MENU_VIEW_TRANS_ENTRY:
			this.LoadTransactionEntryDialog();
			break;
		case MM.MENU_OPTIONS_CONFIG:
			this.LoadOptions();
			break;
		case MM.MENU_HELP_ABOUT:
			this.LoadAboutDialog();
			break;
		case MM.MENU_TREE_ACT_ADD:
			this.LoadNewAccountDialog();
			break;
		case MM.MENU_TREE_EDIT_ACT:
			LoadEditAccountDialog();
			break;
		case MM.MENU_TREE_ADD_ACT_FROM_TEMP:
			this.LoadNewAccountSelectorDialog();
			break;
		case MM.MENU_TREE_REFRESH:
			this.RefreshCurrentView();
			break;
		case MM.MENU_TREE_CLOSEVIEW:
			this.CloseCurrentView();
			break;
		case MM.MENU_TREE_ACT_REMOVE:
			this.DeleteAccount();
			break;
		case MM.MENU_HELP_CONTENTS:
			this.LoadHelp();
			break;
		case MM.MENU_CONSOLE_CLEAR:
			this.ClearConsole();
			break;
		case MM.MENU_TREE_ADD_ACT_BUDGET:
			this.AddToBudget();
			break;
		case MM.MENU_TREE_REM_ACT_BUDGET:
			this.RemoveFromBudget();
			break;
		case MM.MENU_BUDGET_SAVE:

			break;
		case MM.MENU_REPORT_EXECUTE:
		case MM.MENU_REPORTS_MONTHLY_BALANCE:
			try {
				this.RunReport(MM.THIS_MONTH);
			} catch (IOException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			}
			break;
		case MM.MENU_REPORTS_PRIOR_MONTHLY_BALANCE:
			try {
				this.RunReport(MM.LAST_MONTH);
			} catch (IOException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			}
			break;
		case MM.MENU_REPORTS_ACCOUNT_HISTORY:
			try {
				this.RunReport(MM.MENU_REPORTS_ACCOUNT_HISTORY);
			} catch (IOException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			}
			break;
		case MM.MENU_REPORTS_BUDGET_PERFORMANCE:
			try {
				this.RunReport(MM.MENU_REPORTS_BUDGET_PERFORMANCE);
			} catch (IOException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			}
			break;
		case MM.MENU_REPORTS_BUDGET_PERFORMANCE_ACT:
			try {
				this.RunReport(MM.MENU_REPORTS_BUDGET_PERFORMANCE_ACT);
			} catch (IOException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			}
			break;
		case MM.MENU_REPORTS_INCOME_VS_EXPENSE:
			try {
				this.RunReport(MM.MENU_REPORTS_INCOME_VS_EXPENSE);
			} catch (IOException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			}
			break;
		case MM.MENU_REPORT_SAVE:
			this.SaveReport();
			break;
		case MM.MENU_VIEW_CONSOLE:
			this.ToggleConsole(true);
			break;
		case MM.MENU_TREE_LOAD_REGISTER:
		case MM.VIEW_REGISTER:
			this.LoadView(MM.VIEW_REGISTER);
			break;
		case MM.VIEW_BUDGET:
			this.LoadView(MM.VIEW_BUDGET);
			break;
		case MM.MENU_CONSOLE_CLOSE:
			ToggleConsole(false);
			break;
		case MM.MENU_TOPIC_CONFIG:

			break;
		case MM.VIEW_RECURRENCE:
			this.LoadView(MM.VIEW_RECURRENCE);
			break;
		case MM.VIEW_CURRENCY:
			this.LoadView(MM.VIEW_CURRENCY);
			break;
		case MM.MENU_TOPIC_ADD:

			break;
		case MM.MENU_FILE_BACKUP:
			this.BackupDatabase();
			break;
		case MM.MENU_FILE_RESTORE:
			break;

		}
	}

	public void LoadView(int iViewID) {

		BaseView vw = InfinityPfm.qzMain.getVwMain().getView(iViewID);
		InfinityPfm.qzMain.getVwMain().LoadView(vw);
	}

	public void CloseCurrentView() {
		InfinityPfm.qzMain.getVwMain().UnloadCurrentView();
		InfinityPfm.qzMain.getVwMain().getParent().layout(true);
	}

	public void RefreshCurrentView() {
		InfinityPfm.qzMain.getVwMain().RefreshCurrentView();
	}

	public void LoadOptions() {
		OptionsDialog options = new OptionsDialog();
		options.Open();
	}

	public void LoadAboutDialog() {
		BaseDialog about = new AboutDialog();
		about.Open();
	}

	public void LoadNewAccountDialog() {
		BaseDialog act = new NewAccountDialog();
		act.Open();
	}
	
	public void LoadEditAccountDialog() {
		
		Account account = InfinityPfm.qzMain.getTrMain().getSelectedAccount();

		if (account != null) {
			BaseDialog dialog = new NewAccountDialog();
			((NewAccountDialog)dialog).setAccount(account); 
			dialog.Open();
		}
		
	}
	public void LoadNewAccountSelectorDialog() {
		BaseDialog act = new DefaultAccountSelectorDialog();
		act.Open();
	}

	@SuppressWarnings("rawtypes")
	public void AddToBudget() {
		AccountSelectorDialog dlg = new AccountSelectorDialog();
		dlg.Open();

		if (dlg.getAccountName() == null) {
			return;
		}

		String acctName = dlg.getAccountName();
		try {
			Account account = (Account) MM.sqlMap.queryForObject(
					"getAccountForName", acctName);
			if (account != null) {
				BudgetDetail detail = new BudgetDetail();
				detail.setActName(acctName);
				Budget budget = InfinityPfm.qzMain.getTrMain()
						.getSelectedBudget();
				if (budget != null) {
					detail.setBudgetId(budget.getBudgetId());

					List result = (List) MM.sqlMap.queryForList(
							"getBudgetDetailByName", detail);
					if (result.size() == 0) {
						DataHandler dataHandler = new DataHandler();
						dataHandler.AddAccountToBudget(budget, account);
						LoadView(MM.VIEW_BUDGET);
					}
				}
			}
		} catch (SQLException e) {
			MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
					e.getMessage());
			show.Open();

		}

	}

	@SuppressWarnings("rawtypes")
	public void RemoveFromBudget() {
		AccountSelectorDialog dlg = new AccountSelectorDialog();
		dlg.Open();
		String acctName = dlg.getAccountName();
		try {
			Account account = (Account) MM.sqlMap.queryForObject(
					"getAccountForName", acctName);
			if (account != null) {
				BudgetDetail detail = new BudgetDetail();
				detail.setActName(acctName);
				Budget budget = InfinityPfm.qzMain.getTrMain()
						.getSelectedBudget();
				if (budget != null) {
					detail.setBudgetId(budget.getBudgetId());
					detail.setActId(account.getActId());
					List result = (List) MM.sqlMap.queryForList(
							"getBudgetDetailByName", detail);
					if (result.size() > 0) {
						DataHandler dataHandler = new DataHandler();
						dataHandler.DeleteBudgetDetail(detail);
						LoadView(MM.VIEW_BUDGET);
					}
				}
			}
		} catch (SQLException e) {
			MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
					e.getMessage());
			show.Open();
		}

	}

	public void LoadImportDialog(int importType, int showAccount) {

		BaseImport importer = null;
		ImportConfig config = null;

		if (importType == MM.MENU_FILE_IMPORT_QFX) {
			config = new FileImportConfig();
			importer = new QfxImport();
		} else if (importType == MM.MENU_FILE_IMPORT_OFX) {
			config = new FileImportConfig();
			importer = new OfxImport();
		} else if (importType == MM.MENU_FILE_IMPORT_QIF) {
			config = new FileImportConfig();
			importer = new QifImport();
		} else if (importType == MM.MENU_FILE_IMPORT_BTC) {
			config = new BitcoinImportConfig();
			importer = new BitcoinImport();
		} else if (importType == MM.MENU_FILE_IMPORT_CSV) {
			
			
		} else {
			MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
					MM.PHRASES.getPhrase("21"));
			show.Open();
			return;
		}

		try {
			
			List<Transaction> trans = importer.ImportFile(config);
			ShowImportDialog(trans, showAccount);
			
		} catch (FileNotFoundException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
		} catch (ParseException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
		} catch (ConfigurationException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
		}

	}
	
	public void LoadImportRulesDialog(){
		ImportRulesDialog rulesDialog = new ImportRulesDialog();
		rulesDialog.Open();
	}
		
	private void ShowImportDialog(List<Transaction> trans, int showAccount){
		
		ImportDialog importDialog = new ImportDialog(trans, showAccount);
		importDialog.Open();
		InfinityPfm.qzMain.getVwMain().RefreshCurrentView();

	}

	public void LoadTransactionEntryDialog() {
		Account act = InfinityPfm.qzMain.getTrMain().getSelectedAccount();
		if (act != null) {
			TransactionDialog dlg = new TransactionDialog(act);
			dlg.Open();
		} else {
			MessageDialog errDlg = new MessageDialog(MM.DIALOG_INFO,
					MM.APPTITLE, MM.PHRASES.getPhrase("39"));
			errDlg.Open();
		}

	}

	public void LoadHelp() {
		try {
			HelpDialog help = new HelpDialog();
			help.Open();
		} catch (Exception e) {
			MessageDialog msgErr = new MessageDialog(MM.DIALOG_INFO,
					MM.APPTITLE, MM.PHRASES.getPhrase("102") + "\n\n"
							+ MM.PHRASES.getPhrase("103") + ":  "
							+ e.getMessage());
			msgErr.Open();
			InfinityPfm.LogMessage(e.getMessage());
		}
	}

	public void ClearConsole() {
		InfinityPfm.qzMain.getMsgMain().ClearConsole();
	}

	public void ToggleConsole(boolean bShow) {
		InfinityPfm.qzMain.getMnuMain().setConsole(bShow);
		InfinityPfm.qzMain.LoadConsole(bShow);
		InfinityPfm.shMain.layout();
	}

	public void AddAccount(Account act) {
		DataHandler db = new DataHandler();
		try {
			db.AddAccount(act);
		} catch (AccountException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
		}
		InfinityPfm.qzMain.getTrMain().Reload();
	}

	public boolean EditAccount(Account act) {
		DataHandler db = new DataHandler();
		boolean bResult = true;
		
		try {
			db.EditAccount(act);
		} catch (AccountException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
			bResult = false;
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
			bResult = false;
		}
		InfinityPfm.qzMain.getTrMain().Reload();
		
		return bResult;
		
	}
	
	public void DeleteAccount() {
		MessageDialog dlg = new MessageDialog(MM.DIALOG_QUESTION, MM.APPTITLE,
				MM.PHRASES.getPhrase("76"));
		dlg.setDimensions(400, 150);
		int iResult = dlg.Open();

		if (iResult == MM.YES) {

			DataHandler db = new DataHandler();
			Account act = InfinityPfm.qzMain.getTrMain().getSelectedAccount();
			try {
				db.DeleteAccount(act);
			} catch (AccountException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			} catch (SQLException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			}
			InfinityPfm.qzMain.getTrMain().Reload();
		}
	}

	public void AddNewBudget() {
		InfoDialog info = new InfoDialog(MM.APPTITLE,
				MM.PHRASES.getPhrase("104"));

		String result = info.getInput();

		if (result == null) {
			return;
		}

		DataHandler db = new DataHandler();
		if (result != null && result.length() > 0) {
			try {
				db.AddBudget(result);
			} catch (SQLException e) {
				InfinityPfm.LogMessage(e.getMessage(), true);
			}
			InfinityPfm.qzMain.getTrMain().Reload();
		} else {
			MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
					MM.PHRASES.getPhrase("105"));
			show.Open();
		}
	}

	public void AddNewCurrency() {
		BaseDialog curr = new NewCurrencyDialog();
		curr.Open();
	}

	public void AddBudgetDetail() {
		BaseDialog dlg = new AccountSelectorDialog();
		dlg.Open();
	}

	public void RunReport(int reportType) throws IOException {
		
		File result = null;
		
		ReportData reportData = new ReportData(reportType);
		
		if (!reportData.getUserCanceled()){
		
			BaseReport report = ReportFactory.getReport(reportType);
			result = report.execute(reportData);

		}
		
		try {
			if (result != null) {
				
				if (MM.options.isReportsInBrowswer()) 
					Desktop.getDesktop().open(result);
				else 
					InfinityPfm.qzMain.getVwMain().LoadReportView(result.toURI().toURL().toString());
				
			}
			
		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}

	public void SaveReport() {
		ReportView reportView = (ReportView) InfinityPfm.qzMain.getVwMain()
				.getCurrentView();
		URI uri;
		try {
			uri = new URI(reportView.getReportUrl());

			File source = new File(uri);

			if (source.exists()) {

				FileDialog dlg = new FileDialog(InfinityPfm.shMain, SWT.SAVE);
				dlg.setText(MM.PHRASES.getPhrase("38"));
				String[] filterExt = { "*.html", "*.htm", "*.*" };
				dlg.setFilterExtensions(filterExt);
				String sFile = dlg.open();
				FileHandler fileHandler = new FileHandler();

				if (sFile != null) {

					fileHandler.copyFile2(source.getPath(), sFile);

					MessageDialog messageDialog = new MessageDialog(
							MM.DIALOG_INFO, MM.PHRASES.getPhrase("89"),
							MM.PHRASES.getPhrase("141"));

					messageDialog.Open();
				}
			}
		} catch (URISyntaxException e1) {
			InfinityPfm.LogMessage(e1.getMessage());
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}

	public void BackupDatabase() {

		DirectoryDialog dlg = new DirectoryDialog(InfinityPfm.shMain);
		dlg.setFilterPath(MM.DATPATH);
		dlg.setText(MM.PHRASES.getPhrase("20"));
		dlg.setMessage(MM.PHRASES.getPhrase("161"));
		String dir = dlg.open();
		String result = null;

		if (dir != null) {

			Database db = new Database();
			try {
				result = db.backup(dir);
			} catch (IOException e) {
				InfinityPfm.LogMessage(e.getMessage());
			} catch (ArchiveException e) {
				InfinityPfm.LogMessage(e.getMessage());
			}
		}

		if (result != null) {
			MessageDialog messageDialog = new MessageDialog(MM.DIALOG_INFO,
					MM.PHRASES.getPhrase("158"), MM.PHRASES.getPhrase("162")
							+ result);

			InfinityPfm.LogMessage(MM.PHRASES.getPhrase("162") + result);
			messageDialog.setDimensions(500, 150);
			messageDialog.Open();

		}

	}

}
