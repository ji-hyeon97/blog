package cos.blog.service;

import cos.blog.model.RoleType;
import cos.blog.model.User;
import cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User memberFind(String username){
        User user = userRepository.findByUsername(username).orElseGet(()-> {
            return new User();
        });
        return user;
    }



    @Transactional
    public void memberRegister(User user){

        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword); //해쉬됨
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);

        try{
            userRepository.save(user);
            return;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("UserService: 회원가입() : "+e.getMessage());
            return;
        }
    }

    @Transactional
    public void memberModify(User user){
        // 수정시에는 영속성 컨텍스트에 User 오브젝트를 영속화 시키고, 영속화된 User 오브젝트를 수정
        // select 를 해서 User 오브젝트를 DB로 부터 가져오는 이유는 영속화를 하기 위해서!
        // 영속화된 오브젝트를 변경하면 자동으로 DB에 update 문을 날려준다
        User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
            return new IllegalArgumentException("회원 찾기 실패 !");
        });

        // validate check => oauth 필드에 값이 없어야 수정이 가능
        if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
            String rawPassword = user.getPassword();
            String encodePassword = encoder.encode(rawPassword);
            persistance.setPassword(encodePassword);
            persistance.setEmail(user.getEmail());
            // 회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 종료 = commit 이 자동으로 진행
            // 영속화된 persistence 객체의 변화가 감지되면 더티체킹이 되어 update 문을 날려줌
        }
    }

    /**
    @Transactional(readOnly = true) //select 할때 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료(정합성)
    public User Login(User user){
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    } */
}
