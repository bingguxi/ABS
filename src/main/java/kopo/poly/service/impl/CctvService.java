package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.CctvDTO;
import kopo.poly.dto.CctvResultDTO;
import kopo.poly.persistance.mapper.ICctvMapper;
import kopo.poly.service.ICctvService;
import kopo.poly.util.CmmUtil;
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
public class CctvService implements ICctvService {

    private final ICctvMapper cctvMapper;

    @Value("${cctv.api.key}")
    private String apiKey1; // application.properties 또는 application.yml 파일에서 API 키 설정


    @Value("${cctvResult.api.key}")
    private String apiKey;

    @Override
    public List<CctvResultDTO> getCctv() throws Exception {

        log.info(this.getClass().getName() + ".getCctv Start!!");

        CctvDTO pDTO = new CctvDTO();


        String type = CmmUtil.nvl(pDTO.getType());
        String cctvType = CmmUtil.nvl(pDTO.getCctvType());
        String minX = CmmUtil.nvl(pDTO.getMinX());
        String maxX = CmmUtil.nvl(pDTO.getMaxX());
        String minY = CmmUtil.nvl(pDTO.getMinY());
        String maxY = CmmUtil.nvl(pDTO.getMaxY());
        String getType = CmmUtil.nvl(pDTO.getGetType());

        String apiParam = url + "?apiKey=" + apiKey + "&type=" + type + "&cctvType=" + cctvType + "&minX=" + minX + "&maxX=" + maxX + "&minY=" + minY + "&maxY=" + maxY + "&getType=" + getType;
        log.info("apiParam : " + apiParam);

        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "json");

        String json = NetworkUtil.get(apiParam, headers);
//        log.info("json" + json);


        Map<String, Object> rMap = new ObjectMapper().readValue(json, LinkedHashMap.class);

        // CCTV의 정보를 가지고 있는 data 키의 값 가져오기
        Map<String, Object> dMap = (Map<String, Object>) rMap.get("response");

        // 일별 날씨 조회(OpenAPI가 현재 날짜 기준으로 최대 7일까지 제공)
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) dMap.get("data");
//        log.info("dataList.size() : " + dataList.size());

        // OpenAPI로부터 필요한 정보만 가져와서, 처리하기 쉬운 JSON 구조로 변경에 활용
        List<CctvResultDTO> pList = new LinkedList<>();

        for (Map<String, Object> dataMap : dataList) {
            CctvResultDTO rDTO = new CctvResultDTO();

            rDTO.setCoordX(
                    (Number) dataMap.get("coordx")
            );
            rDTO.setCoordY(
                    (Number) dataMap.get("coordy")
            );
            // FIXME api json 키 값은 cctvname (카멜 케이스가 아닌데)
            //  dto에서는 카멜식임
            rDTO.setCctvName(
                    (String) dataMap.get("cctvname")
            );
            rDTO.setCctvUrl(
                    (String) dataMap.get("cctvurl")
            );

//            log.info("rDTO : " + rDTO.toString());

            cctvMapper.insertCctvInfo(rDTO);

            // TODO 얘들이 정상적인 dto인지 확인하고 넣으면 좀더 좋은
            pList.add(rDTO);
        }
        List<CctvResultDTO> rList = cctvMapper.getCctv();

        log.info(this.getClass().getName() + ".getCctv End!!");

        return rList;
    }

    // CCTV 정보 가져오기
    @Override
    public String getCctvData(double lat, double lng) {
        try {
            // CCTV 탐색 범위 지정을 위해 임의로 ±1 만큼 가감
            double minX = lng - 1;
            double maxX = lng + 1;
            double minY = lat - 1;
            double maxY = lat + 1;

            // API 호출 URL 생성
            String apiUrl = "https://openapi.its.go.kr:9443/cctvInfo?"
                    + "apiKey1=" + apiKey1
                    + "&type=ex&cctvType=2"
                    + "&minX=" + minX
                    + "&maxX=" + maxX
                    + "&minY=" + minY
                    + "&maxY=" + maxY
                    + "&getType=json";

            // RestTemplate을 사용하여 API 호출
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return "API 호출에 실패했습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();

            return "API 호출 중 오류가 발생했습니다.";
        }
    }
}
