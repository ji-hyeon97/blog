package cos.blog.controller.api;

import cos.blog.dto.ResponseDto;
import cos.blog.model.RoleType;
import cos.blog.model.User;
import cos.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @PostMapping("/api/user")
    public ResponseDto<Integer> save(@RequestBody User user){ //username. password, email
       // System.out.println("UserApiController");

        user.setRole(RoleType.USER);
        userService.memberRegister(user);

        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }

    @PostMapping("/api/user/login")
    public ResponseDto<Integer> login(@RequestBody User user){
        System.out.println("UserApiController 로그인");
        User principal = userService.Login(user); //principal(접근 주체)

        if (principal != null){
            session.setAttribute("principal", principal);
        }
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
