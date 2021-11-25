package com.stockx.publishmanagerws.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class IndustryStockTrackerAdapter {

    @Autowired
    private RestTemplate restTemplate;
    private static final String BASE_URL = "https://industry-stock-tracker.stockx.software";

    public String getIndustryStockBySector(String sector) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/industry-stock-listings")
                .queryParam("sector", sector).build().toUriString();
        return restTemplate.getForObject(url, String.class);
    }

}
