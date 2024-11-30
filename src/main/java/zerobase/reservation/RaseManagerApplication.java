package zerobase.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RaseManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaseManagerApplication.class, args);
	}

}
