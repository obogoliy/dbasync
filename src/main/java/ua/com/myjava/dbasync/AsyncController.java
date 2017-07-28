package ua.com.myjava.dbasync;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pgasync.ConnectionPoolBuilder;
import com.github.pgasync.Db;

import rx.Single;
import ua.com.myjava.dbasync.domain.Derivative;

@Configuration
@RestController
public class AsyncController {
	@Bean
	public Db db() {
		return new ConnectionPoolBuilder()
				.hostname("localhost")
				.port(5432)
				.database("postgres")
				.username("postgres")
				.password("findata")
				.poolSize(10)
				.build();
	}

	@RequestMapping(path = "/derivative/{isin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Single<Derivative> findDerivativeAsync(@PathVariable String isin) {
		return db().querySet("select derivative_isin, product_name from DERIVATIVE_MASTER_DATA")
				.map(result -> new Derivative(result.row(0).getString("derivative_isin"), result.row(0).getString("product_name")))
				.toSingle();
	}
}
