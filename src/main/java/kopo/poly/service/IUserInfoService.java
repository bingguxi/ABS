package kopo.poly.service;


import kopo.poly.dto.UserInfoDTO;

public interface IUserInfoService {

    // 아이디 중복 체크하기
    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    // 이메일 중복 체크하기
    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;

    // 회원 가입하기
    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    // 로그인 시 아이디, 비밀번호 일치하는지 확인하기
    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;
}
