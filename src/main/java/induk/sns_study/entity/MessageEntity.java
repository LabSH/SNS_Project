package induk.sns_study.entity;

import induk.sns_study.dto.ChatMessageDTO;
import induk.sns_study.dto.ChatRoomDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatRoomId;

    private String senderId;

    private String content;

    private LocalDateTime timestamp;

    public static MessageEntity toMessageEntity(ChatMessageDTO chatMessageDTO) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChatRoomId(chatMessageDTO.getChatRoomId());
        messageEntity.setSenderId(chatMessageDTO.getSenderId());
        messageEntity.setContent(chatMessageDTO.getContent());
        messageEntity.setTimestamp(LocalDateTime.now());
        return messageEntity;
    }
}
