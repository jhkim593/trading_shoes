package jpa.project.repository;

import jpa.project.entity.Board_liked;
import jpa.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Board_likedRepository extends JpaRepository<Board_liked,Long> {

    @Query("select b from Board_liked b where b.member.id=:memberId and b.board.id=:boardId")
    Optional<Board_liked> findByBoardLike(@Param("memberId")Long memberId,@Param("boardId")Long boardId);

    @Query("select count(b) from Board_liked b where b.board.id=:boardId")
    int boardLikeCount(@Param("boardId")Long boardId);
}
