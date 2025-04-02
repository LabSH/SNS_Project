package induk.sns_study.controller;


import induk.sns_study.dto.BoardDTO;
import induk.sns_study.dto.ChatMessageDTO;
import induk.sns_study.dto.MemberDTO;
import induk.sns_study.service.MemberService;
import induk.sns_study.service.MessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MemberService memberService;
    private final MessageService messageService;

    @GetMapping("chat")
    public String index(Model model, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");

        // 세션으로 자신의 정보를 가져와 모델 선언
        MemberDTO memberDTO = memberService.findById(memberId);
        model.addAttribute("member", memberDTO);

        // 세션 데이터를 이용해 해당 멤버 제외하고 다른 멤버 데이터 조회
        List<MemberDTO> memberDTOList = memberService.getOtherMembers(memberId);
        model.addAttribute("memberList", memberDTOList);


        Long number = memberDTOList.stream().count() + 1;
        model.addAttribute("number", number);

        return "chat/index";
    }


    @GetMapping("chat/search")
    public String searchBoard(@RequestParam(name = "name", required = false) String name, Model model, HttpSession session) {

        System.out.println(name);

        // 나 자신의 데이터
        Long memberId = (Long) session.getAttribute("memberId");
        MemberDTO memberDTO = memberService.findById(memberId);
        model.addAttribute("member", memberDTO);

        // 검색한 데이터
        List<MemberDTO> memberDTOList = memberService.searchOtherMembers(name, memberId);
        model.addAttribute("memberList", memberDTOList);

        Long number = memberDTOList.stream().count() + 1;
        model.addAttribute("number", number);

        return "chat/index";
    }


//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/chat")
//    public void sendMessage(@Payload ChatMessageDTO message) {
//        String senderId = message.getSenderId();
//        String targetId = message.getTargetId();
//
//        System.out.println("보낸 사람: " + senderId);
//        System.out.println("대상 사람: " + targetId);
//        System.out.println("내용: " + message.getContent());
//
//        // 보낸 사람에게도 메시지 전송 (자기 자신에게도 보내는 로직)
//        messagingTemplate.convertAndSendToUser(senderId, "/queue/messages", message);  // /user/{senderId}/queue/messages
//        messagingTemplate.convertAndSendToUser(targetId, "/queue/messages", message);  // /user/{targetId}/queue/messages
//    }

    @GetMapping("/popup")
    public String openChatPopup(@RequestParam("senderId") String sendId,
                                @RequestParam("targetId") String targetId,
                                Model model) {
        model.addAttribute("senderId", sendId);
        model.addAttribute("targetId", targetId);
        return "chat/chating";
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;


    //@DestinationVariable
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage) {
        System.out.println("📨 메시지 전송됨: " + chatMessage);
        System.out.println("타겟 사용자 ID: " + chatMessage.getTargetId());
        System.out.println("보낸 사람 ID: " + chatMessage.getSenderId());

        // 대상 유저에게 메시지 전송
        System.out.println("타겟 " + chatMessage.getTargetId() + "에게 메시지 전송 시도");
        messagingTemplate.convertAndSendToUser(
                chatMessage.getTargetId(), "/queue/messages", chatMessage);

        // 보낸 사람에게 메시지 전송
        System.out.println("보낸 사람 " + chatMessage.getSenderId() + "에게 메시지 전송 시도");
        messagingTemplate.convertAndSendToUser(
                chatMessage.getSenderId(), "/queue/messages", chatMessage);
    }
}
