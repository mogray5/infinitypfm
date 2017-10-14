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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.ui.Widget;

public abstract class BaseToolbar implements Widget {

	protected ToolBar cbMain;

	protected CoolItem[] items;

	protected Composite shMain;

	protected abstract void LoadButtons();

	protected abstract void Init(Composite cm);

	public BaseToolbar(Composite sh) {
		super();
		shMain = sh;
		Init(sh);
		LoadButtons();
	}

	public ToolBar getToolbar() {
		return cbMain;
	}

	public void setLayoutDat(Object objLayout) {
		cbMain.setLayoutData(objLayout);
	}

	public void QZDispose() {
		try {

			if (!cbMain.isDisposed()) {
				cbMain.dispose();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected ToolItem addButton(String sImage, String sTip, int data,
			boolean bEnabled) {
		ToolItem item = new ToolItem(cbMain, SWT.PUSH);
		item.setData(new Integer(data));
		item.setImage(InfinityPfm.imMain.getImage(sImage));
		item.setToolTipText(sTip);
		item.setEnabled(bEnabled);
		item.addSelectionListener(new OnClick());

		return item;
	}

	class OnClick extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {

			if (e.widget != null) {
				ToolItem t = (ToolItem) e.widget;
				int item = ((Integer) t.getData()).intValue();
				MainAction action = new MainAction();
				action.ProcessMenuItem(item);
			}
		}
	}
}
