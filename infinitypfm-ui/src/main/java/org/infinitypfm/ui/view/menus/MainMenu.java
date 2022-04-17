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

package org.infinitypfm.ui.view.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.conf.MM;
import org.infinitypfm.ui.Widget;

public class MainMenu implements Widget {

	/*
	 * Widgets
	 */
	private Menu mnuMain;

	private Menu mnuFile;

	private Menu mnuEdit;

	private Menu mnuImport;

	private Menu mnuDatabase;

	private Menu mnuView;
	private Menu mnuReports;
	private MenuItem miConsole = null;
	private MenuItem miBookmarks = null;

	public MainMenu(Shell shMain) {
		super();

		/*
		 * File Menu
		 */
		MenuItem mi;
		mnuMain = new Menu(shMain, SWT.BAR);

		mi = addSelection(mnuMain, SWT.CASCADE, MM.MENU_DEFAULT,
				MM.PHRASES.getPhrase("19"), ' ');

		mnuFile = new Menu(mi);
		mi.setMenu(mnuFile);

		/*
		 * New Sub-Menu
		 */

		mi = addSelection(mnuFile, SWT.CASCADE, MM.MENU_DEFAULT,
				MM.PHRASES.getPhrase("219"), ' ');

		mnuEdit = new Menu(mi);
		mi.setMenu(mnuEdit);

		mi = addSelection(mnuEdit, SWT.PUSH, MM.MENU_EDIT_ADD_ACCOUNT,
				MM.PHRASES.getPhrase("7"), 'A');
		mi = addSelection(mnuEdit, SWT.PUSH, MM.MENU_EDIT_ADD_BUDGET,
				MM.PHRASES.getPhrase("98"), 'B');
		mi = addSelection(mnuEdit, SWT.PUSH, MM.MENU_EDIT_ADD_CURRENCY,
				MM.PHRASES.getPhrase("208"), ' ');

		mi = new MenuItem(mnuFile, SWT.SEPARATOR);

		// import sub-menu
		mi = addSelection(mnuFile, SWT.CASCADE, MM.MENU_DEFAULT,
				MM.PHRASES.getPhrase("11"), ' ');

		mnuImport = new Menu(mi);
		mi.setMenu(mnuImport);

		mi = addSelection(mnuImport, SWT.PUSH, MM.MENU_FILE_IMPORT_QFX, "QFX",
				' ');
		mi = addSelection(mnuImport, SWT.PUSH, MM.MENU_FILE_IMPORT_OFX, "OFX",
				' ');
		mi = addSelection(mnuImport, SWT.PUSH, MM.MENU_FILE_IMPORT_QIF, "QIF",
				' ');
		
		mi = addSelection(mnuImport, SWT.PUSH, MM.MENU_FILE_IMPORT_CSV,
				MM.PHRASES.getPhrase("253"), ' ');
		
		mi = addSelection(mnuImport, SWT.PUSH, MM.MENU_FILE_IMPORT_RULES,
				MM.PHRASES.getPhrase("240"), ' ');

		mi = new MenuItem(mnuFile, SWT.SEPARATOR);

		mi = addSelection(mnuFile, SWT.CASCADE, MM.MENU_DEFAULT,
				MM.PHRASES.getPhrase("160"), ' ');

		mnuDatabase = new Menu(mi);
		mi.setMenu(mnuDatabase);

		mi = addSelection(mnuDatabase, SWT.PUSH, MM.MENU_FILE_BACKUP,
				MM.PHRASES.getPhrase("158"), ' ');

		mi = addSelection(mnuDatabase, SWT.PUSH, MM.MENU_FILE_RESTORE,
				MM.PHRASES.getPhrase("159"), ' ');

		// not available yet
		mi.setEnabled(false);

		mi = new MenuItem(mnuFile, SWT.SEPARATOR);
		mi = addSelection(mnuFile, SWT.PUSH, MM.MENU_FILE_EXIT,
				MM.PHRASES.getPhrase("58"), ' ');

		/*
		 * View Menu
		 */
		mi = addSelection(mnuMain, SWT.CASCADE, MM.MENU_DEFAULT,
				MM.PHRASES.getPhrase("86"), ' ');

		mnuView = new Menu(mi);
		mi.setMenu(mnuView);

		mi = addSelection(mnuView, SWT.PUSH, MM.VIEW_CURRENCY,
				MM.PHRASES.getPhrase("224"), 'C');

		mi = addSelection(mnuView, SWT.PUSH, MM.VIEW_RECURRENCE,
				MM.PHRASES.getPhrase("220"), 'R');

		mi = addSelection(mnuView, SWT.PUSH, MM.MENU_VIEW_TRANS_ENTRY,
				MM.PHRASES.getPhrase("79"), 'T');

		mi = addSelection(mnuView, SWT.PUSH, MM.MENU_OPTIONS_CONFIG,
				MM.PHRASES.getPhrase("67"), 'O');

		if (MM.options.isEnableWallet()) {
		
			mi = new MenuItem(mnuView, SWT.SEPARATOR);
			
			mi = addSelection(mnuView, SWT.PUSH, MM.VIEW_WALLET,
				MM.PHRASES.getPhrase("276"), 'O');
		
		}
		
		mi = new MenuItem(mnuView, SWT.SEPARATOR);
		
		miConsole = addSelection(mnuView, SWT.CHECK, MM.MENU_VIEW_CONSOLE,
				MM.PHRASES.getPhrase("65"), ' ');
		
		mi = new MenuItem(mnuView, SWT.SEPARATOR);
		
		miBookmarks = addSelection(mnuView, SWT.CHECK, MM.MENU_VIEW_BOOKMARKS,
				MM.PHRASES.getPhrase("315"), ' ');

		/*
		 * Reports Menu
		 */

		mi = addSelection(mnuMain, SWT.CASCADE, MM.MENU_DEFAULT,
				MM.PHRASES.getPhrase("122"), ' ');

		mnuReports = new Menu(mi);
		mi.setMenu(mnuReports);

		mi = addSelection(mnuReports, SWT.PUSH,
				MM.MENU_REPORTS_MONTHLY_BALANCE, MM.PHRASES.getPhrase("123"),
				' ');

		mi = addSelection(
				mnuReports,
				SWT.PUSH,
				MM.MENU_REPORTS_PRIOR_MONTHLY_BALANCE,
				MM.PHRASES.getPhrase("123") + " " + MM.PHRASES.getPhrase("126"),
				' ');

		mi = addSelection(
				mnuReports,
				SWT.PUSH,
				MM.MENU_REPORTS_YEARLY_BALANCE,
				MM.PHRASES.getPhrase("311"),
				' ');
		
		mi = addSelection(mnuReports, SWT.PUSH,
				MM.MENU_REPORTS_ACCOUNT_HISTORY, MM.PHRASES.getPhrase("136"),
				' ');

		mi = addSelection(mnuReports, SWT.PUSH,
				MM.MENU_REPORTS_ACCOUNT_HISTORY_ALL_TIME, MM.PHRASES.getPhrase("273"),
				' ');
		
		mi = addSelection(mnuReports, SWT.PUSH,
				MM.MENU_REPORTS_BUDGET_PERFORMANCE,
				MM.PHRASES.getPhrase("164"), ' ');

		mi = addSelection(mnuReports, SWT.PUSH,
				MM.MENU_REPORTS_BUDGET_PERFORMANCE_ACT,
				MM.PHRASES.getPhrase("164") + " " + MM.PHRASES.getPhrase("93"),
				' ');

		mi = addSelection(mnuReports, SWT.PUSH,
				MM.MENU_REPORTS_INCOME_VS_EXPENSE, MM.PHRASES.getPhrase("221"),
				' ');

		/*
		 * Help Menu
		 */
		mi = addSelection(mnuMain, SWT.CASCADE, MM.MENU_DEFAULT,
				MM.PHRASES.getPhrase("62"), ' ');
		Menu mnuHelp = new Menu(mi);
		mi.setMenu(mnuHelp);
		mi = addSelection(mnuHelp, SWT.PUSH, MM.MENU_HELP_ABOUT,
				MM.PHRASES.getPhrase("61"), ' ');

	}

