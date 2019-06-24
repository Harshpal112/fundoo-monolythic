//package com.bridgeit.fundoo.configuration;
//
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//@Configuration
//@EnableRabbit
//public class RabbitmqConfig {
//
//	private String queueName = "queue";
//	private String exchange = "exchange";
//	private String routingkey = "trim";
//
//	@Bean
//	Queue queue() {
//		return new Queue(queueName);
//	}
//
//	@Bean
//	DirectExchange exchange() {
//		return new DirectExchange(exchange);
//	}
//
//	@Bean
//	Binding binding(Queue queue, DirectExchange exchange) {
//		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
//	}
//
//	@Bean
//	public MessageConverter jsonMessageConverter() {
//		return new Jackson2JsonMessageConverter();
//	}

//	@Bean
//	@Primary
//	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//		final RabbitTemplate rabbitTemplate1 = new RabbitTemplate(connectionFactory);
//		rabbitTemplate1.setMessageConverter(jsonMessageConverter());
//		return rabbitTemplate1;
//	}



//	
//}
