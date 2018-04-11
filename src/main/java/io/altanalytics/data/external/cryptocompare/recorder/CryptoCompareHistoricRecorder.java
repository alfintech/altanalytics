package io.altanalytics.data.external.cryptocompare.recorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import io.altanalytics.data.external.cryptocompare.client.CryptoCompareHistoricClient;
import io.altanalytics.domain.currency.CurrencyPair;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;
import io.altanalytics.util.BigDecimalUtil;
import io.altanalytics.util.CurrencyPairUtil;
import io.altanalytics.util.DateUtil;

@EnableScheduling
@Component
public class CryptoCompareHistoricRecorder extends AbstractCryptoCompareRecorder {

	private static final Logger LOG = LoggerFactory.getLogger(CryptoCompareHistoricRecorder.class);

	private static final long HOURS_IN_YEAR = 365 * 24;
	private static final long SECONDS_IN_HOUR = 60 * 60;
	private static final long MILLISECONDS_IN_HOUR = 60 * 60 * 1000;
	private static final long MS_IN_TEN_SECS = 1000 * 10;

	@Value("${recorder.past.active}")
	private boolean active;
	
	@Autowired
	public CryptoCompareHistoricClient marketDataClient;

	@PostConstruct
	public void init() throws Exception {
		if(active) {
			process();
		}
	}

	public void process() throws Exception {

		List<CurrencyPair> currencyPairs = CurrencyPairUtil.constructCurrencyPairs(tradeCurrencies, baseCurrencies);
		Date datetimeToRetrieve = startOfThisHour();

		for(int i=0; i<HOURS_IN_YEAR; i++) {
			LOG.info("Triggered historic pricing recorder");
			long checkpoint1 = Calendar.getInstance().getTimeInMillis();
			datetimeToRetrieve = DateUtil.shiftToPast(datetimeToRetrieve, MILLISECONDS_IN_HOUR);
			List<IntervalPriceRequest> requests = requestsForCurrencyPairs(currencyPairs, datetimeToRetrieve);
			List<IntervalPrice> intervalPrices = fetch(marketDataClient, requests);
			long checkpoint2 = Calendar.getInstance().getTimeInMillis();
//			publish(interpolate(intervalPrices));
			publish(intervalPrices);
			long checkpoint3 = Calendar.getInstance().getTimeInMillis();
			LOG.info("For historic date " +datetimeToRetrieve+ " retrieved " +currencyPairs.size()+ " currencies in " +(checkpoint2-checkpoint1)+ "ms. Published in " +(checkpoint3-checkpoint2)+ "ms. Total in " +(checkpoint3-checkpoint1)+ "ms");

		}
	}

	private Date startOfThisHour() {
		Calendar currentTime = Calendar.getInstance();
		currentTime.set(Calendar.MINUTE, 0);
		currentTime.set(Calendar.SECOND, 0);
		currentTime.set(Calendar.MILLISECOND, 0);
		return currentTime.getTime();
	}
	
	private List<IntervalPrice> interpolate(List<IntervalPrice> marketDataList) {

		List<IntervalPrice> interpolated = new ArrayList<IntervalPrice>();

		for(IntervalPrice marketData : marketDataList) {
			interpolated.addAll(interpolateHour(marketData));
		}
		
		return interpolated;
	}

	private List<IntervalPrice> interpolateHour(IntervalPrice marketData) {

		List<IntervalPrice> interpolated = new ArrayList<IntervalPrice>();
		
		Date slidingWindow = new Date(marketData.getOpenTime().getTime());
		
		while(slidingWindow.before(marketData.getCloseTime())) {
			Date openTime = new Date(slidingWindow.getTime());
			slidingWindow = DateUtil.shiftToFuture(openTime, MS_IN_TEN_SECS);
			Date closeTime = new Date(slidingWindow.getTime());
			interpolated.add(new IntervalPrice(marketData.getCurrencyPair(), openTime, closeTime, marketData.getOpen(), marketData.getClose(), marketData.getLow(), marketData.getHigh(), BigDecimalUtil.divide(marketData.getIntervalVolume(), new BigDecimal(SECONDS_IN_HOUR)), BigDecimal.ZERO));
		}

		return interpolated;
	}
	
}
