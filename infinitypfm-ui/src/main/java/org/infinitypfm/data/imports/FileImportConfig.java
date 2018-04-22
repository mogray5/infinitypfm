/*
 * Copyright (c) 2005-2018 Wayne Gray All rights reserved
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
package org.infinitypfm.data.imports;

import org.eclipse.swt.widgets.FileDialog;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.exception.ConfigurationException;

public class FileImportConfig implements ImportConfig {

	@Override
	public void config() throws ConfigurationException {

		FileDialog dlg = new FileDialog(InfinityPfm.shMain);
		dlg.setText(MM.PHRASES.getPhrase("18"));
		MM.importFile = dlg.open();

		if (MM.importFile == null) {
			throw new ConfigurationException(MM.PHRASES.getPhrase("36"));
		}
		
	}

}
