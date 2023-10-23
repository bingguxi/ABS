package kopo.poly.persistance.mapper;

import kopo.poly.dto.CommunityDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICommunityMapper {

    // 커뮤니티 리스트
    List<CommunityDTO> getCommunityList() throws Exception;

    // 커뮤니티 글 등록
    void insertCommunityInfo(CommunityDTO pDTO) throws Exception;

    // 커뮤니티 상세보기
    CommunityDTO getCommunityInfo(CommunityDTO pDTO) throws Exception;

    // 커뮤니티 조회수 업데이트
    void updateCommunityReadCnt(CommunityDTO pDTO) throws Exception;

    // 커뮤니티 글 수정
    void updateCommunityInfo(CommunityDTO pDTO) throws Exception;

    // 커뮤니티 글 삭제
    void deleteCommunityInfo(CommunityDTO pDTO) throws Exception;
}
