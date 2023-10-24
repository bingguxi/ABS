package kopo.poly.service;


import kopo.poly.dto.UserInfoDTO;

public interface ILoginService {

    // 로그인 시 아이디, 비밀번호 일치하는지 확인하기
    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;
}
