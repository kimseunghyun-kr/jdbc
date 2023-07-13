package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() throws Exception {

//        here to show how DI works, with the reliance of the
//        DataSource interface(provides the standards on how to obtain a connection)
//        and not the impl. only minor changes
//        for the setURL ,PW , UNAME method needed;
        //base DriverManager - always get new connection
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        //connection pooling : HikariProxyConnection -> JdbcConnection
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberV1", 10000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        assertThat(findMember).isEqualTo(member);

        //update money 10000 -> 2000
        repository.update(member.getMemberId(), 2000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(2000);

        //delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        try {
            Thread.sleep(1000);
//            close() for a thread pool allows a Thread to return -> only conn0 thread is used for hikari threadPool;
//            allows the reuse of the same connection -> although the proxy object changes. HikariProxyConnection@xxxxxxxx change
//            exhibits this behaviour despite the connection being the same
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}