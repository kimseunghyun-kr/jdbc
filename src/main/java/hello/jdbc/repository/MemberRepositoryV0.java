package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0 {
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (? ,?)";

        Connection con = null;
        PreparedStatement pstmt = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con,pstmt, null);
        }

    }


    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(2, memberId);
            pstmt.setInt(1, money);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);  //no of updated individuals
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con,pstmt, null);
        }

    }


    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            int deletedNo = pstmt.executeUpdate();
            log.info("deleted No = {}", deletedNo);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con,pstmt, null);
        }

    }


    public Member findById(String memberId) throws NoSuchElementException{
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        con = getConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if(rs.next()) { //rs initially points at nowhere(cursor/pointer). calling next() will move the pointer to the first available data,
//                checking if the data exists during this process;
//while not used as the test expects only one matching data to be present;

                //data exists
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
//                data doesnt exist
                throw new NoSuchElementException("member not found memberID = "+ memberId);
            }
        } catch (SQLException e) {
            log.info("error", e);
        } finally {
            close(con, pstmt , rs);
        }
        return null;
    }


    private void close(Connection con, Statement stmt, ResultSet rs) {

        if(stmt != null) {
            try {
                stmt.close();//Exception can occur here
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }


    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
