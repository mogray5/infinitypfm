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


public class TreeMenu extends PopupMenu {


	public TreeMenu(Shell sh) {
		super(sh);

		addSelection(SWT.PUSH, MM.MENU_TREE_EDIT_ACT,
				MM.PHRASES.getPhrase("59") + " " + MM.PHRASES.getPhrase("93"), 'T');
		
		addSelection(SWT.PUSH, MM.MENU_VIEW_TRANS_ENTRY,
				MM.PHRASES.getPhrase("79"), 'T');
		addSelection(SWT.PUSH, MM.MENU_TREE_ACT_REMOVE, MM.PHRASES
				.getPhrase("52"), 'R');
		
		addSelection(SWT.PUSH, MM.MENU_TREE_ACT_ADD, MM.PHRASES
				.getPhrase("7"), 'A');
		addSelection(SWT.PUSH, MM.MENU_TREE_ADD_ACT_FROM_TEMP, MM.PHRASES
				.getPhrase("132"), 'T');
		
		addSelection(SWT.PUSH, MM.MENU_TREE_CLOSEVIEW, MM.PHRASES
				.getPhrase("54"), 'C');

	}

	public void QZDispose() {
		if (!mnu.isDisposed()) {
			mnu.dispose();
		}
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
