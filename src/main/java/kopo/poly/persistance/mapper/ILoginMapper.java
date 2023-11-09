package kopo.poly.persistance.mapper;

import kopo.poly.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ILoginMapper {

    // 로그인 시 아이디, 비밀번호 일치하는지 확인하기
    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    // 네이버 로그인 시 아이디만 받아서 DB 조회하기
    UserInfoDTO getUserInfoById(UserInfoDTO pDTO) throws Exception;

    // 아이디 찾기에 활용
    UserInfoDTO getUserId(UserInfoDTO pDTO) throws Exception;

    // 비밀번호 찾기시 회원정보 유무여부 확인하기
    UserInfoDTO getUserExists(UserInfoDTO pDTO) throws Exception;

    // 임시 비밀번호로 비밀번호 재설정
    void updatePassword(UserInfoDTO pDTO) throws Exception;

}