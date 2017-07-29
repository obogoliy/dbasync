package ua.com.myjava.dbasync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ua.com.myjava.dbasync.domain.Derivative;

@Configuration
@RestController
public class SyncController {
	@Autowired
	private RandomIsinService randomIsinService;

	@Bean(destroyMethod = "close")
	public BasicDataSource basicDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
		dataSource.setUsername("postgres");
		dataSource.setPassword("findata");
		dataSource.setInitialSize(10);
		dataSource.setMaxActive(10);
		return dataSource;
	}

	@RequestMapping(path = "/derivative-sync", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Derivative findDerivativeSync() throws SQLException {
		try (Connection connection = basicDataSource().getConnection();
				PreparedStatement preparedStatement =
						connection.prepareStatement(
								"select derivative_isin, product_name from DERIVATIVE_MASTER_DATA where derivative_isin like ?")) {
			preparedStatement.setString(1, randomIsinService.getRandomIsin());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return new Derivative(resultSet.getString("derivative_isin"), resultSet.getString("product_name"));
			} else {
				return null;
			}
		}
	}
}
