package com.example.stock.controller;

import com.example.stock.entity.Member;
import com.example.stock.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String showLoginForm(){
        return "login";
    }

    //로그인 처리
    //@RequestParam = html파일에서 값을 가져온다 = HttpServletRequest와 같은 역할
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model,
                        HttpServletResponse response,
                        HttpSession session){
        if(memberService.isValidMember(username, password)){
            //로그인 성공 시
            Member member = memberService.findMemberByUsername(username); // 로그인 아이디에 해당되는 회원정보를 저장
            System.out.println("Test Date = "+member);
            
            model.addAttribute("member", member); // 로그인 정보를 전달
            session.setAttribute("username", username); // 세션을 로그인 정보 저장

            Cookie cookie = new Cookie("username", username); // 쿠키 생성
            response.addCookie(cookie);

            return "home";
        } else {
            //로그인 실패 시
            model.addAttribute("error", "Invalid username or password");
            try {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter w = response.getWriter();
                w.write("<script>alert('아이디 및 패스워드가 일치하지 않습니다.')</script>");
                w.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "login";
        }

    }

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // 세션 삭제
        session.invalidate();

        //쿠키 삭제
        Cookie cookie = new Cookie("username", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/login";
    }


    @GetMapping("/register")
    public String showRegisterForm(){
        return "register";
    }

    //회원가입 처리를 진행하는 메소드
    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("address1") String address1,
                           @RequestParam("address2") String address2,
                           @RequestParam("address3") String address3,
                           @RequestParam("managerCheck") String managerCheck,
                           Model model){
        Member member = new Member(null, username, password, name, email, address1, address2, address3, managerCheck, new Date());
        memberService.registerMember(member);

        model.addAttribute("username", username);

        return "redirect:/login";
    }

    @GetMapping("/registerIdCheck")
    public String registerIdCheck(){
        return "registerIdCheck";
    }

    //아이디를 중복 체크하는 영역
    @GetMapping("/checkUsername")
    @ResponseBody
    public Map<String, Boolean> checkUsername(@RequestParam("username") String username){
        boolean available = memberService.isUsernameCheck(username);
        System.out.println(available);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return response;
    }

    // 아이디 찾는 페이지
    @GetMapping("/findId")
    public String findIdView(){
        return "findId";
    }

    // 패스워드 찾는 페이지
    @GetMapping("/findPassword")
    public String findPasswordView(){
        return "findPassword";
    }

    // 아이디 찾기 기능 처리
    @PostMapping("/findId")
    public String findId(@RequestParam String name,
                         @RequestParam String email,
                         Model model,
                         HttpServletResponse response){
        System.out.println(name + " / " +email);
        String username = memberService.findByUsernameAndEmail(name, email);
        System.out.println("Controller "+username);
        if (username != null){
            //아이디 정상 조회 시
            model.addAttribute("username", username);
            return "findIdResult";
        } else {
            //회원정보가 존재하지 않을 시
            try {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter w = response.getWriter();
                w.write("<script>alert('회원정보가 존재하지 않습니다.')</script>");
                w.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "findId";
        }

    }

    //패스워드 찾기 기능
    @PostMapping("/findPassword")
    public String findPassword(@RequestParam("username") String username,
                               @RequestParam("name") String name,
                               Model model,
                               HttpServletResponse response) {
        boolean match = memberService.userNameAndUsernameCheck(name, username);
        System.out.println(match + " / "+ username + " / " + name);
        if (match) {
            //return "redirect:/findPasswordReset?username="+username;
            model.addAttribute("username", username);
            return "findPasswordReset";
        } else {
            try {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter w = response.getWriter();
                w.write("<script>alert('회원정보가 존재하지 않습니다.')</script>");
                w.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.addAttribute("error", "일치하는 회원정보가 존재하지 않습니다.");
            return "findPassword";
        }
    }

    //비밀번호 재설정
    @GetMapping("findPasswordReset")
    public String findPasswordResetView(@RequestParam("username") String username,
                                    Model model){
        model.addAttribute("username", username);
        return "findPasswordReset";
    }

    @PostMapping("findPasswordReset")
    public String findPasswordReset(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam("passwordCheck") String passwordCheck,
                                    HttpServletResponse response
                                    ) {
        System.out.println("username="+username+" / password="+password+" / passwordCheck="+passwordCheck);
        if (password.equals(passwordCheck)) {
            memberService.resetPassword(username, password);
            return "findPasswordResetSuccess";
        } else {
            try {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter w = response.getWriter();
                w.write("<script>alert('패스워드가 일치하지 않습니다.')</script>");
                w.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //findPasswordReset.html로 갈수 있게 개선 필요 !
            return "findPassword";
        }

    }




}
