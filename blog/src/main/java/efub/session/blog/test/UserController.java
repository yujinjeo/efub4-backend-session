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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User save(@RequestBody UserRequestDTO dto){
        return userService.save(dto);
    }

    // GET - User 조회 컨트롤러 작성
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findById(@PathVariable Long id){
        return userService.findById(id);
    }

    // IllegalArgumentException 예외처리 handler 작성
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void userNotFoundHandler(IllegalArgumentException e){
        System.out.println("UserController.userNotFoundHandler");
    }


}
