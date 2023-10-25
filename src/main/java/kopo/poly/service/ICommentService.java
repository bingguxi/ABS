package kopo.poly.service;

import kopo.poly.dto.CommentDTO;
import kopo.poly.dto.CommunityDTO;

import javax.xml.stream.events.Comment;
import java.util.List;

public interface ICommentService {

    /**댓글 리스트*/

    List<CommentDTO> getCommentList() throws Exception;

    /**댓글 등록*/
    void insertCommentInfo(CommentDTO pDTO) throws Exception;

}
