package com.github.fiviumaustralia.rnshpilot.fhir;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.UUID;

public class RPCClient {
  private static final String REQUEST_QUEUE_NAME = "patient_queue";

  private Connection connection;
  private Channel channel;
  private String replyQueueName;
  private QueueingConsumer consumer;

  public RPCClient(String username, String password, String host, int port) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUsername(username);
    factory.setPassword(password);
    factory.setHost(host);
    factory.setPort(port);
    connection = factory.newConnection();
    channel = connection.createChannel();

    replyQueueName = channel.queueDeclare().getQueue();
    consumer = new QueueingConsumer(channel);
    channel.basicConsume(replyQueueName, true, consumer);
  }

  public byte[] call(String methodName, String message) throws Exception {
    byte[] response = null;
    String corrId = UUID.randomUUID().toString();

    BasicProperties props = new BasicProperties
        .Builder()
        .correlationId(corrId)
        .replyTo(replyQueueName)
        .build();

    channel.basicPublish("", REQUEST_QUEUE_NAME, props, message.getBytes("UTF-8"));

    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      if (delivery.getProperties().getCorrelationId().equals(corrId)) {
        response = delivery.getBody();
        break;
      }
    }

    return response;
  }

  public void close() throws Exception {
    connection.close();
  }
}
