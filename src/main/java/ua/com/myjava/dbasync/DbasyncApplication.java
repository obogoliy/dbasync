package ua.com.myjava.dbasync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pgasync.ConnectionPoolBuilder;
import com.github.pgasync.Db;

import rx.Observable;
import ua.com.myjava.dbasync.domain.Derivative;

@SpringBootApplication
public class DbasyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbasyncApplication.class, args);
	}

	@Bean
	public Db db() {
		return new ConnectionPoolBuilder()
				.hostname("localhost")
				.port(5432)
				.database("postgres")
				.username("postgres")
				.password("findata")
				.poolSize(20)
				.build();
	}

	@RequestMapping(path = "/derivative/{isin}")
	public Observable<Derivative> findQuote(@PathVariable String isin) {
		return db().querySet("select isin, product_name from DERIVATIVE_MASTER_DATA")
				.map(result -> new Derivative(result.row(0).getString("isin"), result.row(0).getString("product_name")));
	}
}
