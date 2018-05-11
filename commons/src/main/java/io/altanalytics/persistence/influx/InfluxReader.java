package io.altanalytics.persistence.influx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Series;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.persistence.Reader;

@Component
public class InfluxReader implements Reader {

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
	}

	@Override
	public IntervalPrice getAllTimeHigh(String currency) throws Exception {

		String queryTemplate = "SELECT openTime, closeTime, intervalVolume, open, close, low, high, dayVolume FROM %s.IntervalPrice%s limit 1";
		String queryFormatted = String.format(queryTemplate, retentionPolicy, currency, retentionPolicy, currency);

		Query query = new Query(queryFormatted, this.database);
		QueryResult result = influxDB.query(query);

		List<Series> series = result.getResults().iterator().next().getSeries();
		return convertFromResultSeries(currency, series.get(0).getValues().get(0));
	}

	@Override
	public List<IntervalPrice> getIntervalPrices(Date fromDate, Date toDate, String currency) throws Exception {

		String queryTemplate = "SELECT openTime, closeTime, intervalVolume, open, close, low, high, dayVolume FROM %s.IntervalPrice%s WHERE closeTime>=%d AND closeTime<=%d";
		String queryFormatted = String.format(queryTemplate, retentionPolicy, currency, fromDate.getTime(), toDate.getTime());

		Query query = new Query(queryFormatted, this.database);
		QueryResult result = influxDB.query(query);

		List<Series> series = result.getResults().iterator().next().getSeries();

		if(series!=null) {
			return convertToList(currency, series.get(0));
		}

		return Collections.emptyList();
	}
	
	@Override
	public List<IntervalPrice> getIntervalPrices(Date fromDate, Date toDate, String currency, int interval) throws Exception {
		
		String queryTemplate = "SELECT first(openTime), last(closeTime), sum(intervalVolume), first(open), last(close), min(low), max(high), last(dayVolume) FROM %s.IntervalPrice%s WHERE closeTime>=%d AND closeTime<=%d GROUP BY time(%dm) fill(none)";
		String queryFormatted = String.format(queryTemplate, retentionPolicy, currency, fromDate.getTime(), toDate.getTime(), interval);

		Query query = new Query(queryFormatted, this.database);
		QueryResult result = influxDB.query(query);

		List<Series> series = result.getResults().iterator().next().getSeries();

		if(series!=null) {
			return convertToList(currency, series.get(0));
		}

		return Collections.emptyList();
	}

	@Override
	public IntervalPrice getIntervalPrice(Date date, String currency) throws Exception {

		String queryTemplate = "SELECT openTime, closeTime, intervalVolume, open, close, low, high, dayVolume FROM %s.IntervalPrice%s WHERE closeTime=%d";
		String queryFormatted = String.format(queryTemplate, retentionPolicy, currency, date.getTime());

		Query query = new Query(queryFormatted, this.database);
		QueryResult result = influxDB.query(query);

		List<Series> series = result.getResults().iterator().next().getSeries();

		if(series!=null && !series.get(0).getValues().isEmpty()) {
			return convertFromResultSeries(currency, series.get(0).getValues().get(0));
		}

		return null;
	}

	@Override
	public Map<Date, IntervalPrice> getIntervalPrices(List<Date> dates, String currency) throws Exception {

		String queryTemplate = "SELECT openTime, closeTime, intervalVolume, open, close, low, high, dayVolume FROM %s.IntervalPrice%s WHERE (%s)";
		String queryFormatted = String.format(queryTemplate, retentionPolicy, currency, toEpochClauses("closeTime", dates));

		Query query = new Query(queryFormatted, this.database);
		QueryResult result = influxDB.query(query);

		List<Series> series = result.getResults().iterator().next().getSeries();

		if(series!=null) {
			return convertToMap(currency, series.get(0));
		}

		return Collections.emptyMap();
	}

	private String toEpochClauses(String fieldName, List<Date> dates) {

		StringBuffer buffer = new StringBuffer();

		for(Iterator<Date> i = dates.iterator(); i.hasNext(); ) {
			buffer.append(fieldName +"=" +i.next().getTime());
			if(i.hasNext()) {
				buffer.append(" or ");
			}
		}

		return buffer.toString();
	}

	public static List<IntervalPrice> convertToList(String currency, Series series) {

		List<IntervalPrice> results = new ArrayList<IntervalPrice>();

		for(List<Object> entry : (List<List<Object>>) series.getValues()) {

			IntervalPrice intervalPrice = convertFromResultSeries(currency, entry);
			results.add(intervalPrice);
		}

		return results;

	}

	public static Map<Date, IntervalPrice> convertToMap(String currency, Series series) {

		Map<Date, IntervalPrice> results = new HashMap<Date, IntervalPrice>();

		for(List<Object> entry : (List<List<Object>>) series.getValues()) {

			IntervalPrice intervalPrice = convertFromResultSeries(currency, entry);
			results.put(intervalPrice.getCloseTime(), intervalPrice);
		}

		return results;

	}

	public static IntervalPrice convertFromResultSeries(String currency, List<Object> entry) {


		long openTimeMs = ((Double) entry.get(1)).longValue();
		long closeTimeMs = ((Double) entry.get(2)).longValue();

		Date openTime = new Date(openTimeMs);
		Date closeTime = new Date(closeTimeMs);

		BigDecimal intervalVolume = new BigDecimal((Double) entry.get(3));
		BigDecimal open = new BigDecimal((Double) entry.get(4));
		BigDecimal close = new BigDecimal((Double) entry.get(5));
		BigDecimal low = new BigDecimal((Double) entry.get(6));
		BigDecimal high = new BigDecimal((Double) entry.get(7));
		BigDecimal dayVolume = new BigDecimal((Double) entry.get(8));

		return new IntervalPrice(currency, openTime, closeTime, open, close, low, high, intervalVolume, dayVolume);
	}

}
