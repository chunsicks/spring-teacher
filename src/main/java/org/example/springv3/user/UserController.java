package org.example.springv3.user;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final HttpSession session;
    private final UserService userService;

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@Valid UserRequest.LoginDTO loginDTO, Errors errors) {
        User sessionUser = userService.로그인(loginDTO);
        System.out.println("12312323123213" + sessionUser.getUsername());
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
    }
}
