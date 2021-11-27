package com.stockx.publishmanagerws.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class CurrencyTrackerAdapter {
    @Autowired
    private RestTemplate restTemplate;
    private static final String BASE_URL = "https://currency-tracker.stockx.software";

    public String convert(String c1, String c2){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/currencies")
                .queryParam("c1", c1).queryParam("c2", c2).build().toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    public String graph(String c1, String c2){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/graph")
                .queryParam("c1", c1).queryParam("c2", c2).build().toUriString();

        return restTemplate.getForObject(url, String.class);
    }

}
