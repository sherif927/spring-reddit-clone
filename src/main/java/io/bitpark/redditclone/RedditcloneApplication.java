package io.bitpark.redditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedditcloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditcloneApplication.class, args);
	}

}
