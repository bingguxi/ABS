package kopo.poly.controller;

import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.ISignupService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "signup")
@Controller
public class SignupController {

    private final ISignupService signupService;

    @GetMapping(value = "")
    public String signup() {

        log.info(this.getClass().getName() + ".signup Start!");
        log.info(this.getClass().getName() + ".signup End!");

        return "/user/signup";
    }

    @ResponseBody
    @PostMapping(value = "/getUserIdExists")
    public UserInfoDTO getUserIdExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId")); // 회원아이디

        log.info("userId : " + userId);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        // 회원아이디를 통해 중복된 아이디인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(signupService.getUserIdExists(pDTO)
        ).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "/getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getEmailExists Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 회원 아이디

        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        // 입력된 이메일이 중복된 이메일인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(signupService.getEmailExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".getEmailExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "/signupProc")
    public MsgDTO signupProc(HttpServletRequest request, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".signupProc start!");

        int res;
        String msg = "";
        String url = "";

        UserInfoDTO pDTO = null;
        MsgDTO dto = null;

        try {
            String userId = CmmUtil.nvl(request.getParameter("userId")); // 아이디
            String userName = CmmUtil.nvl(request.getParameter("userName")); // 이름
            String password = CmmUtil.nvl(request.getParameter("password")); // 비밀번호
            String email = CmmUtil.nvl(request.getParameter("email")); // 이메일
            String addr1 = CmmUtil.nvl(request.getParameter("addr1")); // 주소
            String addr2 = CmmUtil.nvl(request.getParameter("addr2")); // 상세주소

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);


            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);
            pDTO.setUserName(userName);
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            res = signupService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {

                dto = new MsgDTO();

                dto.setMsg("회원가입 완료! 로그인 후 서비스를 이용하실 수 있습니다.");
                dto.setResult(1);

            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다. 다시 시도해주세요.";
            }

        } catch (Exception e) {
            // 저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        }

        log.info(this.getClass().getName() + ".signupProc End!");

        return dto;
    }

}
