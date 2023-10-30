package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.MapApiDTO;
import kopo.poly.service.IMapService;
import kopo.poly.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MapService implements IMapService {


    // 최상단에 넣기
    // 추후에 관리를 용이하게 하기위해서 properties 에서 가져와도됨
    private final String apiUrl = "http://223.130.129.189:9191/getTsunamiShelter1List/numOfRows=999&pageNo=1&type=json";
    @Override
    public List<MapApiDTO> shelterMap() throws Exception {

        log.info(getClass().getName() + "지진 해일 대피장소 파싱 시작!!");


        // 원래는 URL 객체를 만들어줘서 URL을 생성해주어야 하지만 이미 완성된 URL이므로 그냥 바로 API 정보 가쟈오기
        String json = NetworkUtil.get(apiUrl);
//        log.info( "통신한 JSON 결과물" + json);

        // 가져온 객체 JSON => MAP형태로 변환
        Map<String, Object> rMap = new ObjectMapper().readValue(json, LinkedHashMap.class);

        // 파싱된 결과물을 JSON View 로 볼 시에 JsonArray 형태로 TsunamiShelter 이름의 객체가 있음
        // 그래서 jsonArray == List에 담기
        List<Map<String, Object>>  TsunamiShelter= (List<Map<String, Object>>) rMap.get("TsunamiShelter");

        // TsunamiShelter객체의 두번쨰 객체니까 순번으로 1번에서 row명으로된 것 가져오기
        List<Map<String, Object>>  row = (List<Map<String, Object>>) TsunamiShelter.get(1).get("row");



        // 변수에 담을 List
        List<MapApiDTO> resultList = new ArrayList<>();

        for (Map<String, Object> resultMap : row) {

            // 마지막 추출하기
            Double lat = (Double) resultMap.get("lat"); // 위도 가져오기
            Double lon = (Double) resultMap.get("lon"); // 경도  가져오기 + 타입 맞춰주기
            String shel_nm =(String) resultMap.get("shel_nm");

            //DTO 에 담기
            MapApiDTO rDTO = new MapApiDTO();
            rDTO.setLat(lat.toString()); // 사
            rDTO.setLon(lon.toString());
            rDTO.setShel_nm(shel_nm);
            resultList.add(rDTO);

            rDTO = null;

        }


        log.info(getClass().getName() + "지진 해일 대피장소 파싱 끝");
        return resultList;
    }


}
