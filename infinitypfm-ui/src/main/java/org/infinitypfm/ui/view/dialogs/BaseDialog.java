/*
 * Copyright (c) 2005-2012 Wayne Gray All rights reserved
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

package org.infinitypfm.ui.view.dialogs;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.graphics.ImageMap;

/**
 * @author Wayne Gray
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class BaseDialog extends Dialog {

	protected ImageMap imIcons = null;
	protected Shell parent = null;
	protected DataFormatUtil formatter = null;
	protected boolean userCancelled = false;
	
	/*
	 * Widgets
	 */
	protected Shell shell;
	
	
	/**
	 * @param arg0
	 */
	public BaseDialog() {
		super(InfinityPfm.shMain);
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		imIcons = InfinityPfm.imMain;
	}

	public int Open () {
			parent = getParent();
			shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			shell.setLayout(new FormLayout());
			
			shell.setLocation(300, 300);
			LoadUI(shell);
			LoadLayout();
			return -1;
	}
	
	public void CenterWindow(){
		
		Point appcenter = InfinityPfm.qzMain.getWindowCenter();
		Rectangle rect = shell.getBounds();
		int xc = appcenter.x - (rect.height / 2);
		int yc = appcenter.y - (rect.width / 2);
		shell.setLocation(xc, yc);

	}
	
 	public boolean userCancelled() {
		return userCancelled;
	}

 	protected void setButtonImage(Button button, String imageName) {
 		
 		if (button != null && imageName != null){
 			
 			Image img = this.imIcons.getImage(imageName);
 			button.setImage(img);
 		}
 	}
 	
	protected abstract void LoadUI(Shell sh);
 	protected abstract void LoadLayout();
 	
}
