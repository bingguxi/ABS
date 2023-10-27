package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeDTO {

    // 공지사항을 아예 페이지를 분리해서 공지글 작성자 이름을 삭제함 ( 테이블도 )

    private String noticeSeq; // 기본키, 순번
    private String title; // 제목
    private String contents; // 글 내용
    private String userId; // 작성자
    private String readCnt; // 조회수
    private String regId; // 등록자 아이디
    private String regDt; // 등록일
    private String chgId; // 수정자 아이디
    private String chgDt; // 수정일

}
