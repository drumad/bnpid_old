package org.bnp.id.config;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@AllArgsConstructor
@Log4j
public class DBConfig {

    private static Connection connection;

    private String host;

    private String port;

    private String db;

    private String username;

    private String password;

    public Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {
            createConnection();
        }
        return connection;
    }

    private void createConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = String
                .format("jdbc:mysql://%s:%s/%s?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", host,
                    port, db);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
    }
}
