package kopo.poly.service;


import kopo.poly.dto.UserInfoDTO;

import java.util.HashMap;

public interface ILoginService {

    // 로그인 시 아이디, 비밀번호 일치하는지 확인하기
    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    // 카카오 로그인
    String getAccessToken(String authorize_code) throws Exception;
    HashMap<String, Object> getUserInfo (String access_Token) throws Exception;

    // 카카오 로그아웃
    void kakaoLogout(String access_Token) throws Exception;

    // 아이디, 비밀번호 찾기에 활용
    UserInfoDTO findIdOrPasswordProc(UserInfoDTO pDTO) throws Exception;

    // 임시 비밀번호 생성 후 메일 전송
    int pwCode(UserInfoDTO pDTO) throws Exception;
}
