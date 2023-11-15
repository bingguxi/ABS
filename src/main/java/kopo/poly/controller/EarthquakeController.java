package kopo.poly.controller;

import kopo.poly.dto.EarthquakeLiveDTO;
import kopo.poly.dto.TyphoonLiveDTO;
import kopo.poly.service.IEarthquakeService;
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
@RequestMapping(value = "/earthquake")
@RequiredArgsConstructor
@RestController
@Controller
public class EarthquakeController {
    @Autowired
    private IEarthquakeService earthquakeService;

    /**
     * 태풍 실시간 데이터 호출 후 파싱을 거쳐서
     * DB에 있는 기존 데이터 삭제하고 저장하는 로직
     */
    @ResponseBody
    @GetMapping(value = "earthquakeLiveInfo")
    public List<EarthquakeLiveDTO> earthquakeLiveInfo() throws Exception{

        log.info(this.getClass().getName() + ".earthquakeLiveInfo Start!!");

        List<EarthquakeLiveDTO> rList = earthquakeService.getEarthquakeLiveInfo();


        log.info("rList : ", rList);

        for (EarthquakeLiveDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("3(국내지진통보), 2(국외지진정보) : " + rDTO.getTp());
            log.info("발표시간 : " + rDTO.getTmFc());
            log.info("발표일련번호 : " + rDTO.getSeq());
            log.info("진앙시(년월일시분초) : " + rDTO.getTmEqkMsc());
            log.info("규모 : " + rDTO.getMt());
            log.info("진앙 위도 (deg.) : " + rDTO.getLat());
            log.info("진앙 경도 (deg.) : " + rDTO.getLon());
            log.info("진앙 위치 : " + rDTO.getLoc());
            log.info("진도 : " + rDTO.getScale());
            log.info("참고사항 : " + rDTO.getRem());
            log.info("수정사항 : " + rDTO.getCor());
        }

        log.info(this.getClass().getName() + ".earthquakeLiveInfo End!!");

        return rList;

    }

    /**
     * 지진 과거 데이터 API 호출해서 DB에 저장하는 로직
     */
    @GetMapping(value = "getEarthquake")
    public String getEarthquakeApi() throws Exception {
        log.info("test called");
        earthquakeService.setEarthquakeUrl();

        return "/erathquakeInfo";
    }
}
