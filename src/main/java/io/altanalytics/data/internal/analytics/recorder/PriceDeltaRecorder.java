package io.altanalytics.data.internal.analytics.recorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.altanalytics.data.internal.analytics.calculator.DeltaCalculator;
import io.altanalytics.domain.currency.PriceDelta;
import io.altanalytics.persistence.Publisher;
import io.altanalytics.persistence.Reader;
import io.altanalytics.util.DateUtil;

@EnableScheduling
@Component
public class PriceDeltaRecorder {

	private static final Logger LOG = LoggerFactory.getLogger(PriceDeltaRecorder.class);

	@Value("${recorder.analytics.delta.active}")
	private boolean active;

	@Value("${recorder.currencies}")
	protected String[] currencies;

	@Autowired
	protected DeltaCalculator deltaCalculator;
				
	@Autowired
	protected Reader reader;

	@Autowired
	protected Publisher publisher;

	@Scheduled(cron = "${recorder.analytics.delta.schedule}")
	public void tick() throws Exception {
		if(active) {
			LOG.info("Triggered analytics price delta recorder");
			Date analyticDate = DateUtil.intervalStart(DateUtil.MIN_MS);
			
			List<PriceDelta> deltaPrices = new ArrayList<PriceDelta>();
			
			for(String currency : currencies) {
				deltaPrices.add(deltaCalculator.calculate(currency, analyticDate));
			}
			
			publisher.publishPriceDeltas(deltaPrices);
		}
	}
}
