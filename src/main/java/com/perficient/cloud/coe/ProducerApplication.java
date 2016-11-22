package com.perficient.cloud.coe;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	/*@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		return connectionFactory;
	}*/

	@Bean
	TopicExchange exchangePrftNotifier() {
		return new TopicExchange("perficient_notifier");
	}

	@Bean
	Queue queueEnterprise() {
		return new Queue("prft_all", true);
	}

	@Bean
	Queue queueHR() {
		return new Queue("prft_hr", true);
	}
	
	@Bean
	Queue queuePM() {
		return new Queue("prft_pm", true);
	}

	@Bean
	Queue queueSCPG() {
		return new Queue("prft_scpg", true);
	}

	@Bean
	Queue queueSQA() {
		return new Queue("prft_sqa", true);
	}


	@Bean
	Binding bindingExchangeFoo(Queue queueEnterprise, TopicExchange exchangePrftNotifier) {
		return BindingBuilder.bind(queueEnterprise).to(exchangePrftNotifier).with("*.all");
	}

	@Bean
	Binding bindingExchangeBar(Queue queueHR, TopicExchange exchangePrftNotifier) {
		return BindingBuilder.bind(queueHR).to(exchangePrftNotifier).with("*.hr");
	}

	@Bean
	public MappingJackson2MessageConverter jackson2Converter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		return converter;
	}

	@Autowired
	private Sender sender;

	@Override
	public void run(String... args) throws Exception {
		sender.sendToRabbitmq(new Foo(), new Bar());
	}
}

@Service
class Sender {

	@Autowired
	private RabbitMessagingTemplate rabbitMessagingTemplate;
	@Autowired
	private MappingJackson2MessageConverter mappingJackson2MessageConverter;

	public void sendToRabbitmq(final Foo foo, final Bar bar) {

		this.rabbitMessagingTemplate.setMessageConverter(this.mappingJackson2MessageConverter);

		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.all", foo);
		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.hr", bar);
		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.hr", bar);
		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.hr", bar);
		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.hr", bar);
		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.all", foo);
		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.hr", bar);
		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.hr", bar);
		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.hr", bar);
		this.rabbitMessagingTemplate.convertAndSend("perficient_notifier", "prft.hr", bar);

	}
}

class Bar {
	public int age = 26;
}

class Foo {
	public String name = "navin";
}