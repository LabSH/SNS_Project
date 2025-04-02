package induk.sns_study.dto;



import induk.sns_study.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDTO {

    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private String memberName;
    // 여기가 저장소라고만 생각하고
    // memberId로 정보를 가져와서 그 중 이름만 여기다 저장.

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setMemberId(boardEntity.getMemberId());
        boardDTO.setTitle(boardEntity.getTitle());
        boardDTO.setContent(boardEntity.getContent());
        boardDTO.setCreatedDate(boardEntity.getCreatedDate());
        boardDTO.setModifiedDate(boardEntity.getModifiedDate());
        return boardDTO;
    }
}
