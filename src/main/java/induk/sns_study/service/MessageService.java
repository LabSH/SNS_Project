package induk.sns_study.service;

import induk.sns_study.entity.ChatRoom;
import induk.sns_study.entity.MemberEntity;
import induk.sns_study.entity.Message;
import induk.sns_study.repository.ChatRoomRepository;
import induk.sns_study.repository.MemberRepository;
import induk.sns_study.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public Message sendMessage(Long chatRoomId, Long senderId, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        MemberEntity sender = memberRepository.findById(senderId).orElseThrow();

        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getMessages(Long chatRoomId) {
        return messageRepository.findByChatRoomIdOrderByTimestamp(chatRoomId);
    }
}

