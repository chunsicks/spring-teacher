package org.example.springv3.user;

import lombok.RequiredArgsConstructor;
import org.example.springv3.core.error.ex.Exception401;
import org.example.springv3.core.error.ex.Exception400;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;


    public User 로그인(UserRequest.LoginDTO loginDTO) {

            User user = userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                    .orElseThrow(() -> new Exception401("인증되지 않았습니다."));
            return user;
    }

    @Transactional
    public void 회원가입(UserRequest.JoinDTO joinDTO) {
        //유저 이름으로 존재하는지 확인하는 것
        Optional<User> userPs= userRepository.findByUsername(joinDTO.getUsername());
        //코드 짜다보면 널 안 넣을 수 도 있으니까
        if(userPs.isPresent()) {
            throw new Exception400("이미 존재하는 유저입니다.");
        }
        userRepository.save(joinDTO.toEntity());

    }

    public boolean 유저네임중복되었니(String username) {
        Optional<User> userOP = userRepository.findByUsername(username);
        //optional안에 있는 것 꺼내기
        if(userOP.isPresent()) {
            return true;
        }else {
            return false;
        }
    }
}
