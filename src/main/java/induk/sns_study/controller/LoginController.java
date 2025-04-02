package induk.sns_study.controller;


import induk.sns_study.dto.MemberDTO;
import induk.sns_study.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("login")
    public String login() {
        return "login/index";
    }

    @PostMapping("logined") // 로그인 기능
    public String logined(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        System.out.println("logined 함수 호출");
        MemberDTO loginResult = memberService.login(memberDTO);
        System.out.println("loginResult" + loginResult);
        if(loginResult != null){
            String email = loginResult.getEmail();
            Long memberId = loginResult.getId();
            String name = loginResult.getName();

            int idx = email.indexOf("@");
            session.setAttribute("logined", email.substring(0,idx)); // @제외하고 이메일 출력하기 위한 용도

            session.setAttribute("name", name); // 멤버 이름 출력 용도
            session.setAttribute("email", email); // 멤버 이메일 출력 용도
            session.setAttribute("memberId", memberId); // post할때 쓰일 Memeber_id 설정

            System.out.println("로그인 성공");
            return "redirect:/";
            // 로그인 성공
        }else{
            // 로그인 실패
            System.out.println("로그인 실패");
            return "redirect:/";
        }
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
