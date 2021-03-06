package com.lwq.demo5_exchange_topic;

import java.io.IOException;

import com.lwq.ConnectionUtil;
import com.rabbitmq.client.*;

/**
 * @author lwq
 * @date 2020/7/6 0006
 */
public class Receiver2 {
	private final static String EXCHANGE_NAME = "topic_exchange";
	private final static String QUEUE_NAME = "topic_queue2";

	public static void main(String[] argv) throws Exception {

		// 获取到连接以及mq通道
		Connection connection = ConnectionUtil.getConnection();
		final Channel channel = connection.createChannel();

		// 声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 绑定队列到交换机  #可以代表0个或多个单词
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "order.#");

		// 指该消费者在接收到队列里的消息但没有返回确认结果之前,它不会将新的消息分发给它。
		channel.basicQos(1);

		// 定义队列的消费者
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
			                           byte[] body)
					throws IOException {
				System.out.println("消费者2收到消息:" + new String(body));
				// 消费者手动发送ack应答
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		// 监听队列
		channel.basicConsume(QUEUE_NAME, false, consumer);
	}

}
