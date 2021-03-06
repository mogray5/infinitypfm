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
import org.infinitypfm.conf.MM;

public class CurrencyToolbar extends BaseToolbar {

	public CurrencyToolbar(Composite sh) {
		super(sh);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void LoadButtons() {
		
		addButton(MM.IMG_CLOSE_SMALL, MM.PHRASES.getPhrase("54"),
				MM.MENU_TREE_CLOSEVIEW, true);

	}

	@Override
	protected void Init(Composite cm) {
		
		cbMain = new ToolBar(cm, SWT.FLAT);

	}

}
