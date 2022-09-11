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
package org.infinitypfm.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.core.data.CurrencyMethod;
import org.infinitypfm.util.PageHandler;

public class RateParser {

	public static String getRate(CurrencyMethod method) {

		BigDecimal rate = null;
		String page = null;
		if (method != null)
			page = PageHandler.getPage(method.getMethodUrl());

		if (page != null && page.length() > 0) {

			try {

				JSONTokener tokener = new JSONTokener(page);
				Object rawResponseMessage;
				rawResponseMessage = tokener.nextValue();
				JSONObject part = (JSONObject) rawResponseMessage;

				String[] parts = method.getMethodPath().split("\\.");

				for (int i = 0; i < parts.length; i++) {

					if (!parts[i].equals("$")) {

						if (i < parts.length - 1) {
							part = (JSONObject) part.get(parts[i]);
						} else {
							rate = new BigDecimal(part.getString(parts[i]));
							rate = rate.setScale(8, RoundingMode.HALF_UP);
						}
					}
				}

			} catch (Exception e) {
				InfinityPfm.LogMessage(e.getMessage());
				rate = new BigDecimal("-1");
			}

		}

		return rate != null ? rate.toString() : null;
	}

}
