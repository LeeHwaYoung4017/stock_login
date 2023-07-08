package com.example.stock.repository;

import com.example.stock.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


//JpaRepository<> 상속받는 인터페이스로 데이터를 저장하고 조회하기 위한 데이터 액세스 객체
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //아이디에 해당하는 회원을 조회하는 메소드
    Member findByUsername(String username);

    //중복 확인하는 메소드
    boolean existsByUsername(String username);

    //아이디 찾기
    @Query("SELECT m FROM Member m WHERE m.name = :name AND m.email = :email")
    Member findByUsernameAndEmail(@Param("name") String name,@Param("email") String email);

    //아이디 이름이 일치한 회원정보를 확인
    @Query("SELECT m FROM Member m WHERE m.name = :name AND m.username = :username")
    Member userNameAndUsernameCheck(@Param("name") String name, @Param("username") String username);

}
