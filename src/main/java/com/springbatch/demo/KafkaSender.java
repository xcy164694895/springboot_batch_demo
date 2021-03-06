package com.springbatch.demo;

import com.alibaba.fastjson.JSON;
import com.springbatch.constant.BatchCnst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;


/**
 * @author xiongchenyang
 * @Date 2019/9/2
 **/
@Slf4j
@Component
public class KafkaSender<T> {

    @Resource
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void send(T obj){
        String jsonObj = JSON.toJSONString(obj);
        log.info("---------------- message = {}",jsonObj);
        ListenableFuture<SendResult<String,Object>> future = kafkaTemplate.send(BatchCnst.BATCH_TOPIC,jsonObj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("Produce: The message failed to be sent:" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //TODO 业务处理
                log.info("Produce: The message was sent successfully:");
                log.info("Produce: _+_+_+_+_+_+_+ result: " + stringObjectSendResult.toString());
            }
        });

    }
}
