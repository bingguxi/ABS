package kopo.poly.controller;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.ILoginService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "login")
@Controller
public class LoginController {

    private final ILoginService loginService;

    @GetMapping("")
    public String login() {

        log.info(this.getClass().getName() + ".login Start!");
        log.info(this.getClass().getName() + ".login End!");

        return "/user/login";
    }

    @GetMapping(value = "/naverLogin")
    public String naverLogin() {

        log.info(this.getClass().getName() + ".naverLogin Loading Complete!");

        return "/user/naverLogin";
    }

    @GetMapping(value = "/naverLogin/callBack")
    public String naverCallBack() {

        log.info(this.getClass().getName() + ".naverCallBack Start!");
        log.info(this.getClass().getName() + ".naverCallBack End!");

        return "/user/naverLogin_callback";
    }

    @RequestMapping(value = "/kakaoLogin")
    public String kakaoLogin(@RequestParam("code") String code, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".kakaoLogin Start!");

        String access_Token = loginService.getAccessToken(code);
        HashMap<String, Object> userInfo = loginService.getUserInfo(access_Token);
        System.out.println("login Controller : " + userInfo);

        //    클라이언트의 이메일이 존재할 때 세션에 해당 이메일과 토큰 등록
        if (userInfo.get("email") != null) {
            // session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("SS_USER_ID", userInfo.get("email"));
            session.setAttribute("access_Token", access_Token);
        }

        log.info("code : " + code);
        log.info("access_Token : " + access_Token);
        log.info("userInfo.get(\"email\") : " + userInfo.get("email"));

        log.info(this.getClass().getName() + ".kakaoLogin End!");

        return "/index";
    }

    @PostMapping(value = "/loginProc")
    @ResponseBody
    public Map<String, Object> loginProc(HttpServletRequest request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        log.info(this.getClass().getName() + ".loginProc Start!");

        int res = 0;
        String msg = "";
        String url = "/index";

        UserInfoDTO pDTO = null;

        try {

            String userId = CmmUtil.nvl(request.getParameter("userId"));
            String password = CmmUtil.nvl(request.getParameter("password"));

            log.info("userId : " + userId);
            log.info("password : " + password);

            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            log.info("pDTO.setUserId(userId) : " + pDTO.getUserId());
            log.info("pDTO.setPassword(password) : " + pDTO.getPassword());

            UserInfoDTO rDTO = loginService.getLogin(pDTO);

            log.info("rDTO.getUserId() : " + rDTO.getUserId());

            if (CmmUtil.nvl(rDTO.getUserId()).length() > 0) {

                res = 1;

                log.info("SS_USER_ID : " + userId);
                session.setAttribute("SS_USER_ID", userId);
                log.info("세션에 저장 후 session.getAttribute(\"SS_USER_ID\") : " + session.getAttribute("SS_USER_ID"));

                msg = "로그인 성공! " + rDTO.getUserName() + "님 환영합니다.";
                url = "/index";

            } else {

                msg = "아이디와 비밀번호가 올바르지 않습니다.";
            }
        } catch (Exception e) {

            msg = "시스템 문제로 로그인이 실패했습니다.";
            log.info(e.toString());
            e.printStackTrace();

        }

        response.put("result", res);
        response.put("msg", msg);
        response.put("url", url);

        log.info(this.getClass().getName() + ".loginProc End!");

        return response;
    }




    @RequestMapping(value="/logout")
    public String logout(HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".logout Start!");

        // 만약 access_Token이 존재하면, 카카오 로그아웃 메소드 호출하기!
        if (session.getAttribute("access_Token") != null) {

            // 카카오 로그아웃 메소드 호출
            loginService.kakaoLogout((String) session.getAttribute("access_Token"));

            // 세션에 있는 접근토큰 삭제하기!
            session.removeAttribute("access_Token");
        }

        // 세션에 있는 유저아이디 삭제하기!
        session.removeAttribute("SS_USER_ID");

        log.info("세션 삭제 후 session.getAttribute(\"SS_USER_ID\") : " + session.getAttribute("SS_USER_ID"));

        log.info(this.getClass().getName() + ".logout End!");

        return "/user/login";
    }


}
