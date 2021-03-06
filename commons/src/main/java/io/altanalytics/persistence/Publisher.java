package io.altanalytics.persistence;

import io.altanalytics.domain.calendar.CalendarEvent;
import io.altanalytics.domain.currency.Analytic;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.PriceDelta;
import io.altanalytics.domain.market.MarketCap;
import io.altanalytics.domain.social.SocialStats;

import java.io.IOException;
import java.util.List;

public interface Publisher {

	void publishMarketData(List<IntervalPrice> prices) throws IOException;

	void publishAnalytics(List<Analytic> analytics) throws IOException;

	void publishEvents(List<CalendarEvent> events) throws IOException;

	void publishMarketCap(MarketCap marketCap) throws IOException;

	void publishPriceDeltas(List<PriceDelta> priceDeltas);

	void publishSocialStats(List<SocialStats> socialStatsList);

}