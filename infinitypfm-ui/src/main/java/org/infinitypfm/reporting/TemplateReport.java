package org.infinitypfm.reporting;

import java.io.File;
import java.util.List;

import org.infinitypfm.conf.MM;

public class TemplateReport {

	private final List<ReportRow> _reportRows1;
	private final List<ChartRow> _chartRows1;

	public TemplateReport(List<ReportRow> reportRows, List<ChartRow> chartRows) {
		_reportRows1 = reportRows;
		_chartRows1 = chartRows;
	}

	public File genReport() {
		
		//MM.templateConfig.
		
		return null;
		
	}
	
}
