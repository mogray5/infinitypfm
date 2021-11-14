/*
 * Copyright (c) 2005-2013 Wayne Gray All rights reserved
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
package org.infinitypfm.ui.view.dialogs;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.ImportRule;
import org.infinitypfm.types.ImportRuleNames;

public class ImportRulesDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	Text txtKeyword = null;
	Label lblRules = null;
	Combo cmbRules = null;
	Label lblAccounts = null;
	Combo cmbAccounts = null;
	List lstRules = null;
	Button cmdAdd = null;
	Button cmdRemove = null;
	Button cmdClose = null;

	@Override
	protected void LoadUI(Shell sh) {

		txtKeyword = new Text(sh, SWT.NONE);
		lblRules = new Label(sh, SWT.NONE);
		lblRules.setText(MM.PHRASES.getPhrase("242"));
		cmbRules = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);
		lblAccounts = new Label(sh, SWT.NONE);
		lblAccounts.setText(MM.PHRASES.getPhrase("241"));
		cmbAccounts = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);
		lstRules = new List(sh, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		cmdAdd = new Button(sh, SWT.PUSH);
		cmdAdd.setText(MM.PHRASES.getPhrase("45"));
		cmdAdd.addSelectionListener(cmdAdd_onClick);
		cmdRemove = new Button(sh, SWT.PUSH);
		cmdRemove.setText(MM.PHRASES.getPhrase("6"));
		cmdRemove.addSelectionListener(cmdRemove_onClick);
		cmdClose = new Button(sh, SWT.PUSH);
		cmdClose.setText(MM.PHRASES.getPhrase("54"));
		cmdClose.addSelectionListener(cmdClose_onClick);

		LoadAccounts();
		LoadConditions();
		LoadImportRules();
	}

	@Override
	protected void LoadLayout() {

		FormData lblrulesdata = new FormData();
		lblrulesdata.top = new FormAttachment(0, 20);
		lblrulesdata.left = new FormAttachment(0, 20);
		lblRules.setLayoutData(lblrulesdata);

		FormData cmbrulesdata = new FormData();
		cmbrulesdata.top = new FormAttachment(0, 15);
		cmbrulesdata.left = new FormAttachment(lblRules, 20);
		cmbRules.setLayoutData(cmbrulesdata);

		FormData txtkeyworddata = new FormData();
		txtkeyworddata.top = new FormAttachment(0, 20);
		txtkeyworddata.left = new FormAttachment(cmbRules, 20);
		txtkeyworddata.right = new FormAttachment(cmbRules, 300);
		txtKeyword.setLayoutData(txtkeyworddata);

		FormData lblaccountsdata = new FormData();
		lblaccountsdata.top = new FormAttachment(0, 20);
		lblaccountsdata.left = new FormAttachment(txtKeyword, 20);
		lblAccounts.setLayoutData(lblaccountsdata);

		FormData cmbaccountsdata = new FormData();
		cmbaccountsdata.top = new FormAttachment(0, 15);
		cmbaccountsdata.left = new FormAttachment(lblAccounts, 20);
		cmbAccounts.setLayoutData(cmbaccountsdata);

		FormData cmdadddata = new FormData();
		cmdadddata.top = new FormAttachment(lblRules, 20);
		cmdadddata.left = new FormAttachment(35, 20);
		cmdadddata.right = new FormAttachment(35, 90);
		cmdAdd.setLayoutData(cmdadddata);

		FormData cmdremovedata = new FormData();
		cmdremovedata.top = new FormAttachment(lblRules, 20);
		cmdremovedata.left = new FormAttachment(cmdAdd, 10);
		cmdRemove.setLayoutData(cmdremovedata);

		FormData listrulesdata = new FormData();
		listrulesdata.top = new FormAttachment(cmdRemove, 10);
		listrulesdata.left = new FormAttachment(0, 20);
		listrulesdata.right = new FormAttachment(100, -20);
		listrulesdata.bottom = new FormAttachment(100, -50);
		lstRules.setLayoutData(listrulesdata);

		FormData cmdclosedata = new FormData();
		cmdclosedata.top = new FormAttachment(lstRules, 10);
		cmdclosedata.left = new FormAttachment(45, 0);
		cmdclosedata.right = new FormAttachment(35, 140);
		cmdClose.setLayoutData(cmdclosedata);

	}

	private void LoadAccounts() {

		try {
			Account act = null;
			@SuppressWarnings("rawtypes")
			java.util.List list = MM.sqlMap.selectList(
					"getAllAccountsByType", null);

			cmbAccounts.removeAll();

			for (int i = 0; i < list.size(); i++) {
				act = (Account) list.get(i);
				cmbAccounts.add(act.getActName());
				cmbAccounts.setData(act.getActName(), act);

			}

		} catch (Exception se) {
			InfinityPfm.LogMessage(se.getMessage());
		}

	}

	private void LoadConditions() {

		cmbRules.add(ImportRuleNames.STARTSWITH);
		cmbRules.add(ImportRuleNames.ENDSWITH);
		cmbRules.add(ImportRuleNames.CONTAINS);

		cmbRules.select(2);

	}

	private void LoadImportRules() {

		lstRules.removeAll();
		String ruleLabel = null;
		ImportRule rule = null;

		try {
			@SuppressWarnings("rawtypes")
			java.util.List rules = (java.util.List) MM.sqlMap
					.selectList("getImportRules");

			for (int i = 0; i < rules.size(); i++) {
				rule = (ImportRule) rules.get(i);
				ruleLabel = rule.getRuleName() + "\t-->\t" + rule.getKeyword()
						+ "\t-->\t<" + MM.PHRASES.getPhrase("241") + ">\t-->\t"
						+ rule.getActName();
				lstRules.add(ruleLabel);
				lstRules.setData(ruleLabel, rule);
			}

		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}

	@Override
	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("240"));
		shell.setSize(800, 400);
		this.CenterWindow();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return 1;
	}

	/*
	 * Listeners
	 */
	SelectionAdapter cmdAdd_onClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			if (txtKeyword.getText() != null
					&& txtKeyword.getText().length() > 0) {

				if (cmbAccounts.getText().length() > 0) {

					ImportRule rule = new ImportRule();
					Account account = (Account) cmbAccounts.getData(cmbAccounts
							.getText());
					rule.setActId(account.getActId());
					rule.setKeyword(txtKeyword.getText());
					rule.setRuleName(cmbRules.getText());

					// Make sure rule doesn't already exist
					try {
						ImportRule ruleCheck = (ImportRule) MM.sqlMap
								.selectOne("getImportRule", rule);

						if (ruleCheck == null) {

							MM.sqlMap.insert("insertImportRule", rule);
							LoadImportRules();
						}

					} catch (Exception e1) {
						InfinityPfm.LogMessage(e1.getMessage(), true);
					}

				}

			}

		}
	};

	SelectionAdapter cmdRemove_onClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			String[] selectedRules = lstRules.getSelection();
			ImportRule rule = null;

			for (int i = 0; i < selectedRules.length; i++) {

				rule = (ImportRule) lstRules.getData(selectedRules[i]);
				try {
					MM.sqlMap.delete("deleteImportrule", rule);
				} catch (Exception e1) {
					InfinityPfm.LogMessage(e1.getMessage(), true);
				}
			}

			LoadImportRules();

		}
	};

	SelectionAdapter cmdClose_onClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			shell.dispose();
		}
	};

}
