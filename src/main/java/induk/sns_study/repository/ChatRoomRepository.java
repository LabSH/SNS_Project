package induk.sns_study.repository;

import induk.sns_study.entity.ChatRoomEntity;
import induk.sns_study.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {

    @Query("SELECT r.id FROM ChatRoomEntity r WHERE (r.user1 = :user1 AND r.user2 = :user2) OR (r.user1 = :user2 AND r.user2 = :user1)")
    Long findRoomByUser(@Param("user1") String user1, @Param("user2") String user2);

}
