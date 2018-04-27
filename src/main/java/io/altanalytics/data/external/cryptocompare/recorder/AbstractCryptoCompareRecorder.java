package io.altanalytics.data.external.cryptocompare.recorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.altanalytics.data.external.cryptocompare.client.CryptoCompareClient;
import io.altanalytics.data.external.cryptocompare.client.CryptoCompareRequestManager;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;
import io.altanalytics.persistence.Publisher;

public abstract class AbstractCryptoCompareRecorder {
	
	@Autowired
	protected Publisher publisher;

	@Value("${recorder.currencies}")
	protected String[] currencies;

	protected List<IntervalPriceRequest> requestsForCurrencyPairs(String[] currencies, Date date) {
		
		List<IntervalPriceRequest> requests = new ArrayList<IntervalPriceRequest>();
		
		for(String currency : currencies) {
			requests.add(new IntervalPriceRequest(currency, date));
		}

		return requests;
	}
	
	protected List<IntervalPrice> fetch(CryptoCompareClient marketDataClient, List<IntervalPriceRequest> marketDataRequests) throws Exception {
		CryptoCompareRequestManager marketDataRequestManager = new CryptoCompareRequestManager(marketDataClient);
		return marketDataRequestManager.fetch(marketDataRequests);
	}
	
	protected void publish(List<IntervalPrice> prices) throws Exception {
		publisher.publishMarketData(prices);		
	}

	protected void publish(IntervalPrice price) throws Exception {
		List<IntervalPrice> prices = new ArrayList<IntervalPrice>();
		prices.add(price);
		publisher.publishMarketData(prices);		
	}

}
