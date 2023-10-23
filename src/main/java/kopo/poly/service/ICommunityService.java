package kopo.poly.service;

import kopo.poly.dto.CommunityDTO;

import java.util.List;

public interface ICommunityService {

    /**
     * 커뮤니티 리스트
     * @return 조회결과
     */
    List<CommunityDTO> getCommunityList() throws Exception;
    /**
     *  커뮤니티 상세 보기
     *  @param pDTO 상세내용 조회할 communitySeq 값
     *  @param type 조회수 증가여부(수정보기는 조회수 증가하지 않음)
     *  @return 조회결과
     */

    CommunityDTO getCommunityInfo(CommunityDTO pDTO, boolean type) throws Exception;
    /**
     * 커뮤니티 등록
     * @param pDTO 화면에서 입력된 커뮤니티 입력된 값들
     */
    void insertCommunityInfo(CommunityDTO pDTO) throws Exception;

    /**
     * 커뮤니티 수정
     * @param pDTO 화면에서 입력된 수정되기 위한 커뮤니티 입력된 값들
     */
    void updateCommunityInfo(CommunityDTO pDTO) throws Exception;

    /**
     * 공지사항 삭제
     * @param pDTO 삭제할 communitySeq 값
     * */
    void deleteCommunityInfo(CommunityDTO pDTO) throws Exception;

}
