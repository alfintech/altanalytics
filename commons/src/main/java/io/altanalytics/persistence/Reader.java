package io.altanalytics.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.altanalytics.domain.currency.IntervalPrice;

public interface Reader {

	IntervalPrice getAllTimeHigh(String currency) throws Exception;

	List<IntervalPrice> getIntervalPrices(Date fromDate, Date toDate, String currency) throws Exception;

	Map<Date, IntervalPrice> getIntervalPrices(List<Date> dates, String currency) throws Exception;

	IntervalPrice getIntervalPrice(Date date, String currency) throws Exception;

	List<IntervalPrice> getIntervalPrices(Date fromDate, Date toDate, String currency, int minuteIntervals) throws Exception;

}