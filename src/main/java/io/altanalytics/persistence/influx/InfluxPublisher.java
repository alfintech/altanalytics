package io.altanalytics.persistence.influx;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.altanalytics.domain.calendar.CalendarEvent;
import io.altanalytics.domain.currency.Analytic;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.PriceDelta;
import io.altanalytics.domain.market.MarketCap;
import io.altanalytics.domain.social.SocialStats;
import io.altanalytics.persistence.Publisher;

@Component
public class InfluxPublisher implements Publisher {

	@Value("${influx.username}")
	private String username;

	@Value("${influx.password}")
	private String password;

	@Value("${influx.host}")
	private String host;

	@Value("${influx.database}")
	private String database;

	@Value("${influx.policy}")
	private String retentionPolicy;

	private InfluxDB influxDB;

	@PostConstruct
	public void initalise() {
		this.influxDB = InfluxDBFactory.connect(host, username, password);
		if(!influxDB.databaseExists(database)) {
			influxDB.createDatabase(database);
			influxDB.createRetentionPolicy(retentionPolicy, database, "30d", 1, true);
		}
	}

	@Override
	public void publishMarketData(List<IntervalPrice> prices) throws IOException {

		BatchPoints batchPoints = BatchPoints
				.database(database)	
				.retentionPolicy(retentionPolicy)
				.build();

		for(IntervalPrice price : prices) {
			Point point = Point.measurement("IntervalPrice" +price.getCurrency())
					.time(price.getCloseTime().getTime(), TimeUnit.MILLISECONDS)
					.addField("currency", price.getCurrency())
					.addField("dayVolume", price.getDayVolume())
					.addField("intervalVolume", price.getIntervalVolume())
					.addField("high", price.getHigh().doubleValue())
					.addField("low", price.getLow().doubleValue())
					.addField("open", price.getOpen().doubleValue())
					.addField("close", price.getClose().doubleValue())
					.addField("closeUSD", price.getCloseUSD().doubleValue())
					.addField("openTime", price.getOpenTime().getTime())
					.addField("closeTime", price.getCloseTime().getTime())
					.build();

			batchPoints.point(point);
		}

		influxDB.write(batchPoints);
	}

	@Override
	public void publishAnalytics(List<Analytic> analytics) throws IOException {

		BatchPoints batchPoints = BatchPoints
				.database(database)
				.retentionPolicy(retentionPolicy)
				.build();

		for(Analytic analytic : analytics) {
			Point point = Point.measurement("Analytic" +analytic.getCurrency())
					.time(analytic.getDate().getTime(), TimeUnit.MILLISECONDS)
					.addField("currency", analytic.getCurrency())
					.addField("intervalVolume", analytic.getIntervalVolume())
					.addField("percentageAllTimeHigh", analytic.getPercentageAllTimeHigh())
					.addField("percentageVolume", analytic.getPercentageVolume())
					.addField("dayAverage", analytic.getDayAverage())
					.build();

			batchPoints.point(point);
		}

		influxDB.write(batchPoints);
	}

	@Override
	public void publishEvents(List<CalendarEvent> events) throws IOException {

		BatchPoints batchPoints = BatchPoints
				.database(database)
				.retentionPolicy(retentionPolicy)
				.build();

		for(CalendarEvent event : events) {
			for(String currency : event.getCurrencies()) {
				Point point = Point.measurement("CalendarEvent" +currency)
						.time(event.getEventDate().getTime(), TimeUnit.MILLISECONDS)
						.addField("currency", currency)
						.addField("description", event.getDescription())
						.addField("eventId", event.getEventId())
						.addField("title", event.getTitle())
						.addField("positiveVoteCount", event.getPositiveVoteCount())
						.addField("voteCount", event.getVoteCount())
						.addField("createdDate", event.getCreatedDate().getTime())
						.build();
				batchPoints.point(point);
			}
		}
		influxDB.write(batchPoints);
	}

	@Override
	public void publishMarketCap(MarketCap marketCap) throws IOException {


		Point point = Point.measurement("MarketCap")
				.time(marketCap.getDate().getTime(), TimeUnit.MILLISECONDS)
				.addField("activeCurrencies", marketCap.getActiveCurrencies())
				.addField("btcDominance", marketCap.getBtcDominance())
				.addField("dayVolume", marketCap.getDayVolume())
				.addField("marketCap", marketCap.getMarketCap())
				.build();

		influxDB
		.setDatabase(database)
		.setRetentionPolicy(retentionPolicy)
		.write(point);
	}

	@Override
	public void publishPriceDeltas(List<PriceDelta> priceDeltas) {

		BatchPoints batchPoints = BatchPoints
				.database(database)
				.retentionPolicy(retentionPolicy)
				.build();

		for(PriceDelta priceDelta : priceDeltas) {
			Point point = Point.measurement("PriceDelta" +priceDelta.getCurrency())
					.time(priceDelta.getAnalyticDate().getTime(), TimeUnit.MILLISECONDS)
					.addField("currency", priceDelta.getCurrency())
					.addField("fiveMinute", priceDelta.getFiveMinuteDelta())
					.addField("fifteenMinute", priceDelta.getFifteenMinuteDelta())
					.addField("thirtyMinute", priceDelta.getThirtyMinuteDelta())
					.addField("oneHour", priceDelta.getHourDelta())
					.addField("sixHour", priceDelta.getSixHourDelta())
					.addField("twelveHour", priceDelta.getTwelveHourDelta())
					.addField("oneDay", priceDelta.getOneDayDelta())
					.addField("sevenDays", priceDelta.getSevenDaysDelta())
					.addField("thirtyDays", priceDelta.getThirtyDaysDelta())
					.build();
			batchPoints.point(point);
		}
		influxDB.write(batchPoints);
	}

	@Override
	public void publishStats(List<SocialStats> socialStatsList) {
		// TODO Auto-generated method stub
		
	}

}
