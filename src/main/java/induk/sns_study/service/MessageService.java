package induk.sns_study.service;

import induk.sns_study.dto.ChatMessageDTO;
import induk.sns_study.dto.ChatRoomDTO;
import induk.sns_study.dto.MemberDTO;
import induk.sns_study.entity.ChatRoomEntity;
import induk.sns_study.entity.MemberEntity;
import induk.sns_study.entity.MessageEntity;
import induk.sns_study.repository.ChatRoomRepository;
import induk.sns_study.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

//    public List<ChatMessageDTO> findMessageListByRoomId(Long roomId) {
//
//    }

    public void save(ChatMessageDTO chatMessageDTO){
        MessageEntity messageEntity = MessageEntity.toMessageEntity(chatMessageDTO);
        messageRepository.save(messageEntity);
    }

    public List<ChatMessageDTO> findByMessage(Long id){
        List<MessageEntity> messageEntityList = messageRepository.findByMessage(id);
        List<ChatMessageDTO> ChatMessageDTOList = new ArrayList<>();
        for(MessageEntity messageEntity : messageEntityList){
            ChatMessageDTOList.add(ChatMessageDTO.toChatMessageDTO(messageEntity));
        }
        return ChatMessageDTOList;
    }
}

