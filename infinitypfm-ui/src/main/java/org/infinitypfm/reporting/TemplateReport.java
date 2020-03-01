package org.infinitypfm.reporting;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.infinitypfm.conf.MM;
import org.infinitypfm.data.ReportData;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class TemplateReport extends BaseReport {
	
	public TemplateReport() {
		super(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public File execute(ReportData reportData) {


		try {
					
			Template temp = MM.templateConfig.getTemplate(reportData.getTemplate());
			Writer out = new OutputStreamWriter(System.out);
	
			this.addDocHeader(reportData);
			
			temp.process(reportData, out);
			
			
		} catch (TemplateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
