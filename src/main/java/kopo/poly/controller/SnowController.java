package kopo.poly.controller;

import kopo.poly.dto.SnowDTO;
import kopo.poly.service.ISnowService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/snow")
@RequiredArgsConstructor
@RestController
public class SnowController {

    private final ISnowService snowService;

    @ResponseBody
    @GetMapping(value = "snowInfo")
    public List<SnowDTO> snowInfo() throws Exception{

        log.info(this.getClass().getName() + ".snowInfo Start!!");

        List<SnowDTO> rList = snowService.getSnowInfo();


        log.info("rList : ", rList);

        for (SnowDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("날짜 : " + rDTO.getDt());
            log.info("국내 지점번호 : " + rDTO.getStnId());
            log.info("지점명 : " + rDTO.getStnKo());
            log.info("경도 : " + rDTO.getLon());
            log.info("위도 : " + rDTO.getLat());
            log.info("적설값 : " + rDTO.getSd());
        }

        log.info(this.getClass().getName() + ".snowInfo End!!");

        return rList;

    }

}
