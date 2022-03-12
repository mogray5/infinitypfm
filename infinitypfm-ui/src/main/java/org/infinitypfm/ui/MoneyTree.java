/*
 * Copyright (c) 2005-2022 Wayne Gray All rights reserved
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

package org.infinitypfm.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.action.TreeNodeInfo;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Budget;
import org.infinitypfm.ui.view.menus.BudgetMenu;
import org.infinitypfm.ui.view.menus.TreeMenu;

/**
 * Account tree
 */
public class MoneyTree {

	private Tree trMain;
	private TreeMenu mnuPopup = null;
	private BudgetMenu mnuBudget = null;
	private TreeNodeInfo bookmarks = null;
	private Shell shMain = null;
	private Account actSelected = null;
	private Budget budgetSelected = null;

	public MoneyTree(Shell sh) {
		super();

		shMain = sh;

		trMain = new Tree(shMain, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.SINGLE);
		trMain.addSelectionListener(on_Click);

		bookmarks = new TreeNodeInfo();

		// init popup menus
		mnuPopup = new TreeMenu(shMain);
		mnuBudget = new BudgetMenu(shMain);

	}

	public void Reload() {
		BackgroundLoader bl = new BackgroundLoader();
		new Thread(bl).start();
	}

	private void ReLoadTree() {

		// clear the tree
		trMain.removeAll();

		// set up root nodes
		TreeItem budgetNode = new TreeItem(trMain, 0);
		budgetNode.setText(MM.PHRASES.getPhrase("97"));
		budgetNode.setData("budget");
		budgetNode.setImage(InfinityPfm.imMain.getImage(MM.IMG_LOGO_ICON_SMALL));
		bookmarks.setBudgetNode(budgetNode);
		TreeItem bankNode = new TreeItem(trMain, 1);
		bankNode.setText(MM.PHRASES.getPhrase("8"));
		bankNode.setData("bank");
		bankNode.setImage(InfinityPfm.imMain.getImage(MM.IMG_LOGO_ICON_SMALL));
		bookmarks.setBankAccountNode(bankNode);
		TreeItem liabilityNode = new TreeItem(trMain, 2);
		liabilityNode.setText(MM.PHRASES.getPhrase("53"));
		liabilityNode.setData("liability");
		liabilityNode.setImage(InfinityPfm.imMain.getImage(MM.IMG_LOGO_ICON_SMALL));
		TreeItem expenseNode = new TreeItem(trMain, 3);
		expenseNode.setText(MM.PHRASES.getPhrase("9"));
		expenseNode.setData("expense");
		expenseNode.setImage(InfinityPfm.imMain.getImage(MM.IMG_LOGO_ICON_SMALL));
		bookmarks.setExpenseNode(expenseNode);
		TreeItem incomeNode = new TreeItem(trMain, 4);
		incomeNode.setText(MM.PHRASES.getPhrase("10"));
		incomeNode.setData("income");
		incomeNode.setImage(InfinityPfm.imMain.getImage(MM.IMG_LOGO_ICON_SMALL));

		// TODO: Remove duplication of code below for each account type.

		try {

			TreeItem ti = null;
			Account act = null;

			// add bank accounts
			@SuppressWarnings("rawtypes")
			List bankList = MM.sqlMap
					.selectList("getAccountsForType", "Bank");

			if (bankList != null) {

				for (int i = 0; i < bankList.size(); i++) {
					act = (Account) bankList.get(i);
					ti = new TreeItem(bankNode, i);
					ti.setText(act.getActName());
					ti.setData(act);
					ti.setImage(InfinityPfm.imMain.getImage(MM.IMG_SPREADSHEET));
					System.out.println(act.getActName() + ":"
							+ Integer.toString(act.getActId()));

				}

			}

			// add liability accounts
			@SuppressWarnings("rawtypes")
			List liabilityList = MM.sqlMap.selectList("getAccountsForType",
					"Liability");

			if (liabilityList != null) {

				for (int i = 0; i < liabilityList.size(); i++) {
					act = (Account) liabilityList.get(i);
					ti = new TreeItem(liabilityNode, i);
					ti.setText(act.getActName());
					ti.setData(act);
					ti.setImage(InfinityPfm.imMain.getImage(MM.IMG_STAR_EMPTY));
					System.out.println(act.getActName() + ":"
							+ Integer.toString(act.getActId()));

				}

			}

			// add expense accounts
			@SuppressWarnings("rawtypes")
			List expenseList = MM.sqlMap.selectList("getAccountsForType",
					"Expense");

			if (expenseList != null) {

				for (int i = 0; i < expenseList.size(); i++) {
					act = (Account) expenseList.get(i);
					ti = new TreeItem(expenseNode, i);
					ti.setText(act.getActName());
					ti.setData(act);
					ti.setImage(InfinityPfm.imMain.getImage(MM.IMG_STAR_EMPTY));
				}

			}

			// add income accounts
			@SuppressWarnings("rawtypes")
			List incomeList = MM.sqlMap.selectList("getAccountsForType",
					"Income");

			if (incomeList != null) {

				for (int i = 0; i < incomeList.size(); i++) {
					act = (Account) incomeList.get(i);
					ti = new TreeItem(incomeNode, i);
					ti.setText(act.getActName());
					ti.setData(act);
					ti.setImage(InfinityPfm.imMain.getImage(MM.IMG_STAR_FULL));
				}

			}

			// add budgets
			@SuppressWarnings("rawtypes")
			List budgetList = MM.sqlMap.selectList("getAllBudgets", null);

			if (budgetList != null) {
				Budget budget = null;
				for (int i = 0; i < budgetList.size(); i++) {
					budget = (Budget) budgetList.get(i);
					ti = new TreeItem(budgetNode, i);
					ti.setText(budget.getBudgetName());
					ti.setData(budget);
					ti.setImage(InfinityPfm.imMain.getImage(MM.IMG_CALCULATOR));
				}
			}

		} catch (Exception se) {
			InfinityPfm.LogMessage(se.getMessage());
		}

		bankNode.setExpanded(true);
		liabilityNode.setExpanded(true);
		incomeNode.setExpanded(true);
		expenseNode.setExpanded(true);
		budgetNode.setExpanded(true);

	}

