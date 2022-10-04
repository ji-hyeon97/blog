package cos.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
//@DynamicInsert // Insert 할때 null 인 필드 제외
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    //@ColumnDefault("user")
    @Enumerated(EnumType.STRING) //DB는 RoleType이 없다
    private RoleType role; //Enum 을 쓰는 것이 좋다. admin, user, manager -> 사실 도메인 설정이 필요

    @CreationTimestamp //시간이 자동으로 입력
    private Timestamp createDate;

}
