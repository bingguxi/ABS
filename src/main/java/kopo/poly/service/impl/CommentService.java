package kopo.poly.service.impl;

import kopo.poly.dto.CommentDTO;
import kopo.poly.persistance.mapper.ICommentMapper;
import kopo.poly.service.ICommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService implements ICommentService {
    private final ICommentMapper commentMapper;

    @Override
    public List<CommentDTO> getCommentList() throws Exception {

        log.info(this.getClass().getName() + "getCommentList Start!!");

        return commentMapper.getCommentList();
    }

    @Override
    public void insertCommentInfo(CommentDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertCommentInfo Start!!");

        commentMapper.insertCommentInfo(pDTO);
    }

    @Override
    public void deleteCommentInfo(CommentDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteCommentInfo Start!!");

        commentMapper.deleteCommentInfo(pDTO);
    }

}
