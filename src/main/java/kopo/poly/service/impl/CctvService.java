package kopo.poly.service.impl;

import kopo.poly.service.ICctvService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CctvService implements ICctvService {

    @Value("${cctv.api.key}")
    private String apiKey; // application.properties 또는 application.yml 파일에서 API 키 설정

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
                    + "apiKey=" + apiKey
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
