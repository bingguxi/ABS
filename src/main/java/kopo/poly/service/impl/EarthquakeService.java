package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import kopo.poly.dto.EarthquakeLiveDTO;
import kopo.poly.dto.EarthquakeResultDTO;
import kopo.poly.persistance.mapper.IEarthquakeMapper;
import kopo.poly.service.IEarthquakeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EarthquakeService implements IEarthquakeService {

    private final IEarthquakeMapper earthquakeMapper;

    @Value("${bhg.api.key}")
    private String apiKey;

    /**
     * 지진 과거 API 접근을 위한 URL 정보 생성 로직
     */
    @Override
    public void setEarthquakeUrl() throws Exception {

        log.info(this.getClass().getName() + ".setEarthquakeUrl Start !");

        // 현재 시간을 가져옵니다.
        Date currentDate = new Date();

        // 날짜 및 시간 형식을 설정합니다.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        // 현재 시간을 원하는 형식으로 변환합니다.
        String formattedDate = dateFormat.format(currentDate);

        log.info("현재 시각 : " + formattedDate);

        // 과거 지진데이터가 있는 기준일로부터 현재 시각까지의 데이터를 수집하기 위한 URL 정보 생성
        String frDate = "20180101";
        String laDate = formattedDate;
        String cntDiv = "Y";
        String orderTy = "xml";

        int i = 0;  // 수정된 부분: 무한루프를 돌지 않도록 조건 수정
        while (i < 1) {
            log.info("현재 i : " + i);

            String apiParam = "?frDate=" + frDate + "&laDate=" + laDate + "&cntDiv=" + cntDiv + "&orderTy=" + orderTy + "&authKey=" + apiKey;
            log.info("apiParam : " + apiParam);

            // 수정된 부분: API 호출과 결과 처리를 별도의 메서드로 분리
            getEarthquakeInfo(apiParam);

            i++;
        }
        log.info(this.getClass().getName() + ".setEarthquakeUrl End !");
    }

    /**
     * 위에서 받은 URL 정보를 가지고 지진 과거 API 호출 후
     * XML 파싱 후 DB에 담는 로직
     */
    @Override
    public void getEarthquakeInfo(String apiParam) {
        try {
            // URL 객체를 생성하고 웹 주소를 넣습니다.
            URL url = new URL(IEarthquakeService.apiURL + apiParam);
            log.info(url.toString());

            // HttpURLConnection 객체를 생성합니다.
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 요청 방식을 GET으로 설정합니다.
            urlConnection.setRequestMethod("GET");
            log.info("url 요청 전달 완료");

            // BufferedReader 객체를 생성하고 HttpURLConnection의 InputStream을 넣습니다.
            try (InputStream inputStream = urlConnection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                StringBuilder response = new StringBuilder();

                // 서버로부터 받은 데이터를 줄단위로 읽습니다.
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // API 응답 결과를 문자열로 변환
                String resultXml = response.toString();
                log.info("API 호출 결과 (시작태그는 <alert>): " + resultXml);

                // XmlMapper를 사용하여 XML을 Java 객체로 변환
                XmlMapper xmlMapper = new XmlMapper();

//                List <LinkedHashMap<String, LinkedHashMap<String, String>>> resultData = xmlMapper.readValue(resultXml, typeRef);

                // TypeReference를 사용하여 제네릭 타입 지정
                TypeReference<List<Object>> typeRef = new TypeReference<List<Object>>() {};
                List<Object> resultData = xmlMapper.readValue(resultXml, typeRef);
                log.info("rd : " + resultData);

                List<Map<String, String>> dataList = new ArrayList<>();

                // 결과 데이터를 가공하여 필요한 정보 추출
                for (Object item : resultData) {
                    if (item instanceof LinkedHashMap) {
                        LinkedHashMap<String, Object> itemMap = (LinkedHashMap<String, Object>) item;

                        for (Map.Entry<String, Object> entry : itemMap.entrySet()) {
                            if ("info".equals(entry.getKey()) && entry.getValue() instanceof ArrayList) {
                                ArrayList<LinkedHashMap<String, String>> items = (ArrayList<LinkedHashMap<String, String>>) entry.getValue();

                                for (LinkedHashMap<String, String> infoMap : items) {
                                    // 이제 infoMap에서 필요한 정보를 추출하여 Map에 담을 수 있습니다.
                                    Map<String, String> dataMap = new HashMap<>(infoMap);
                                    dataList.add(dataMap);
                                }
                            }
                        }
                    }
                }

                log.info("dataList : " + dataList);

                log.info("xmlMapper의 readValue 작업 완료");

                log.info("resultData : " + resultData);
                log.info("생성된 DTO의 개수 : " + resultData.size());

                List<EarthquakeResultDTO> erList = new ArrayList<>();

                // DTO로 변환하여 DB에 저장
                for (Map<String, String> resultDTO : dataList) {

                    EarthquakeResultDTO erDTO = new EarthquakeResultDTO();

                    log.info("resultDTO : " + resultDTO);

                    erDTO.setMsgCode(resultDTO.get("msgCode"));
                    erDTO.setCntDiv(resultDTO.get("cntDiv"));
                    erDTO.setArDiv(resultDTO.get("arDiv"));
                    erDTO.setEqArCdNm(resultDTO.get("eqArCdNm"));
                    erDTO.setEqPt(resultDTO.get("eqPt"));
                    erDTO.setNkDiv(resultDTO.get("nkDiv"));
                    erDTO.setTmIssue(resultDTO.get("tmIssue"));
                    erDTO.setEqDate(resultDTO.get("eqDate"));
                    erDTO.setMagMl(resultDTO.get("magMl"));
                    erDTO.setMagDiff(resultDTO.get("magDiff"));
                    erDTO.setEqDt(resultDTO.get("eqDt"));
                    erDTO.setEqLt(resultDTO.get("eqLt"));
                    erDTO.setEqLn(resultDTO.get("eqLn"));
                    erDTO.setMajorAxis(resultDTO.get("majorAxis"));
                    erDTO.setMinorAxis(resultDTO.get("minorAxis"));
                    erDTO.setDepthDiff(resultDTO.get("depthDiff"));
                    erDTO.setJdLoc(resultDTO.get("jdLoc"));
                    erDTO.setJdLocA(resultDTO.get("jdLocA"));
                    erDTO.setReFer(resultDTO.get("ReFer"));

                    erList.add(erDTO);

                    log.info("erDTO : " + erDTO);
                    log.info("erList : " + erList);

                    earthquakeMapper.insertEarthquakeInfo(erDTO);

                    erDTO = null;
                }

                log.info("매핑된 EarthquakeResultDTO 리스트 : " + erList);
            }
            // 예외 발생시 스택트레이스를 출력합니다.
        } catch (Exception e) {
            log.error("Error in processEarthquakeData: " + e.getMessage(), e);
        }
    }

    // 과거 지진 정보 조회하기
    @Override
    public List<EarthquakeResultDTO> getEarthquakeList() throws Exception {

        log.info(this.getClass().getName() + ".getEarthquakeList Start!");
        log.info(this.getClass().getName() + ".getEarthquakeList End!");

        return earthquakeMapper.getEarthquakeList();
    }

    // 실시간 지진 정보 조회하기
    @Override
    public List<EarthquakeLiveDTO> getEarthquakeLiveList() throws Exception {

        log.info(this.getClass().getName() + ".getEarthquakeLiveInfo Start!");
        log.info(this.getClass().getName() + ".getEarthquakeLiveInfo End!");

        return earthquakeMapper.getEarthquakeLiveList();
    }
}
