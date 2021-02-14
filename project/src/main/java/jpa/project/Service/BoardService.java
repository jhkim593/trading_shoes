package jpa.project.Service;

import jpa.project.advide.exception.CNotOwnerException;
import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.dto.board.BoardCreateRequestDto;
import jpa.project.dto.board.BoardDto;
import jpa.project.entity.Board;
import jpa.project.entity.Board_liked;
import jpa.project.entity.Member;
import jpa.project.repository.custom.BoardRepository;
import jpa.project.repository.search.BoardSearch;
import jpa.project.repository.Board_likedRepository;
import jpa.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final Board_likedRepository board_likedRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardDto save(String name, BoardCreateRequestDto bcrDto
                         ){
        Optional<Member> findMember = memberRepository.findByUsername(name);
        Member member = findMember.orElseThrow(() -> new NoSuchElementException());
        Board board = Board.addBoard(member, bcrDto);
        boardRepository.save(board);
       return new BoardDto(board);
    }

    @Transactional
    public BoardDto update(Long id,String username,BoardCreateRequestDto boardCreateRequestDto){
        Optional<Board> findBoard = boardRepository.findById(id);
        Board board = findBoard.orElseThrow(() -> new CResourceNotExistException());
        if(!board.getMember().getUsername().equals(username)){
            throw new CNotOwnerException();
        }
        board.update(boardCreateRequestDto);
        return new BoardDto(board);


    }
    @Transactional
    public void delete(Long id,String username){
        Optional<Board> findBoard = boardRepository.findById(id);
        Board board = findBoard.orElseThrow(() -> new CResourceNotExistException());
        if(!board.getMember().getUsername().equals(username)){
            throw  new CNotOwnerException();
        }

        boardRepository.deleteById(id);
    }

    public List<BoardDto> findAll(){
        List<Board> board = boardRepository.findAll();
        List<BoardDto>boardDtoList=new ArrayList<>();
       for(Board boards:board){
           BoardDto boardDto=new BoardDto(boards);
           boardDtoList.add(boardDto);
       }
       return boardDtoList;
    }
    public BoardDto find(Long id){
        Optional<Board> opBoard = boardRepository.findById(id);
        Board board = opBoard.orElseThrow(() -> new CResourceNotExistException());
        BoardDto boardDto=new BoardDto(board);
        return boardDto;
    }

    @Transactional

    public void boardLike(BoardDto boardDto,String name){
        Optional<Member> findMember = memberRepository.findByUsername(name);
        Member member = findMember.orElseThrow(() -> new NoSuchElementException());
        Optional<Board> findBoard = boardRepository.findById(boardDto.getId());
        Board board = findBoard.orElseThrow(() -> new NoSuchElementException());
        Optional<Board_liked> findBoard_liked = board_likedRepository.findByBoardLike(member.getId(), board.getId());

        if(findBoard_liked.isEmpty()) {


           Board_liked board_liked1 = new Board_liked(board, member);
            board_likedRepository.save(board_liked1);
        }
        else
        {
            Board_liked board_liked = findBoard_liked.orElseThrow(() -> new NoSuchElementException());
            board_likedRepository.delete(board_liked);
        }

    }
//    public List<Board>findAllBy(){

//        PageRequest pageRequest = PageRequest.of(i, 10, Sort.by(Sort.Direction.DESC, "username"));
//        Page<Board> page = boardRepository.findAllBy();
//        content.size()).isEqualTo(1);
//        page.getTotalElements()).isEqualTo(5);
//        page.getNumber()).isEqualTo(1);
//        page.getTotalPages()).isEqualTo(2);
//        page.isFirst()).isFalse();
//        page.hasNext()).isFalse();


//    }
    @Transactional
    public BoardDto boardLikeCount(BoardDto boardDto){
        int i = board_likedRepository.boardLikeCount(boardDto.getId());
        boardDto.setBoardLikeCount(i);
        return boardDto;
    }

    public Page<BoardDto>search(BoardSearch search,Pageable pageable){
        return boardRepository.search(search,pageable);
    }
//    public List<BoardDto> searchTest(BoardSearch boardSearch){
//       return boardRepository.searchTest(boardSearch);
//
//    }
}