	private MenuItem addSelection(Menu mnu, int iStyle, int iIndex,
			String sCaption, char sExcel) {
		MenuItem mi;
		mi = new MenuItem(mnu, iStyle);
		mi.addListener(SWT.Selection, mnuClick);
		mi.setText(sCaption);
		if (sExcel != ' ')
			mi.setAccelerator(SWT.ALT | sExcel);
		if (iIndex >= 0) {
			mi.setData(new Integer(iIndex));
		}

		return mi;
	}

	public Menu getMenu() {
		return mnuMain;
	}

	public void setConsole(boolean bVal) {
		miConsole.setSelection(bVal);
	}
	
	public void setBookmarks(boolean bVal) {
		miBookmarks.setSelection(bVal);
	}

	/* add listener */
	Listener mnuClick = new Listener() {

		public void handleEvent(Event e) {
			MenuItem mi = (MenuItem) e.widget;
			Integer iResult = (Integer) mi.getData();

			if (iResult != null) {

				MainAction action = new MainAction();

				if (iResult.intValue() == MM.MENU_VIEW_CONSOLE) {
					if (!mi.getSelection()) {
						action.ProcessMenuItem(MM.MENU_CONSOLE_CLOSE);
					} else {
						action.ProcessMenuItem(iResult.intValue());
					}
				} else if (iResult.intValue() == MM.MENU_VIEW_BOOKMARKS) {
					if (!mi.getSelection()) {
						action.ProcessMenuItem(MM.MENU_BOOKMARKS_CLOSE);
					} else {
						action.ProcessMenuItem(iResult.intValue());
					}
				} else {
					action.ProcessMenuItem(iResult.intValue());
				}

			}
		}
	};

	public void QZDispose() {

		if (!mnuMain.isDisposed()) {
			mnuMain.dispose();
		}
	}

	public void Refresh() {

	}
}