	public Tree getTree() {
		return trMain;
	}

	private TreeItem getItem(String sName) {
		TreeItem ti = null;
		TreeItem[] aTI = trMain.getItems();

		for (int i = 0; i < trMain.getItemCount(); i++) {
			if (sName.equals(aTI[i].getText())) {
				ti = aTI[i];
			}
		}
		return ti;
	}

	public void setItemSelected(String sName) {
		TreeItem ti = getItem(sName);
		if (ti != null) {
			TreeItem[] aTI = { ti };
			trMain.setSelection(aTI);
			trMain.showItem(ti);
		}
	}

	public Account getSelectedAccount() {
		return actSelected;
	}

	public Budget getSelectedBudget() {
		return budgetSelected;
	}

	public void QZDispose() {

		mnuPopup.QZDispose();

		if (!trMain.isDisposed()) {
			trMain.dispose();
		}
	}

	public void setConnected(String qs, boolean bConnected) {

	}

	private class BackgroundLoader implements Runnable {

		public void run() {

			Display.getDefault().syncExec(new BusyStatus(true));
			Display.getDefault().syncExec(new TreeLoader());
			Display.getDefault().syncExec(new BusyStatus(false));

		}
	}

	private class TreeLoader implements Runnable {

		public TreeLoader() {

		}

		public void run() {

			ReLoadTree();
		}
	}

	private class BusyStatus implements Runnable {

		boolean busy = false;

		public BusyStatus(boolean bStatus) {
			busy = bStatus;
		}

		public void run() {
			InfinityPfm.qzMain.getStMain().setBusy(busy);
		}

	}

	/*
	 * Listeners
	 */

	SelectionAdapter on_Click = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			TreeItem ti = (TreeItem) e.item;
			Object obj = ti.getData();

			trMain.setMenu(null);

			if (obj == null) {
				return;
			}

			MainAction action = new MainAction();

			if (obj instanceof Account) {
				trMain.setMenu(mnuPopup.getMenu());
				actSelected = (Account) obj;
				budgetSelected = null;
				action.LoadView(MM.VIEW_REGISTER);
			} else if (obj instanceof Budget) {
				trMain.setMenu(mnuBudget.getMenu());
				actSelected = null;
				budgetSelected = (Budget) obj;
				action.LoadView(MM.VIEW_BUDGET);
			} else if (obj instanceof String) {
				String acctType = obj.toString();
				if (acctType.equalsIgnoreCase("bank")
						|| acctType.equalsIgnoreCase("expense")
						|| acctType.equalsIgnoreCase("liability")
						|| acctType.equalsIgnoreCase("income")) {

					trMain.setMenu(mnuPopup.getMenu());

				}
			}

		}
	};

}
