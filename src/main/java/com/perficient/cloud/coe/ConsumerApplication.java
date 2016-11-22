package com.perficient.cloud.coe;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableRabbit
public class ConsumerApplication implements RabbitListenerConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@Bean
	public MappingJackson2MessageConverter jackson2Converter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		return converter;
	}

	@Bean
	public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		factory.setMessageConverter(jackson2Converter());
		return factory;
	}

	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
		registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
	}

	@SuppressWarnings("unused")
	@Autowired
	private Receiver receiver;

}

@Service
class Receiver {
	@RabbitListener(queues = "prft_all")
	public void receiveMessage(Foo1 foo) {
		System.out.println("Received Foo<" + foo.name + ">");
	}

	@RabbitListener(queues = "prft_hr")
	public void receiveMessage(Bar1 bar) {
		System.out.println("Received Bar<" + bar.age + ">");
	}
}

class Foo1 {
	public String name;
}

class Bar1 {
	public int age;
}