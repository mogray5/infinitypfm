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
package org.infinitypfm.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.data.ReportData;


public abstract class BaseReport {

	public static final String CHART_TYPE_PIE = "pie";
	public static final String CHART_TYPE_BAR = "bar";
	public static final String CHART_TYPE_LINE = "line";
	
	private File outFile = null;
	//private FileWriter fileWriter = null;
	private BufferedWriter out = null;
	private static final String FILE_HEADER_START = "<html><head>";
	private static final String FILE_HEADER_END = "</head><body>";
	private static final String TABLE_HEADER_START = "<table border=\"1\" cellspacing=\"1\" style=\"width:100%;background-color:#004f78;font-size:12px;\">";
	private static final String TABLE_HEADER_END = "</table>";
	
	private static final String FILE_FOOTER = "</body></html>";
	private static final String PIE_CHART_BASE = "piechartBase.js";
	private static final String PIE_CHART_1 ="piechart1.js";
	private static final String PIE_CHART_2 ="piechart2.js";
	private static final String BAR_CHART_BASE = "barChartBase.js";
	private static final String BAR_CHART_1 = "barchart1.js";
	private static final String LINE_CHART_1 = "linechart1.js";
	private static final String LINE_CHART_BASE = "linechartBase.js";
	
	
	private ScriptLoader scriptLoader = null;
	protected HashMap<String, String> reportParams = null;
	protected HashMap<String, String> rowColors = null; 
	protected DataFormatUtil formatter = null;
	
	
	public BaseReport() throws IOException {
		
//		try {
			
			formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
	        outFile = File.createTempFile("infinitypfm", ".html");
	        out = new BufferedWriter(new FileWriter(outFile));
	        scriptLoader = new ScriptLoader();
	        reportParams = new HashMap<String, String>();
			
//			MM.LogMessage(outFile.getPath());
//		} catch (IOException e) {
//			MM.LogMessage(e.getMessage());
//		}
		
	}
	
	public abstract File execute(ReportData reportData);
	
	public void addDocHeader(int numCharts, String chartType) throws IOException{
		out.write(FILE_HEADER_START);
		if (numCharts > 0){
			out.write(scriptLoader.getGraphLib());
			out.write(scriptLoader.getJsLib());
			
			if (chartType.equalsIgnoreCase(CHART_TYPE_PIE)){
				out.write(scriptLoader.getScript(PIE_CHART_BASE));
				if (numCharts == 2) {
					out.write(scriptLoader.getScript(PIE_CHART_2));
				} else {
					out.write(scriptLoader.getScript(PIE_CHART_1));
				}
			} else if (chartType.equalsIgnoreCase(CHART_TYPE_BAR)){
				out.write(scriptLoader.getScript(BAR_CHART_BASE));
				out.write(scriptLoader.getScript(BAR_CHART_1));
			} else if (chartType.equalsIgnoreCase(CHART_TYPE_LINE)){
				out.write(scriptLoader.getScript(LINE_CHART_BASE));
				out.write(scriptLoader.getScript(LINE_CHART_1));
			}
		}
		out.write(FILE_HEADER_END);
	}
	
	public void addChartTestData() throws IOException{
		out.write(scriptLoader.getFileAsString("testTable.html"));
	}
	
	public void startChartData(String chartID, String chartTitle) throws IOException {
		out.write("<input id=\"chart" + chartID + "Title\" type=\"hidden\" value=\"" + chartTitle + "\">");
		out.write("<table id=\"chart" + chartID + "\">");
	}
	
	public void startChartData(String chartID, String chartTitle, String guideLabel) throws IOException {
		out.write("<input id=\"chart" + chartID + "Title\" type=\"hidden\" value=\"" + chartTitle + "\">");
		out.write("<table id=\"chart" + chartID + "\" guideLineLbl=\"" + guideLabel + "\">");
	}
	
	public void endChartData(String chartID) throws IOException {
		out.write("</table><span id=\"holder" + chartID +"\"></span>");
	}
	
	public void addChartLabel(String label) throws IOException{
		out.write("<th>" + label + "</th>");
	}
	
	public void addChartValue(String val) throws IOException{
		out.write("<td class=\"barVal\">" + val + "</td>");
	}
	
	public void addChartValue(String val, String series) throws IOException{
		out.write("<td class=\"barVal" + series +"\">" + val + "</td>");
	}
	
	public void addChartGuideValue(String val) throws IOException{
		out.write("<td class=\"guideVal\">" + val + "</td>");
	}
	
	public void startTableData() throws IOException {
		out.write(TABLE_HEADER_START);
	}
	
	public void endTableData() throws IOException {
		out.write(TABLE_HEADER_END);
	}
	
	public void startRow() throws IOException{
		out.write("<tr>");
	}
	
	public void startRow(String backColor, String color) throws IOException{
		out.write("<tr style=\"background-color:" + backColor +
				";color:" + color +";\">");
	}
	
	public void endRow() throws IOException {
		out.write("</tr>");
	}
	
	public void addTitle(String val) throws IOException{
		out.write("<h3 style=\"color:#2f7991;\">" + val + "</h3>");
	}
	
	public void addHeader(String val) throws IOException{
		out.write("<td style=\"color:white;\">" + val + "</td>");
	}
	
	public void addCell(long val) throws IOException{
		out.write("<td>" + formatter.getAmountFormatted(val) + "</td>");
	}
	
	public void addCell(String val) throws IOException{
		out.write("<td>" + val + "</td>");
	}
	
	public void addCell(boolean val) throws IOException{
		out.write("<td>" + val + "</td>");
	}
	
	public void addCell(Date val) throws IOException{
		out.write("<td>" + val + "</td>");
	}
	
	public void addCell(int val) throws IOException{
		out.write("<td>" + val + "</td>");
	}
	
	public void addTotal(long val) throws IOException{
		out.write("<td><b>" + formatter.getAmountFormatted(val) + "</b></td>");
	}
	
	public void addTotal(String val) throws IOException{
		out.write("<td><b>" + val + "</b></td>");
	}
	
	public void addTotal(boolean val) throws IOException{
		out.write("<td><b>" + val + "</b></td>");
	}
	
	public void addTotal(Date val) throws IOException{
		out.write("<td><b>" + val + "</b></td>");
	}
	
	public void addTotal(int val) throws IOException{
		out.write("<td><b>" + val + "</b></td>");
	}
	
	public void Close() throws IOException {
		out.write(FILE_FOOTER);
		try {out.close();} catch (Exception e1){}
		//MM.LogMessage("file close");
	}

	public File getOutFile() {
		return outFile;
	}

	public void setOutFile(File outFile) {
		this.outFile = outFile;
	}
	
	public void addParam(String name, String val){
		if (reportParams.containsKey(name)) {
			reportParams.remove(name);
		}
		
		reportParams.put(name, val);
	}
	
}
