package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @AfterEach
    void afterEach() throws SQLException {

    }
    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberV1", 10000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember = {} ", findMember);
        log.info("findMember == Member {}", findMember == member);
        log.info("member equals findMember {} ", member.equals(findMember));
        assertThat(findMember).isEqualTo(member);

        //update money 10000 -> 2000
        repository.update(member.getMemberId(), 2000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(2000);

        //delete
        repository.delete(member.getMemberId());

    }
}