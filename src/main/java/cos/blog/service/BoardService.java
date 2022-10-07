package cos.blog.service;

import cos.blog.model.Board;
import cos.blog.model.User;
import cos.blog.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public void write(Board board, User user){ //title, content 받음

        board.setUser(user);
        board.setCount(0);

        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> list(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board boardDetail(int id){
        return boardRepository.findById(id).orElseThrow(()-> {
            return new IllegalArgumentException("글 상세보기 실패! : 아이디를 찾을 수 없습니다.");
        });
    }

    @Transactional
    public void delete(int id){
        boardRepository.deleteById(id);
    }

    @Transactional
    public void boardUpdate(int id, Board requestBoard){
        Board board = boardRepository.findById(id).orElseThrow(()-> {return new IllegalArgumentException("글찾기 실패! : 아이디를 찾을 수 없습니다");});
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        // 해당 함수로 종료시 트랜잭션이 service 가 종료될 때 트랜잭션이 종료됨 -> 더티체킹이 발생한다 -> 자동으로 flush 가 된다
    }
}
