package induk.sns_study.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRoomDTO {
    private Long id;
    private String user1;
    private String user2;
}
