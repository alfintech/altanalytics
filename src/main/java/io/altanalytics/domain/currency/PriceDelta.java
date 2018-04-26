package io.altanalytics.domain.currency;

import java.math.BigDecimal;
import java.util.Date;

public class PriceDelta {

	private Date analyticDate;
	private String currency;
	private BigDecimal fiveMinuteDelta;
	private BigDecimal fifteenMinuteDelta;
	private BigDecimal thirtyMinuteDelta;
	private BigDecimal hourDelta;
	private BigDecimal sixHourDelta;
	private BigDecimal twelveHourDelta;
	private BigDecimal oneDayDelta;
	private BigDecimal sevenDaysDelta;
	private BigDecimal thirtyDaysDelta;
	
	public PriceDelta(Date analyticDate, String currency, BigDecimal fiveMinuteDelta, BigDecimal fifteenMinuteDelta,
			BigDecimal thirtyMinuteDelta, BigDecimal hourDelta, BigDecimal sixHourDelta, BigDecimal twelveHourDelta,
			BigDecimal oneDayDelta, BigDecimal sevenDaysDelta, BigDecimal thirtyDaysDelta) {

		this.analyticDate = analyticDate;
		this.currency = currency;
		this.fiveMinuteDelta = fiveMinuteDelta;
		this.fifteenMinuteDelta = fifteenMinuteDelta;
		this.thirtyMinuteDelta = thirtyMinuteDelta;
		this.hourDelta = hourDelta;
		this.sixHourDelta = sixHourDelta;
		this.twelveHourDelta = twelveHourDelta;
		this.oneDayDelta = oneDayDelta;
		this.sevenDaysDelta = sevenDaysDelta;
		this.thirtyDaysDelta = thirtyDaysDelta;
	}
	
	public Date getAnalyticDate() {
		return analyticDate;
	}
	
	public String getCurrency() {
		return currency;
	}

	public BigDecimal getFiveMinuteDelta() {
		return fiveMinuteDelta;
	}

	public BigDecimal getFifteenMinuteDelta() {
		return fifteenMinuteDelta;
	}

	public BigDecimal getThirtyMinuteDelta() {
		return thirtyMinuteDelta;
	}

	public BigDecimal getHourDelta() {
		return hourDelta;
	}

	public BigDecimal getSixHourDelta() {
		return sixHourDelta;
	}

	public BigDecimal getTwelveHourDelta() {
		return twelveHourDelta;
	}

	public BigDecimal getOneDayDelta() {
		return oneDayDelta;
	}

	public BigDecimal getSevenDaysDelta() {
		return sevenDaysDelta;
	}

	public BigDecimal getThirtyDaysDelta() {
		return thirtyDaysDelta;
	}
	
}
