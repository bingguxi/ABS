package kopo.poly.controller;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.ILoginService;
import kopo.poly.service.ISignupService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
