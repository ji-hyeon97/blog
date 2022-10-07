package cos.blog.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
@Entity
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob //대용량 데이터
    private String content; //섬머노트 라이브러리 <html>태그가 섞여서 디자인이 됨

    private int count; //조회수

    @ManyToOne(fetch = FetchType.EAGER) //many(board) one(user)
    @JoinColumn(name = "userId")
    private User user; //DB는 오브젝트를 저장할 수 없다 -> 자바는 오브젝트를 저장할 수 있다

    @OneToMany(mappedBy = "board") //mappedBy가 있으면 연관관계의 주인이 아니다 -> DBDp FK를 만들지 마시오
    private List<Reply> reply;

    @CreationTimestamp
    private Timestamp createDate;
}
