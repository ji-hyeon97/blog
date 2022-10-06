package cos.blog.controller;

import cos.blog.auth.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController { // 컨트롤러에서 세션을 어떻게 찾는지 ..?

    @GetMapping({"","/"})
    public String index(@AuthenticationPrincipal PrincipalDetail principalDetail){
        System.out.println("로그인 사용자 아이디 : "+principalDetail.getUsername());
        return "index";
    }
}
