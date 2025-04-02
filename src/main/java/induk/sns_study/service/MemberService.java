package induk.sns_study.service;


import induk.sns_study.dto.MemberDTO;
import induk.sns_study.entity.MemberEntity;
import induk.sns_study.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 자신외의 멤버 리스트 조회
    public List<MemberDTO> getOtherMembers(Long memberId) {
        List<MemberEntity> memberEntityList = memberRepository.getOtherMembers(memberId);
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    // 자신외 멤버 리스트 조회(검색)
    public List<MemberDTO> searchOtherMembers(String name, Long memberId) {
        List<MemberEntity> memberEntityList = memberRepository.searchOtherMembers(name, memberId);
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public void save(MemberDTO memberDTO){
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO){
        // 1. DB 조회
        // 2. 아이디외 비번 일치하는지 판단.
        Optional<MemberEntity> memberEmail = memberRepository.findByEmail(memberDTO.getEmail());
        if(memberEmail.isPresent()){
            // 조회 성공
            MemberEntity memberEntity = memberEmail.get();
            if(memberEntity.getPassword().equals(memberDTO.getPassword())){
                // 비번 일치
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                System.out.println(dto);
                return dto;
            }else{
                return null;
            }
        }else{
            // 조회 실패
            return null;
        }
    }

    public MemberDTO findById(Long id){
        Optional<MemberEntity> memberRepositoryById = memberRepository.findById(id);
        if(memberRepositoryById.isPresent()){
            return MemberDTO.toMemberDTO(memberRepositoryById.get());
        }else{
            return null;
        }
    }

    public String findByName(Long id){
        String memberName = memberRepository.findNameById(id);
        if(memberName != null){
            System.out.println(memberName);
            return memberName;
        }else{
            return null;
        }
    }

    public MemberDTO updateForm(Long id){
        Optional<MemberEntity> opmemberEntity = memberRepository.findById(id);
        if(opmemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(opmemberEntity.get());
        }else{
            return null;
        }
    }

    public void update(MemberDTO memberDTO){
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public Long count(){
        return memberRepository.count();
    }
}
