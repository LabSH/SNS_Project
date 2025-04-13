package induk.sns_study.service;

import induk.sns_study.dto.BoardDTO;
import induk.sns_study.dto.ChatRoomDTO;
import induk.sns_study.entity.BoardEntity;
import induk.sns_study.entity.ChatRoomEntity;
import induk.sns_study.entity.MemberEntity;
import induk.sns_study.repository.ChatRoomRepository;
import induk.sns_study.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Long findByRoomId(String user1, String user2) {
        System.out.println("findByRoomId 메서드 작동");
        System.out.println("findByRoomId: " + user1 + " and " + user2);
        Long chatRoomId = chatRoomRepository.findRoomByUser(user1,user2);
        if(chatRoomId != null){
            return chatRoomId;
        }else{
            return null;
        }

    }

    public void save(ChatRoomDTO chatRoomDTO){
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.toChatRoomEntity(chatRoomDTO);
        chatRoomRepository.save(chatRoomEntity);
    }
}

