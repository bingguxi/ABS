package kopo.poly.controller;

import kopo.poly.dto.SnowDTO;
import kopo.poly.dto.TyphoonLiveDTO;
import kopo.poly.service.ITyphoonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping(value = "/typhoon")
@RequiredArgsConstructor
@RestController
@Controller
public class TyphoonController {

    @Autowired
    private ITyphoonService typhoonService;

    /**
     * 태풍 실시간 데이터 호출 후 파싱을 거쳐서
     * DB에 있는 기존 데이터 삭제하고 저장하는 로직
     */
    @ResponseBody
    @GetMapping(value = "typhoonLiveInfo")
    public List<TyphoonLiveDTO> typhoonLiveInfo() throws Exception{

        log.info(this.getClass().getName() + ".typhoonLiveInfo Start!!");

        List<TyphoonLiveDTO> rList = typhoonService.getTyphoonLiveInfo();


        log.info("rList : ", rList);

        for (TyphoonLiveDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("0(분석), 1(예측) : " + rDTO.getFt());
            log.info("태풍번호 : " + rDTO.getTyp());
            log.info("발표번호 : " + rDTO.getSeq());
            log.info("태풍 분석시각 (UTC) : " + rDTO.getTypTm());
            log.info("위도 : " + rDTO.getLat());
            log.info("경도 : " + rDTO.getLon());
            log.info("태풍진행방향 (16방위기호) : " + rDTO.getDir());
            log.info("태풍진향속도 (km/h) : " + rDTO.getSp());
            log.info("태풍중심기압 (hpa) : " + rDTO.getPs());
            log.info("태풍최대풍속 (m/s) : " + rDTO.getWs());
            log.info("15m/s 반경 (km) : " + rDTO.getRad15());
            log.info("25m/s 반경 (km) : " + rDTO.getRad25());
            log.info("위치 : " + rDTO.getLoc());
        }

        log.info(this.getClass().getName() + ".typhoonLiveInfo End!!");

        return rList;

    }

    /**
     * 태풍 과거 API 호출 후 값 저장하는 로직
     */
    @GetMapping(value = "getTyphoon")
    public String getEarthquakeApi() throws Exception {
        log.info("test called");
        typhoonService.setTyphoonUrl();

        return "/TyphoonInfo";
    }
}
