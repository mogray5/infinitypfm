package org.infinitypfm.core.data;

import java.io.Serializable;
import java.util.Date;

import org.infinitypfm.core.types.DefaultDateFormat;

public class RegisterEntry implements Serializable, IReportable {

	private static final long serialVersionUID = 3187626130107193990L;

	private Date transactionDate;
	private String memo;
	private long debit;
	private long credit;
	private DataFormatUtil _formatter = null;
	
	public RegisterEntry() {
	}

	public String getTransactionDateFormatted() {
		_formatter.setDate(transactionDate);
		return _formatter.getFormat(DefaultDateFormat.DAY);
	}

	public String getMemo() {
		return memo;
	}
	
	public void setMemo(String sMemo) {
		memo = sMemo;
	}
	
	public String getDebitFormatted() {
		return _formatter.getAmountFormatted(debit);
	}

	public String getCreditFormatted() {
		return _formatter.getAmountFormatted(credit);
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setDebit(long debit) {
		this.debit = debit;
	}

	public void setCredit(long credit) {
		this.credit = credit;
	}

	@Override
	public String getHeaderRow() {
		return null;
	}

	@Override
	public String toReportRow() {
		return null;
	}

	@Override
	public void setFormatter(DataFormatUtil formatter) {
		_formatter = formatter;

	}

}
