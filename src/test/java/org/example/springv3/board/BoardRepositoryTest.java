package org.example.springv3.board;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


@DataJpaTest //메모리에 다 뜬다! JpaRepository를 상속하면 import쓸 필요 없다!
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void mFindByIdwithReply_test(){
       // Board board = boardRepository.mFindByIdWithReply(5).get();
        //System.out.println(board.getReplies().get(0).getComment());
    }
    //given


    //when


    //then
    @Test
    public void mFindAll_test() throws JsonProcessingException {
        /*
        //given
        String title ="1";
        //when
            List<Board> boardList = boardRepository.mFindAll(title);
        //then
        System.out.println(boardList.size());
        System.out.println(boardList.get(0).getTitle());
         */
        //given
        String title = "제목";

        //when
        Pageable pageable = PageRequest.of(0,3);
        Page<Board> boardPG = boardRepository.mFindAll(title, pageable);

        //eye
        ObjectMapper om = new ObjectMapper();
        //쓰로우 해주기
        String responseBody = om.writeValueAsString(boardPG);
        //om.readValue(responseBody, (타입 /클래스) Board.class);    이게 뭐하는 거지?
        System.out.println(responseBody);
    }

    //toString으로 보는 테스트
    @Test
    public void mFindAllV2_test() throws JsonProcessingException {
        // given
        String title = "제목";

        // when
        Pageable pageable = PageRequest.of(0, 3);
        Page<Board> boardPG = boardRepository.mFindAll(title, pageable);

        // eye
        System.out.println(boardPG.getContent());
    }
}
