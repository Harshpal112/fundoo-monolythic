package com.bridgeit.fundoo.user.service;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bridgeit.fundoo.user.model.Email;

@Service
//@EnableRabbit
public class MailService {
	
	@Autowired
	private static JavaMailSender javamailsender;
	
	
	@SuppressWarnings("static-access")
	public MailService(JavaMailSender javamailsender) {
		this.javamailsender = javamailsender;
	}
//	@RabbitListener(queues="${spring.rabbitmq.template.default-receive-queue}")
	public static void send(Email email) {
		SimpleMailMessage simple = new SimpleMailMessage();
		simple.setTo(email.getTo());
		simple.setSubject(email.getSubject());
		simple.setText(email.getBody());
		javamailsender.send(simple);	
	}	
}


