package jpa.project.controller;

import jpa.project.dto.member.MemberDto;
import jpa.project.service.MemberService;
import jpa.project.dto.member.MemberRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String index(){
        return "index";
    }


    @GetMapping("/user/signup")
    public String getSignup(Model model){
        model.addAttribute("userForm",new MemberDto());
        return "signup";
    }


    @PostMapping("/user/signup")
    public String postSignup(@Valid MemberRegisterRequestDto mrrDto , BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("userForm",mrrDto);


            return "signup";

        }

        memberService.join(mrrDto);
        return "redirect:/user/login";
    }
    @GetMapping("/user/login")
    public String getLogin(){
        return "login";
    }
    //로그인 결과페이지
    @GetMapping("/user/login/result")
    public String getLoginResult(){
        return "loginSuccess";
    }
    @GetMapping("/user/logout/result")
    public String getLogout(){
        return "logout";
    }
    //접근거부페이지
    @GetMapping("user/denied")
    public String getDenide(){
        return "denied";
    }
    //내정보
    @GetMapping("/user/myinfo")
    public String getMyInfo(){
        return "myinfo";
    }
    @GetMapping("/admin")
    public String getAdmin(){
        return "admin";
    }

}
