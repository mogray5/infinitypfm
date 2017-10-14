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
package org.infinitypfm.ui.view.toolbars;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Budget;
import org.infinitypfm.data.DataHandler;
import org.infinitypfm.ui.view.dialogs.MessageDialog;
import org.infinitypfm.ui.view.views.BudgetView;

/**
 * @author wayne
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BudgetToolbar extends BaseToolbar {

    BudgetView vwBudget = null;

	/**
	 * @param sh
	 */
	public BudgetToolbar(Composite sh) {
		super(sh);
		vwBudget = (BudgetView) sh;
	}

	/* (non-Javadoc)
	 * @see net.mogray.infinitypfm.ui.view.toolbars.BaseToolbar#LoadButtons()
	 */
	protected void LoadButtons() {
		
		//change the default selection listener for this button because we need
        //more interaction with the attached view
		ToolItem item = addButton(MM.IMG_SAVE, MM.PHRASES.getPhrase("127"),
				MM.MENU_BUDGET_SAVE, true);

		item.addSelectionListener(new OnSave());

		addButton(MM.IMG_CLOSE_SMALL, MM.PHRASES.getPhrase("54"),
				MM.MENU_TREE_CLOSEVIEW, true);

	}

	/* (non-Javadoc)
	 * @see net.mogray.infinitypfm.ui.view.toolbars.BaseToolbar#Init(org.eclipse.swt.widgets.Composite)
	 */
	protected void Init(Composite cm) {
		cbMain = new ToolBar(cm, SWT.FLAT);

	}

	/* (non-Javadoc)
	 * @see net.mogray.infinitypfm.ui.Widget#Refresh()
	 */
	public void Refresh() {
		// TODO Auto-generated method stub

	}

    class OnSave extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {

            Budget budget = InfinityPfm.qzMain.getTrMain().getSelectedBudget();
            DataHandler handler = new DataHandler();
           

        	//confirm save
			MessageDialog msg = new MessageDialog(MM.DIALOG_QUESTION, MM.APPTITLE,
					MM.PHRASES.getPhrase("90"));
			
			int answer = msg.Open();
			
			if (answer == MM.YES) {
                 try {
					handler.SyncBudgetMonth(budget, vwBudget.getMonth());
				} catch (SQLException e1) {
					InfinityPfm.LogMessage(e1.getMessage(), true);
				}
                 MessageDialog show = new MessageDialog(MM.DIALOG_INFO,
							MM.APPTITLE, MM.PHRASES.getPhrase("128"));
					show.Open();
            }
			
		}
	}
}
