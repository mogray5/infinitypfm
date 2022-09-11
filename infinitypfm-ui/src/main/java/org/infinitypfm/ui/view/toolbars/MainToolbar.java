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


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.infinitypfm.conf.MM;

public class MainToolbar extends BaseToolbar {

	/**
	 * @param sh
	 */
	public MainToolbar(Composite sh) {
		super(sh);
	}

	/**
	 * Overide Init and LoadButtons methods to define toolbar appearance
	 */
	protected void Init(Composite cm) {
		cbMain = new ToolBar(cm, SWT.FLAT);
		//FormData cbmaindata = new FormData();
	}

	public void EnablePlugin(boolean bVal) {
		//enable toolbar
		ToolItem ti;
		for (int i = 1; i < 4; i++) {
			ti = cbMain.getItem(i);
			ti.setEnabled(bVal);
		}
	}

	public void EnablePluginRunning(boolean bVal) {
		//enable toolbar
		ToolItem ti;
		for (int i = 1; i < 4; i++) {
			ti = cbMain.getItem(i);
			if (i == 2 || i == 3) {
				ti.setEnabled(false);
			} else {
				ti.setEnabled(bVal);
			}
		}
	}

	protected void LoadButtons() {
        
        addButton(MM.IMG_NEWOBJECT, MM.PHRASES.getPhrase("7"),
        		MM.MENU_EDIT_ADD_ACCOUNT, true);

		addButton(MM.IMG_CHART, MM.PHRASES.getPhrase("98"),
				MM.MENU_EDIT_ADD_BUDGET, true);
		
		addButton(MM.IMG_CLOCK, MM.PHRASES.getPhrase("155"),
				MM.VIEW_RECURRENCE, true);
		
		addButton(MM.IMG_DOLLAR, MM.PHRASES.getPhrase("214"),
				MM.VIEW_CURRENCY, true);
		
		if (MM.options.isEnableWallet()) 
			addButton(MM.IMG_WALLET, MM.PHRASES.getPhrase("276"),
				MM.VIEW_WALLET, true);
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.QZWidget#Refresh()
	 */
	public void Refresh() {
		// TODO Auto-generated method stub

	}
}
