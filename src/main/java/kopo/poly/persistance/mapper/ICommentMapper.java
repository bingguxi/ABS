package kopo.poly.persistance.mapper;

import kopo.poly.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICommentMapper {

    // 댓글 리스트
    List<CommentDTO> getCommentList() throws Exception;

    // 댓글 등록
    void insertCommentInfo(CommentDTO pDTO) throws Exception;

    // 댓글 삭제
    void deleteCommentInfo(CommentDTO pDTO) throws Exception;
}
