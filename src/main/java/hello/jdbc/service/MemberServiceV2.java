package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {


        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);
            bizLogic(con, fromId, toId, money);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }
    }

    private void release(Connection con) {
        if(con != null) {
            try{
                con.setAutoCommit(true); //to account for connection pool where connection reused
                con.close();
            } catch (Exception e) {
                log.info("error occured", e);
            }
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {

        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(con, fromId, fromMember.getMoney()- money);
        validation(toMember);
        memberRepository.update(con ,toId, fromMember.getMoney()+money);
    }

    //    just to test validation for exception
    public void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("error during transaction");
        }
    }
}
