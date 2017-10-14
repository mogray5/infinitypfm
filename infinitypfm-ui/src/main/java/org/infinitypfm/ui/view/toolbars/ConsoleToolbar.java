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

public class ConsoleToolbar extends BaseToolbar {

	/**
	 * @param sh
	 */
	public ConsoleToolbar(Composite sh) {
		super(sh);
	}

	/**
	 * @param sh
	 * @param im
	 */

	protected void Init(Composite cm) {
		cbMain = new ToolBar(cm, SWT.FLAT);
	}

	protected void LoadButtons() {

		addButton(MM.IMG_CLEAR, MM.PHRASES.getPhrase("66"),
				MM.MENU_CONSOLE_CLEAR, true);

		addButton(MM.IMG_CLOSE_SMALL, MM.PHRASES.getPhrase("88"),
				MM.MENU_CONSOLE_CLOSE, true);

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
