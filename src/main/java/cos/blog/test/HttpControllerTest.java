package cos.blog.test;

import org.springframework.web.bind.annotation.*;

@RestController
public class HttpControllerTest {

    private static final String TAG = "HttpController Test : ";

    @GetMapping("/http/lombok")
    public String lombokTest(){
        Member m = Member.builder().username("Seo").password("1234").email("sgh9702@naver.com").build();
        System.out.println(TAG+ "getter : "+m.getUsername());
        m.setUsername("Kim");
        System.out.println(TAG+ "setter : "+m.getUsername());
        return "lombok test 완료";
    }

    @GetMapping("/http/get")
    public String getTest(/** @RequestParam int id, @RequestParam String username **/ Member m){
        System.out.println(TAG + "getter : "+m.getId());
        m.setId(5000);
        System.out.println(TAG + "getter : "+m.getId());
        Member m1 = new Member();
        Member m2 = new Member();
        return "get 요청 "+ m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();
    }

    @PostMapping("/http/post")
    public String postTest(/** Member m */ @RequestBody Member m){
        return "post 요청 "+ m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();
    }

    @PutMapping("/http/put")
    public String putTest(@RequestBody Member m){
        return "put 요청 "+ m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();
    }

    @DeleteMapping("/http/delete")
    public String deleteTest(){
        return "delete 요청";
    }
}


