package cos.blog.service;

import cos.blog.model.User;
import cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void memberRegister(User user){
        try{
            userRepository.save(user);
            return;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("UserService: 회원가입() : "+e.getMessage());
            return;
        }
    }

    @Transactional(readOnly = true) //select 할때 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료(정합성)
    public User Login(User user){
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
