package jpa.project.repository.member;

import jpa.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {



    Optional<Member> findByUsername(@Param("username") String username);



    Optional<Member>findByUsernameAndProvider(String username,String provider);
}
