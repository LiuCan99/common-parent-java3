package com.czxy.bos.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class TestProducter {

    public static void main(String[] args) throws Exception {
        //创建连接工程，使用默认路径（tcp://localhost:61616）
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        //获得连接
        Connection connection = connectionFactory.createConnection();
        //开始连接
        connection.start();

        //建立会话 (自动确认，客户端发送和接收消息不需要做额外的工作)
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        //创建队列
        Queue queue = session.createQueue("HelloWorld");


        //生产者发送消息
        MessageProducer producer = session.createProducer(queue);
        TextMessage textMessage = session.createTextMessage("重大新闻666");
        producer.send(textMessage);

        session.commit();
        session.close();
        connection.close();

    }
}


