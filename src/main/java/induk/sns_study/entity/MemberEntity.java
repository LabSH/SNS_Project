package induk.sns_study.entity;

import induk.sns_study.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


// 테이블 이름
@Getter
@Setter
@Table(name = "member")
@Entity // 고정
public class MemberEntity {

    // 고유 번호 id 속성에 적용하는 어노테이션으로 id 속성을 기본 키로 지정한다.
    @Id
    // 속성에 값을 따로 지정하지 않아도 1씩 자동으로 증가하게 된다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // 컬럼
    @Column
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String phone;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName(memberDTO.getName());
        memberEntity.setEmail(memberDTO.getEmail());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setPhone(memberDTO.getPhone());
        return memberEntity;
    }

    public static MemberEntity toUpdateMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setName(memberDTO.getName());
        memberEntity.setEmail(memberDTO.getEmail());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setPhone(memberDTO.getPhone());
        return memberEntity;
    }
}


