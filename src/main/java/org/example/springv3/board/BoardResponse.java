package org.example.springv3.board;

import lombok.Data;
import org.example.springv3.reply.Reply;
import org.example.springv3.user.User;
import org.springframework.data.domain.Page;


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
    @Data
    public static class PageDTO {
        private Integer number; // 현재페이지
        private Integer totalPage; // 전체페이지 개수
        private Integer size; // 한페이지에 아이템 개수
        private Boolean first;
        private Boolean last;
        private Integer prev; // 현재페이지 -1
        private Integer next; // 현재페이지 +1
        private List<Content> contents = new ArrayList<>();
        private List<Integer> numbers = new ArrayList<>();

        public PageDTO(Page<Board> boardPage) {
            this.number = boardPage.getNumber();
            this.totalPage = boardPage.getTotalPages();
            this.size = boardPage.getSize();
            this.first = boardPage.isFirst();
            this.last = boardPage.isLast();
            this.prev = boardPage.getNumber()-1;
            this.next = boardPage.getNumber()+1;
            int temp = (number / 3)*3;  // 0-> 0, 3->3, 6->6

            for(int i = temp; i<temp+2; i++){
                this.numbers.add(i);
            }

            //for로 id title만 있으면 확인이 가능하다
            //어디서 값을 가지고 와야 하지?
            for(Board board : boardPage.getContent()) {
                contents.add(new Content(board));
            }
        }
        //생성자를 만들어서

        @Data
        class Content {
            private Integer id;
            private String title;

            public Content(Board board) {
                this.id = board.getId();
                this.title = board.getTitle();
            }
        }
    }
}
