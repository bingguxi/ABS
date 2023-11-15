package kopo.poly.controller;

import kopo.poly.dto.FireDTO;
import kopo.poly.service.IFireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/fire")
@Controller
public class FireController {

    private final IFireService fireService;

    @GetMapping(value = "fireInfo")
    public String getFireInfo(ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".getFireInfo 컨트롤러 시작!");

        // 산불 정보 수집 후, DB에 저장하는 로직 호출
        fireService.insertFireInfo();

        // 저장된 산불 정보 조회하기
        List<FireDTO> rList = Optional.ofNullable(fireService.getFireInfo()).orElseGet(ArrayList::new);

        // 조회 결과 HTML에 전달하기
        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".getFireInfo 컨트롤러 끝!");

        return "/fire/fireList";
    }

}
