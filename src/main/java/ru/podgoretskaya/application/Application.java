package ru.podgoretskaya.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.podgoretskaya.application.client.ConveyorClient;

@SpringBootApplication
@EnableFeignClients(clients = ConveyorClient.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
