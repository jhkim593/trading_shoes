package jpa.project.repository.board;

import jpa.project.dto.board.BoardDto;
import jpa.project.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository

public interface BoardRepositoryCustom {
    Page<BoardDto> search(BoardSearch boardSearch, Pageable pageable);
//    List<BoardDto> searchTest(BoardSearch boardSearch);




}
