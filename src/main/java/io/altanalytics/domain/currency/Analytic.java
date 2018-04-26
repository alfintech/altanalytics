package io.altanalytics.domain.currency;

import java.math.BigDecimal;
import java.util.Date;

public class Analytic {

	private BigDecimal intervalVolume;
	private BigDecimal dayAverage;
	private BigDecimal percentageVolume;
	private BigDecimal percentageAllTimeHigh;
	private String currency;
	private Date date;
	
	public Analytic(String currency, BigDecimal intervalVolume, BigDecimal dayAverage, BigDecimal percentageVolume, BigDecimal percentageAllTimeHigh, Date date) {
		this.intervalVolume = intervalVolume;
		this.dayAverage = dayAverage;
		this.currency = currency;
		this.percentageVolume = percentageVolume;
		this.percentageAllTimeHigh = percentageAllTimeHigh;
		this.date = date;
	}

	public BigDecimal getIntervalVolume() {
		return intervalVolume;
	}

	public BigDecimal getDayAverage() {
		return dayAverage;
	}

	public BigDecimal getPercentageVolume() {
		return percentageVolume;
	}

	public BigDecimal getPercentageAllTimeHigh() {
		return percentageAllTimeHigh;
	}

	public String getCurrency() {
		return currency;
	}
	
	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "Analytic [intervalVolume=" + intervalVolume + ", dayAverage=" + dayAverage
				+ ", percentageVolume=" + percentageVolume + ", percentageAllTimeHigh=" + percentageAllTimeHigh
				+ ", currency=" + currency + ", date=" + date + "]";
	}

}
