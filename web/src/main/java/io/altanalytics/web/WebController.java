package io.altanalytics.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.altanalytics.persistence.influx.InfluxReader;

@Controller
public class WebController {

	public static final Logger logger = LoggerFactory.getLogger(WebController.class);

	@Autowired
	private InfluxReader reader;

    @GetMapping("/currency")
	public String currency(Map<String, Object> model, String currency) {
        model.put("currency", currency);
        model.put("decimals", 6);
        return "currency";
    }
}