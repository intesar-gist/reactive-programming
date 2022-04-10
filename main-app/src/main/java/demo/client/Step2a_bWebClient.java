package demo.client;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import demo.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class Step2a_bWebClient {

	private static final Logger logger = LoggerFactory.getLogger(Step2a_bWebClient.class);

	private static WebClient client = WebClient.create("http://localhost:8081?delay=2");


	public static void main(String[] args) throws InterruptedException {

		Instant start = Instant.now();
//
//		for (int i = 1; i <= 3; i++) {
//			System.out.println("Getting id=" + i);
//
//			client.get()
//				  .uri("/person/{id}", i)
//				  .retrieve()
//				  .bodyToMono(Person.class);
//		}

		List<Mono<Person>> collect = Stream.of(1, 2, 3)
						.map(i -> client.get()
										.uri("/person/{id}", i)
										.retrieve()
										.bodyToMono(Person.class)
										.doOnNext(person -> System.out.println("Got " + person)))
						.collect(Collectors.toList());

		Mono.when(collect).block();

		logTime(start);
	}


	private static void logTime(Instant start) {
		logger.debug("Total: " + Duration.between(start, Instant.now()).toMillis() + " millis");
	}

}


/*

2b: Compose on completion of multiple requests

		List<Mono<Person>> collect = Stream.of(1, 2, 3)
						.map(i -> client.get()
										.uri("/person/{id}", i)
										.retrieve()
										.bodyToMono(Person.class)
										.doOnNext(person -> System.out.println("Got " + person)))
						.collect(Collectors.toList());

		Mono.when(collect).block();


 */
