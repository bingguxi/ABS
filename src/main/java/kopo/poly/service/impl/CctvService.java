package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.CctvResultDTO;
import kopo.poly.persistance.mapper.ICctvMapper;
import kopo.poly.service.ICctvService;
import kopo.poly.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CctvService implements ICctvService {

    private final ICctvMapper cctvMapper;

    @Value("${cctvResult.api.key}")
    private String apiKey;

    @Override
    public void insertCctvInfo() throws Exception {

        log.info(this.getClass().getName() + ".insertCctvInfo Start!!");

        log.info("CCTV 데이터 DB 삭제 시작");

        cctvMapper.deleteCctvInfo();

        String type = "ex";
        String cctvType = "1";
        String minX = "127.100000";
        String maxX = "128.890000";
        String minY = "34.100000";
        String maxY = "39.100000";
        String getType = "json";

        String apiParam = url + "?apiKey=" + apiKey + "&type=" + type + "&cctvType=" + cctvType + "&minX=" + minX + "&maxX=" + maxX + "&minY=" + minY + "&maxY=" + maxY + "&getType=" + getType;
        log.info("apiParam : " + apiParam);

        Map<String, String> headers = new HashMap<>();

        String json = NetworkUtil.get(apiParam, headers);

        Map<String, Object> rMap = new ObjectMapper().readValue(json, LinkedHashMap.class);

        // CCTV의 정보를 가지고 있는 data 키의 값 가져오기
        Map<String, Object> dMap = (Map<String, Object>) rMap.get("response");

        // 일별 날씨 조회(OpenAPI가 현재 날짜 기준으로 최대 7일까지 제공)
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) dMap.get("data");

        for (Map<String, Object> dataMap : dataList) {
            CctvResultDTO rDTO = new CctvResultDTO();

            // FIXME api json 키 값은 cctvname (카멜 케이스가 아닌데)
            //  dto에서는 카멜식임
            rDTO.setCoordX(String.valueOf(dataMap.get("coordx")));
            rDTO.setCoordY(String.valueOf(dataMap.get("coordy")));
            rDTO.setCctvName((String) dataMap.get("cctvname"));
            rDTO.setCctvUrl((String) dataMap.get("cctvurl"));

            cctvMapper.insertCctvInfo(rDTO);

        }

        log.info(this.getClass().getName() + ".insertCctvInfo End!!");

    }


    @Override
    public CctvResultDTO getCctvURL(CctvResultDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getCctvURL 시작!");

        CctvResultDTO rDTO = cctvMapper.getCctvURL(pDTO);

        log.info(this.getClass().getName() + ".getCctvURL 끝!");

        return rDTO;
    }

    @Override
    public List<CctvResultDTO> getCctv() throws Exception {

        log.info(this.getClass().getName() + ".getCctv Start!");
        log.info(this.getClass().getName() + ".getCctv End!");

        return cctvMapper.getCctv();
    }

}
