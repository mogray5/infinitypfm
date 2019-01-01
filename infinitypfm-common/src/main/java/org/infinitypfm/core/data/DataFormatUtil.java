/*
 * Copyright (c) 2005-2018 Wayne Gray All rights reserved
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
package org.infinitypfm.core.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.infinitypfm.core.conf.LangInstance;
import org.infinitypfm.core.conf.PfmSettings;

/**
 * Helper methods for working with dates, number formats,
 * and rounding.
 * 
 */
public class DataFormatUtil implements Serializable {
	
	protected static final Logger LOG = Logger.getLogger(DataFormatUtil.class.getName());

	private static final long serialVersionUID = 6215132601470795925L;
	public static final String DefaultDateFormat = "M-dd-yyyy";

	String[] monthName = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November",
			"December" };

	private Date date = null;
	private GregorianCalendar calendar = null;
	private DateFormat dateFmt = new SimpleDateFormat(DataFormatUtil.DefaultDateFormat);
	private DecimalFormat formatter = null;
	private GregorianCalendar today = null;
	private URLCodec _codec = null;
	private int _precision;
	
	public DataFormatUtil() {
		today = new GregorianCalendar();
		calendar = new GregorianCalendar();
		_codec = new URLCodec();
		formatter = new DecimalFormat(NumberFormat.getDefault());
	}
	
	/**
	 * 
	 * @param precision set precision level of numbers.
	 * Bitcoin and related currencies can be up to eight
	 * decimal places.
	 */
	public DataFormatUtil(int precision) {
		today = new GregorianCalendar();
		calendar = new GregorianCalendar();
		_codec = new URLCodec();
		_precision = precision;
		formatter = new DecimalFormat(NumberFormat.getDefault(_precision));
	}

	/**
	 * Use a date created external to this class.
	 * 
	 * @param dt Date to format
	 */
	public void setDate(Date dt) {
		date = dt;
		calendar.setTime(date);

	}

	/**
	 * Use a date with passed year and month.
	 * 
	 * @param yr Year
	 * @param month Month 1-12
	 */
	public void setDate(int yr, int month) {
		calendar = new GregorianCalendar(yr, month - 1, 1);
		date = calendar.getTime();
	}

	
	/**
	 * Convert the passed string to a date
	 * using the current format setting.
	 * 
	 * @param sDate String to parse into a date
	 */
	public void setDate(String sDate) {

		try {
			date = dateFmt.parse(sDate);
		} catch (ParseException e) {
			LOG.fine(e.getMessage());
		}

		calendar = new GregorianCalendar();
		if (date != null) {
			calendar.setTime(date);
		}

	}

	/**
	 * Convert the passed string to a date
	 * using the passed format setting.
	 * 
	 * @param sDate String to parse into a date
	 * @param format Format tu use for parsing the date
	 */
	public void setDate(String sDate, String format) {
		dateFmt = new SimpleDateFormat(format);
		setDate(sDate);
	}
	
	public Date getDate() {
		return calendar.getTime();
	}

	public int getMonth() {
		return calendar.get(Calendar.MONTH) + 1;

	}

	/**
	 * Return the full name for month of the current
	 * date setting.  January, February etc.
	 * 
	 * @param offset Java offsetted month to return 0-11 
	 * @return
	 */
	public String getMonthName(int offset) {
		return monthName[calendar.get(Calendar.MONTH) + offset];
	}

	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	public int getPrecision() {
		return _precision;
	}

	public void setPrecision(int _precision) {
		this._precision = _precision;
	}
	
	public String getFormat(String format) {
		dateFmt = new SimpleDateFormat(format);
		if (date != null)
			return dateFmt.format(date);
		else
			return dateFmt.format(this.getToday());
	}

	/**
	 * Format a number using default format with passed
	 * precision.
	 * 
	 * @param amount in long format
	 * @return Formatted amount as string
	 */
	public String getAmountFormatted(long amount) {
		formatter.applyPattern(NumberFormat.getDefault(_precision));

		BigDecimal amtD = strictDivide(Long.toString(amount), "100000000",
				_precision);

		return formatter.format(amtD);

	}

	/**
	 * Format a number using passed format with passed
	 * precision.
	 * 
	 * @param amount in long format
	 * @param format format to use
	 * @return Formatted amount as a string
	 */
	public String getAmountFormatted(long amount, String format) {

		formatter.applyPattern(format);

		BigDecimal amtD = strictDivide(Long.toString(amount), "100000000",
				_precision);

		return formatter.format(amtD);
	}

	/**
	 * Round passed double using the passed format.
	 * 
	 * @param val number to be rounded
	 * @param format format to use
	 * @return rounded double
	 */
	public double roundDouble(double val, String format) {
		DecimalFormat twoDForm = new DecimalFormat(format);
		return Double.valueOf(twoDForm.format(val));
	}

	/**
	 * Move a date forward to a future date.  Used in recurring
	 * transactions 
	 * 
	 * @param frequency as string "Monthly, Daily, etc"
	 * @return Future date
	 */
	public Date setNext(String frequency) {

		LangInstance lang = LangInstance.getInstance();
		
		if (frequency.equals(lang.getPhrase(PfmSettings.RECUR_BIWEEKLY))) {
			calendar.add(Calendar.WEEK_OF_YEAR, 2);
		} else if (frequency.equals(lang.getPhrase(PfmSettings.RECUR_MONTHLY))) {
			calendar.add(Calendar.MONTH, 1);
		} else if (frequency.equals(lang.getPhrase(PfmSettings.RECUR_WEEKLY))) {
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
		} else if (frequency.equals(lang.getPhrase(PfmSettings.RECUR_YEARLY))) {
			calendar.add(Calendar.YEAR, 1);
		}

		return calendar.getTime();
	}

	public Date getToday() {
		return today.getTime();
	}

	/**
	 * Convert a money value formatted as a string back into 
	 * long format which is what's stored in the DB.
	 *  
	 * @param val money value as a String
	 * @return money value converted long format
	 */
	public static long moneyToLong(String val) {

		if (val.startsWith("(")) {

			val = val.replaceAll("\\(", "\\-").replaceAll("\\)", "");
		}

		// Strip off commas
		val = val.replaceAll("\\,", "");
		
		BigDecimal newVal = new BigDecimal(val);

		return moneyToLong(newVal);

	}

	/**
	 * Convert a money value formatted as a BigDecimal back into 
	 * long format which is what's stored in the DB.
	 * 
	 * @param val money value as BigDecimal
	 * @return money value converted long format
	 */
	public static long moneyToLong(BigDecimal val) {

		return val.multiply(new BigDecimal("100000000")).longValue();

	}

	/**
	 * Divide two money values without losing precision.
	 * 
	 * @param numerator BigDecimal
	 * @param denominator BigDecimal
	 * @param scale Desired decimal precision of the output
	 * @return BigDecimal of divided result
	 */
	public BigDecimal strictDivide(String numerator, String denominator,
			int scale) {

		BigDecimal num = new BigDecimal(numerator);
		BigDecimal den = new BigDecimal(denominator);
		BigDecimal result = num.divide(den, scale, RoundingMode.HALF_UP);

		return result;

	}

	/**
	 * Multiply two money values passed as strings without losing
	 * precision.
	 * 
	 * @param x money value as string
	 * @param y value as string
	 * @return BigDecimal of multiplied result
	 */
	public BigDecimal strictMultiply(String x, String y) {

		BigDecimal valX = new BigDecimal(x);
		BigDecimal valY = new BigDecimal(y);
		BigDecimal result = valX.multiply(valY);

		return result;

	}
	
	/**
	 * Format string to be a valid URL
	 * @param orig 
	 * @return
	 * @throws EncoderException 
	 */
	public String urlEncode(String orig) throws EncoderException{
		
		String result = "";
		
		if (orig!=null && orig.length()>0) result = _codec.encode(orig);	
		return result;
	}
	
	/**
	 * URL Encode a date value
	 * 
	 * @param dt Date
	 * @return String result
	 */
	public String urlEncode(Date dt) {
		
		if (dt == null) return "";
		
		this.setDate(dt);
		return this.getFormat(DataFormatUtil.DefaultDateFormat);
	}
	
	/**
	 * Decode url encoded string
	 * 
	 * @param orig
	 * @return
	 * @throws DecoderException
	 */
	public String urlDecode(String orig) {
		
		String result = orig;
		
		if (orig!=null && orig.length()>0) {
			try {
				result = _codec.decode(orig);
			} catch (DecoderException e) {}	
		}
		return result;
		
	}
}
