package ua.garmash.internetshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternetShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternetShopApplication.class, args);
/*		ConfigurableApplicationContext context = SpringApplication.run(InternetShopApplication.class, args);
		PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
		System.out.println(encoder.encode("admin"));*/
    }

}
