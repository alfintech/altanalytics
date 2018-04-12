package io.altanalytics.data.external.cryptocompare.client;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;

public class CryptoCompareRequestConsumer implements Callable<IntervalPrice> {

	private static final Logger LOG = Logger.getLogger(CryptoCompareRequestConsumer.class);
	private static final int RETRY_THRESHOLD = 2;

	private CryptoCompareClient client;
	private IntervalPriceRequest request;
	private int retryCount;

	public CryptoCompareRequestConsumer(CryptoCompareClient marketDataClient, IntervalPriceRequest request) {
		this.client = marketDataClient;
		this.request = request;
	}

	public IntervalPrice call()  {

		try {
			IntervalPrice intervalPrice = client.fetch(request);
			return intervalPrice;
		} catch (Exception e) {
			retryCount++;
			if(retryCount < RETRY_THRESHOLD) {
				LOG.warn("Retrying request: " +request);
				return call();
			} else {
				throw new RuntimeException();
			}
		}

	}
	
}
