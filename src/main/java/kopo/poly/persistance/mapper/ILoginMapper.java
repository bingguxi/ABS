package kopo.poly.persistance.mapper;

import kopo.poly.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ILoginMapper {

    // 로그인 시 아이디, 비밀번호 일치하는지 확인하기
    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    // 아래에 아이디, 비밀번호 찾기 이어서 작성 예정

}