package induk.sns_study.entity;

import induk.sns_study.dto.BoardDTO;
import induk.sns_study.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


// 테이블 이름
@Getter
@Setter
@Table(name = "board")
@Entity // 고정
public class BoardEntity {

    // 고유 번호 id 속성에 적용하는 어노테이션으로 id 속성을 기본 키로 지정한다.
    @Id
    // 속성에 값을 따로 지정하지 않아도 1씩 자동으로 증가하게 된다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // 컬럼
    @Column // 게시판 ID
    private Long id;

    // 작성자 ID
    @Column(nullable = false)
    private Long memberId;

    // 제목
    @Column(nullable = false, length = 100)
    private String title;

    // 내용
    @Column(nullable = false, length = 1000)
    private String content;

    // 등록날짜
    @Column
    @CreationTimestamp
    private LocalDateTime createdDate;

    // 마지막 수정날짜
    @Column
    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    public static BoardEntity toBoardEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setMemberId(boardDTO.getMemberId());
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());
        return boardEntity;
    }

    public static BoardEntity toUpdateBoardEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setMemberId(boardDTO.getMemberId());
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());
        boardEntity.setCreatedDate(boardDTO.getCreatedDate());
        return boardEntity;
    }
}
