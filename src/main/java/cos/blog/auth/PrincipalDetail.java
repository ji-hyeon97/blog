package cos.blog.auth;

import cos.blog.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리타가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 객체를
// 스프링 시큐리티의 고유한 세션 저장소에 저장을 한다.
public class PrincipalDetail implements UserDetails {
    private User user; //콤포지션 : 객체를 들고 있는것

    public PrincipalDetail(User user){
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되지 않았는지 리턴한다 (true:만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않았는지 리턴한다(true:잠기지 않음)
     @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되지 않았는지를 리턴한다(true:만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화(사용가능)인지 리턴한다(true:활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 계정의 권한을 리턴한다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();

        /**
        collectors.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_"+user.getRole(); //ROLE_USER -> 앞에 ROLE 을 붙혀주어야 인식을 한다!
            }
        });   */

        collectors.add(()-> {
            return "ROLE_"+user.getRole();
        });

        return collectors;
    }
}
