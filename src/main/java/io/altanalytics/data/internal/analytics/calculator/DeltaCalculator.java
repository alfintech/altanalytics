package io.altanalytics.data.internal.analytics.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.PriceDelta;
import io.altanalytics.persistence.Reader;
import io.altanalytics.util.BigDecimalUtil;
import io.altanalytics.util.DateUtil;

@Component
public class DeltaCalculator {

	private static final long MIN_MS = 1000 *60;
	private static final long FIVE_MIN_MS = 5 * MIN_MS;
	private static final long FIFTEEN_MIN_MS = 15 * MIN_MS;
	private static final long THIRTY_MIN_MS = 30 * MIN_MS;
	private static final long ONE_HOUR_MIN_MS = 60 * MIN_MS;
	private static final long SIX_HOURS_MIN_MS = 6 * ONE_HOUR_MIN_MS;
	private static final long TWELVE_HOURS_MIN_MS = 12 * ONE_HOUR_MIN_MS;
	private static final long ONE_DAY_MIN_MS = 24 * ONE_HOUR_MIN_MS;
	private static final long SEVEN_DAYS_MS = 7 * ONE_DAY_MIN_MS;
	private static final long THIRTY_DAYS_MS = 30 * ONE_DAY_MIN_MS;

	@Autowired
	protected Reader reader;

	public PriceDelta calculate(String currency, Date analyticDate) throws Exception {

		Date fiveMinsAgo = DateUtil.shiftToPast(analyticDate, FIVE_MIN_MS);
		Date fifteenMinsAgo = DateUtil.shiftToPast(analyticDate, FIFTEEN_MIN_MS);
		Date thirtyMinsAgo = DateUtil.shiftToPast(analyticDate, THIRTY_MIN_MS);
		Date oneHourAgo = DateUtil.shiftToPast(analyticDate, ONE_HOUR_MIN_MS);
		Date sixHoursAgo = DateUtil.shiftToPast(analyticDate, SIX_HOURS_MIN_MS);
		Date twelveHoursAgo = DateUtil.shiftToPast(analyticDate, TWELVE_HOURS_MIN_MS);
		Date oneDayAgo = DateUtil.shiftToPast(analyticDate, ONE_DAY_MIN_MS);
		Date sevenDaysAgo = DateUtil.shiftToPast(analyticDate, SEVEN_DAYS_MS);
		Date thirtyDaysAgo = DateUtil.shiftToPast(analyticDate, THIRTY_DAYS_MS);

		List<Date> datesToAnalyse = new ArrayList<Date>();
		datesToAnalyse.add(analyticDate);
		datesToAnalyse.add(fiveMinsAgo);
		datesToAnalyse.add(fifteenMinsAgo);
		datesToAnalyse.add(thirtyMinsAgo);
		datesToAnalyse.add(oneHourAgo);
		datesToAnalyse.add(sixHoursAgo);
		datesToAnalyse.add(twelveHoursAgo);
		datesToAnalyse.add(oneDayAgo);
		datesToAnalyse.add(sevenDaysAgo);
		datesToAnalyse.add(thirtyDaysAgo);

		Map<Date, IntervalPrice> intervalPrices = reader.getIntervalPrices(datesToAnalyse, currency);

		BigDecimal deltaFiveMinutes = delta(intervalPrices.get(analyticDate), intervalPrices.get(fiveMinsAgo));
		BigDecimal deltaFifteenMinutes = delta(intervalPrices.get(analyticDate), intervalPrices.get(fifteenMinsAgo));
		BigDecimal deltaThirtyMinutes = delta(intervalPrices.get(analyticDate), intervalPrices.get(thirtyMinsAgo));
		BigDecimal deltaOneHour = delta(intervalPrices.get(analyticDate), intervalPrices.get(oneHourAgo));
		BigDecimal deltaSixHours = delta(intervalPrices.get(analyticDate), intervalPrices.get(sixHoursAgo));
		BigDecimal deltaTwelveHours = delta(intervalPrices.get(analyticDate), intervalPrices.get(twelveHoursAgo));
		BigDecimal deltaOneDay = delta(intervalPrices.get(analyticDate), intervalPrices.get(oneDayAgo));
		BigDecimal deltaSevenDays = delta(intervalPrices.get(analyticDate), intervalPrices.get(sevenDaysAgo));
		BigDecimal deltaThirtyDays = delta(intervalPrices.get(analyticDate), intervalPrices.get(thirtyDaysAgo));

		return new PriceDelta(analyticDate, currency, deltaFiveMinutes, deltaFifteenMinutes, deltaThirtyMinutes, 
				deltaOneHour, deltaSixHours, deltaTwelveHours, deltaOneDay, deltaSevenDays, deltaThirtyDays);
	}

	private BigDecimal delta(IntervalPrice analyticDate, IntervalPrice pastDate) {
		if(pastDate!=null && analyticDate!=null) {
			return BigDecimalUtil.divide(BigDecimalUtil.subtract(analyticDate.getClose(), pastDate.getClose()), pastDate.getClose());
		} 
		return BigDecimal.ZERO;
	}
}
