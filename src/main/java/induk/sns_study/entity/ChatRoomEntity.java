package induk.sns_study.entity;


import induk.sns_study.dto.ChatRoomDTO;
import induk.sns_study.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String user1;

    private String user2;

    public static ChatRoomEntity toChatRoomEntity(ChatRoomDTO ChatDTO) {
        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.setUser1(ChatDTO.getUser1());
        chatRoomEntity.setUser2(ChatDTO.getUser2());
        return chatRoomEntity;
    }
}
