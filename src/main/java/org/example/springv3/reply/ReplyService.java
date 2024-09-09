package org.example.springv3.reply;

import lombok.RequiredArgsConstructor;
import org.example.springv3.board.Board;
import org.example.springv3.board.BoardRepository;
import org.example.springv3.core.error.ex.ExceptionApi403;
import org.example.springv3.core.error.ex.ExceptionApi404;
import org.example.springv3.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service  //컴포넌트 스켄 IOC에 넣기
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;


    @Transactional
    public void 댓글삭제(int id, User sessionUser) {
        //애는 integer인지 어떻게 알았을까? 프라이머리키에 integer로 적어놔서 안다!
        //jpa레파지토리 만들기 어렵지 않다 다음에 만들어보자! 궁금하다고 하면 알려주심 제내릭만 잘 알면 된다!

        //삭제 전에 조회 먼저 하자
        //ajax요청이니까 apiException터트려야 한다!! 받아줘야 하니까 앞에 reply에 PS 넣어줌
        Reply replyPS = replyRepository.findById(id).orElseThrow(
                () -> new ExceptionApi404("해당 댓글을 찾을 수 없습니다")
        );
        //지금 댓글만 조회할건데 User Board 안 내용은 비어있음 하지만 getUser.getId 포린키가 프라이머리키 들고있으니까 조회안함!!
        // BoardId, User Id 는 이미 있음 래이지 로딩은 안 내용은 없지만 프라이머리키는 가지고 있다
        //즉 select한번 더 할 필요 없다
        // 하지만 getusername()하려면 select한번 더 해야함
        //이렇게 하려면 mFindById만들어서 join하는 쿼리 만들고 1번만 select할 수 있게 만들면 된다!!

        //findById하면 reply객체 만들어줌  id에 들어가고 comment들어가고 user, board에는 set id만 들어가 있고 안차있다!!
        if (replyPS.getUser().getId() != sessionUser.getId()) {
            throw new ExceptionApi403("댓글 삭제 권한이 없습니다");
            //이런 것 전부가 트랜잭션이다 그래서 @Transaction붙여야 한다
        }
        replyRepository.deleteById(id);
    }

    @Transactional
    public ReplyResponse.DTO 댓글쓰기(ReplyRequest.SaveDTO saveDTO, User sessionUser) {
        //1. 게시글 존재 유무 확인
        Board boardPs = boardRepository.findById(saveDTO.getBoardId())
                .orElseThrow(() -> new ExceptionApi404("게시글을 찾을 수 없습니다."));
        //2. 비영속 댓글 객체 만들기
        Reply reply = saveDTO.toEntity(sessionUser, boardPs);  //여기까지는 비영속

        //3. 댓글 저장
        replyRepository.save(reply); //담궈지자 마자 insert 프라이머리키 생긴다
        //DTO 응답 이유 해야지 jpa 안터진다!
        //실 목적은 화면에 필요한 데이터만 주려고 만든다!
        return new ReplyResponse.DTO(reply);
    }
}
