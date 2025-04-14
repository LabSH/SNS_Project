package induk.sns_study.dto;


import induk.sns_study.entity.MemberEntity;
import induk.sns_study.entity.MessageEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessageDTO {
    private Long chatRoomId;
    private String targetId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;

    public static ChatMessageDTO toChatMessageDTO(MessageEntity messageEntity) {
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setChatRoomId(messageEntity.getChatRoomId());
        chatMessageDTO.setSenderId(messageEntity.getSenderId());
        chatMessageDTO.setContent(messageEntity.getContent());
        chatMessageDTO.setTimestamp(messageEntity.getTimestamp());
        return chatMessageDTO;
    }
}
