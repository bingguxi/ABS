package kopo.poly.service.impl;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.persistance.mapper.ILoginMapper;
import kopo.poly.persistance.mapper.ISignupMapper;
import kopo.poly.service.ILoginService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService implements ILoginService {

    private final ILoginMapper loginMapper;

    @Override
    public UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getLogin Start!");

        UserInfoDTO rDTO = Optional.ofNullable(loginMapper.getLogin(pDTO)).orElseGet(UserInfoDTO::new);

        log.info("rDTO.toString() : " + rDTO.toString());

        if (CmmUtil.nvl(rDTO.getUserId()).length() > 0) {

            log.info("로그인 성공");

        } else {
            
            log.info("로그인 안 성공");
        }

        log.info(this.getClass().getName() + ".getLogin End!");

        return rDTO;
    }
}
