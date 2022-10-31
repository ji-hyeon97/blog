package cos.blog.repository;

import cos.blog.dto.ReplySaveRequestDto;
import cos.blog.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    //순서는 ReplySaveRequestDto 필드 순서랑 잘 맞아야 한다
    @Modifying
    @Query(value = "INSERT INTO reply(userId, boardId, content, createDate) VALUES(?1,?2,?3,now())", nativeQuery = true)
    int mSave(int userId, int boardId, String content); //@Modifying: 업데이트된 행의 개수를 리턴해 준다
}
