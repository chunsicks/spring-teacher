package org.example.springv3.reply;

import lombok.Data;
import org.example.springv3.board.Board;
import org.example.springv3.user.User;

public class ReplyRequest {

    @Data
    public static class SaveDTO{
        private Integer boardId;
        private String comment;

        //원래는
    /*
    insert into reply_tb(comment, board_id, user_id, created_at) values('댓글1', 5, 1, now());
    네이티브 쿼리 쓰면 insert끝나는데
    jpa쓰려면 객체 만들어서
    어자피 조회해야하지 않나? 그래서 board_id찾고 없으면 404 있으면 넣고
    jpa는 객체를 퍼시스트한다!
     */
        //toEntity만들어라!! 순수한 비영속 객체 만들어야 한다 ! insert할 때는 투 엔티티
        public Reply toEntity(User sessionUser, Board board){
            //리플리에 빌더 없음
            return Reply.builder()
                    .comment(comment)
                    .user(sessionUser)
                    .board(board)
                    .build();
            //보드 객체를 넣어야함
        }
    }


}
