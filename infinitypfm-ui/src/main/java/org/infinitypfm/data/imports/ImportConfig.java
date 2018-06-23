/*
 * Copyright (c) 2005-2013 Wayne Gray All rights reserved
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

import org.infinitypfm.exception.ConfigurationException;

public interface ImportConfig {
	
	/**
	 * Use this interface for import types that require extra set-up to get the 
	 * list of incoming transctions.
	 * 
	 * Will be called by the base importer before trying to process the import file.
	 * 
	 * @throws ConfigurationException
	 */
	public void config() throws ConfigurationException;

}
