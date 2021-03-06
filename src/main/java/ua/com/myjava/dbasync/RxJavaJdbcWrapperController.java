package ua.com.myjava.dbasync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.davidmoten.rx.jdbc.Database;

import rx.Single;
import ua.com.myjava.dbasync.domain.Derivative;

@Configuration
@RestController
public class RxJavaJdbcWrapperController {
	@Autowired
	private RandomIsinGenerator randomIsinGenerator;

	@Bean
	public Database database() {
		return Database.builder().url("jdbc:postgresql://localhost:5432/postgres")
				.username("postgres")
				.password("findata")
				.pool(10, 10).build();
	}

	@RequestMapping(path = "/derivative-rxjava-jdbc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Single<Derivative> findDerivativeRxJavaJdbc() {
		return database().select("select derivative_isin, product_name from derivatives where derivative_isin = ?")
				.parameter(randomIsinGenerator.getRandomIsin())
				.get(resultSet -> new Derivative(resultSet.getString("derivative_isin"), resultSet.getString("product_name")))
				.toSingle();
	}

}
