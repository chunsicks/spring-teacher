package org.example.springv3.board;

import org.example.springv3.user.User;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    //9.10일 검색한다고 추가
    @Query("select b from Board b where b.title like %:title% order by b.id desc")
    List<Board> mFindAll(@Param("title") String title);

    // 내가 만든 메서드는 앞에 m을 붙인다 그냥 스스로 만든 약속
    // 이렇게 적으면 sort안 써도 됨
   // @Query("select b from Board b order by b.id desc")
   // List<Board> mFindAll();

    // 만약 네이티브 쿼리로 join할라면
   // @Query(value = "select * from board_tb bt inner join user_tb ut bt.user_id = ut.id where bt.id=?", nativeQuery = true);
    @Query("select b from Board b join fetch b.user u where b.id=:id")
   // Board mFindById(@Param("id") int id);
    Optional<Board> mFindById(@Param("id")int id);

   @Query("select b from Board b join fetch b.user left join fetch b.replies r left join fetch r.user where b.id=:id")
   Optional<Board> mFindByIdWithReply(@Param("id")int id);
}

