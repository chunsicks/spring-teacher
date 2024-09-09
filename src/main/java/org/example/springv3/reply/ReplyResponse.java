package org.example.springv3.reply;

import lombok.Data;

public class ReplyResponse {
    @Data
    public static class DTO{
        private Integer id;
        private String comment;
        private String username;

        //깊은 복사 시작
        //이게 바디안에 리턴된다!
        public DTO(Reply reply) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.username = reply.getUser().getUsername();
        }
    }
}
