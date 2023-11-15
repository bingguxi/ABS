package kopo.poly.controller;

import kopo.poly.dto.CctvResultDTO;
import kopo.poly.service.ICctvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("cctv")
@RestController
public class CctvController {

//    @Autowired
    private final ICctvService cctvService;

    @GetMapping("/findCctv")
    public String findCctv(Model model) {

        log.info(this.getClass().getName() + ".findCctv Start!");

        double lat = 36.58629; // 고정값 (임의)
        double lng = 128.186793; // 고정값 (임의)

        String cctvData = cctvService.getCctvData(lat, lng);

        model.addAttribute("cctvData", cctvData);

        log.info(this.getClass().getName() + ".findCctv End!");

        return "cctvResult";
    }

    @ResponseBody
    @GetMapping("getCctv")
    public List<CctvResultDTO> getCctv() throws Exception {

        log.info(this.getClass().getName() + ".getCctv Start!!");


        List<CctvResultDTO> rList = cctvService.getCctv();

        log.info("rList : ", rList);

        for (CctvResultDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("날짜 : " + rDTO.getCctvName());
            log.info("국내 지점번호 : " + rDTO.getCctvUrl());
            log.info("지점명 : " + rDTO.getCoordX());
            log.info("경도 : " + rDTO.getCoordY());
        }


        return rList;
    }
}