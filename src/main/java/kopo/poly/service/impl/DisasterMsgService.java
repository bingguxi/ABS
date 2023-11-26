package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.CctvResultDTO;
import kopo.poly.dto.DisasterMsgResultDTO;
import kopo.poly.persistance.mapper.ICctvMapper;
import kopo.poly.persistance.mapper.IDisasterMsgMapper;
import kopo.poly.service.IDisasterMsgService;
import kopo.poly.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class DisasterMsgService implements IDisasterMsgService {

    private final IDisasterMsgMapper disasterMsgMapper;

    @Value("${data.api.key}")
    private String apiKey; // application.properties 또는 application.yml 파일에서 API 키 설정

    @Override
    public List<DisasterMsgResultDTO> getDisasterMsg() throws Exception {

        log.info(this.getClass().getName() + ".getDisasterMsg Start!!");

        log.info("DB 삭제 시작");

        disasterMsgMapper.deleteDisasterMsgInfo();

        String pageNo = "1";
        String numOfRows = "100";
        String type = "json";

        String apiParam = apiURL + "?serviceKey=" + apiKey + "&pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&type=" + type;
        log.info("apiParam : " + apiParam);

        Map<String, String> head = new HashMap<>();

        String json = NetworkUtil.get(apiParam, head);

        log.info("json : " + json);

        Map<String, Object> rMap = new ObjectMapper().readValue(json, LinkedHashMap.class);

        // OpenAPI로부터 필요한 정보만 가져와서, 처리하기 쉬운 JSON 구조로 변경에 활용
        List<DisasterMsgResultDTO> pList = new LinkedList<>();

// 수정: "DisasterMsg" 내의 "row"에 접근
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) rMap.get("DisasterMsg");
        if (dataList != null && !dataList.isEmpty()) {
            for (Map<String, Object> dataMap : dataList) {
                // 수정: "row"에 접근
                List<Map<String, Object>> rowList = (List<Map<String, Object>>) dataMap.get("row");
                if (rowList != null && !rowList.isEmpty()) {
                    for (Map<String, Object> rowMap : rowList) {
                        DisasterMsgResultDTO rDTO = new DisasterMsgResultDTO();

                        // FIXME api json 키 값은 cctvname (카멜 케이스가 아닌데)
                        //  dto에서는 카멜식임
                        rDTO.setCreateDate(String.valueOf(rowMap.get("create_date")));
                        rDTO.setLocationId(String.valueOf(rowMap.get("location_id")));
                        rDTO.setLocationName((String) rowMap.get("location_name"));
                        rDTO.setMd101Sn((String) rowMap.get("md101_sn"));
                        rDTO.setMsg((String) rowMap.get("msg"));
                        rDTO.setSendPlatform((String) rowMap.get("send_platform"));

                        disasterMsgMapper.insertDisasterMsgInfo(rDTO);

                        pList.add(rDTO);
                    }
                }
            }
        }
        List<DisasterMsgResultDTO> rList = disasterMsgMapper.getDisasterMsg();

        log.info(this.getClass().getName() + ".getDisasterMsg End!!");

        return rList;
    }

    // CCTV 정보 가져오기
//    @Override
//    public String getCctvData(double lat, double lng) {
//        try {
//            // CCTV 탐색 범위 지정을 위해 임의로 ±1 만큼 가감
//            double minX = lng - 1;
//            double maxX = lng + 1;
//            double minY = lat - 1;
//            double maxY = lat + 1;
//
//            // API 호출 URL 생성
//            String apiUrl = "https://openapi.its.go.kr:9443/cctvInfo?"
//                    + "apiKey1=" + apiKey1
//                    + "&type=ex&cctvType=2"
//                    + "&minX=" + minX
//                    + "&maxX=" + maxX
//                    + "&minY=" + minY
//                    + "&maxY=" + maxY
//                    + "&getType=json";
//
//            // RestTemplate을 사용하여 API 호출
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
//
//            if (response.getStatusCode().is2xxSuccessful()) {
//                return response.getBody();
//            } else {
//                return "API 호출에 실패했습니다.";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            return "API 호출 중 오류가 발생했습니다.";
//        }
//    }
}
