package induk.sns_study.repository;

import induk.sns_study.entity.ChatRoom;
import induk.sns_study.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUser1AndUser2(MemberEntity user1, MemberEntity user2);
}
