package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_3 {

    private final MemberRepositoryV3 memberRepository;

// MemberServiceV3_3$$SpringCGLIB$$0 -> Spring creates a class that inherits this class
// and creates a proxy class via overriding some code internally thru cglib
// to be further mentioned in SPRING AOP
    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {

        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney()- money);
        validation(toMember);
        memberRepository.update(toId, fromMember.getMoney()+money);
    }

    public void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("error during transaction");
        }
    }
}
