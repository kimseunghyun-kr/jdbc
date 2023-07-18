package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UncheckedAppTest {

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    private static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    private static class Repository {

        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }


    private static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("failed to connect");
        }
    }

    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(controller::request)
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex", e);
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException () {

        }

        public RuntimeSQLException(Throwable cause) {super(cause);}
    }
}
