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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;

public class DefaultAccountSelectorDialog extends BaseDialog {

	@SuppressWarnings("rawtypes")
	private java.util.List allActList = null;
	private Tree tree = null;
	private Button cmdSave = null;
	private Button cmdCancel = null;

	public DefaultAccountSelectorDialog() {
		super();

		try {
			allActList = MM.sqlMap.selectList("getAccountTemplates");
		} catch (Exception se) {
			InfinityPfm.LogMessage(se.getMessage());
		}

	}

	@Override
	protected void LoadUI(Shell sh) {

		tree = new Tree(shell, SWT.BORDER | SWT.CHECK);
		if (allActList != null) {

			String oldAcctCategory = null;
			Account account = null;
			TreeItem categoryItem = null;
			for (int i = 0; i < allActList.size(); i++) {
				account = (Account) allActList.get(i);
				if (!account.getActTypeName().equalsIgnoreCase(oldAcctCategory)) {

					if (categoryItem != null) {
						categoryItem.setExpanded(true);
					}
					categoryItem = new TreeItem(tree, SWT.NONE);
					categoryItem.setText(account.getActTypeName());
					categoryItem.setData(new Integer(account.getActTypeId()));
					oldAcctCategory = account.getActTypeName();
				}

				TreeItem accountItem = new TreeItem(categoryItem, SWT.NONE);
				accountItem.setText(account.getActName());

			}
			// expand the last category
			if (categoryItem != null) {
				categoryItem.setExpanded(true);
			}
		}

		cmdSave = new Button(sh, SWT.PUSH);
		cmdSave.setText(MM.PHRASES.getPhrase("14"));
		cmdSave.addSelectionListener(cmdSave_OnClick);
		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);

		tree.addSelectionListener(on_Click);
	}

	@Override
	protected void LoadLayout() {

		FormData treetdata = new FormData();
		treetdata.top = new FormAttachment(0, 10);
		treetdata.left = new FormAttachment(0, 10);
		treetdata.right = new FormAttachment(100, -10);
		treetdata.bottom = new FormAttachment(100, -40);
		tree.setLayoutData(treetdata);

		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(tree, 5);
		cmdcanceldata.right = new FormAttachment(cmdSave, -10);
		cmdCancel.setLayoutData(cmdcanceldata);

		FormData cmdsavedata = new FormData();
		cmdsavedata.top = new FormAttachment(tree, 5);
		cmdsavedata.right = new FormAttachment(100, -10);
		cmdSave.setLayoutData(cmdsavedata);
		cmdSave.setEnabled(false);

	}

	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("133") + " " + MM.APPTITLE);

		shell.setSize(300, 400);
		this.CenterWindow();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return 1;
	}

	SelectionAdapter on_Click = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			TreeItem ti = (TreeItem) e.item;
			TreeItem parent = ti.getParentItem();

			if (parent == null) {
				TreeItem[] items = ti.getItems();
				for (int i = 0; i < items.length; i++) {
					TreeItem item = items[i];
					item.setChecked(ti.getChecked());
				}
			}

			if (ti.getChecked()) {

				cmdSave.setEnabled(true);

			}

		}
	};

	/*
	 * Events
	 */

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			shell.dispose();
		}
	};

	SelectionAdapter cmdSave_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			InfinityPfm.qzMain.getStMain().setBusy(true);
			InfinityPfm.qzMain.getStMain()
					.setStatus(MM.PHRASES.getPhrase("89"));

			// confirm save
			MessageDialog msg = new MessageDialog(MM.DIALOG_QUESTION,
					MM.APPTITLE, MM.PHRASES.getPhrase("134"));

			int answer = msg.Open();

			if (answer == MM.YES) {

				TreeItem[] categories = tree.getItems();
				Account account = null;
				for (int i = 0; i < categories.length; i++) {

					TreeItem category = categories[i];
					TreeItem[] items = category.getItems();

					for (int j = 0; j < items.length; j++) {

						TreeItem ti = items[j];
						if (ti.getChecked()) {
							account = new Account();
							account.setActName(ti.getText());
							account.setActTypeName(ti.getParentItem().getText());
							account.setActBalance(0);
							Integer catId = (Integer) ti.getParentItem()
									.getData();
							account.setActTypeId(catId.intValue());
							try {
								MM.sqlMap.insert("insertAccount", account);
							} catch (Exception se) {
								InfinityPfm.LogMessage(se.getMessage());
							}

						}

					}
				}
			}

			InfinityPfm.qzMain.getStMain()
					.setStatus(MM.PHRASES.getPhrase("87"));
			InfinityPfm.qzMain.getStMain().setBusy(false);
			InfinityPfm.qzMain.getTrMain().Reload();

			if (answer != MM.CANCEL) {
				shell.dispose();
			}

		}
	};
}
