package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {

    private String commentSeq; // 기본키, 댓글 순번
    private String contents; // 댓글 내용
    private String userId; // 작성자
    private String regDt; // 작성일
    private String communitySeq; // 외래키, 순번
}
