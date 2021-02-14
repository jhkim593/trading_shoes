package jpa.project.repository.custom;

import jpa.project.entity.Board;
import jpa.project.repository.custom.BoardRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> , BoardRepositoryCustom {

    Page<Board>findAllBy(Pageable pageable);

}
