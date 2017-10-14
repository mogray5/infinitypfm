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

/**
 * @author wggray
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class LogToolbar extends BaseToolbar {

	/**
	 * @param sh
	 */
	public LogToolbar(Composite sh) {
		super(sh);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.view.toolbars.QZToolbar#LoadButtons()
	 */
	protected void LoadButtons() {

		addButton(MM.IMG_REFRESH, MM.PHRASES.getPhrase("47"),
				MM.MENU_TREE_REFRESH, true);

		addButton(MM.IMG_CLOSE_SMALL, MM.PHRASES.getPhrase("89"),
				MM.MENU_TREE_CLOSEVIEW, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.view.toolbars.MM.Toolbar#Init(org.eclipse.swt.widgets.Composite)
	 */
	protected void Init(Composite cm) {
		cbMain = new ToolBar(cm, SWT.FLAT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.MM.Widget#Refresh()
	 */
	public void Refresh() {
		// TODO Auto-generated method stub

	}

}
