package io.altanalytics.data.external.cryptocompare.recorder;

import java.util.Calendar;
import java.util.Date;

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

@EnableScheduling
@Component
public class CryptoCompareBTCLiveRecorder extends AbstractCryptoCompareRecorder {

	private static final Logger LOG = LoggerFactory.getLogger(CryptoCompareAltsLiveRecorder.class);

	@Value("${recorder.live.btc.active}")
	private boolean active;

	@Value("${recorder.live.btc.interval}")
	private int interval;

	private static IntervalPrice currentBTCPrice = null;

	public static IntervalPrice getBTCPrice() {
		return currentBTCPrice;
	}

	@Autowired
	public CryptoCompareLiveClient marketDataClient;

	@Scheduled(cron = "${recorder.live.btc.schedule}")
	public void tick() throws Exception {
		if(active) {
			LOG.info("Triggered live pricing recorder");
			long checkpoint1 = Calendar.getInstance().getTimeInMillis();
			Date requestDate = DateUtil.intervalStart(interval);

			IntervalPriceRequest request = new IntervalPriceRequest("BTC", requestDate);
			IntervalPrice latestIntervalPrice = marketDataClient.fetch(request, "USD");
			
			long checkpoint2 = Calendar.getInstance().getTimeInMillis();
			
			if(currentBTCPrice!=null) {
				IntervalPrice delta = delta(latestIntervalPrice, requestDate);
				publish(delta);
			}
			
			currentBTCPrice = latestIntervalPrice;
			long checkpoint3 = Calendar.getInstance().getTimeInMillis();
			LOG.info("Retrieved BTC/USD in " +(checkpoint2-checkpoint1)+ "ms. Published in " +(checkpoint3-checkpoint2)+ "ms. Total in " +(checkpoint3-checkpoint1)+ "ms");
		}
	}

	private IntervalPrice delta(IntervalPrice latestIntervalPrice, Date requestDate) {

		return delta(latestIntervalPrice, currentBTCPrice);
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

		delta.setCloseUSD(delta.getClose());
		return delta;
	}

}