package org.example.springv3.reply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springv3.board.Board;
import org.example.springv3.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Table(name = "reply_tb")
@Data
@NoArgsConstructor
@Entity
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String comment; // 댓글 내용
    @CreationTimestamp//em.persist 할 때 발동
    private Timestamp createdAt;

    @Builder
    public Reply(Integer id, User user, Board board, String comment, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.board = board;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", user=" + user +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
