package io.altanalytics.util;

import java.math.BigDecimal;

import io.altanalytics.data.external.cryptocompare.recorder.CryptoCompareBTCLiveRecorder;

public class PriceUtil {

	public static BigDecimal convertToBTC(BigDecimal priceInBTC) {
		return BigDecimalUtil.multiply(priceInBTC, CryptoCompareBTCLiveRecorder.getBTCPrice().getClose());
	}

}
