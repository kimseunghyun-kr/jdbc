package hello.jdbc.repository;
import hello.jdbc.domain.Member;
import java.sql.SQLException;
public interface MemberRepositoryEx {

    //checked exception still bogs down the usage of a interface to facilitate DI
//    this is evident from the throws SQLException , which is a JDBC Exception, in an interface
//    originally meant to facilitate DI for various repositories using different data access methods(JDBC / JPA)

//    should a class that depends on this interface to use a specific impl of this interface,
//    which in this case is JDBC
    Member save(Member member) throws SQLException;
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}