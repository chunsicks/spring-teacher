package org.example.springv3.board;

import lombok.Data;
import org.example.springv3.reply.Reply;
import org.example.springv3.user.User;

import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @Data
    public static class DetailDTO {

        private Integer id;
        private String title;
        private String content;
        private Boolean isOwner;
       // private Integer userId;
        private String username;

        //리플라이 엔티티 집이 넣으면 안된다 레이지 로딩 되니까! 똑같이 생긴 DTO만들면 된다 비영속객체 만들어서 응답하게 하는게 좋다!
        private List<ReplyDTO> replies = new ArrayList<>();

        public DetailDTO(Board board, User sessionUser) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isOwner = false;

            if (sessionUser != null) {
                if (board.getUser().getId() == sessionUser.getId()) {
                    isOwner = true; // 권한체크
                }
            }
           // this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();

            for(Reply reply : board.getReplies()) {
                replies.add(new ReplyDTO(reply,sessionUser));
            }
        }
    }
    @Data
    public static class ReplyDTO{
        private Integer id;
        private String comment;
        private String username;
        private Boolean isOwner;

        public ReplyDTO(Reply reply, User sessionUser) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.username = reply.getUser().getUsername();
            this.isOwner = false;

            if (sessionUser != null) {
                if (reply.getUser().getId() == sessionUser.getId()) {
                    isOwner = true; // 권한체크
                }
            }
        }
    }
    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String content;

        public DTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }
}
