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
package org.infinitypfm.ui.view.menus;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.conf.MM;

public class BudgetMenu extends PopupMenu {

	public BudgetMenu(Shell sh) {
		super(sh);
		
		addSelection(SWT.PUSH, MM.MENU_TREE_ADD_ACT_BUDGET, MM.PHRASES
				.getPhrase("120"), 'A');
		addSelection(SWT.PUSH, MM.MENU_TREE_REM_ACT_BUDGET, MM.PHRASES
				.getPhrase("140"), 'A');
		
		addSelection(SWT.PUSH, MM.MENU_TREE_REM_BUDGET, MM.PHRASES
				.getPhrase("330"), 'R');
		
		addSelection(SWT.PUSH, MM.MENU_TREE_CLOSEVIEW, MM.PHRASES
				.getPhrase("54"), 'C');
	}

	@Override
	public void QZDispose() {		
	}



	@Override
	public void Refresh() {
		
	}

}
