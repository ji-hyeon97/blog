package cos.blog.service;

import cos.blog.model.Board;
import cos.blog.model.User;
import cos.blog.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Page<Board> list(Pageable pageable){
        return boardRepository.findAll(pageable);
    }
}
