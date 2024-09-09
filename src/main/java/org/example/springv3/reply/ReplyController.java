package org.example.springv3.reply;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.springv3.core.error.ex.ExceptionApi403;
import org.example.springv3.core.util.Resp;
import org.example.springv3.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class ReplyController {

    private final HttpSession session;
    private final ReplyService replyService;

    //ajax쓰고 있어서 save, delete 이런거 필요 없음 구분할거니까
    //2개 받아야함!!  즉 reqeustDTO만들어야함
    @PostMapping("/api/reply")
    public ResponseEntity<?> save(@RequestBody ReplyRequest.SaveDTO saveDTO){
        User sessionUser = (User) session.getAttribute("sessionUser");
        ReplyResponse.DTO replyDTO = replyService.댓글쓰기(saveDTO, sessionUser);
        return ResponseEntity.ok(Resp.ok(replyDTO));

        //리플라이 객체에 json으로 들어가면 user, board다 들어감  곳 nosession된다!!
        /*
         User sessionUser = (User) session.getAttribute("sessionUser");
        Reply replyPs = replyService.댓글쓰기(saveDTO, sessionUser);
        return ResponseEntity.ok(Resp.ok(replyPs));
         */
    }

    //delete매핑이니까 동사를 뺄 수 잇다 post면 붙여야 한다!1
    @DeleteMapping("/api/reply/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        //1. 세션 체크 인증체크 해야한다  api만 붙이면 됨 -> 주소로 처리가능 클리어
        //2. 서비스 로직 호출 -> 댓글 삭제 해서
        User sessionUser = (User) session.getAttribute("sessionUser");
        replyService.댓글삭제(id, sessionUser);
        //3. 로그인한 유저, 해당 글 맞는지 권한 체크
        //4. 응답

        //스테틱으로 되서 new 할 필요 없다!! T body자리다
        //지금은 돌려줄 바디가 필요 없다 무조건 Resp해야함
        //상태코드 집어넣으려고!!  ResponseEntity
        return ResponseEntity.ok(Resp.ok(null));
       // throw new ExceptionApi403("권한이 없습니다");
    }
}
