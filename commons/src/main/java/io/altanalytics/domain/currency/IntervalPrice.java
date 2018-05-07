package io.altanalytics.domain.currency;

import java.math.BigDecimal;
import java.util.Date;

public class IntervalPrice implements Comparable<IntervalPrice>{

	private String currency;
	private Date openTime;
	private Date closeTime;
	private BigDecimal open;
	private BigDecimal low;
	private BigDecimal high;
	private BigDecimal close;
	private BigDecimal closeUSD;
	private BigDecimal intervalVolume;
	private BigDecimal dayVolume;

	public IntervalPrice(String currency, Date openTime, Date closeTime, BigDecimal open, BigDecimal close, BigDecimal low, BigDecimal high, BigDecimal intervalVolume, BigDecimal dayVolume) {
		this.currency = currency;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.open = open;
		this.close = close;
		this.low = low;
		this.high = high;
		this.intervalVolume = intervalVolume;
		this.dayVolume = dayVolume;
	}

	public String getCurrency() {
		return currency;
	}
	
	public Date getOpenTime() {
		return openTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public BigDecimal getClose() {
		return close;
	}

	public BigDecimal getLow() {
		return low;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public BigDecimal getIntervalVolume() {
		return intervalVolume;
	}

	public BigDecimal getDayVolume() {
		return dayVolume;
	}

	public BigDecimal getCloseUSD() {
		return closeUSD;
	}

	public void setCloseUSD(BigDecimal closeUSD) {
		this.closeUSD = closeUSD;
	}

	@Override
	public int compareTo(IntervalPrice marketData) {
		return this.openTime.compareTo(marketData.getOpenTime());
	}

	@Override
	public String toString() {
		return "MarketData [currency=" + currency + ", openTime=" + openTime + ", closeTime=" + closeTime
				+ ", open=" + open + ", low=" + low + ", high=" + high + ", close=" + close 
				+ ", intervalVolume=" + intervalVolume + ", dayVolume=" + dayVolume+ "]";
	}

}
