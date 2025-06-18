package induk.sns_study.controller;

import induk.sns_study.dto.BoardDTO;
import induk.sns_study.dto.MemberDTO;
import induk.sns_study.service.BoardService;
import induk.sns_study.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping("board")
    public String index() {
        return "board/index";
    }

    // 자신의 게시물을 전체보기 (본인 계정 탐색)
    @GetMapping("board/myBoard")
    public String myBoard(Model model, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        System.out.println(memberId);

        MemberDTO memberDTO = memberService.findById(memberId);
        String name = memberDTO.getName();

        List<BoardDTO> BoardDTO = boardService.findByMemberId(memberId);
        model.addAttribute("boardList", BoardDTO);
        model.addAttribute("name", name);


        System.out.println(BoardDTO);
        return "board/author";
    }

    // 특정 회원의 게시물을 전체보기 (다른 계정 탐색)
    @GetMapping("board/youBoard/{id}")
    public String youBoard(@PathVariable("id") Long id, Model model) {
        List<BoardDTO> BoardDTO = boardService.findByMemberId(id);
        MemberDTO memberDTO = memberService.findById(BoardDTO.get(0).getMemberId());
        String name = memberDTO.getName();
        model.addAttribute("boardList", BoardDTO);
        model.addAttribute("name", name);
        System.out.println(BoardDTO);
        return "board/author";
    }


    // 1개의 게시판을 상세보기
    @GetMapping("board/show/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        MemberDTO memberDTO = memberService.findById(boardDTO.getMemberId());
        System.out.println(boardDTO);
        System.out.println(memberDTO);
        model.addAttribute("membername", memberDTO.getName());
        model.addAttribute("memberId", memberDTO.getId());
        model.addAttribute("board", boardDTO);

        // 게시판의 데이터 3개 가져옴
        // 근데 내가 보고있는 게시판외의 데이터를 가져와야한다.
        int count = 3; // 가져올 게시판의 개수
        List<BoardDTO> Board_List = boardService.noMy_findAll(count, id);
        System.out.println(Board_List);

        // 가져온 데이터 4개의 작성자들의 내용을 가져오는 로직
        List<BoardDTO> Under_List = new ArrayList<>();
        for(BoardDTO list : Board_List){
            list.setMemberName(memberService.findByName(list.getMemberId()));
            Under_List.add(list);
        }
        System.out.println(Under_List);
        model.addAttribute("Under_List", Under_List);

        return "board/show";
    }

    @GetMapping("board/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        BoardDTO boardDTO = boardService.updateForm(id);
        System.out.println(boardDTO);
        model.addAttribute("updateBoard", boardDTO);
        return "board/update";
    }

    @PostMapping("board/update")
    public String update(@ModelAttribute BoardDTO boardDTO) {
        System.out.println(boardDTO);
        boardService.update(boardDTO);
        return "redirect:/";
    }

    @GetMapping("board/create")
    public String create(Model model) {
        int count = 3;
        List<BoardDTO> Under_List = boardService.findAll(count);
        System.out.println(Under_List);
        model.addAttribute("Under_List", Under_List);
        return "board/create";
    }

    @PostMapping("board/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        System.out.println("boardDTO" + boardDTO);
        boardService.save(boardDTO);
        return "redirect:/";
    }

    @GetMapping("board/delete/{id}")
    public String deleteById(@PathVariable("id") Long id, Model model) {
        boardService.deleteById(id);
        return "redirect:/board/myBoard";
    }

}
