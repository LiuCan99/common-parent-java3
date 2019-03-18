package com.czxy.bos.consumer;

import javax.jms.MapMessage;
import javax.jms.Message;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by 音老怪 on 2018/9/17.
 */
@Component
public class SmsConsumer {

    @JmsListener(destination="czxy.queue")
    public void receiveQueue(Message message){
        try {
            MapMessage mapMessage = (MapMessage) message;
            String phone = mapMessage.getString("phone");
            String code = mapMessage.getString("code");

            //SmsUtil.sendSms(phone, null, code, null, null);

            System.out.println("消费者consumer：" + phone + " : " + code);
        } catch (Exception e) {
        }
    }


}

