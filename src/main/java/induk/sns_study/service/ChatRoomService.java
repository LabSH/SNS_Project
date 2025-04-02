package induk.sns_study.service;

import induk.sns_study.dto.MemberDTO;
import induk.sns_study.entity.ChatRoom;
import induk.sns_study.entity.MemberEntity;
import induk.sns_study.repository.ChatRoomRepository;
import induk.sns_study.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatRoom createOrGetChatRoom(Long user1Id, Long user2Id) {
        MemberEntity user1 = memberRepository.findById(user1Id).orElseThrow();
        MemberEntity user2 = memberRepository.findById(user2Id).orElseThrow();

        return chatRoomRepository.findByUser1AndUser2(user1, user2)
                .orElseGet(() -> {
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setUser1(user1);
                    chatRoom.setUser2(user2);
                    return chatRoomRepository.save(chatRoom);
                });
    }
}

