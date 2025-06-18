package induk.sns_study.controller;

import induk.sns_study.dto.MemberDTO;
import induk.sns_study.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    //생성자 주입
    private final MemberService memberService;

    // [404] 페이지 이동
    @GetMapping("admin_member/404")
    public String error404() {
        return "admin/admin_404";
    }

    // [Blank] 페이지 이동
    @GetMapping("admin_member/blank")
    public String blank() {
        return "admin/admin_blank";
    }

    // [인덱스] 전체 리스트 출력
    @GetMapping("admin_member")
    public String index(Model model) {
        // 어떠한 데이터를 html파일로 가져가려면 Model사용
        List<MemberDTO> memberDTOList = memberService.findAll();
        Long number = memberService.count();
        model.addAttribute("memberList", memberDTOList);
        model.addAttribute("count", number);
        return "admin_member/index";
    }

    // [생성] 회원가입 페이지 이동
    @GetMapping("admin_member/create")
    public String create() {
        return "admin_member/create";
    }

    // [저장] 받아온 데이터를 저장
    @PostMapping("admin_member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        System.out.println("memberDTO" + memberDTO);
        memberService.save(memberDTO);
        return "redirect:/";
        // 이게 index()함수의 호출을 하게 해줌으로서 index로 이동
    }

    // 상세보기
    @GetMapping("admin_member/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "admin_member/show";
    }

    // 1. 특정 ID 회원 update [ID와 동일한 회원의 정보를 가져온다.]
    @GetMapping("admin_member/update/{id}/{state}")
    public String updateForm(@PathVariable("id") Long id, @PathVariable("state") Long state, Model model) {
        MemberDTO memberDTO = memberService.updateForm(id);
        model.addAttribute("updateMember", memberDTO);
        return "admin_member/update";
    }

    // 2. 특정 ID 회원 update
    @PostMapping("admin_member/update")
    public String update(@ModelAttribute MemberDTO memberDTO, @RequestParam("state") Long state) {
        memberService.update(memberDTO);

        if(state == 0){
            System.out.println(state);
            return "redirect:/admin_member";
        }else{
            System.out.println(state);
            return "redirect:/";
        }
    }

    @GetMapping("admin_member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/admin_member";
    }

}
