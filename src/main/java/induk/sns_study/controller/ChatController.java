package induk.sns_study.controller;


import induk.sns_study.dto.BoardDTO;
import induk.sns_study.dto.ChatMessageDTO;
import induk.sns_study.dto.ChatRoomDTO;
import induk.sns_study.dto.MemberDTO;
import induk.sns_study.service.ChatRoomService;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MemberService memberService;
    private final ChatRoomService chatRoomService;
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

    @GetMapping("/popup")
    public String openChatPopup(@RequestParam("senderId") String sendId,
                                @RequestParam("targetId") String targetId,
                                @RequestParam("targetName") String targetName,
                                Model model) {
        model.addAttribute("senderId", sendId);
        model.addAttribute("targetId", targetId);
        model.addAttribute("targetName", targetName);

        // 메세지 조회를 위한 두 유저간의 채팅방
        Long ChatRoomId = chatRoomService.findByRoomId(sendId, targetId);
        // 채팅방 번호를 알아냈으니 그 메세지 엔터티에 채팅방 아이디를 매칭시켜 메세지에 대한 내용을 구함 구하고
        List<ChatMessageDTO> messageList = messageService.findByMessage(ChatRoomId);
        System.out.println(messageList);
        model.addAttribute("messageList", messageList);

        return "chat/chating";
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    /**
     * /app/chat.sendMessage 로 들어온 메시지를 처리
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage, Principal principal) {
        String senderId = principal.getName(); // 현재 로그인한 사용자 ID (ChatHandshakeHandler에서 설정됨)
        chatMessage.setSenderId(senderId);

        System.out.println("📨 메시지 전송됨: " + chatMessage);
        System.out.println("타겟 사용자 ID: " + chatMessage.getTargetId());
        System.out.println("보낸 사람 ID: " + chatMessage.getSenderId());

        // 대상 유저에게 메시지 전송
        System.out.println("타겟 " + chatMessage.getTargetId() + "에게 메시지 전송 시도");
        messagingTemplate.convertAndSendToUser(
                chatMessage.getTargetId(), "/queue/messages", chatMessage);

        // 보낸 사람에게도 메시지를 전송 (본인 채팅창 갱신용 등)
        System.out.println("보낸 사람 " + chatMessage.getSenderId() + "에게 메시지 전송 시도");
        messagingTemplate.convertAndSendToUser(
                chatMessage.getSenderId(), "/queue/messages", chatMessage);

        // 채팅 저장 서비스 로직
        // 이게 저장로직인데 이미 방이있을 경우에는 이 방법을 선택하면 안됨. 이럴경우 계속 방생성함
        // 그러므로 룸을 찾는 로직도 구현해보겠음

        // 룸 아이디 찾는 기능


        Long ChatRoomId = chatRoomService.findByRoomId(chatMessage.getSenderId(), chatMessage.getTargetId());

        System.out.println("기존 채팅방 ID: "+ChatRoomId);


        if (ChatRoomId != null) { // 채팅방이 존재 할겅우
            // 룸 아이디를 가지고 메세지 엔티티에 메세지를 저장

            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
            chatMessageDTO.setChatRoomId(ChatRoomId);
            chatMessageDTO.setSenderId(chatMessage.getSenderId());
            chatMessageDTO.setContent(chatMessage.getContent());
            messageService.save(chatMessageDTO);

        }else{ // 채팅방이 없을경우
            // 처음부터 방을 만들고 채팅을 저장
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setUser1(chatMessage.getSenderId());
            chatRoomDTO.setUser2(chatMessage.getTargetId());
            chatRoomService.save(chatRoomDTO);

            // 저장 완료
            System.out.println("채팅방 DB저장 완료");

            Long secondChatRoomId = chatRoomService.findByRoomId(chatMessage.getSenderId(), chatMessage.getTargetId());
            System.out.println("새로만든 채팅방 ID: "+secondChatRoomId);

            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
            chatMessageDTO.setChatRoomId(secondChatRoomId);
            chatMessageDTO.setSenderId(chatMessage.getSenderId());
            chatMessageDTO.setContent(chatMessage.getContent());
            messageService.save(chatMessageDTO);
        }
    }
}
