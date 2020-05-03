package org.bnp.id.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@PropertySource(value = "classpath:conf/db.properties")
public class DBConfig {

    @Value("${db.host}")
    private String host;

    @Value("${db.port}")
    private String port;

    @Value("${db.name}")
    private String db;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    private String url = String.format("jdbc:mysql://%s:%s/%s", host, port, db);

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url, username, password);
    }
}
