package raf.rs.SportsBooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SportsBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsBookingApplication.class, args);
	}

}
