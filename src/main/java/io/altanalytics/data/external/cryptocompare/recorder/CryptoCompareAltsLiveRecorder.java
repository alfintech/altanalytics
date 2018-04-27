package io.altanalytics.data.external.cryptocompare.recorder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.altanalytics.data.external.cryptocompare.client.CryptoCompareLiveClient;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;
import io.altanalytics.util.BigDecimalUtil;
import io.altanalytics.util.DateUtil;
import io.altanalytics.util.PriceUtil;

@EnableScheduling
@Component
public class CryptoCompareAltsLiveRecorder extends AbstractCryptoCompareRecorder {

	private static final Logger LOG = LoggerFactory.getLogger(CryptoCompareAltsLiveRecorder.class);
	
	@Value("${recorder.live.alts.active}")
	private boolean active;

	@Value("${recorder.live.alts.interval}")
	private int interval;

	private Map<String, IntervalPrice> currentMarketData = new HashMap<String, IntervalPrice>();

	@Autowired
	public CryptoCompareLiveClient marketDataClient;

	@Scheduled(cron = "${recorder.live.alts.schedule}")
	public void tick() throws Exception {
		if(active) {
			LOG.info("Triggered live pricing recorder");
			long checkpoint1 = Calendar.getInstance().getTimeInMillis();
			Date requestDate = DateUtil.intervalStart(interval);

			List<IntervalPriceRequest> requests = requestsForCurrencyPairs(currencies, requestDate);
			List<IntervalPrice> latestIntervalPrices = fetch(marketDataClient, requests);
			List<IntervalPrice> deltas = delta(latestIntervalPrices, requestDate);
			long checkpoint2 = Calendar.getInstance().getTimeInMillis();
			if(!deltas.isEmpty()) {
				publish(deltas);
			}
			long checkpoint3 = Calendar.getInstance().getTimeInMillis();
			LOG.info("Retrieved " +currencies.length+ " currencies in " +(checkpoint2-checkpoint1)+ "ms. Published in " +(checkpoint3-checkpoint2)+ "ms. Total in " +(checkpoint3-checkpoint1)+ "ms");
		}
	}

	private List<IntervalPrice> delta(List<IntervalPrice> latestIntervalPrices, Date requestDate) {

		List<IntervalPrice> deltas = new ArrayList<IntervalPrice>();

		for(IntervalPrice latestIntervalPrice : latestIntervalPrices) {
			if(currentMarketData.containsKey(latestIntervalPrice.getCurrency())) {
				IntervalPrice delta = delta(latestIntervalPrice, currentMarketData.get(latestIntervalPrice.getCurrency()));
				deltas.add(delta);
			}
			currentMarketData.put(latestIntervalPrice.getCurrency(), latestIntervalPrice);
		}

		return deltas;
	} 

	private IntervalPrice delta(IntervalPrice latestIntervalPrice, IntervalPrice priorIntervalPrice) {

		IntervalPrice delta = new IntervalPrice(
				latestIntervalPrice.getCurrency(), 
				DateUtil.intervalStart(interval),
				DateUtil.intervalEnd(interval),
				priorIntervalPrice.getClose(), 
				latestIntervalPrice.getClose(),
				latestIntervalPrice.getClose(),
				latestIntervalPrice.getClose(),
				BigDecimalUtil.subtractToZero(latestIntervalPrice.getIntervalVolume(), priorIntervalPrice.getIntervalVolume()),
				latestIntervalPrice.getDayVolume());

		delta.setCloseUSD(PriceUtil.convertToBTC(delta.getClose()));

		return delta;
	}

}
