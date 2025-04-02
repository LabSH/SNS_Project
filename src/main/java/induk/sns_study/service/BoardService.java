package induk.sns_study.service;


import induk.sns_study.dto.BoardDTO;


import induk.sns_study.dto.MemberDTO;
import induk.sns_study.entity.BoardEntity;
import induk.sns_study.entity.MemberEntity;
import induk.sns_study.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시판 4개의 데이터만 가져오기
    public List<BoardDTO> findAll(int a) {
        List<BoardEntity> boardEntityList = boardRepository.findLimitedBoards(a);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList){

            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        System.out.println(boardDTOList);
        return boardDTOList;
    }

    // 자신의 게시판을 제외한 3개 게시판 데이터 조화
    // Under_list사용
    public List<BoardDTO> noMy_findAll(int a, Long b) {
        List<BoardEntity> boardEntityList = boardRepository.find_NoMy_Boards(a,b);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList){

            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        System.out.println(boardDTOList);
        return boardDTOList;
    }


    // 게시판 4개의 데이터만 가져오기
    public List<BoardDTO> findAll_noRand(int a) {
        List<BoardEntity> boardEntityList = boardRepository.findLimitedBoards_noRand(a);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList){

            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        System.out.println(boardDTOList);
        return boardDTOList;
    }

    // 검색한 제목에 포함된 게시판만 조회

    public List<BoardDTO> findAll_search(String title) {
        List<BoardEntity> boardEntityList = boardRepository.findSearchBoard(title);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList){

            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        System.out.println(boardDTOList);
        return boardDTOList;
    }



    // create 역할
    public void save(BoardDTO boardDTO){
        BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    // 자신의 게시판을 전부 조회
    public List<BoardDTO> findByMemberId(Long id){
        List<BoardEntity> boardEntityList = boardRepository.findByMemberId(id);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        if(!boardEntityList.isEmpty()) {
            for (BoardEntity boardEntity : boardEntityList) {
                boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
            }
            return boardDTOList;
        }
        System.out.println("BoardService : findByMemberId 값 없다.");
        return null;
    }

    // 1개의 게시판 상세보기
    public BoardDTO findById(Long id){
        Optional<BoardEntity> boardRepositoryById = boardRepository.findById(id);
        if(boardRepositoryById.isPresent()){
            return BoardDTO.toBoardDTO(boardRepositoryById.get());
        }else{
            return null;
        }
    }

    // 게시판 업데이트
    public BoardDTO updateForm(Long id){
        Optional<BoardEntity> boardEntity = boardRepository.findById(id);
        if(boardEntity.isPresent()){
            return BoardDTO.toBoardDTO(boardEntity.get());
        }else{
            return null;
        }
    }

    public void update(BoardDTO boardDTO){
        boardRepository.save(BoardEntity.toUpdateBoardEntity(boardDTO));
    }

    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }
}
