/*
 * Copyright (c) 2005-2011 Wayne Gray All rights reserved
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
package org.infinitypfm.reporting;

import java.io.IOException;

import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;

public class ReportFactory {

	public static BaseReport getReport(int reportType) {

		BaseReport report = null;
		
		try {
			report = new TemplateReport();
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

		return report;

	}

}
