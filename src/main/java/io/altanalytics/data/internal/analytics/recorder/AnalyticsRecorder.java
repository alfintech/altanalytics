package io.altanalytics.data.internal.analytics.recorder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.altanalytics.domain.currency.Analytic;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.persistence.Publisher;
import io.altanalytics.persistence.Reader;
import io.altanalytics.util.DateUtil;

@EnableScheduling
@Component
public class AnalyticsRecorder {

	private static final Logger LOG = LoggerFactory.getLogger(AnalyticsRecorder.class);

	@Value("${recorder.analytics.active}")
	private boolean active;

	@Value("${recorder.analytics.interval}")
	private int interval;

	@Value("${recorder.currencies}")
	protected String[] currencies;

	@Autowired
	protected Reader reader;

	@Autowired
	protected Publisher publisher;

	@Scheduled(cron = "${recorder.analytics.schedule}")
	public void tick() throws Exception {
		if(active) {
			
			//			BigDecimal intervalVolume = VolumeCalculator.cumulative(intervalPrices);
			//			BigDecimal dayVolume = intervalPrices.get(0).getDayVolume();
			//			BigDecimal dayAverageVolume = VolumeCalculator.average(dayVolume, interval);
			//			BigDecimal percentageVolume = BigDecimalUtil.multiply(BigDecimalUtil.divide(intervalVolume, dayAverageVolume), new BigDecimal(100));
			//
			//			//ATH % calcs
			//			BigDecimal allTimeHigh = reader.getAllTimeHigh(currencyPair).getClose();
			//			BigDecimal currentPrice = intervalPrices.get(intervalPrices.size()-1).getClose();
			//			BigDecimal percentageATH = BigDecimalUtil.divide(currentPrice, allTimeHigh);

			
			LOG.info("Triggered analytics recorder");
			Date analyticsEndDate = DateUtil.now();
			
			Date analyticsStartDate = DateUtil.shiftToPast(analyticsEndDate, interval);
			long checkpoint1 = Calendar.getInstance().getTimeInMillis();

			List<Analytic> analytics = new ArrayList<Analytic>();
			for(String currency : currencies) {

				//Volume calcs
				List<IntervalPrice> intervalPrices = reader.getIntervalPrices(analyticsStartDate, analyticsEndDate, currency);

				if(!intervalPrices.isEmpty()) {
					analytics.add(null);
				}
			}
			long checkpoint2 = Calendar.getInstance().getTimeInMillis();			
			publisher.publishAnalytics(analytics);
			long checkpoint3 = Calendar.getInstance().getTimeInMillis();
			LOG.info("Calculated analytics (interval " +analyticsStartDate+ "-" +analyticsEndDate+ ") for " +currencies.length+ " currencies in " +(checkpoint2-checkpoint1)+ "ms. Published in " +(checkpoint3-checkpoint2)+ "ms. Total in " +(checkpoint3-checkpoint1)+ "ms");
		}
	}

}
