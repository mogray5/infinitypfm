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

package org.infinitypfm.core.conf;

import java.util.HashMap;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;


/**
 * This class loads a HashMap of words and phrases in the user's local language and is
 * referenced throughout the application.  
 * 
 * @author Wayne Gray
 */
public class LangLoader {

	private I18n i18n = null;
	private HashMap<String, String> hmPhrases = new HashMap<String, String>();
	
	public LangLoader(){
		super();
		
		i18n = I18nFactory.getI18n(this.getClass(), "app.i18n.Messages", java.util.Locale.getDefault(),
		         org.xnap.commons.i18n.I18nFactory.FALLBACK);
		
		//i18n.setLocale(Locale.GERMAN);
		
		hmPhrases.put("1", i18n.tr("Name"));
		hmPhrases.put("2", i18n.tr("Balance"));
		hmPhrases.put("3", i18n.tr("Log Setup"));
		hmPhrases.put("4", i18n.tr("Cancel"));
		hmPhrases.put("5", i18n.tr("OK"));
		hmPhrases.put("6", i18n.tr("Remove"));
		hmPhrases.put("7", i18n.tr("New Account"));
		hmPhrases.put("8", i18n.tr("Bank Accounts"));
		hmPhrases.put("9", i18n.tr("Expense Accounts"));
		hmPhrases.put("10", i18n.tr("Income Accounts"));
		hmPhrases.put("11", i18n.tr("Import"));
		hmPhrases.put("12", i18n.tr("Yes"));
		hmPhrases.put("13", i18n.tr("No"));
		hmPhrases.put("14", i18n.tr("Commit"));
		hmPhrases.put("15", i18n.tr("Import Account"));
		hmPhrases.put("16", i18n.tr("Connection Error"));
		hmPhrases.put("17", i18n.tr("Log"));
		hmPhrases.put("18", i18n.tr("Import Transactions"));
		hmPhrases.put("19", i18n.tr("File"));
		hmPhrases.put("20", i18n.tr("Location"));
		hmPhrases.put("21", i18n.tr("File format not supported"));
		hmPhrases.put("22", i18n.tr("Liabilities"));
		hmPhrases.put("23", i18n.tr("Logs"));
		hmPhrases.put("24", i18n.tr("Date"));
		hmPhrases.put("25", i18n.tr("No such source file:"));
		hmPhrases.put("26", i18n.tr("FileCopy: source file"));
		hmPhrases.put("27", i18n.tr("is unreadable"));
		hmPhrases.put("28", i18n.tr("FileCopy: destination"));
		hmPhrases.put("29", i18n.tr("file is unwritable"));
		hmPhrases.put("30", i18n.tr("already exists.  Overwrite? (Y/N)"));
		hmPhrases.put("31", i18n.tr("FileCopy: copy cancelled."));
		hmPhrases.put("32", i18n.tr("is not a file"));
		hmPhrases.put("33", i18n.tr("directory does not exist"));
		hmPhrases.put("34", i18n.tr("directory is unwritable"));
		hmPhrases.put("35", i18n.tr("does not exist!"));
		hmPhrases.put("36", i18n.tr("Error reading/writing files"));
		hmPhrases.put("37", i18n.tr("Error applying updates.  Error is:"));
		hmPhrases.put("38", i18n.tr("Save"));
		hmPhrases.put("39", i18n.tr("Invalid account name"));
		hmPhrases.put("40", i18n.tr("Invalid account type"));
		hmPhrases.put("41", i18n.tr("Memo"));
		hmPhrases.put("42", i18n.tr("Offset Account"));
		hmPhrases.put("43", i18n.tr("Refresh All"));
		hmPhrases.put("44", i18n.tr("Select All"));
		hmPhrases.put("45", i18n.tr("Add"));
		hmPhrases.put("46", i18n.tr("Credit"));
		hmPhrases.put("47", i18n.tr("Refresh"));
		hmPhrases.put("48", i18n.tr("Debit"));
		hmPhrases.put("50", i18n.tr("Delete failed, account has a balance"));
		hmPhrases.put("51", i18n.tr("Connection"));
		hmPhrases.put("52", i18n.tr("Delete Account"));
		hmPhrases.put("53", i18n.tr("Liability Accounts"));
		hmPhrases.put("54", i18n.tr("Close"));
		hmPhrases.put("55", i18n.tr("Amount"));
		hmPhrases.put("56", i18n.tr("Type"));
		hmPhrases.put("57", i18n.tr("deleted"));
		hmPhrases.put("58", i18n.tr("Exit"));
		hmPhrases.put("59", i18n.tr("Edit"));
		hmPhrases.put("60", i18n.tr("Contents"));
		hmPhrases.put("61", i18n.tr("About"));
		hmPhrases.put("62", i18n.tr("Help"));
		hmPhrases.put("63", i18n.tr("Personal Finance"));
		hmPhrases.put("64", i18n.tr("No bank accounts exist"));
		hmPhrases.put("65", i18n.tr("Console"));
		hmPhrases.put("66", i18n.tr("Clear"));
		hmPhrases.put("67", i18n.tr("Options"));
		hmPhrases.put("68", i18n.tr("Setup"));
		hmPhrases.put("69", i18n.tr("Language File"));
		hmPhrases.put("70", i18n.tr("You must restart for changes to take affect."));
		hmPhrases.put("71", i18n.tr("Version"));
		hmPhrases.put("72", i18n.tr("Visit"));
		hmPhrases.put("73", i18n.tr("This product contains software from the following:"));
		hmPhrases.put("74", i18n.tr("The Apache Software Foundation (http://www.apache.org)"));
		hmPhrases.put("75", i18n.tr("HSQL Database Engine (http://www.hsqldb.org/)"));
		hmPhrases.put("76", i18n.tr("Are you sure you want to delete account?"));
		hmPhrases.put("77", i18n.tr("Error stopping server"));
		hmPhrases.put("78", i18n.tr("Updates found for InfinityPFM.  They will now be applied."));
		hmPhrases.put("79", i18n.tr("Transaction Entry"));
		hmPhrases.put("80", i18n.tr("User Name"));
		hmPhrases.put("81", i18n.tr("Password"));
		hmPhrases.put("82", i18n.tr("Add/Edit Account"));
		hmPhrases.put("83", i18n.tr("Checking for updates"));
		hmPhrases.put("84", i18n.tr("Starting Balance"));
		hmPhrases.put("85", i18n.tr("is not connected to"));
		hmPhrases.put("86", i18n.tr("View"));
		hmPhrases.put("87", i18n.tr("Ready"));
		hmPhrases.put("88", i18n.tr("Close Console"));
		hmPhrases.put("89", i18n.tr("Saving transactions..."));
		hmPhrases.put("90", i18n.tr("Do you want to commit these transactions?"));
		hmPhrases.put("91", i18n.tr("Current Year"));
		hmPhrases.put("92", i18n.tr("added"));
		hmPhrases.put("93", i18n.tr("Account"));
		hmPhrases.put("94", i18n.tr("Expenses"));
		hmPhrases.put("95", i18n.tr("Over/Under Month"));
		hmPhrases.put("96", i18n.tr("Updates applied!"));
		hmPhrases.put("97", i18n.tr("Budgets"));
		hmPhrases.put("98", i18n.tr("New Budget"));
		hmPhrases.put("99", i18n.tr("Month"));
		hmPhrases.put("100", i18n.tr("Budget"));
		hmPhrases.put("101", i18n.tr("Configure"));
		hmPhrases.put("102", i18n.tr("An error occurred loading Infinity PFM help files.  Infinity PFM requires version 1.5 or higher of Mozilla if running on Linux.  If this does not solve your problem then please submit a bug report to http://sourceforge.net/tracker/?group_id=114604"));
		hmPhrases.put("103", i18n.tr("Error"));
		hmPhrases.put("104", i18n.tr("Enter budget name."));
		hmPhrases.put("105", i18n.tr("Budget name invalid"));
		hmPhrases.put("106", i18n.tr("January"));
		hmPhrases.put("107", i18n.tr("February"));
		hmPhrases.put("108", i18n.tr("March"));
		hmPhrases.put("109", i18n.tr("April"));
		hmPhrases.put("110", i18n.tr("May"));
		hmPhrases.put("111", i18n.tr("June"));
		hmPhrases.put("112", i18n.tr("July"));
		hmPhrases.put("113", i18n.tr("August"));
		hmPhrases.put("114", i18n.tr("September"));
		hmPhrases.put("115", i18n.tr("October"));
		hmPhrases.put("116", i18n.tr("November"));
		hmPhrases.put("117", i18n.tr("December"));
		hmPhrases.put("118", i18n.tr("Income"));
		hmPhrases.put("119", i18n.tr("Profit/Loss"));
		hmPhrases.put("120", i18n.tr("Add to budget"));
		hmPhrases.put("121", i18n.tr("Account already in budget"));
		hmPhrases.put("122", i18n.tr("Reports"));
		hmPhrases.put("123", i18n.tr("Monthly Balances"));
		hmPhrases.put("124", i18n.tr("Eclipse.org (http://www.eclipse.org/)"));
		hmPhrases.put("125", i18n.tr("Actual"));
		hmPhrases.put("126", i18n.tr("Prior Month"));
		hmPhrases.put("127", i18n.tr("Save budget values to all months"));
		hmPhrases.put("128", i18n.tr("Budget values have been applied to all months."));
		hmPhrases.put("129", i18n.tr("Over/Under Year"));
		hmPhrases.put("130", i18n.tr("Budget Remaining"));
		hmPhrases.put("131", i18n.tr("Reset estimated budget remaining"));
		hmPhrases.put("132", i18n.tr("Add Accounts From List"));
		hmPhrases.put("133", i18n.tr("Default Accounts"));
		hmPhrases.put("134", i18n.tr("Are you sure you want to add these accounts?"));
		hmPhrases.put("135", i18n.tr("Leaving the offset account blank will skip the row."));
		hmPhrases.put("136", i18n.tr("Account History"));
		hmPhrases.put("137", i18n.tr("Avg Weekly"));
		hmPhrases.put("138", i18n.tr("Budget Weekly"));	
		hmPhrases.put("139", i18n.tr("Select Account"));
		hmPhrases.put("140", i18n.tr("Remove From Budget"));
		hmPhrases.put("141", i18n.tr("Report save successful"));
		hmPhrases.put("142", i18n.tr("Add Recurring Transaction"));
		hmPhrases.put("143", i18n.tr("Name"));
		hmPhrases.put("144", i18n.tr("Frequency"));
		hmPhrases.put("145", i18n.tr("Next Run Date"));
		hmPhrases.put("146", i18n.tr("Monthly"));
		hmPhrases.put("147", i18n.tr("Weekly"));
		hmPhrases.put("148", i18n.tr("Yearly"));
		hmPhrases.put("149", i18n.tr("Bi-Weekly"));
		hmPhrases.put("150", i18n.tr("Pending Transactions"));
		hmPhrases.put("151", i18n.tr("Delete"));
		hmPhrases.put("152", i18n.tr("Saved Recurring Transactions"));
		hmPhrases.put("153", i18n.tr("Are you sure you want to delete this recurring transaction?"));
		hmPhrases.put("154", i18n.tr("Processing recurring transactions"));
		hmPhrases.put("155", i18n.tr("Manage Recurring Transactions"));
		hmPhrases.put("156", i18n.tr("Term(Mths)"));
		hmPhrases.put("157", i18n.tr("APR"));
		hmPhrases.put("158", i18n.tr("Backup"));
		hmPhrases.put("159", i18n.tr("Restore"));
		hmPhrases.put("160", i18n.tr("Database"));
		hmPhrases.put("161", i18n.tr("Select a directory"));
		hmPhrases.put("162", i18n.tr("Database backed up to: "));
		hmPhrases.put("163", i18n.tr("Select Date"));
		hmPhrases.put("164", i18n.tr("Budget Performance"));
		hmPhrases.put("165", i18n.tr("Select Budget"));
		hmPhrases.put("166", i18n.tr("Year"));
		
		hmPhrases.put("167", i18n.tr("Electric"));
		hmPhrases.put("168", i18n.tr("Telephone"));
		hmPhrases.put("169", i18n.tr("Groceries"));
		hmPhrases.put("170", i18n.tr("Eat Out"));
		hmPhrases.put("171", i18n.tr("Bank Fees"));
		hmPhrases.put("172", i18n.tr("Auto Insurance"));
		hmPhrases.put("173", i18n.tr("Auto Repairs"));
		hmPhrases.put("174", i18n.tr("Hobbies"));
		hmPhrases.put("175", i18n.tr("Clothing"));
		hmPhrases.put("176", i18n.tr("HBA"));
		hmPhrases.put("177", i18n.tr("Donations"));
		hmPhrases.put("178", i18n.tr("Property Taxes"));
		hmPhrases.put("179", i18n.tr("Income Taxes"));
		hmPhrases.put("180", i18n.tr("Recreation"));
		hmPhrases.put("181", i18n.tr("Home Improvement"));
		hmPhrases.put("182", i18n.tr("Medical"));
		hmPhrases.put("183", i18n.tr("Childcare"));
		hmPhrases.put("184", i18n.tr("ATM Withdrawal"));
		hmPhrases.put("185", i18n.tr("Fuel"));
		hmPhrases.put("186", i18n.tr("Grooming"));
		hmPhrases.put("187", i18n.tr("Cable/Internet"));
		hmPhrases.put("188", i18n.tr("Sanitation"));
		hmPhrases.put("189", i18n.tr("Water Utility"));
		hmPhrases.put("190", i18n.tr("Income"));
		hmPhrases.put("191", i18n.tr("Income Spouse"));
		hmPhrases.put("192", i18n.tr("Income Interest"));
		hmPhrases.put("193", i18n.tr("Income Other"));
		hmPhrases.put("194", i18n.tr("Bank"));
		hmPhrases.put("195", i18n.tr("Primary Checking"));
		hmPhrases.put("196", i18n.tr("Primary Savings"));
		hmPhrases.put("197", i18n.tr("Liability"));
		hmPhrases.put("198", i18n.tr("Credit Card"));
		hmPhrases.put("199", i18n.tr("First Mortgage"));
		hmPhrases.put("200", i18n.tr("Second Mortgage"));
		hmPhrases.put("201", i18n.tr("Car Loan"));
		hmPhrases.put("202", i18n.tr("Home Equity Loan"));
		hmPhrases.put("203", i18n.tr("Personal Loan"));
		hmPhrases.put("204", i18n.tr("Line Of Credit"));
		hmPhrases.put("205", i18n.tr("School Loan"));
		hmPhrases.put("206", i18n.tr("Expense"));
		hmPhrases.put("207", i18n.tr("Default Currency"));
		hmPhrases.put("208", i18n.tr("New Currency"));
		hmPhrases.put("209", i18n.tr("Currency Name"));
		hmPhrases.put("210", i18n.tr("Currency Code"));
		hmPhrases.put("211", i18n.tr("Exchange Rate"));
		hmPhrases.put("212", i18n.tr("US Dollar"));
		hmPhrases.put("213", i18n.tr("USD"));
		hmPhrases.put("214", i18n.tr("Update Currencies"));
		hmPhrases.put("215", i18n.tr("Last Update"));
		hmPhrases.put("216", i18n.tr("Update Method"));
		hmPhrases.put("217", i18n.tr("Bitcoin"));
		hmPhrases.put("218", i18n.tr("BTC"));
		hmPhrases.put("219", i18n.tr("New"));
		hmPhrases.put("220", i18n.tr("Recurring Transactions"));
		hmPhrases.put("221", i18n.tr("Income vs Expenses"));
		hmPhrases.put("222", i18n.tr("Start Date"));
		hmPhrases.put("223", i18n.tr("End Date"));
		hmPhrases.put("224", i18n.tr("Currencies"));
		hmPhrases.put("225", i18n.tr("Precision"));
		hmPhrases.put("226", i18n.tr("Method Name"));
		hmPhrases.put("227", i18n.tr("Service URL"));
		hmPhrases.put("228", i18n.tr("Search Path"));
		hmPhrases.put("229", i18n.tr("Define New Update Methods"));
		hmPhrases.put("230", i18n.tr("Delete Update Method"));
		hmPhrases.put("231", i18n.tr("Select Update Method"));
		hmPhrases.put("232", i18n.tr("Are you sure you want to delete this update method?"));
		hmPhrases.put("233", i18n.tr("Split Transaction"));
		hmPhrases.put("234", i18n.tr("Invalid offset account or amount"));
		hmPhrases.put("235", i18n.tr("Split"));
		hmPhrases.put("236", i18n.tr("Delete Row"));
		hmPhrases.put("237", i18n.tr("Check"));
		hmPhrases.put("238", i18n.tr("Post All"));
		hmPhrases.put("239", i18n.tr("pending transactions posted"));
		hmPhrases.put("240", i18n.tr("Import Rules"));
		hmPhrases.put("241", i18n.tr("Assign To"));
		hmPhrases.put("242", i18n.tr("New Rule"));
		hmPhrases.put("243", i18n.tr("Contains"));
		hmPhrases.put("244", i18n.tr("Starts With"));
		hmPhrases.put("245", i18n.tr("Ends With"));
		hmPhrases.put("246", i18n.tr("Import rule contains an invalid account!"));
		hmPhrases.put("247", i18n.tr("Name already in use."));
		hmPhrases.put("248",  i18n.tr("Gnome Look Icon Theme (www.gnome-look.org"));
		hmPhrases.put("249",  i18n.tr("Open reports externally in web browser."));
		hmPhrases.put("250",  i18n.tr("General"));
		hmPhrases.put("251",  i18n.tr("Open console on start."));
		//hmPhrases.put("251",  i18n.tr("Invalid Credentials"));
		hmPhrases.put("252",  i18n.tr("Mail"));
		hmPhrases.put("253",  i18n.tr("CSV"));
		hmPhrases.put("254",  i18n.tr("Import Name"));
		hmPhrases.put("255",  i18n.tr("Import Type"));
		hmPhrases.put("256",  i18n.tr("Date Field"));
		hmPhrases.put("257",  i18n.tr("Memo Field"));
		hmPhrases.put("258",  i18n.tr("Amount Field"));
		hmPhrases.put("259",  i18n.tr("None"));
		hmPhrases.put("260",  i18n.tr("Date Format"));
		hmPhrases.put("261",  i18n.tr("Continue"));
		hmPhrases.put("262",  i18n.tr("Income Total"));
		hmPhrases.put("263",  i18n.tr("Expense Total"));
		hmPhrases.put("264",  i18n.tr("Liability Total"));
		hmPhrases.put("265",  i18n.tr("Year-Month"));
		hmPhrases.put("266",  i18n.tr("Account Balance"));
		hmPhrases.put("267",  i18n.tr("Expense Balance"));
		hmPhrases.put("268",  i18n.tr("Budget Balance"));
		hmPhrases.put("269",  i18n.tr("Budget Name"));
		hmPhrases.put("270",  i18n.tr("Income Balance"));
		hmPhrases.put("271",  i18n.tr("Liability Balance"));
		hmPhrases.put("272",  i18n.tr("Account Type"));
		hmPhrases.put("273", i18n.tr("Account History All Time"));
		hmPhrases.put("274", i18n.tr("Apache FreeMarker (https://freemarker.apache.org/) "));
		hmPhrases.put("275",  i18n.tr("Account Total"));
		hmPhrases.put("276",  i18n.tr("Wallet"));
		hmPhrases.put("277",  i18n.tr("Send"));
		hmPhrases.put("278",  i18n.tr("Receive"));
		hmPhrases.put("279",  i18n.tr("To"));
		hmPhrases.put("280",  i18n.tr("Amount Local"));
		hmPhrases.put("281",  i18n.tr("Amount BSV"));
		hmPhrases.put("282",  i18n.tr("From"));
		hmPhrases.put("283",  i18n.tr("Enable Bitcoin SV Wallet (www.relysia.com)"));
		hmPhrases.put("284",  i18n.tr("Spending Password"));
		hmPhrases.put("285",  i18n.tr("Problem Saving Options"));
		hmPhrases.put("286",  i18n.tr("Are you sure you do not want to set a spending password?"));
		hmPhrases.put("286",  i18n.tr("Offset"));
		hmPhrases.put("288",  i18n.tr("Send address and send amount required."));
		hmPhrases.put("289",  i18n.tr("Address"));
		hmPhrases.put("290",  i18n.tr("Confirm"));
		hmPhrases.put("291",  i18n.tr("Show mnemonic phrase"));
		hmPhrases.put("292",  i18n.tr("Invalid wallet password"));
		hmPhrases.put("293",  i18n.tr("Enter mnemonic phrase"));
		hmPhrases.put("294",  i18n.tr("Mnemonic phrase required"));
		hmPhrases.put("295",  i18n.tr("Wallet restored"));
		hmPhrases.put("296",  i18n.tr("Coins Received"));
		hmPhrases.put("297",  i18n.tr("Detail"));
		hmPhrases.put("298",  i18n.tr("Aquire Currency"));
		hmPhrases.put("299",  i18n.tr("Cost Currency"));
		hmPhrases.put("300",  i18n.tr("Aquire Date"));
		hmPhrases.put("301",  i18n.tr("Qty Lifo"));
		hmPhrases.put("302",  i18n.tr("Qty Fifo"));
		hmPhrases.put("303",  i18n.tr("Cost"));
		hmPhrases.put("304",  i18n.tr("Email Address"));
		hmPhrases.put("305",  i18n.tr("Sign In"));
		hmPhrases.put("306",  i18n.tr("Sign In Successfull"));
		hmPhrases.put("307",  i18n.tr("Mnemonic phrase"));
		hmPhrases.put("308",  i18n.tr("Please keep this in a secure location."));
		hmPhrases.put("309",  i18n.tr("Copy to clipboard"));
		hmPhrases.put("310",  i18n.tr("Coins Sent!"));
		hmPhrases.put("311",  i18n.tr("Yearly Balances"));
		hmPhrases.put("312",  i18n.tr("Report was empty"));
		hmPhrases.put("313",  i18n.tr("Open as report"));
		hmPhrases.put("314",  i18n.tr("Register Report"));
		hmPhrases.put("315",  i18n.tr("Bookmarks"));
		hmPhrases.put("316",  i18n.tr("Browser not available"));
		hmPhrases.put("317",  i18n.tr("Bookmarks URL"));
		hmPhrases.put("318",  i18n.tr("Enter a paymail or Bitcoin address"));
		hmPhrases.put("319",  i18n.tr("Wallet Id"));
		hmPhrases.put("320",  i18n.tr("Leave blank to use default wallet"));
		hmPhrases.put("321",  i18n.tr("Report Path"));
		hmPhrases.put("322",  i18n.tr("Show unspent transactions outputs (UTXO) in console"));
		hmPhrases.put("323",  i18n.tr("Height"));
		hmPhrases.put("324",  i18n.tr("Position"));
		hmPhrases.put("325",  i18n.tr("Hash"));
		hmPhrases.put("326",  i18n.tr("Script"));
		hmPhrases.put("327",  i18n.tr("Path"));
		hmPhrases.put("328",  i18n.tr("Value"));
		hmPhrases.put("329",  i18n.tr("UTXO Report"));
		hmPhrases.put("330",  i18n.tr("Remove Budget"));
		hmPhrases.put("331", i18n.tr("Are you sure you want to delete budget: %s?"));
		hmPhrases.put("332", i18n.tr("Retirement Planner"));
		hmPhrases.put("333", i18n.tr("Plan"));
		hmPhrases.put("334", i18n.tr("Add Plan"));
		hmPhrases.put("335", i18n.tr("Delete Plan"));
		hmPhrases.put("336", i18n.tr("Run Plan"));
		hmPhrases.put("337", i18n.tr("Plan Name"));
		hmPhrases.put("338", i18n.tr("Starting Balance"));
		hmPhrases.put("339", i18n.tr("Starting Age"));
		hmPhrases.put("340", i18n.tr("Plan name can not be empty, starting age must be an integer, and starting balance must be a number."));
		hmPhrases.put("341", i18n.tr("Plan name already in use.  Please choose a different plan name."));
		hmPhrases.put("342", i18n.tr("Invest"));
		hmPhrases.put("343", i18n.tr("Draw"));
		hmPhrases.put("344", i18n.tr("Earn"));
		hmPhrases.put("345", i18n.tr("Return"));
		hmPhrases.put("346", i18n.tr("Add Event"));
		hmPhrases.put("347", i18n.tr("Event Name"));
		hmPhrases.put("348", i18n.tr("Event Type"));
		hmPhrases.put("349", i18n.tr("Event Value"));
		hmPhrases.put("350", i18n.tr("Event Value Type"));
		hmPhrases.put("351", i18n.tr("Percent"));
		hmPhrases.put("352", i18n.tr("Number"));
		hmPhrases.put("353", i18n.tr("Delete Event"));
		hmPhrases.put("354", i18n.tr("Ending Age"));
		hmPhrases.put("355", i18n.tr("Are you sure you want to delete plan: %s?"));
		hmPhrases.put("356", i18n.tr("Plan Results: %s"));
		hmPhrases.put("357", i18n.tr("Age"));
		hmPhrases.put("358", i18n.tr("Distribution"));
		hmPhrases.put("359", i18n.tr("Investment Returns"));
		hmPhrases.put("360", i18n.tr("Contributions"));
		hmPhrases.put("361", i18n.tr("Net Earnings"));
		hmPhrases.put("362", i18n.tr("Clone Plan"));
		hmPhrases.put("363", i18n.tr("New Plan Name"));
		hmPhrases.put("364", i18n.tr("New plan name cannot be empty"));
		hmPhrases.put("365", i18n.tr("Plan name already exists"));
		hmPhrases.put("366", i18n.tr("Plan not found"));
		hmPhrases.put("367", i18n.tr("Edit Event"));
		hmPhrases.put("368", i18n.tr("Fee"));
		hmPhrases.put("369", i18n.tr("Rename Plan"));
		hmPhrases.put("370", i18n.tr("Enter a new plan name"));
	}
	
	public String getPhrase(String id){
		//return i18n.tr((String)hmPhrases.get(id));
		return (String)hmPhrases.get(id);
	}

}
