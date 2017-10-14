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

package org.infinitypfm.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.conf.LangLoader;
import org.infinitypfm.core.data.Options;
import org.infinitypfm.data.DataHandler;
import org.infinitypfm.data.Database;
import org.infinitypfm.data.InfinityUpdates;
import org.infinitypfm.exception.ConfigurationException;
import org.infinitypfm.graphics.ImageMap;
import org.infinitypfm.ui.MainFrame;
import org.infinitypfm.ui.view.dialogs.MessageDialog;
import org.infinitypfm.util.FileHandler;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class InfinityPfm {

	private static final String CONFIG_ERROR_ENG = "Error setting up home directory for: ";
	private static File homeDirectory = new File(System.getProperty("user.home") + "/.infinitypfm");
	
	public static void main(String[] args) {
		Display display = new Display();
		
		if (System.getenv(MM.ENVAPPHOME) != null) {
			homeDirectory = new File(System.getenv(MM.ENVAPPHOME));
		}

		try {
			//set up config folder
			initConfig();
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
			return;
		}
		
		//load language
		getLanguage();
		
		//load data layer
		try {
			loadDatabase();
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
		/* Sleak Startup code
		 * 
		 * DeviceData data = new DeviceData();
		 * data.tracking = true;
		 * display = new Display(data);
		 * Sleak sleak = new Sleak();
		 * sleak.open();
		 */

		shMain = new Shell(display);

		//Load icons
		imMain = new ImageMap(shMain);

		/*
		 * Check for updates
		 */
		InfinityUpdates updates = new InfinityUpdates();
		try {
			updates.ProcessUpdates();
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
		}
		
		//Load app options
		try {
			MM.options = (Options) MM.sqlMap.queryForObject("getOptions");
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		
		//Load main form		
		MainFrame frmMain = new MainFrame();
		qzMain = frmMain;
		frmMain.LoadUI(true);
		frmMain.getVwMain().LoadDefaultView();
		frmMain.LoadLayout();
		frmMain.Open();
	
		/*
		 * Remove leftover files from last sessions
		 */
		removeOldFiles();
		
		//Process recurring transactions
		DataHandler handler = new DataHandler();
		try {
			handler.ProcessRecurringTransaction();
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		
		qzMain.getTrMain().Reload();
			
		//while (!sleak.shell.isDisposed())
		while (!shMain.isDisposed())
		{
		  if (!display.readAndDispatch())
			display.sleep();
		}
		
	   
		//clean up		
		/*
		try {
			MM.sqlMap.insert("shutdown", null);
		} catch (SQLException se){}
		*/
		qzMain.QZDispose();
		
		display.dispose();
		System.exit(0);
	}
	
	private static void getLanguage(){
		
		try {
			
		//Load Language
		
		MM.PHRASES = new LangLoader();
			
	
		} catch(Exception e){
			
				System.err.println("Error Loading Language File");
				e.printStackTrace();
		}
		
		
		
	}
	
	private static void initConfig() throws ConfigurationException{
		
		boolean canContinue = true;
		
		//Check if folders set up in 		 
		 if (!homeDirectory.exists()){
			 canContinue = homeDirectory.mkdir();
		 }
		 
		 if (!canContinue) {
			throw new ConfigurationException(CONFIG_ERROR_ENG + homeDirectory.getPath()); 
		 }
		 
		 File dataDirectory = new File(homeDirectory.getPath() + "/data");
		 
		 if (!dataDirectory.exists()){
			 canContinue = dataDirectory.mkdir();
		 }
		 
		 if (!canContinue) {
			throw new ConfigurationException(CONFIG_ERROR_ENG + homeDirectory.getPath()); 
		 }
		 
		 File pluginDirectory = new File(homeDirectory.getPath()+ "/plugins");
		 
		 if (!pluginDirectory.exists()){
			 canContinue = pluginDirectory.mkdir();
		 }

		 if (!canContinue) {
			throw new ConfigurationException(CONFIG_ERROR_ENG + homeDirectory.getPath()); 
		 }
		 
		 String sPropsFile = homeDirectory.getPath() + "/" + MM.PROPS_FILE;
		 
		 //check if database exists
		 File propsFile = new File (sPropsFile);
		 
		 if (!propsFile.exists()){
			 			 
			 InputStream in = null;
			 OutputStream out = null;
			 
			 try {
				 in = Resources.getResourceAsStream (MM.PROPS_FILE);
				 out=new FileOutputStream(sPropsFile);
				 
				 @SuppressWarnings("rawtypes")
				List lines = IOUtils.readLines(in);
				 ArrayList<String> outLines = new ArrayList<String>();
				 
				 String line = null;
				 
				 for (int i=0; i< lines.size(); i++){
					 
					 line = (String)lines.get(i);
					 line = line.replace("{HOME}", homeDirectory.getPath());
					 
					 outLines.add(line);
	
				 }
				 
				 IOUtils.writeLines(outLines, null, out);
				 
				    
			 } catch (IOException e){
				 System.err.println("Error Creating " + MM.PROPS_FILE);
				 e.printStackTrace();
			 } finally {
				 
				 try {out.close();} catch (Exception e) {System.err.println(e.getMessage());}
				 try {in.close();} catch (Exception e) {System.err.println(e.getMessage());}
				 
			 }
		 }

	}

	private static void loadDatabase() throws ConfigurationException{
		
		Reader reader = null;
		try {
			
			String propsFile = homeDirectory.getPath() + "/" + MM.PROPS_FILE;
			
			PropertiesConfiguration propsConfig = new PropertiesConfiguration(propsFile);
			Properties props = propsConfig.getProperties("db.source");
			reader = Resources.getResourceAsReader (MM.MAPFILE);
						
			MM.sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader, props);
	
			// See if we can get a connection	
			MM.sqlMap.getDataSource().setLoginTimeout(2);
			MM.sqlMap.getDataSource().getConnection();
			
			
		} catch (Exception se){
			throw new ConfigurationException("Error connecting to database: " + se.getMessage()); 
		}

		if (MM.sqlMap != null){
			
			 //check tables
			 try {
				  
				 Connection conn = MM.sqlMap.getDataSource().getConnection();
				 DatabaseMetaData metadata = conn.getMetaData();
				  
				 ResultSet result = metadata.getTables(null, null, "ACCOUNTS", null);
				 boolean exists = result.next();
				 result.close();
				 
				 if (!exists) {
					 //still not getting the accounts tables, re-create the schema
					 Database database = new Database();
					 database.build();
				 }
				 
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	private static void removeOldFiles() {
	
		FileHandler fileUtil = new FileHandler();
		File[] fileList = fileUtil.getFileList(System.getProperty("java.io.tmpdir"), 
					"infinitypfm", "");
		if (fileList != null){
			for (int i=0; i<fileList.length; i++){
				fileList[i].delete();
			}
		}
	}
	
	public static void LogMessage(String sMsg){
		if (qzMain.getMsgMain()==null){
			return;
		}

		qzMain.getMsgMain().AppendMsg(sMsg);
		
	}
	
	public static void LogMessage(String sMsg, boolean promptUser){
		
		LogMessage(sMsg);

		if (promptUser){
			MessageDialog show = new MessageDialog(MM.DIALOG_INFO,
				MM.APPTITLE, 
				sMsg);
			show.Open();
		}
		
		
	}
	
	public static MainFrame qzMain;	
	public static Shell shMain;
	public static ImageMap imMain;
}
