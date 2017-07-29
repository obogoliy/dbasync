package ua.com.myjava.dbasync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
	@Autowired
	private RandomIsinService randomIsinService;

	@Bean(destroyMethod = "close")
	public Db db() {
		return new ConnectionPoolBuilder()
				.hostname("localhost")
				.port(5432)
				.database("postgres")
				.username("postgres")
				.password("findata")
				.poolSize(10)
				.pipeline(true)
				.build();
	}

	@RequestMapping(path = "/derivative-async", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Single<Derivative> getRandomDerivative() {
		return db().queryRows("select derivative_isin, product_name from DERIVATIVE_MASTER_DATA where derivative_isin = $1",
				randomIsinService.getRandomIsin())
				.map(result -> new Derivative(result.getString("derivative_isin"), result.getString("product_name")))
				.toSingle();
	}
}
