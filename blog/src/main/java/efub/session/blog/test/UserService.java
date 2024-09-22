package efub.session.blog.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    //사용자 저장
    public User save(UserRequestDTO dto){
        return userRepository.save(dto.toEntity());
    }


    //사용자 조회
    public User findById(Long id){
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));
        return findUser;
    }
}

