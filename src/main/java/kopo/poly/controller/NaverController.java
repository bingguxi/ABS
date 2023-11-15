package kopo.poly.controller;

import kopo.poly.dto.NaverDTO;
import kopo.poly.dto.TokenDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.INaverService;
import kopo.poly.service.ISignupService;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NaverController {

    private final INaverService naverService;
    private final ISignupService signupService;

    @Value("${naver.client_id}")
    private String naverClientId;
    @Value("${naver.client_secret}")
    private String naverClientSecret;
    @Value("${naver.redirect_uri}")
    private String naverRedirectUri;

/* 네이버 로그인 엑세스 토큰 받기 */

    @GetMapping(value = "/auth/naver/callback")
    public String naverCallback(String code, HttpSession session, ModelMap modelMap) throws Exception {

        log.info(".controller 네이버 회원가입 및 로그인 실행");

        log.info("콜백 컨트롤러 들어와서 매개변수로 받은 code 확인! : " + code);

        String msg = "";
        String url = "";
        int res; // 회원 가입 결과 /// 1 성공, 2 이미 가입

        TokenDTO tokenDTO = naverService.getAccessToken(code);

        log.info("네이버 엑세스 토큰 : " + tokenDTO.getAccess_token());

        NaverDTO naverDTO = naverService.getNaverUserInfo(tokenDTO);

        String userId = "naver_" + naverDTO.getResponse().getId();
        String userName = naverDTO.getResponse().getName();
        String nickname = naverDTO.getResponse().getNickname();

        log.info("네이버 아이디 : " + naverDTO.getResponse().getId());

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);


        UserInfoDTO rDTO = naverService.getUserInfoById(pDTO);

        // 첫 로그인시 회원가입 로직 실행
        if (rDTO == null) {

            String email = naverDTO.getResponse().getEmail();
            String tel = naverDTO.getResponse().getMobile();
            String birth = naverDTO.getResponse().getBirthyear() + "-" + naverDTO.getResponse().getBirthday();
            String gender = naverDTO.getResponse().getGender();
            String addr1 = "a1";
            String addr2 = "a2";

            if (Objects.equals(gender, "F")) {
                gender = "female";

            } else if (Objects.equals(gender, "M")) {
                gender = "male";

            }

            log.info("네이버 아이디(문자) : " + userId);
            log.info("네이버 이름 : " + userName);
            log.info("네이버 닉네임 : " + nickname);
            log.info("네이버 이메일 : " + email);
            log.info("네이버 폰번호 : " + tel);
            log.info("네이버 생일 : " + birth);
            log.info("네이버 성별 : " + gender);

            pDTO.setUserId(userId);
            pDTO.setUserName(userName);
            pDTO.setNickname(nickname);
            pDTO.setPassword(EncryptUtil.encHashSHA256(userId)); // 오 유저아이디를 해시로 암호화 해서 비밀번호로 저장하는구나
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            pDTO.setTel(tel);
            pDTO.setBirth(birth);
            pDTO.setGender(gender);
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            res = signupService.insertUserInfo(pDTO);

            if (res == 1) {
                log.info("회원가입 성공");

                session.setAttribute("SS_USER_ID", userId);

                msg = "회원가입에 성공했습니다. \\n로그인 성공했습니다 \\n" + userName + "님 환영합니다.";
                url = "/index";

            } else {
                log.info("회원가입 실패");

            }

        } else {
            log.info("계정 보유로 로그인 실행");

            session.setAttribute("SS_USER_ID", userId);

            msg = "로그인 성공했습니다 \\n" + userName + "님 환영합니다.";
            url = "/index";

        }

        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("url", url);

        log.info(".controller 네이버 회원가입 및 로그인 종료");

        return "/redirect";
    }


}
