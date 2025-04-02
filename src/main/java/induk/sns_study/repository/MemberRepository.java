package induk.sns_study.repository;

import induk.sns_study.entity.BoardEntity;
import induk.sns_study.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


// JpaRepository는 기본적인 CRUD(Create, Read, Update, Delete) 메서드를 제공하는 인터페이스입니다.
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 이메일로 회원번호 조회 (select * from member where email=?)
    Optional<MemberEntity> findByEmail(String email);
    //save 메서드는 id가 없으면 insert 있으면 update?

    @Query("SELECT m.name FROM MemberEntity m WHERE m.id = :id")
    String findNameById(@Param("id") Long id);

    // 자신외의 멤버 리스트 조회
    @Query("SELECT DISTINCT m FROM MemberEntity m WHERE m.id != :memberId")
    List<MemberEntity> getOtherMembers(@Param("memberId") Long memberId);

    // 자신외 멤버 리스트 조회(검색)
    @Query("SELECT DISTINCT m FROM MemberEntity m WHERE (m.id != :memberId) AND (m.name LIKE %:name%)")
    List<MemberEntity> searchOtherMembers(@Param("name") String name, @Param("memberId") Long memberId);
}
