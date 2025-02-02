package com.app.domain.member.repository;

import com.app.domain.member.entity.Member;
import java.util.Optional;

import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.budget = :budget WHERE m.memberId = :memberId")
    int updateBudget(@Param("memberId") Long memberId, @Param("budget") int budget);

}
