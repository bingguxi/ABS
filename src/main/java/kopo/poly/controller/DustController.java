package kopo.poly.controller;

import kopo.poly.dto.DustDTO;
import kopo.poly.service.IDustService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping(value = "/dust")
@RequiredArgsConstructor
@RestController
public class DustController {

    private final IDustService dustService;

    @ResponseBody
    @GetMapping(value = "dustInfo")
    public List<DustDTO> dustInfo() throws Exception {

        log.info(this.getClass().getName() + ".dustInfo Start!!");

        List<DustDTO> rList = dustService.getDustInfo();

        log.info("rList : " + rList);

        for (DustDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("국내 지점번호 : " + rDTO.getStnId());
            log.info("황사 시간평균값 : " + rDTO.getMean());
        }

        log.info(this.getClass().getName() + ".dustInfo End!!");

        return rList;
    }
}
