package org.infinitypfm.ui.view.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ImportDefSelector extends BaseDialog {

	private Composite cmpAddEditDef = null;
	private Composite cmpDefList = null;
	private Composite cmpSelectedDef = null;
	
	@Override
	protected void LoadUI(Shell sh) {

		cmpAddEditDef = new Composite(sh, SWT.BORDER);
		cmpAddEditDef.setLayout(new FormLayout());
		cmpDefList = new Composite(sh, SWT.BORDER);
		cmpDefList.setLayout(new FormLayout());
		cmpSelectedDef = new Composite(sh, SWT.BORDER);
		cmpSelectedDef.setLayout(new FormLayout());
	}

	@Override
	protected void LoadLayout() {
		// TODO Auto-generated method stub

	}

}
