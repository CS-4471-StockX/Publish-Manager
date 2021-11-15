package com.stockx.publishmanagerws.adapters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class LiveStockTrackerAdapter {

    @Autowired
    private RestTemplate restTemplate;
    private static final String BASE_URL = "https://live-stock-tracker.stockx.software";

    public String stockQuote(String ticker){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/stock-quote")
                .queryParam("ticker", ticker).build().toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    public String stockGraphs(String ticker){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/graphs")
                .queryParam("ticker", ticker).build().toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    public void updateMinutes(String ticker){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/update-minutes")
                .queryParam("ticker", ticker).build().toUriString();

        restTemplate.put(url, null);
    }

    public void updateHours(String ticker){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/update-hours")
                .queryParam("ticker", ticker).build().toUriString();

        restTemplate.put(url, null);
    }

    public void updateDays(String ticker){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/update-days")
                .queryParam("ticker", ticker).build().toUriString();

        restTemplate.put(url, null);
    }

}
