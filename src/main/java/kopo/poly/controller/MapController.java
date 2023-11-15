package kopo.poly.controller;

import com.google.gson.Gson;
import kopo.poly.dto.MapApiDTO;
import kopo.poly.service.IMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
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


    @GetMapping(value = "shelterMap")
    // @ResponseBody
    public String shelterMap(ModelMap model)throws Exception {

        log.info("api 파싱 시작!");

        List<MapApiDTO> resultList = mapService.shelterMap();

        log.info("api 파싱 끝!");

        // 대피소 정보를 JavaScript 객체로 변환하여 리스트에 추가
        List<String> positions = new ArrayList<>();

        for (MapApiDTO rDTO : resultList) {

            String position = "{title: " + rDTO.getShel_nm() + ", lat: " + rDTO.getLat() + ", lon: " + rDTO.getLon() + "}";
            positions.add(position);

            log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
            log.info("대피소 장소명 : " + rDTO.getShel_nm());
            log.info("위도 : " + rDTO.getLat());
            log.info("경도 : " + rDTO.getLon());
        }

        log.info("positions : " + positions);

        // positions 리스트를 JSON 형태로 변환하여 모델에 추가
        Gson gson = new Gson();
        String jsonPositions = gson.toJson(positions);

        log.info("jsonPositions : " + jsonPositions);

        model.addAttribute("positions", jsonPositions);

        // return resultList;
         return "/map/shelterMap";
        // return "/map/shelterMap_test";
    }

    @GetMapping(value = "")
    public String map() {

        log.info(this.getClass().getName() + ".map Start!");
        log.info(this.getClass().getName() + ".map End!");

        return "/map/map";
    }

}
