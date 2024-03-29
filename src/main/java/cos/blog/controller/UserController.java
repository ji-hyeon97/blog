package cos.blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cos.blog.model.KakaoProfile;
import cos.blog.model.OAuthToken;
import cos.blog.model.User;
import cos.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

// 인증이 안된 사용자들이 출입할 수 있는 경로 : /auth/** 허용
// 그냥 주소가 /이면 index.jsp 허용
// static 이하에 있는 /js/**, /css/**, /image/** 허용

@Controller
public class UserController {

    @Value("${cos.key}")
    private String cosKey;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/auth/joinForm")
    public String joinForm(){
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm(){
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm(){
        return "user/updateForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code){

        // post 방식으로 key value 데이터를 요청(카카오로!) Retrofit2, OkHttp, RestTemplate 등의 라이브러리가 있다
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","d852327901316bc90e64af893ee770df");
        params.add("redirect_uri","http://localhost:8080/auth/kakao/callback");
        params.add("code",code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params,headers);

        // Http요청하기 - post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try{
            oauthToken = objectMapper.readValue(response.getBody(),OAuthToken.class);
        } catch(JsonMappingException e){
            e.printStackTrace();
        } catch(JsonProcessingException e){
            e.printStackTrace();
        }

       // System.out.println("카카오 토큰 : "+ oauthToken.getAccess_token());


        RestTemplate rt2 = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");


        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        // Http요청하기 - post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        System.out.println(response2.getBody());

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try{
            kakaoProfile = objectMapper2.readValue(response2.getBody(),KakaoProfile.class);
        } catch(JsonMappingException e){
            e.printStackTrace();
        } catch(JsonProcessingException e){
            e.printStackTrace();
        }

        // user 오브젝트 : username, password, email
        System.out.println("카카오 아이디 : "+kakaoProfile.getId());
        System.out.println("카카오 이메일 : "+kakaoProfile.getKakao_account().getEmail());

        // UUID란 중복되지 않는 어떤 특정 값을 만들어 내는 알고리즘
        // UUID garbagePassword = UUID.randomUUID();

        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
                .password(cosKey)
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")
                .build();

        //가입자 or 비가입자 체크해서 처리
        User originUser = userService.memberFind(kakaoUser.getUsername());

        if(originUser.getUsername() == null){
            userService.memberRegister(kakaoUser);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(),cosKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }
}
