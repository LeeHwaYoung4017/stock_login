package com.example.stock.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String Home(HttpSession session, HttpServletRequest request){
        // 세션에서 로그인 정보 가져오기
        String username = (String) session.getAttribute("username");

        //로그인 정보가 없을 경우 로그인 페이지로 이동
        if (username == null) {
            return "redirect:/login";
        }

        //쿠키 가져오기
        Cookie[] cookies = request.getCookies();
        String username1 = null;

        //쿠키에서 로그인 정보 찾기
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username1 = cookie.getValue();
                    break;
                }
            }
        }

        //로그인 정보가 없을 경우 로그인 페이지로 이동
        if (username1 == null) {
            return "redirect:/login";
        }

        return "home";
    }

}
