package com.stockx.publishmanagerws.adapters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


public class MarketIndexTrackerAdapter {

    @Autowired
    private RestTemplate restTemplate;
    private static final String BASE_URL = "https://market-index-tracker.stockx.software";

    public String getMarketIndexQuote(String ticker){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/market-index")
                .queryParam("ticker", ticker).build().toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    public String getHistoricalMarketIndex(String ticker){
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/historical/market-index-price")
                .queryParam("ticker", ticker).build().toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}
