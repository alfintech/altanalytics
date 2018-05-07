package io.altanalytics.data.external.common;

import java.math.BigDecimal;

import io.altanalytics.data.external.cryptocompare.recorder.CryptoCompareBTCLiveRecorder;
import io.altanalytics.util.BigDecimalUtil;

public class PriceUtil {

	public static BigDecimal convertToBTC(BigDecimal priceInBTC) {
		return BigDecimalUtil.multiply(priceInBTC, CryptoCompareBTCLiveRecorder.getBTCPrice().getClose());
	}

}
