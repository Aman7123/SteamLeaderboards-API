package com.aaronrenner.SteamAPI;

import java.util.concurrent.Executor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@EnableAsync
public class SteamApiApplication {

	public static void main(String[] args) {
		/* Total number of processors or cores available to the JVM */
		System.out.println("Available processors (cores): " + 
				Runtime.getRuntime().availableProcessors());

		/* Total memory currently in use by the JVM */
		System.out.println("Total memory (mb): " + 
				Runtime.getRuntime().totalMemory()/1000000);

		/* Total amount of free memory available to the JVM */
		System.out.println("Free memory (mb): " + 
				Runtime.getRuntime().freeMemory()/1000000);

		/* This will return Long.MAX_VALUE if there is no preset limit */
		long maxMemory = Runtime.getRuntime().maxMemory();
		/* Maximum amount of memory the JVM will attempt to use */
		System.out.println("Maximum memory (mb): " + 
				(maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory/1000000));
		  
		SpringApplication.run(SteamApiApplication.class, args);
	}
	
	/**
	 * This method is part of creating a faster means of executing deeper code.
	 * {@link https://spring.io/guides/gs/async-method/}
	 * @return a new java.util.concurrent.Executor
	 */
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//executor.setCorePoolSize(2);
		//executor.setMaxPoolSize(2);
		//executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("ProfileLookup");
		executor.initialize();
		return executor;
	}

}
