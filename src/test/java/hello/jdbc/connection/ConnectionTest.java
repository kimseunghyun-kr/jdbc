package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {
    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection = {}, class = {}", con1 , con1.getClass());
        log.info("connection = {}, class = {}", con2 , con2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        //DriverManagerDataSource -> always gets a new Connection
        DataSource dataSource = new  DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        //connection pooling
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10); //already 10 by default
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        Thread.sleep(1000); //wait for connection creation time @ Connection pool

    }

    private void useDataSource(DataSource dataSource) throws SQLException { //fjp?
        Connection con1 = dataSource.getConnection(); //if poolsize == 1
        Connection con2 = dataSource.getConnection(); //wait till pool is available
        log.info("connection = {} , class = {}", con1 , con1.getClass());
        log.info("connection = {} , class = {}", con2 , con2.getClass());
    }
}
