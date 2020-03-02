/*
 * Copyright (c) 2005-2020 Wayne Gray All rights reserved
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.infinitypfm.core.data.IReportable;
import org.infinitypfm.data.ReportData;
import org.infinitypfm.types.NumberFormat;

public class AccountHistory extends BaseReport implements ChartProvider,
		TableProvider {

	private long total = 0;

	public AccountHistory() throws IOException {
		super();
		rowColors = new HashMap<String, String>();
		rowColors.put("liabilitycolor", "#2e2e2b");
		rowColors.put("liabilityback", "#f3f6c2");
		rowColors.put("expensecolor", "#381f1f");
		rowColors.put("expenseback", "#fbedef");
		rowColors.put("bankcolor", "#000000");
		rowColors.put("bankback", "#ffffff");
		rowColors.put("incomecolor", "#001929");
		rowColors.put("incomeback", "#dafde4");
	}

	@Override
	public File execute(ReportData reportData) {
		try {

			String row = null;

			this.addDocHeader(1, CHART_TYPE_BAR);

			this.addTitle(reportData.getTitle());

			@SuppressWarnings("rawtypes")
			List rows = reportData.getReportData();

			if (rows.size() > 0) {
				IReportable reportRow = (IReportable) rows.get(0);
				String[] columns = reportRow.toReportRow().split("\\|");
				this.startChartData("1", columns[1]);
			}

			for (int i = 0; i < rows.size(); i++) {
				IReportable reportRow = (IReportable) rows.get(i);

				addChartRow(reportRow.toReportRow());
			}

			this.endChartData("1");

			this.startTableData();

			for (int i = 0; i < rows.size(); i++) {

				IReportable reportRow = (IReportable) rows.get(i);
				if (i == 0) {
					addHeaderRow(reportRow.getHeaderRow().split("\\|"));
				}

				String[] columns = reportRow.toReportRow().split("\\|");
				addDataRow(columns);

			}
			row = "Total|" + total + "|";
			addTotalRow(row.split("\\|"));

			this.endTableData();

			this.Close(true);
		} catch (IOException ioe) {
		}
		return this.getOutFile();
	}

	@Override
	public void addHeaderRow(String[] columns) throws IOException {

		this.startRow();
		int pos = 0;
		for (int i = 1; i < columns.length; i++) {
			pos = columns.length - i;
			this.addHeader(columns[pos]);
		}
		this.endRow();

	}

	@Override
	public void addDataRow(String[] columns) throws IOException {

		this.startRow(rowColors.get("bankback"), rowColors.get("bankcolor"));

		int pos = 0;

		for (int i = 1; i < columns.length; i++) {
			pos = columns.length - i;
			if (i == 2) {
				long longVal = Long.parseLong(columns[pos]);
				if (longVal < 0) {
					longVal = -longVal;
				}
				total += longVal;
				this.addCell(longVal);
			} else {
				this.addCell(columns[pos]);
			}
		}
		this.endRow();
	}

	@Override
	public void addTotalRow(String[] columns) throws IOException {

		this.startRow(rowColors.get("bankback"), rowColors.get("bankcolor"));

		for (int i = 0; i < columns.length; i++) {
			if (i == 1) {
				long longVal = Long.parseLong(columns[i]);
				if (longVal < 0) {
					longVal = -longVal;
				}
				this.addTotal(longVal);
			} else {
				this.addTotal(columns[i]);
			}
		}
		this.endRow();
	}

	@Override
	public void addChartRow(String row) throws IOException {
		String[] columns = row.split("\\|");

		this.startRow();

		this.addChartLabel(columns[4]);
		String sChartVal = columns[3].replace('(', ' ').replace(')', ' ')
				.trim();

		if (sChartVal == null || sChartVal.length() == 0) {
			sChartVal = "0";
		}

		long longVal = Long.parseLong(sChartVal);
		if (longVal < 0) {
			longVal = -longVal;
		}

		this.addChartValue(formatter.getAmountFormatted(longVal,
				NumberFormat.getNoCommaNoParems()));

		this.endRow();
	}

	@Override
	public void addChartRow(String row, String actType, float totalVal)
			throws IOException {
		// TODO Auto-generated method stub

	}

}
