package com.stockx.publishmanagerws.adapters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


public class MarketIndexTrackerAdapter {

    @Autowired
    private RestTemplate restTemplate;
    private static final String BASE_URL = "";

    public String marketIndex(String ticker){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/market-index")
                .queryParam("ticker", ticker).build().toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    public String historicalMarketIndex(String ticker){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("historical/market-index-price")
                .queryParam("ticker", ticker).build().toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}
