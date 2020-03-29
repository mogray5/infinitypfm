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


public class GridReport  extends BaseReport implements ChartProvider, TableProvider {

	private long totalIncome = 1L;
	private long totalExpense = 1L;
	private long totalLiability = 1L;
	private String actType = null;
	
	public GridReport() throws IOException {
		super();
		rowColors = new HashMap<String,String>();
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
			
			if (reportData.getIncomeTotal() != null)
				totalIncome = Long.parseLong(reportData.getIncomeTotal());
			
			if (totalIncome < 0) totalIncome = -totalIncome;
			
			if (reportData.getExpenseTotal() != null)
				totalExpense = Long.parseLong(reportData.getExpenseTotal());
			
			if (reportData.getLiabilityTotal() != null)
				totalLiability = Long.parseLong(reportData.getLiabilityTotal());
			
			this.addDocHeader(2, "pie");
				
			this.addTitle(reportData.getTitle());
			
			@SuppressWarnings("rawtypes")
			List rows = reportData.getReportData();
			
			this.startChartData("1", "Income");
			for (int i=0; i< rows.size(); i++){			
				IReportable reportRow = (IReportable) rows.get(i);
				addChartRow(reportRow.toReportRow(), "Income",  totalIncome);
			}
			this.endChartData("1");
			
			this.startChartData("2", "Expenses");
			for (int i=0; i< rows.size(); i++){			
				IReportable reportRow = (IReportable) rows.get(i);
				addChartRow(reportRow.toReportRow(), "Expense",  totalExpense);
			}
			this.endChartData("2");
			
			this.startTableData();
			
			for (int i=0; i< rows.size(); i++){
			
				IReportable reportRow = (IReportable) rows.get(i);
				if(i==0){
					addHeaderRow(reportRow.getHeaderRow().split("\\|"));
				} 		
				
				String[] columns = reportRow.toReportRow().split("\\|");
				if (columns[0].equalsIgnoreCase(actType) || actType == null) {
					if (actType==null){
						actType = columns[0];
					}
				} else {
					if (actType.equalsIgnoreCase("Expense")){
						row = "Expense||Total|" + totalExpense;
						addTotalRow(row.split("\\|"));
					} else if (actType.equalsIgnoreCase("Income")){
						row = "Income||Total|" + totalIncome;
						addTotalRow(row.split("\\|"));
					}
					actType = columns[0];
				}
				
				addDataRow(columns);
			
			}
			
			if (actType != null && actType.equalsIgnoreCase("Liability")){
				row = "Liability||Total|" + totalLiability;
				addTotalRow(row.split("\\|"));
			}
			
			this.endTableData();
			
			this.Close(true);
		} catch (IOException ioe){}
		return this.getOutFile();
	}
	
	@Override
	public void addHeaderRow(String[] columns) throws IOException {
		
		this.startRow();
		for (int i=0; i<columns.length; i++) {
			this.addHeader(columns[i]);
		}
		this.endRow();
		
	}
	
	@Override
	public void addDataRow(String[] columns) throws IOException{
		
		this.startRow(rowColors.get(columns[0].toLowerCase()+ "back"),
					rowColors.get(columns[0].toLowerCase()+ "color"));
		
		for (int i=0; i<columns.length; i++) {
			if (i==3) {
				long longVal = Long.parseLong(columns[i]);
				if (longVal < 0){
					longVal = -longVal;
				}
				this.addCell(longVal);
			} else {
				this.addCell(columns[i]);
			}
		}
		this.endRow();
	}
	
	@Override
	public void addTotalRow(String[] columns) throws IOException{
		
		this.startRow(rowColors.get(columns[0].toLowerCase()+ "back"),
					rowColors.get(columns[0].toLowerCase()+ "color"));
		
		for (int i=0; i<columns.length; i++) {
			if (i==3) {
				long longVal = Long.parseLong(columns[i]);
				if (longVal < 0){
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
	public void addChartRow(String row, String actType, float totalVal) throws IOException{
		String[] columns = row.split("\\|");

		if (columns[0].equalsIgnoreCase(actType)){

			this.startRow();
			
			this.addChartLabel(columns[1]);
			String sChartVal = columns[3].replace('(', ' ').replace(')', ' ').trim();
			
			if (totalVal<0){
				totalVal = -totalVal;
			}
			long longVal = Long.parseLong(sChartVal);
			if (longVal < 0){
				longVal = -longVal;
			}
			
			this.addChartValue(Long.toString(Math.round((longVal/totalVal)*100F)));
			
			this.endRow();
		}
		
	}

	@Override
	public void addChartRow(String row) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}
