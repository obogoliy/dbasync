package ua.com.myjava.dbasync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.asyncsql.AsyncSQLClient;
import io.vertx.rxjava.ext.asyncsql.PostgreSQLClient;
import rx.Single;
import ua.com.myjava.dbasync.domain.Derivative;

@Configuration
@RestController
public class VertxController {
	@Autowired
	private RandomIsinService randomIsinService;

	@Bean
	public AsyncSQLClient client() {
		Vertx vertx = Vertx.vertx();
		JsonObject config = new JsonObject().put("host", "localhost")
				.put("port", 5432)
				.put("username", "postgres")
				.put("password", "findata")
				.put("database", "postgres")
				.put("maxPoolSize", 10);
		return PostgreSQLClient.createShared(vertx, config);
	}

	@RequestMapping(path = "/derivative-vertx-async", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Single<Derivative> getRandomDerivative() {
		return client().rxGetConnection().flatMap(conn -> {
			Single<Derivative> resa =
					conn.rxQueryWithParams("select derivative_isin, product_name from DERIVATIVE_MASTER_DATA where derivative_isin = ?",
							new JsonArray().add(randomIsinService.getRandomIsin()))
							.map(result -> new Derivative(result.getRows().get(0).getString("derivative_isin"),
									result.getRows().get(0).getString("product_name")));
			return resa.doAfterTerminate(conn::close);
		});
	}
}
