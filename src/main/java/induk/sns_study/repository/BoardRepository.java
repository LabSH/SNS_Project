package induk.sns_study.repository;

import induk.sns_study.entity.BoardEntity;
import induk.sns_study.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByMemberId(Long memberId);
    // (select * from board where memberId=?)

    // 네이티브 쿼리
    // 4개의 게시판만 가져오는 쿼리 -> board/index.html 상단
    @Query(value = "SELECT DISTINCT * FROM board ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    // DISTINCT함수를 사용해 중복되는 행은 가져오지않는다.
    // 그리고 랜덤함수로 무작위 정보를 가져오고 리미트 4개로 제한을 건다.
    List<BoardEntity> findLimitedBoards(@Param("limit") int limit);

    // board/index.html 하단
    @Query(value = "SELECT * FROM board ORDER BY id ASC LIMIT :limit", nativeQuery = true)
    List<BoardEntity> findLimitedBoards_noRand(@Param("limit") int limit);

    // 자신의 게시물을 제외한 3개의 게시판 랜덤으로 조회
    @Query(value = "SELECT DISTINCT * FROM board WHERE id != :myId ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<BoardEntity> find_NoMy_Boards(@Param("limit") int limit, @Param("myId") Long myId);

    // 검색(제목) 쿼리
    @Query(value = "SELECT * FROM board WHERE title LIKE %:title%", nativeQuery = true)
    List<BoardEntity> findSearchBoard(@Param("title") String title);
}
