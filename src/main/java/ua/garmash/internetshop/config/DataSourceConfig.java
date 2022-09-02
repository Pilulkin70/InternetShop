package ua.garmash.internetshop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public DataSource postgresDataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        String dbUrl = "jdbc:postgresql://localhost:5432/internetshop";
        String username = "postgres";
        String password = "root";

        if (databaseUrl != null) {
            logger.info("Initializing PostgreSQL database: {}", databaseUrl);
            try {
                URI dbUri = new URI(databaseUrl);
                dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +
                        "?sslmode=require";
                username = dbUri.getUserInfo().split(":")[0];
                password = dbUri.getUserInfo().split(":")[1];
            } catch (URISyntaxException | NullPointerException e) {
                logger.error(String.format("Invalid DATABASE_URL: %s", databaseUrl), e);
                return null;
            }
        } else {
            logger.info("Initializing PostgreSQL database: {}", dbUrl);
        }

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }
}
