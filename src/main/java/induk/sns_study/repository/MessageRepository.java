package induk.sns_study.repository;

import induk.sns_study.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT r FROM MessageEntity r WHERE r.chatRoomId = :roomId order by timestamp ")
    List<MessageEntity> findByMessage(@Param("roomId") Long roomId);
}
