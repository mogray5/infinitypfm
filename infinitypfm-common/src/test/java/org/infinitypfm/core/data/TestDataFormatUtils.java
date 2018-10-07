package org.infinitypfm.core.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class TestDataFormatUtils {

	@Test
	public void TestFormatting() {
		
		DataFormatUtil format = new DataFormatUtil(5);
		//Month starts at zero for some reason so this is October 5th 2018
		Date date = new GregorianCalendar(2018, 9, 5).getTime();
		format.setDate(date);
		
		String result = format.getFormat(DataFormatUtil.DefaultDateFormat);
		assertEquals("10-05-2018", result);

		result = format.getAmountFormatted(10000000000L);
		assertEquals("100.00000", result);
		
		format.setPrecision(4);
		result = format.getAmountFormatted(10000000000L);
		assertEquals("100.0000", result);
		
		result = format.getAmountFormatted(10000090000L);
		assertEquals("100.0009", result);
		
		result = format.getAmountFormatted(10009990000L);
		assertEquals("100.0999", result);
		
	}
	
	@Test
	public void MoneyAndRoundingTest() {
		
		DataFormatUtil format = new DataFormatUtil();
		double result = format.roundDouble(1123.659D, "####.0");
		assertEquals("1123.7", Double.toString(result));
		
		long lResult = DataFormatUtil.moneyToLong("(100.60)");
		assertEquals(-10060000000L, lResult);
		
		lResult = DataFormatUtil.moneyToLong("100.60");
		assertEquals(10060000000L, lResult);
		
		BigDecimal rBigDecimal = format.strictDivide("500", "500", 2);
		assertEquals("1.00", rBigDecimal.toString());
		
		rBigDecimal = format.strictMultiply("1", "0");
		assertEquals("0", rBigDecimal.toString());
	}
	
	@Test
	public void EncodingTest() {
		
		DataFormatUtil format = new DataFormatUtil();
		EncoderException error = null;
		String result = null;
		
		try {
			result = format.urlEncode("aslash/and spaces");
			
		} catch (EncoderException e) {
			error = e;
		}
		
		assertNull(error);
		assertEquals("aslash%2Fand+spaces", result);
		
		result = format.urlDecode(result);
		
		assertEquals("aslash/and spaces", result);
		
		Date date = new GregorianCalendar(2018, 9, 5).getTime();
		format.setDate(date);
		
		result = format.urlEncode(date);
		
		assertEquals("10-05-2018", result);
		
	}
}
