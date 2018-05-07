package io.altanalytics.rest;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.persistence.influx.InfluxReader;

@RestController
@RequestMapping("api")
public class APIRestController {

	    public static final Logger logger = LoggerFactory.getLogger(APIRestController.class);
	  
	    @Autowired
	    private InfluxReader reader;
	 	 
	    @RequestMapping(value = "singlePrice")
	    public String price(long epochDate, String currency) throws Exception {
	    	IntervalPrice price = reader.getIntervalPrice(new Date(epochDate), currency);
	    	ObjectMapper mapper = new ObjectMapper();
	    	return mapper.writeValueAsString(price);
	    }
	 	 
	    @RequestMapping(value = "intervalPrices")
	    public String price(long fromEpochDate, long toEpochDate, String currency) throws Exception {
	    	List<IntervalPrice> prices = reader.getIntervalPrices(new Date(fromEpochDate), new Date(toEpochDate), currency);
	    	ObjectMapper mapper = new ObjectMapper();
	    	return mapper.writeValueAsString(prices);
	    }

}
