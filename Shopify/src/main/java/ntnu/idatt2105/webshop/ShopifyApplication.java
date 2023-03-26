package ntnu.idatt2105.webshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
@EntityScan("ntnu.idatt2105.webshop.model")
public class ShopifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopifyApplication.class, args);
	}

}
