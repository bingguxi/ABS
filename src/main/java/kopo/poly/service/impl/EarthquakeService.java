package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import kopo.poly.dto.EarthquakeDTO;
import kopo.poly.dto.EarthquakeResultDTO;
import kopo.poly.persistance.mapper.IEarthquakeMapper;
import kopo.poly.service.IEarthquakeService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EarthquakeService implements IEarthquakeService {

    private final IEarthquakeMapper earthquakeMapper;

    @Value("${bhg.api.key}")
    private String apiKey;

    // 수정된 부분: fixedDelay 값 및 무한루프 수정
    @Scheduled(fixedDelay = 60000, initialDelay = 500)
    @Override
    public void getEarthquakeApi() throws Exception {
        log.info(this.getClass().getName() + ".getEarthquakeApi Start !");

        String frDate = "20200101";
        String laDate = "20201231";
        String cntDiv = "Y";
        String orderTy = "xml";

        int i = 0;  // 수정된 부분: 무한루프를 돌지 않도록 조건 수정
        while (i < 1) {
            log.info("현재 i : " + i);

            String apiParam = "?frDate=" + frDate + "&laDate=" + laDate + "&cntDiv=" + cntDiv + "&orderTy=" + orderTy + "&authKey=" + apiKey;
            log.info("apiParam : " + apiParam);

            // 수정된 부분: API 호출과 결과 처리를 별도의 메서드로 분리
            processEarthquakeData(apiParam);

            i++;
        }
        log.info(this.getClass().getName() + ".getEarthquakeApi End !");
    }

    // 수정된 부분: API 호출과 결과 처리를 별도의 메서드로 분리
    private void processEarthquakeData(String apiParam) {
        try {
            URL url = new URL(IEarthquakeService.apiURL + apiParam);
            log.info(url.toString());

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            log.info("url 요청 전달 완료");

            try (InputStream inputStream = urlConnection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                String resultXml = response.toString();
                log.info("api 호출 결과 ( 시작태그는 <alert> : " + resultXml);

                XmlMapper xmlMapper = new XmlMapper();
                TypeReference
                        <List<LinkedHashMap<String, LinkedHashMap<String, String>>>> typeRef =
                        new TypeReference<List<LinkedHashMap<String, LinkedHashMap<String, String>>>>() {
                        };
                List<LinkedHashMap<String, LinkedHashMap<String, String>>> resultData =
                        xmlMapper.readValue(resultXml, typeRef);

                log.info("xmlMapper의 readValue 작업 완료");

                log.info("info : " + resultData);
                log.info("생성된 DTO의 개수 : " + resultData.size());

                List<EarthquakeResultDTO> erList = new ArrayList<>();

                for (LinkedHashMap<String, LinkedHashMap<String, String>> resultDTO : resultData) {
                    EarthquakeResultDTO erDTO = new EarthquakeResultDTO();

                    // 다음 라인을 수정
                    LinkedHashMap<String, String> infoMap = resultDTO.values().iterator().next();

                    erDTO.setMsgCode(infoMap.get("msgCode"));
                    erDTO.setCntDiv(infoMap.get("cntDiv"));
                    erDTO.setArDiv(infoMap.get("arDiv"));
                    erDTO.setEqArCdNm(infoMap.get("eqArCdNm"));
                    erDTO.setEqPt(infoMap.get("eqPt"));
                    erDTO.setNkDiv(infoMap.get("nkDiv"));
                    erDTO.setTmIssue(infoMap.get("tmIssue"));
                    erDTO.setEqDate(infoMap.get("eqDate"));
                    erDTO.setMagMl(infoMap.get("magMl"));
                    erDTO.setMagDiff(infoMap.get("magDiff"));
                    erDTO.setEqDt(infoMap.get("eqDt"));
                    erDTO.setEqLt(infoMap.get("eqLt"));
                    erDTO.setEqLn(infoMap.get("eqLn"));
                    erDTO.setMajorAxis(infoMap.get("majorAxis"));
                    erDTO.setMinorAxis(infoMap.get("minorAxis"));
                    erDTO.setDepthDiff(infoMap.get("depthDiff"));
                    erDTO.setJdLoc(infoMap.get("jdLoc"));
                    erDTO.setJdLocA(infoMap.get("jdLocA"));
                    erDTO.setReFer(infoMap.get("ReFer"));

                    erList.add(erDTO);

                    log.info("erList : " + erList);
                }

                // 나머지 코드 생략

                // 각 EarthquakeResultDTO에 해당하는 EarthquakeDTO의 List를 설정
                for (int j = 0; j < erList.size(); j++) {
                    List<LinkedHashMap<String, String>> infoList = new ArrayList<>(resultData.get(j).values());

                    // infoList를 EarthquakeDTO 리스트로 변환
                    List<EarthquakeDTO> earthquakeDTOList = new ArrayList<>();
                    for (LinkedHashMap<String, String> infoMap : infoList) {
                        EarthquakeDTO earthquakeDTO = new EarthquakeDTO();
                        earthquakeDTO.setFrDate(infoMap.get("frDate"));
                        earthquakeDTO.setLaDate(infoMap.get("laDate"));
                        earthquakeDTO.setCntDiv(infoMap.get("cntDiv"));
                        earthquakeDTO.setOrderTy(infoMap.get("orderTy"));

                        // 기타 필요한 매핑 로직 추가

                        earthquakeDTOList.add(earthquakeDTO);
                    }

                    erList.get(j).setInfoList(earthquakeDTOList);
                }

                log.info("매핑된 EarthquakeResultDTO 리스트 : " + erList);
            }
        } catch (Exception e) {
            log.error("Error in processEarthquakeData: " + e.getMessage(), e);
        }
    }

    // 나머지 코드는 주석 처리된 상태로 두었습니다.
    // 필요에 따라 주석을 해제하고 로직을 추가하세요.
}
