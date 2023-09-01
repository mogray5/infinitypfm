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

public class PlanMenu extends PopupMenu {

	public PlanMenu(Shell sh) {
		super(sh);
		
		addSelection(SWT.PUSH, MM.MENU_TREE_RUN_PLAN, MM.PHRASES
				.getPhrase("336"), 'D');
		
		addSelection(SWT.PUSH, MM.MENU_TREE_RENAME_PLAN, MM.PHRASES
				.getPhrase("369"), 'R');
		
		addSelection(SWT.PUSH, MM.MENU_TREE_DELETE_PLAN, MM.PHRASES
				.getPhrase("335"), 'D');
		
	}

	@Override
	public void QZDispose() {		
	}



	@Override
	public void Refresh() {
		
	}

}
