package com.example.stock.service;


import com.example.stock.entity.Member;
import com.example.stock.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    
    //생성자 주입방식 (필드 주입방식[@Autowired]는 비권장
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberByUsername(String username) {
        return  memberRepository.findByUsername(username);
    }


    //아이디,패스워드가 유효한지 체크
    public boolean isValidMember(String username, String password){

        Member member = memberRepository.findByUsername(username);

        if(member != null && member.getPassword().equals(password)){
            return true;
        }

        return false;

    }


    //회원가입
    public void registerMember(Member member){
        memberRepository.save(member);
    }


    //아이디 중복 체크
    public boolean isUsernameCheck(String username) {
        return !memberRepository.existsByUsername(username);
    }

    //아이디 찾기
    public String findByUsernameAndEmail(String name, String email) {
        Member member = memberRepository.findByUsernameAndEmail(name, email);
        if(member != null) {
            return member.getUsername();
        }
        return null;
    }

    //패스워드 찾기
    public boolean userNameAndUsernameCheck(String username, String name) {
        Member member = memberRepository.userNameAndUsernameCheck(username, name);
        return member != null;
    }

    //패스워드 변경
    public void resetPassword(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if(member != null) {
            member.setPassword(password);
            memberRepository.save(member);
        }
    }


}
