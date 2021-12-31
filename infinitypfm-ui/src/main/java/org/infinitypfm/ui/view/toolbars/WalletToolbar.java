/*
 * Copyright (c) 2005-2019 Wayne Gray All rights reserved
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

public class WalletToolbar extends BaseToolbar {

	public WalletToolbar(Composite sh) {
		super(sh);
	}

	@Override
	public void Refresh() {

	}

	@Override
	protected void LoadButtons() {

		addButton(MM.IMG_REFRESH2, MM.PHRASES.getPhrase("47"),
				MM.MENU_WALLET_REFRESH, true);
		
		addButton(MM.IMG_REFRESH, MM.PHRASES.getPhrase("159"),
				MM.MENU_WALLET_RESTORE, true);
		
		addButton(MM.IMG_KEY, MM.PHRASES.getPhrase("277"),
				MM.MENU_WALLET_SHOW_MNEMONIC, true);
	
		addButton(MM.IMG_FOLDER_DOWNLOAD, MM.PHRASES.getPhrase("158"),
				MM.MENU_WALLET_BACKUP, true);
		
		addButton(MM.IMG_CLOSE_SMALL, MM.PHRASES.getPhrase("54"),
				MM.MENU_TREE_CLOSEVIEW, true);
		
	}

	@Override
	protected void Init(Composite cm) {
		cbMain = new ToolBar(cm, SWT.FLAT);
	}

}
