package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.ShelterDTO;
import kopo.poly.persistance.mapper.IShelterMapper;
import kopo.poly.service.IShelterService;
import kopo.poly.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShelterService implements IShelterService {

    private final IShelterMapper shelterMapper;

    @Override
    public void insertShelter() throws Exception {

        log.info(this.getClass().getName() + ".insertShelter 시작!");

        for (int i = 1; i < 13; i++) {

            log.info("for문 총 12번 중 " + i + "번째 실행!");

            String url = "https://apis.data.go.kr/1741000/EmergencyAssemblyArea_Earthquake2/getArea1List?serviceKey=QcSbF6LpTTP%2F%2BvelsigggEn7eKceTY3yqegazE4GU9BizeNBy8IQWtijUgtx3WfUkaB6J8qDK36b6AXgFHXn%2BA%3D%3D&pageNo=" + i + "&numOfRows=1000&type=json";
            log.info("url : " + url);

            String json = NetworkUtil.get(url);

            // 가져온 객체 JSON을 MAP형태로 변환
            Map<String, Object> rMap = new ObjectMapper().readValue(json, LinkedHashMap.class);

            List<Map<String, Object>> EarthquakeOutdoorsShelter = (List<Map<String, Object>>) rMap.get("EarthquakeOutdoorsShelter");

            List<Map<String, Object>> row = (List<Map<String, Object>>) EarthquakeOutdoorsShelter.get(1).get("row");

            for (Map<String, Object> shelterInfo : row) {

                // TODO 지진옥외대피소 API에서 위도 경도 거꾸로 제공함 ㅎ...
                String xcord = (String) shelterInfo.get("ycord"); // 위도
                String ycord = (String) shelterInfo.get("xcord"); // 경도
                String vt_acmdfclty_nm = (String) shelterInfo.get("vt_acmdfclty_nm"); // 대피소명
                String acmdfclty_sn = shelterInfo.get("acmdfclty_sn").toString(); // 시설일련번호

                ShelterDTO pDTO = new ShelterDTO();

                pDTO.setXcord(xcord);
                pDTO.setYcord(ycord);
                pDTO.setVtAcmdfcltyNm(vt_acmdfclty_nm);
                pDTO.setAcmdfcltySn(acmdfclty_sn);

                log.info("-----------------------------");
                log.info("위도 : " + pDTO.getXcord());
                log.info("경도 : " + pDTO.getYcord());
                log.info("시설명 : " + pDTO.getVtAcmdfcltyNm());
                log.info("시설일련번호 : " + pDTO.getAcmdfcltySn());

                shelterMapper.insertShelter(pDTO);

                pDTO = null;

            }

        }

        log.info(this.getClass().getName() + ".insertShelter 끝!");

    }

    @Override
    public List<ShelterDTO> getShelterList() throws Exception {

        log.info(this.getClass().getName() + ".getShelterList 시작!");

        log.info("대피소 정보 리스트를 조회합니다.");

        log.info(this.getClass().getName() + ".getShelterList 끝!");

        return shelterMapper.getShelterList();
    }

    @Override
    public ShelterDTO getShelter() throws Exception {

        log.info(this.getClass().getName() + ".getShelter 시작!");

        log.info("대피소 상세 정보를 조회합니다.");

        log.info(this.getClass().getName() + ".getShelter 끝!");

        return shelterMapper.getShelter();
    }
}
