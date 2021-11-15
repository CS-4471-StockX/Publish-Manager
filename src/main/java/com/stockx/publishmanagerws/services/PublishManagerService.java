package com.stockx.publishmanagerws.services;

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
    MqttAdapter mqttAdapter;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LiveStockTrackerAdapter liveStockTrackerAdapter;

    @Autowired
    MarketIndexTrackerAdapter marketIndexTrackerAdapter;

    @Scheduled(fixedDelay = 30000)
    void publisherUpdate(){

        List<Topic> liveStockList = topicRepository.getTopicByService("live-stock-tracker-ws");

        for(Topic topic : liveStockList){
            if(topic.getNumOfSubscribers() > 0) {
                String msg = liveStockTrackerAdapter.stockQuote(topic.getSymbol());
                publishMessage(topic.getSymbol(), msg);

                msg = liveStockTrackerAdapter.stockGraphs(topic.getSymbol());
                publishMessage(topic.getSymbol(), msg);

            }
        }

        //Code for Market Index Updates once that service is added to AWS.
        /*List<Topic> marketIndexList = topicRepository.getTopicByService("market-index-tracker-ws");

        for(Topic topic : marketIndexList){
            if(topic.getNumOfSubscribers() > 0) {
                String msg = marketIndexTrackerAdapter.marketIndex(topic.getSymbol());
                publishMessage(topic.getSymbol(), msg);

                msg = marketIndexTrackerAdapter.historicalMarketIndex(topic.getSymbol());
                publishMessage(topic.getSymbol(), msg);
            }
        }*/
    }

    @Scheduled(cron = "0 * 10-16 * * *")
    private void updateMinutes(){
        List<Topic> liveStockList = topicRepository.getTopicByService("live-stock-tracker-ws");

        for(Topic topic : liveStockList){
            liveStockTrackerAdapter.updateMinutes(topic.getSymbol());
        }

    }

    @Scheduled(cron = "0 0 10-16 * * *")
    private void updateHours(){
        List<Topic> liveStockList = topicRepository.getTopicByService("live-stock-tracker-ws");

        for(Topic topic : liveStockList){
            liveStockTrackerAdapter.updateHours(topic.getSymbol());
        }
    }

    @Scheduled(cron = "0 0 10 * * *")
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
