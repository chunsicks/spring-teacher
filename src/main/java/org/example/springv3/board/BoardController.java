package org.example.springv3.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springv3.core.error.ex.Exception404;
import org.example.springv3.core.error.ex.ExceptionApi404;
import org.example.springv3.core.util.Resp;
import org.example.springv3.user.User;
import org.example.springv3.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardService boardService;

    //localhost:8080?title=제목 이러면?   requestParam 생략 가능
    // 이유는 적어주면 requestParam에서(defaultValue ="", name="title") 이렇게 쓸 수 있다 쿼리 안에 title이 없으면 터지는데 (파싱 못하니까) 그래서 공백으로 넣어라 할 수 있다.!
    @GetMapping("/")
    public String list(@RequestParam(value = "title", required = false) String title,
                       @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                       HttpServletRequest request) {
        //쿼리스트링으로 page넣을 거임
        // 디폴트 값 넣을 수 있다.
        /*
        if(page == null) {
            page = 0;
        }
        이렇게 넘길 수 있다. /호출하면 넣어줄 수 있다.
        defaultValue는 쿼리스트링으로 받는 거라서 숫자 0으로는 안됨 문자열 0으로 해야함 그래서 defaultValue하면 if 위에거 사용 안해도 됨
         */
        BoardResponse.PageDTO boardPG = boardService.게시글목록보기(title,page);
        //가방에 담고 list에 있는 것만 꺼낼꺼니까 DTO안만들어도 됨
        request.setAttribute("model", boardPG);
        return "board/list";
    }

    @Transactional
    @PostMapping("/api/board/{id}/delete")
    public String removeBoard(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.게시글삭제하기(id, sessionUser);
        return "redirect:/";
    }


    @GetMapping("/api/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }


    @PostMapping("/api/board/save")
    public String save(@Valid BoardRequest.SaveDTO saveDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.게시글쓰기(saveDTO, sessionUser);
        return "redirect:/";
    }


    @GetMapping("/api/board/{id}/update-form")
    public String updateForm(@PathVariable("id") int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardService.게시글수정화면(id, sessionUser);
        request.setAttribute("model", board);
        return "board/update-form";
    }

    @GetMapping("/v2/api/board/{id}/update-form")
    public @ResponseBody BoardResponse.DTO updateForm(@PathVariable("id") int id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DTO model = boardService.게시글수정화면V2(id, sessionUser);
        return model;
    }

    @PostMapping("/api/board/{id}/update")
    public String update(@PathVariable("id") int id, @Valid BoardRequest.UpdateDTO updateDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.게시글수정(id, updateDTO, sessionUser);
        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DetailDTO model = boardService.게시글상세보기(sessionUser, id);
        request.setAttribute("model", model);
        return "board/detail";
    }

    //필요한 정보만 가지고 오는지 json으로 확인해보자
    @GetMapping("/v2/board/{id}")
    public @ResponseBody BoardResponse.DetailDTO detailV2(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO model = boardService.게시글상세보기(sessionUser, id);
        return model;
    }
    @GetMapping("/v3/board/{id}")
    public @ResponseBody Board detailV3(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        Board model = boardService.게시글상세보기V3(sessionUser, id);

        return model;
    }

    @GetMapping("/test/v1")
    public @ResponseBody Resp testV1(){
        User u = new User();
        u.setId(1);
        u.setUsername("ssar");
        u.setPassword("1234");
        u.setEmail("ssar@gmail.com");
        return Resp.ok(u);
    }
    //uset2명 만듬
    @GetMapping("/test/v2")
    public @ResponseBody Resp testV2(){
        User u1 = new User();
        u1.setId(1);
        u1.setUsername("ssar");
        u1.setPassword("1234");
        u1.setEmail("ssar@gmail.com");

        User u2 = new User();
        u2.setId(1);
        u2.setUsername("ssar");
        u2.setPassword("1234");
        u2.setEmail("ssar@gmail.com");

        //2개를 리턴하고 싶으면 arrayList에 넣어야 한다
        //new arrayList한거와 같다!  들어갈 때 내부가 T다!!
        List<User> users = Arrays.asList(u1, u2);
        return Resp.ok(users);
    }
//터트림
    @GetMapping("/test/v3")
    public @ResponseBody Resp testV3(){
        return Resp.fail(404, "유저를 찾을 수 없습니다");
    }
    @GetMapping("/test/v4")
    public @ResponseBody Resp testV4(HttpServletResponse response){
        response.setStatus(404);
        return Resp.fail(404, "유저를 찾을 수 없습니다");
    }
    @GetMapping("/test/v5")
    public ResponseEntity<?> testV5(){ // 1. ResponseBody 생략, 상태코드를 넣을 수 있따.
        return new ResponseEntity<>(Resp.fail(404, "찾을 수 없습니다"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/test/v6")
    public ResponseEntity<?> testV6(){ // 1. ResponseBody 생략, 상태코드를 넣을 수 있따.
       throw new ExceptionApi404("페이지를 찾을 수 없습니다");
    }

    @PostMapping("/text/form")
    public @ResponseBody String form(){
        return "ok";
        //return "user/join-form";
    }
    // return "user/join-form";
}
