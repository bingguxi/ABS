package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.persistance.mapper.ISignupMapper;
import kopo.poly.service.ISignupService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class SignupService implements ISignupService {

    private final ISignupMapper signupMapper;
    private final MailService mailService;

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        UserInfoDTO rDTO = signupMapper.getUserIdExists(pDTO);

        log.info("아이디 중복 여부 : " + rDTO.getExistsYn());

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }

    @Override
    public UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getEmailExists Start!");

        UserInfoDTO rDTO = signupMapper.getEmailExists(pDTO);

        if (rDTO == null) {
            rDTO = new UserInfoDTO();
        } /* 근데 이건 왜 해주는거지? null 처리하려고? */

        String existsYn = CmmUtil.nvl(rDTO.getExistsYn());

        log.info("메일 중복 여부 : " + existsYn);

        if (existsYn.equals("N")) { // 만약 이메일이 DB에 존재하지 않으면,

            int authNumber = ThreadLocalRandom.current().nextInt(100000,1000000);

            log.info("랜덤으로 생성된 인증번호 : " + authNumber);

            MailDTO dto = new MailDTO();

            dto.setTitle("이메일 인증번호 발송 메일");
            dto.setContents("회원님의 인증번호는 " + authNumber + " 입니다.");
            dto.setToWho(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mailService.doSendMail(dto);

            dto = null;

            rDTO.setAuthNumber(authNumber);

        }

        log.info(this.getClass().getName() + ".getEmailExists End!");

        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        int res = 0;

        res = signupMapper.insertUserInfo(pDTO);

        log.info(this.getClass().getName() + ".insertUserInfo End!");

        return res;
    }

}
