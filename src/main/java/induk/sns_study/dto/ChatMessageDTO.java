package induk.sns_study.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessageDTO {
    private String targetId;
    private String senderId;
    private String content;
}
