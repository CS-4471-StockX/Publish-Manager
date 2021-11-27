package com.stockx.publishmanagerws.services;

import com.stockx.publishmanagerws.adapters.IndustryStockTrackerAdapter;
import com.stockx.publishmanagerws.adapters.LiveStockTrackerAdapter;
import com.stockx.publishmanagerws.adapters.MarketIndexTrackerAdapter;
import com.stockx.publishmanagerws.adapters.MqttAdapter;
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

    @Scheduled(fixedDelay = 30000)
    void updateSubscribedStocks(){

        List<Topic> liveStockList = topicRepository.getTopicByService("live-stock-tracker-ws");

        for(Topic topic : liveStockList){
            if(topic.getNumOfSubscribers() > 0) {
                String msg = liveStockTrackerAdapter.stockQuote(topic.getSymbol());
                publishMessage(topic.getSymbol(), msg);

                msg = liveStockTrackerAdapter.stockGraphs(topic.getSymbol());
                publishMessage(topic.getSymbol(), msg);

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
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void updateIndustryStockListings() {
        publishMessage("Energy", industryStockTrackerAdapter.getIndustryStockBySector("Energy"));
        publishMessage("Materials", industryStockTrackerAdapter.getIndustryStockBySector("Materials"));
        publishMessage("Industrials", industryStockTrackerAdapter.getIndustryStockBySector("Industrials"));
        publishMessage("Utilities", industryStockTrackerAdapter.getIndustryStockBySector("Utilities"));
        publishMessage("Healthcare", industryStockTrackerAdapter.getIndustryStockBySector("Healthcare"));
        publishMessage("Financials", industryStockTrackerAdapter.getIndustryStockBySector("Financials"));
        publishMessage("ConsumerDiscretionary", industryStockTrackerAdapter.getIndustryStockBySector("ConsumerDiscretionary"));
        publishMessage("ConsumerStaples", industryStockTrackerAdapter.getIndustryStockBySector("ConsumerStaples"));
        publishMessage("InformationTechnology", industryStockTrackerAdapter.getIndustryStockBySector("InformationTechnology"));
        publishMessage("CommunicationServices", industryStockTrackerAdapter.getIndustryStockBySector("CommunicationServices"));
        publishMessage("RealEstate", industryStockTrackerAdapter.getIndustryStockBySector("RealEstate"));
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
