package efub.session.blog.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST - User 생성 컨트롤러 작성

    // GET - User 조회 컨트롤러 작성

    // IllegalArgumentException 예외처리 handler 작성



}
