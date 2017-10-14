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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.ui.Widget;

public abstract class PopupMenu implements Widget {

	/*
	 * Widgets
	 */
	protected Menu mnu = null;

	public PopupMenu(Shell sh) {
		super();
		mnu = new Menu(sh, SWT.POP_UP);

	}

	protected MenuItem addSelection(int iStyle, int iIndex, String sCaption,
			char sExcel) {
		MenuItem mi;
		mi = new MenuItem(mnu, iStyle);
		mi.setText(sCaption);
		mi.setAccelerator(SWT.ALT | sExcel);
		if (iIndex >= 0) {
			mi.setData(new Integer(iIndex));
		}

		mi.addSelectionListener(on_Click);

		return mi;
	}

	public Menu getMenu() {
		return mnu;
	}

	public abstract void QZDispose();

	/*
	 * Listeners
	 */

	SelectionAdapter on_Click = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			Integer i = null;
			MenuItem mi = (MenuItem) e.widget;
			if (mi != null) {
				i = (Integer) mi.getData();
			}

			if (i == null) {
				return;
			}

			MainAction action = new MainAction();
			action.ProcessMenuItem(i.intValue());
		}
	};
}
