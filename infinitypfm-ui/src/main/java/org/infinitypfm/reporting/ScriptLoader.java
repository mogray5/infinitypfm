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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScriptLoader {

	private String JS_LIB = "jquery.js";
	private String GRAPH_LIB = "raphael-min.js";
	 
	public String getGraphLib() throws IOException {
		return getScript(GRAPH_LIB);
	}
	
	public String getJsLib() throws IOException{
		return getScript(JS_LIB);
	}
	
	public String getScript(String fileName) throws IOException {
		
		return "\n<script type=\"text/javascript\">" +
			readFileAsString("" + fileName) + 
		"</script>\n";
			
	}
	
	public String getFileAsString(String fileName) throws IOException{
		
		//"net/mogray/infinitypfm/core/reporting/"
		return readFileAsString("" + fileName);
		
	}
	
	/** 
     * @param filePath      name of file to open. The file can reside
     *                      anywhere in the classpath
     */
    private String readFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath)));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

	
}
