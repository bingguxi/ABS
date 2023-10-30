package kopo.poly.controller;

import kopo.poly.dto.MapApiDTO;
import kopo.poly.service.IMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "map")
public class MapController {

    private final IMapService mapService;

    @GetMapping(value = "/getCurrentPosition")
    public String getCurrentPosition() {

        log.info(this.getClass().getName() + ".getCurrentPosition Start!");
        log.info(this.getClass().getName() + ".getCurrentPosition End!");

        return "getCurrentPosition";
    }

    @GetMapping(value = "/watchPosition")
    public String watchPosition() {

        log.info(this.getClass().getName() + ".watchPosition Start!");
        log.info(this.getClass().getName() + ".watchPosition End!");

        return "watchPosition";
    }


    @GetMapping(value = "apiPassing")
    @ResponseBody
    public List<MapApiDTO> apiPassing()throws Exception {
        log.info("api 파싱 시작");

        List<MapApiDTO> resultList = mapService.shelterMap();
        log.info("api 파싱 끝");


        for (MapApiDTO rDTO : resultList) {
            log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
            log.info("대피소 장소명 : " + rDTO.getShel_nm());
            log.info("위도 : " + rDTO.getLat());
            log.info("경도 : " + rDTO.getLon());
        }

        return resultList;
    }

    @GetMapping(value = "")
    public String map() {

        log.info(this.getClass().getName() + ".map Start!");
        log.info(this.getClass().getName() + ".map End!");

        return "/map/map";
    }

}
