package com.shop.Controller;


import com.shop.constant.ViewModel;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MailService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    String epw="";

    @GetMapping(value = "/new") // /member/new get일경우
    public String memberForm(Model model){
        model.addAttribute("memberFormDto",new MemberFormDto());
        model.addAttribute(ViewModel.USER_ID, ViewModel.getUserName());
        return "member/memberForm";
    }


    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) { //유효성 검사 실패시 실행
            return "member/memberForm";
        }
        try{
            Member member = Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){ // 유효성 검사는 OK 똑같은 이메일이 벌써 가입이 되어 있다.
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        model.addAttribute(ViewModel.USER_ID, ViewModel.getUserName());
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/{mail}/emailConfirm")
    public @ResponseBody ResponseEntity emailConfirm(@PathVariable("mail") String mail) throws Exception{
        System.out.println("이메일 : "+mail);
        epw=mailService.sendSimpleMessage(mail);
        return new ResponseEntity<String> ("인증 메일을 보냈습니다.", HttpStatus.OK);
    }

    @GetMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity codeConfirm(@PathVariable("code") String code) throws Exception{
        if(code.equals(epw)){
            return new ResponseEntity<String> ("인증 성공하였습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<String> ("인증 코드를 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST);
    }



}
