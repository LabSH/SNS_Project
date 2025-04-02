package induk.sns_study.controller;

import induk.sns_study.dto.BoardDTO;
import induk.sns_study.dto.MemberDTO;
import induk.sns_study.service.BoardService;
import induk.sns_study.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;
    private final MemberService memberService;

    // 4개의 데이터만 가져오는 메인 화면
    @GetMapping("/")
    public String index(Model model) {

        // 게시판의 데이터 4개 가져옴
        int f_count = 4; // 가져올 게시판의 개수
        List<BoardDTO> Featured_List = boardService.findAll(f_count);
        System.out.println(Featured_List);

        // 가져온 데이터 4개의 작성자들의 내용을 가져오는 로직
        List<BoardDTO> F_DTOList = new ArrayList<>();
        for(BoardDTO boardDTO : Featured_List){
            boardDTO.setMemberName(memberService.findByName(boardDTO.getMemberId()));
            F_DTOList.add(boardDTO);
        }
        System.out.println(F_DTOList);
        model.addAttribute("Featured", F_DTOList);

        // 게시판 데이터 6개 가져옴
        // 이부분은 랜덤이 아님
        int a_count = 6; // 가져올 게시판의 개수
        List<BoardDTO> AllStorise_List = boardService.findAll_noRand(a_count);
        System.out.println(AllStorise_List);

        // 가져온 데이터 4개의 작성자들의 내용을 가져오는 로직
        List<BoardDTO> A_DTOList = new ArrayList<>();
        for(BoardDTO boardDTO : AllStorise_List){
            boardDTO.setMemberName(memberService.findByName(boardDTO.getMemberId()));
            A_DTOList.add(boardDTO);
        }
        System.out.println(A_DTOList);
        model.addAttribute("AllStorise", A_DTOList);

        return "board/index";
    }

    @GetMapping("board/search")
    public String searchBoard(@RequestParam(name = "title", required = false) String title, Model model) {

        System.out.println(title);

        // 검색어와 연관된 게시판 데이터
        // 여기는 값이 null일 가능성있으니 데이터 검사를 한번 걸쳐줘야함
        List<BoardDTO> search_List = boardService.findAll_search(title);
        System.out.println(search_List);

        if(search_List.isEmpty()) {
            model.addAttribute("Featured", null);
        }else{
            List<BoardDTO> search_DTOList = new ArrayList<>();
            for (BoardDTO boardDTO : search_List) {
                boardDTO.setMemberName(memberService.findByName(boardDTO.getMemberId()));
                search_DTOList.add(boardDTO);
            }
            System.out.println(search_DTOList);
            model.addAttribute("Featured", search_DTOList);
        }
        // 게시판 데이터 6개 가져옴
        // 이부분은 랜덤이 아님
        int a_count = 6; // 가져올 게시판의 개수
        List<BoardDTO> AllStorise_List = boardService.findAll_noRand(a_count);
        System.out.println(AllStorise_List);

        // 가져온 데이터 4개의 작성자들의 내용을 가져오는 로직
        List<BoardDTO> A_DTOList = new ArrayList<>();
        for(BoardDTO boardDTO : AllStorise_List){
            boardDTO.setMemberName(memberService.findByName(boardDTO.getMemberId()));
            A_DTOList.add(boardDTO);
        }
        System.out.println(A_DTOList);
        model.addAttribute("AllStorise", A_DTOList);


        return "board/index";
    }

    @GetMapping("admin")
    public String admin() {
        return "admin_index";
    }
}
