import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * RabbitTemplate发送消息的方法常用的有send和convertAndSend（两个方法都提供了非常多的重载的方法），可以结合自己的需求使用对应的方法。
 * 发送消息时可以设置属性及header信息，还有消息编码、contenttype等。
 * @author lwq
 * @date 2020/7/7 0007
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:rabbitmq-context.xml"})
public class ProducerTest {

	Logger logger = LoggerFactory.getLogger(ProducerTest.class);

	@Resource
	private RabbitTemplate rabbitTemplate;
	//CorrelationData中包含一个id(我们可以自定义，将其与发送的消息相关联)，当开启了confirm机制后，会收到包含该参数的回调，用于消息可靠性投递，可选的
	private CorrelationData corrData=new CorrelationData(String.valueOf(System.currentTimeMillis()));

	/**
	 * 发送简单文本消息
	 * 运行结果：可以看到控制台有如下输出：可以看到消费者收到了消息，confirm回调也触发了。
	 */
	@Test
	public void test_sendTextMsg1(){
		//1 创建消息
		//可以添加一些消息属性
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.getHeaders().put("desc", "信息描述..");
		messageProperties.getHeaders().put("type", "自定义消息类型..");
		Message message = new Message("send简单文本消息".getBytes(), messageProperties);
		rabbitTemplate.send("spring_topic_exchange","test.add", message,corrData);
	}

	/**
	 * 发送简单文本消息
	 */
	@Test
	public void test_sendTextMsg2(){
		//可以设置消息属性，可选的
		MessagePostProcessor mp=new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				logger.info("------发消息时添加额外的设置---------");
				message.getMessageProperties().getHeaders().put("desc", "额外修改的信息描述");
				message.getMessageProperties().getHeaders().put("attr", "额外新加的属性");
				return message;
			}
		};
		rabbitTemplate.convertAndSend("spring_topic_exchange","test.add","convertAndSend简单文本消息", mp, corrData);
	}

	/**
	 * 发送json文本消息
	 */
	@Test
	public void test_sendJsonStringMsg(){
		JSONObject json = new JSONObject();
		json.put("name", "张三");
		json.put("age", 20);
		rabbitTemplate.convertAndSend("spring_topic_exchange","test.add",json.toJSONString(),corrData);
	}

	/**
	 * 发送json消息
	 */
	@Test
	public void test_sendJsonObjMsg(){
		JSONObject json = new JSONObject();
		json.put("name", "李四");
		json.put("age", 36);
		byte[] bytes = JSONObject.toJSONBytes(json, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
		MessageProperties props=new MessageProperties();
		props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		Message message=new Message(bytes, props);
		rabbitTemplate.send("spring_topic_exchange","test.add", message, corrData);
	}

	/**
	 * 测试消息无法被路由，触发return回调
	 * 运行结果：可以看到消费者没有收到消息，触发了return回调表示消息无法被路由，另外confirm回调也触发了。
	 */
	@Test
	public void test_return_noRoute(){
		rabbitTemplate.convertAndSend("spring_topic_exchange","errorKey","无法路由的消息",corrData);
	}

}
