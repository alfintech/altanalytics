package io.altanalytics.domain.currency;

import java.util.Date;

public class IntervalPriceRequest {

	private String currency;
	private Date date;
	
	public IntervalPriceRequest(String currency, Date date) {
		this.currency = currency;
		this.date = date;
	}
	
	public String getCurrency() {
		return currency;
	}
	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "MarketDataRequest [currency=" + currency + ", date=" + date + "]";
	}
	
}
