package com.stockx.publishmanagerws.services;

import com.stockx.publishmanagerws.adapters.*;
import com.stockx.publishmanagerws.entity.Topic;
import com.stockx.publishmanagerws.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PublishManagerService {

    @Autowired
    private MqttAdapter mqttAdapter;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LiveStockTrackerAdapter liveStockTrackerAdapter;

    @Autowired
    private MarketIndexTrackerAdapter marketIndexTrackerAdapter;

    @Autowired
    private IndustryStockTrackerAdapter industryStockTrackerAdapter;

    @Autowired
    private CurrencyTrackerAdapter currencyTrackerAdapter;

    @Scheduled(fixedDelay = 10000)
    void updateSubscribedStocks(){

        List<Topic> liveStockList = topicRepository.getTopicByService("live-stock-tracker-ws");

        for(Topic topic : liveStockList){
            System.out.println("Trying to deal with: "+topic.getSymbol());
            if(topic.getNumOfSubscribers() > 0) {
                try {
                    String msg = liveStockTrackerAdapter.stockQuote(topic.getSymbol());
                    publishMessage(topic.getSymbol(), msg);

                    msg = liveStockTrackerAdapter.stockGraphs(topic.getSymbol());
                    publishMessage(topic.getSymbol(), msg);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Scheduled(fixedDelay = 3600000)
    public void updateMarketIndexListings() {
        //Publishing Market Index data, currently having issues with external API being exhausted.
        List<Topic> marketIndexList = topicRepository.getTopicByService("market-index-tracker-ws");

        for(Topic topic : marketIndexList){
            if(topic.getNumOfSubscribers() > 0) {
                String msg = marketIndexTrackerAdapter.getMarketIndexQuote(topic.getSymbol());
                publishMessage(topic.getSymbol(), msg);

                msg = marketIndexTrackerAdapter.getHistoricalMarketIndex(topic.getSymbol());
                publishMessage(topic.getSymbol(), msg);
            }
        }

        List<Topic> currencyList = topicRepository.getTopicByService("currency-tracker-ws");

        for(Topic topic : currencyList){
            if(topic.getNumOfSubscribers() > 0) {
                String msg = currencyTrackerAdapter
                        .convert(topic.getSymbol().split("_")[0], topic.getSymbol().split("_")[1]);
                publishMessage(topic.getSymbol(), msg);

                msg = currencyTrackerAdapter
                        .graph(topic.getSymbol().split("_")[0], topic.getSymbol().split("_")[1]);
                publishMessage(topic.getSymbol(), msg);
            }
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void updateIndustryStockListings() {
        List<Topic> sectorList = topicRepository.getTopicByService("industry-stock-listings-ws");

        for(Topic topic : sectorList){
            if(topic.getNumOfSubscribers() > 0) {
                String msg = industryStockTrackerAdapter.getIndustryStockBySector(topic.getSymbol());
                publishMessage(topic.getSymbol(), msg);
            }
        }
    }

    @Scheduled(cron = "0 * 15-21 * * *")
    private void updateMinutes(){
        List<Topic> liveStockList = topicRepository.getTopicByService("live-stock-tracker-ws");

        for(Topic topic : liveStockList){
            liveStockTrackerAdapter.updateMinutes(topic.getSymbol());
        }

    }

    @Scheduled(cron = "0 0 15-21 * * *")
    private void updateHours(){
        List<Topic> liveStockList = topicRepository.getTopicByService("live-stock-tracker-ws");

        for(Topic topic : liveStockList){
            liveStockTrackerAdapter.updateHours(topic.getSymbol());
        }
    }

    @Scheduled(cron = "0 0 15 * * *")
    private void updateDays(){
        List<Topic> liveStockList = topicRepository.getTopicByService("live-stock-tracker-ws");

        for(Topic topic : liveStockList){
            liveStockTrackerAdapter.updateDays(topic.getSymbol());
        }
    }

    public void publishMessage(String topic, String message) {
        mqttAdapter.publish(topic, message);
    }

}